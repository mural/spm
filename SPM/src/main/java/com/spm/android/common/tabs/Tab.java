package com.spm.android.common.tabs;

import android.app.Activity;

/**
 * 
 * @author Maxi Rosson
 */
public interface Tab {
	
	/**
	 * @return the targetActivityClass
	 */
	public Class<? extends Activity> getTargetActivityClass();
	
	/**
	 * @return the imageResourceId
	 */
	public int getImageResourceId();
	
	/**
	 * @return the imageHighlightResourceId
	 */
	public int getImageHighlightResourceId();
	
	/**
	 * @return the indicatorResourceId
	 */
	public int getIndicatorResourceId();
}
