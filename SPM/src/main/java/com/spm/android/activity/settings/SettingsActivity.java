package com.spm.android.activity.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import com.spm.android.common.activity.AbstractPreferenceActivity;
import com.spm.android.common.c2dm.C2DMManager;
import com.spm.common.domain.Application.ApplicationProvider;
import com.spm.R;

/**
 * Settings Activity
 * 
 * @author Maxi Rosson
 */
public class SettingsActivity extends AbstractPreferenceActivity implements OnSharedPreferenceChangeListener {
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(C2DMManager.C2DM_ENABLED_KEY)) {
			Boolean enablePush = sharedPreferences.getBoolean(key, true);
			if (enablePush) {
				C2DMManager.register(this, ApplicationProvider.C2DM_SENDER);
			} else {
				C2DMManager.unregister(this);
			}
		}
	}
}
