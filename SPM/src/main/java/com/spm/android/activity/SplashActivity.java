package com.spm.android.activity;

import android.content.Intent;
import android.os.Bundle;
import com.spm.R;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.activity.AbstractActivity;
import com.spm.common.utils.ThreadUtils;
import com.spm.domain.User;

/**
 * 
 * @author Agustin Sgarlata
 */
public class SplashActivity extends AbstractActivity {
	
	/**
	 * @see com.spm.android.common.activity.AbstractActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		User user = AndroidApplication.get().getUser();
		if (user == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ActivityLauncher.launchActivity(LoginActivity.class, intent);
			finish();
			
		} else {
			
			setContentView(R.layout.splash_activity);
			
			ThreadUtils.execute(new Runnable() {
				
				@Override
				public void run() {
					load();
				}
			});
			
		}
	}
	
	void load() {
		AndroidApplication.get().dbHelper();
		
		// We first load the initial data from the database
		// new DBClientRepository(this).loadInitialData();
		// new DBUserRepository(this).loadInitialData();
		// new DBOrderRepository(this).loadInitialData();
		// new DBLineRepository(this).loadInitialData();
		// new DBProductRepository(this).loadInitialData();
		// new DBVisitRepository(this).loadInitialData();
		
		//ActivityLauncher.launchActivity(DashBoardActivity.class);
		DashBoardActivity_.intent(this).start();
		finish();
	}
}
