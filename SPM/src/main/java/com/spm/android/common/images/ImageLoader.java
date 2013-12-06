package com.spm.android.common.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import com.google.inject.internal.Lists;
import com.google.inject.internal.Maps;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.utils.BitmapUtils;
import com.spm.common.http.HttpClientFactory;
import com.spm.common.utils.FileUtils;
import com.spm.common.utils.Utils;

/**
 * @author Fernando Perez
 * @author Luciano Rey
 */
public class ImageLoader {
	
	private static final String TAG = ImageLoader.class.getSimpleName();
	private static final ImageLoader INSTANCE = new ImageLoader();
	
	// Retry Interval in milliseconds
	private static final int RETRY_INTERVAL = 100000;
	
	private Map<String, SoftReference<Bitmap>> bitmapCache = Maps.newHashMap();
	private Map<String, Long> failedBitmapCache = Maps.newHashMap();
	private ImagesQueue imagesQueue = new ImagesQueue();
	private List<ImageLoaderThread> loaders = Lists.newArrayList();
	
	public static ImageLoader get() {
		return INSTANCE;
	}
	
	private ImageLoader() {
		ImageLoaderThread imageLoaderThread = new ImageLoaderThread();
		imageLoaderThread.start();
		loaders.add(imageLoaderThread);
	}
	
	public void displayImage(String url, RemoteImageView imageView) {
		SoftReference<Bitmap> bitmapReference = bitmapCache.get(url);
		Bitmap bitmap = bitmapReference != null ? bitmapReference.get() : null;
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			if (bitmapReference != null) {
				bitmapCache.remove(url);
			}
			
			// If the image failed to be downloaded previously, try again after the RETRY_INTERVAL
			if (failedBitmapCache.containsKey(url)) {
				if ((System.currentTimeMillis() - failedBitmapCache.get(url)) > RETRY_INTERVAL) {
					failedBitmapCache.remove(url);
					queueImage(url, imageView);
				}
			} else {
				queueImage(url, imageView);
			}
			imageView.showStubImage();
		}
	}
	
	private void queueImage(String url, RemoteImageView imageView) {
		// This ImageView may be used for other images before. So there may be
		// some old tasks in the queue. We need to discard them.
		imagesQueue.clean(imageView);
		ImageToLoadTask imageToLoadTask = new ImageToLoadTask(url, imageView, imageView.getMaxWidth(),
				imageView.getMaxHeight());
		synchronized (imagesQueue) {
			imagesQueue.push(imageToLoadTask);
			imagesQueue.notifyAll();
		}
	}
	
	/**
	 * Clear memory cache
	 */
	public void clearCache() {
		bitmapCache.clear();
		failedBitmapCache.clear();
	}
	
	/**
	 * Task for the queue.
	 * 
	 * @author Luciano Rey
	 */
	private class ImageToLoadTask implements Runnable {
		
		private String url;
		private RemoteImageView imageView;
		private Bitmap bitmap;
		private Integer maxWidth;
		private Integer maxHeight;
		
		public ImageToLoadTask(String url, RemoteImageView imageView, Integer maxWidth, Integer maxHeight) {
			this.url = url;
			this.imageView = imageView;
			this.maxWidth = maxWidth;
			this.maxHeight = maxHeight;
		}
		
		/**
		 * Used to display bitmap in the UI thread.
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
			} else {
				imageView.showStubImage();
			}
		}
		
		public Boolean isValid() {
			return imageView.getTag().equals(url);
		}
		
		public Activity getActivity() {
			return (Activity)imageView.getContext();
		}
		
		private Bitmap getBitmap() {
			// REVIEW Identify images by hashcode. Find a better way to do this
			File file = new File(AndroidApplication.get().getCacheDirectory(), String.valueOf(url.hashCode()));
			
			// If the file doesn't exists on the SD cache directory, it is retrieved from the web
			if (!file.exists()) {
				
				InputStream is = null;
				OutputStream os = null;
				try {
					// make client for http.
					DefaultHttpClient client = HttpClientFactory.createDefaultHttpClient();
					
					// make request.
					HttpUriRequest request = new HttpGet(url);
					
					// execute request.
					HttpResponse httpResponse = client.execute(request);
					
					if (httpResponse.getStatusLine().getStatusCode() != 404) {
						// Process response
						is = httpResponse.getEntity().getContent();
						os = new FileOutputStream(file);
						Utils.copyStream(is, os);
						Log.d(TAG, "Image [" + url + "] downloaded.");
					} else {
						Log.d(TAG, "Image [" + url + "] not found.");
						return null;
					}
					
				} catch (Exception ex) {
					Log.e(TAG, "", ex);
					return null;
				} finally {
					FileUtils.safeClose(os);
					FileUtils.safeClose(is);
				}
			}
			return BitmapUtils.fromUri(Uri.fromFile(file), maxWidth, maxHeight);
		}
	}
	
	/**
	 * Stores list of images to download.
	 * 
	 * @author Luciano Rey
	 */
	private class ImagesQueue extends Stack<ImageToLoadTask> {
		
		/**
		 * Removes all instances of this ImageView.
		 * 
		 * @param image
		 */
		public void clean(ImageView image) {
			for (int i = 0; i < size();) {
				if (get(i).imageView == image) {
					remove(i);
				} else {
					i++;
				}
			}
		}
	}
	
	/**
	 * @author Luciano Rey
	 */
	private class ImageLoaderThread extends Thread {
		
		public ImageLoaderThread() {
			super(ImageLoaderThread.class.getSimpleName());
			// Make the background thread low priority. This way it will not affect the UI performance.
			setPriority(Thread.MIN_PRIORITY);
		}
		
		/**
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the queue.
					if (imagesQueue.isEmpty()) {
						synchronized (imagesQueue) {
							imagesQueue.wait();
						}
					}
					
					if (!imagesQueue.isEmpty()) {
						ImageToLoadTask imageToLoadTask = null;
						synchronized (imagesQueue) {
							if (!imagesQueue.isEmpty()) {
								imageToLoadTask = imagesQueue.pop();
							}
						}
						if (imageToLoadTask != null) {
							Bitmap bmp = imageToLoadTask.getBitmap();
							if (bmp != null) {
								bitmapCache.put(imageToLoadTask.url, new SoftReference<Bitmap>(bmp));
								if (imageToLoadTask.isValid()) {
									imageToLoadTask.bitmap = bmp;
									imageToLoadTask.getActivity().runOnUiThread(imageToLoadTask);
								}
							} else {
								// Save the the image URL to retry the download in the future
								failedBitmapCache.put(imageToLoadTask.url, System.currentTimeMillis());
							}
						}
					}
				}
			} catch (InterruptedException e) {
				Log.e(TAG, "", e);
			}
		}
	}
}
