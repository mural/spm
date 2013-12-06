package com.spm.android.common.utils;

import com.spm.android.common.AndroidApplication;

/**
 * 
 * @author Maxi Rosson
 */
public class MeasureUtils {
	
	public static int pxToDp(int px) {
		final float scale = AndroidApplication.get().getResources().getDisplayMetrics().density;
		return (int)(px * scale + 0.5f);
	}
	
}
