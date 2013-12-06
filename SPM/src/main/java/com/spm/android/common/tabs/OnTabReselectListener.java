package com.spm.android.common.tabs;

/**
 * Interface definition for a callback to be invoked when a current selected tab in a TabHost is selected again.
 * 
 * @author Maxi Rosson
 */
public interface OnTabReselectListener {
	
	/**
	 * Called when a current visible tab is selected again. Will not be invoked on tab changes.
	 */
	public void onTabReselect();
	
}
