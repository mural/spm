package com.spm.android;

import android.app.Activity;
import android.view.MenuItem;
import com.spm.R;
import com.spm.android.activity.settings.DevSettingsActivity;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.activity.BaseActivity;
import com.spm.common.domain.Application.ApplicationProvider;

/**
 * 
 * @author Agustin Sgarlata
 */
public class SPMBaseActivity extends BaseActivity {
	
	/**
	 * @param activity
	 */
	public SPMBaseActivity(Activity activity) {
		super(activity);
		
	}
	
	/**
	 * @see com.spm.android.common.activity.BaseActivity#getMenuResourceId()
	 */
	@Override
	public int getMenuResourceId() {
		int menuResourceId = 0;
		if (getActivityIf().isAuthenticated()) {
			menuResourceId = R.menu.menu;
		} else {
			if (ApplicationProvider.DEV_SETTINGS) {
				menuResourceId = R.menu.dev_menu;
			}
		}
		return menuResourceId;
	}
	
	/**
	 * @see com.spm.android.common.activity.BaseActivity#hasOptionsMenu()
	 */
	@Override
	public boolean hasOptionsMenu() {
		return true;
	}
	
	/**
	 * @see com.spm.android.common.activity.BaseActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.logoutItem:
				AndroidApplication.get().logout();
				return true;
			case R.id.devSettingsItem:
				ActivityLauncher.launchActivity(DevSettingsActivity.class);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
