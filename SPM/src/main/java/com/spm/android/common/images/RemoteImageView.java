package com.spm.android.common.images;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author Luciano Rey
 */
public class RemoteImageView extends ImageView {
	
	private int stubId;
	private Integer maxWidth;
	private Integer maxHeight;
	
	public RemoteImageView(Context context) {
		super(context);
	}
	
	public RemoteImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setImageURL(String imageURL, int stubId) {
		setImageURL(imageURL, stubId, null, null);
	}
	
	/**
	 * @param imageURL The image URL
	 * @param stubId The id of the resource stub to display while the image is been downloaded
	 * @param maxWidth The maximum width of the image used to scale it. If null, the image won't be scaled
	 * @param maxHeight The maximum height of the image used to scale it. If null, the image won't be scaled
	 */
	public void setImageURL(String imageURL, int stubId, Integer maxWidth, Integer maxHeight) {
		this.stubId = stubId;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		if (imageURL != null) {
			this.setTag(imageURL);
			ImageLoader.get().displayImage(imageURL, this);
		} else {
			showStubImage();
		}
	}
	
	public void showStubImage() {
		this.setImageResource(stubId);
	}
	
	/**
	 * @return the maxWidth
	 */
	@Override
	public int getMaxWidth() {
		return maxWidth;
	}
	
	/**
	 * @return the maxHeight
	 */
	@Override
	public int getMaxHeight() {
		return maxHeight;
	}
}
