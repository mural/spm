package com.spm.android.common.utils;

import com.spm.android.common.AndroidApplication;

/**
 * @author Luciano Rey
 */
public final class LocalizationUtils {
	
	/**
	 * Returns a formatted string, using the localized resource as format and the supplied arguments
	 * 
	 * @param resId The resource id to obtain the format
	 * @param args arguments to replace format specifiers
	 * @return The localized and formated string
	 */
	public static String getString(int resId, Object... args) {
		return AndroidApplication.get().getString(resId, args);
	}
}
