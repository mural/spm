package com.spm.android.common.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * 
 * @author Maxi Rosson
 * @param <T>
 */
public class HolderOverlayItem<T> extends OverlayItem {
	
	private T data;
	
	/**
	 * @param point
	 * @param title
	 * @param snippet
	 * @param data
	 */
	public HolderOverlayItem(GeoPoint point, String title, String snippet, T data) {
		super(point, title, snippet);
		this.data = data;
	}
	
	/**
	 * @param point
	 * @param title
	 * @param data
	 */
	public HolderOverlayItem(GeoPoint point, String title, T data) {
		this(point, title, null, data);
	}
	
	/**
	 * @param point
	 * @param data
	 */
	public HolderOverlayItem(GeoPoint point, T data) {
		this(point, null, null, data);
	}
	
	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}
	
}
