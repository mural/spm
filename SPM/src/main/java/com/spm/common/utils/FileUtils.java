package com.spm.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import android.util.Log;
import com.spm.common.exception.UnexpectedException;

/**
 * This class contains functions for working with files within the application.
 * 
 * @author Maxi Rosson
 */
public abstract class FileUtils {
	
	private static final String TAG = FileUtils.class.getSimpleName();
	
	private static final int BUFFER_SIZE = 16384;
	
	/**
	 * Private constructor
	 */
	private FileUtils() {
		// Do nothing
	}
	
	/**
	 * Deletes an instance of {@link File} even if it is a directory containing files.<br>
	 * If the file is a directory and has contents, then executes itself on every content.
	 * 
	 * @see File#delete()
	 * @param file The {@link File} to be deleted.
	 */
	public static void forceDelete(File file) {
		if (file.exists()) {
			
			// If the File instance to delete is a directory, delete all it's
			// contents.
			if (file.isDirectory()) {
				for (File contentFile : file.listFiles()) {
					FileUtils.forceDelete(contentFile);
				}
			}
			
			if (file.delete()) {
				Log.i(TAG, "File " + file.getPath() + " was succesfully deleted.");
			} else {
				Log.w(TAG, "File " + file.getPath() + " couldn't be deleted.");
			}
		}
	}
	
	/**
	 * @param source the source {@link InputStream}
	 * @param destin the destin {@link File}
	 */
	public static void copyStream(InputStream source, File destin) {
		FileOutputStream out = null;
		try {
			File dir = destin.getParentFile();
			if (dir != null) {
				dir.mkdirs();
			}
			out = new FileOutputStream(destin);
			FileUtils.copyStream(source, out);
		} catch (IOException e) {
			throw new UnexpectedException(
					new StringBuilder("Error copying the file to [").append(destin).append("]").toString(), e);
		} finally {
			safeClose(out);
		}
	}
	
	/**
	 * @param source the source {@link InputStream}
	 * @param destin the destin {@link OutputStream}
	 * @param closeOutputStream
	 */
	public static void copyStream(InputStream source, OutputStream destin, Boolean closeOutputStream) {
		try {
			byte[] buffer = new byte[FileUtils.BUFFER_SIZE];
			for (int i = source.read(buffer); i != -1; i = source.read(buffer)) {
				destin.write(buffer, 0, i);
			}
		} catch (IOException e) {
			throw new UnexpectedException("Error copying file", e);
		} finally {
			if (closeOutputStream) {
				safeClose(destin);
			}
		}
	}
	
	/**
	 * @param source the source {@link InputStream}
	 * @param destin the destin {@link OutputStream}
	 */
	public static void copyStream(InputStream source, OutputStream destin) {
		copyStream(source, destin, true);
	}
	
	/**
	 * @param source the source {@link InputStream}
	 * @return the input stream that can be reset {@link ByteArrayInputStream}
	 */
	public static ByteArrayInputStream copy(InputStream source) {
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		copy(source, tmp, true);
		return new ByteArrayInputStream(tmp.toByteArray());
	}
	
	/**
	 * @param source the source {@link InputStream}
	 * @param destin the destin {@link OutputStream}
	 * @param closeOutputStream
	 */
	public static void copy(InputStream source, OutputStream destin, Boolean closeOutputStream) {
		try {
			byte[] buffer = new byte[FileUtils.BUFFER_SIZE];
			for (int i = source.read(buffer); i != -1; i = source.read(buffer)) {
				destin.write(buffer, 0, i);
			}
		} catch (IOException e) {
			throw new UnexpectedException("Error copying file", e);
		} finally {
			if (closeOutputStream) {
				safeClose(destin);
			}
		}
	}
	
	/**
	 * Receives an InputStream and iterates over all its lines and returns a String.
	 * 
	 * @param in the InputStream to be converted
	 * @return The content of the file as String
	 */
	public static String toString(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer contents = new StringBuffer();
		String text = null;
		
		// repeat until all lines are read
		try {
			while ((text = reader.readLine()) != null) {
				contents.append(text).append(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			throw new UnexpectedException("Error reading the stream", e);
		} finally {
			safeClose(in);
		}
		return contents.toString();
	}
	
	public static void safeClose(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				Log.w(TAG, "Exception thrown when trying to close the closeable", e);
			}
		}
	}
}
