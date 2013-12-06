package com.spm.android.common.c2dm;

import android.content.SharedPreferences.Editor;
import com.spm.android.common.utils.SharedPreferencesUtils;

/**
 * Utilities for device registration.
 * 
 * Will keep track of the registration token in a private preference.
 */
// REVIEW Change the static way of accessing this class
public class C2DMRepository {
	
	private static final String REGISTRATION_ID = "c2dm.registrationId";
	private static final String BACKOFF = "c2dm.backoff";
	
	private static final long DEFAULT_BACKOFF = 30000;
	
	/**
	 * Return the current registration id.
	 * 
	 * If result is null, the registration has failed.
	 * 
	 * @return registration id, or null if the registration is not complete.
	 */
	public static String getRegistrationId() {
		return SharedPreferencesUtils.loadPreference(REGISTRATION_ID);
	}
	
	public static long getBackoff() {
		return SharedPreferencesUtils.loadPreferenceAsLong(BACKOFF, DEFAULT_BACKOFF);
	}
	
	public static void setBackoff(long backoff) {
		SharedPreferencesUtils.savePreference(BACKOFF, backoff);
	}
	
	public static void clearRegistrationId() {
		SharedPreferencesUtils.removePreferences(REGISTRATION_ID);
	}
	
	public static void updateRegistrationId(String registrationId) {
		Editor editor = SharedPreferencesUtils.getEditor();
		editor.putString(REGISTRATION_ID, registrationId);
		editor.putLong(BACKOFF, DEFAULT_BACKOFF);
		editor.commit();
	}
}
