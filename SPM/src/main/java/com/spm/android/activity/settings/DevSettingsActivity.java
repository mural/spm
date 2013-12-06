package com.spm.android.activity.settings;

import android.os.Bundle;
import com.spm.android.common.activity.AbstractPreferenceActivity;
import com.spm.R;

/**
 * Settings Activity just for development purposes. These settings are disabled on the production environment
 * 
 * @author Maxi Rosson
 */
public class DevSettingsActivity extends AbstractPreferenceActivity {
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.dev_preferences);
	}
}