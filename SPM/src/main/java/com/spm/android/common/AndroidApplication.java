package com.spm.android.common;

import java.io.File;
import java.util.List;
import roboguice.application.RoboApplication;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import com.google.inject.Module;
import com.spm.R;
import com.spm.android.activity.LoginActivity;
import com.spm.android.common.activity.BaseActivity;
import com.spm.android.common.utils.AlertDialogUtils;
import com.spm.android.common.utils.AndroidUtils;
import com.spm.android.common.utils.ToastUtils;
import com.spm.android.exception.ExceptionHandler;
import com.spm.common.domain.Application;
import com.spm.common.utils.DateUtils;
import com.spm.common.utils.FileUtils;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AndroidApplication extends RoboApplication implements Application {
	
	private static AndroidApplication INSTANCE;
	
	/** Current activity in the top stack. */
	private Activity currentActivity;
	
	private File cacheDirectory;
	
	private boolean inBackground = false;
	
	public AndroidApplication() {
		INSTANCE = this;
	}
	
	/**
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		Application.APPLICATION_PROVIDER.set(this);
		
		// This is required to initialize the statics fields of the utils classes.
		AlertDialogUtils.init();
		ToastUtils.init();
		DateUtils.init();
		
		Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler.getInstance());
		
		// TODO Uncomment if c2dm is required
		// C2DMManager.initializeC2DM(this, ApplicationProvider.C2DM_SENDER);
		// PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		
		// Configure the cache dir for the whole application
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheDirectory = new File(android.os.Environment.getExternalStorageDirectory(),
					AndroidUtils.getPackageName());
			// Remove the content of the cache dir
			FileUtils.forceDelete(cacheDirectory);
		} else {
			// TODO We could listen the Intent.ACTION_DEVICE_STORAGE_LOW and clear the cache
			cacheDirectory = getCacheDir();
		}
		
		if (!cacheDirectory.exists()) {
			cacheDirectory.mkdirs();
		}
		
		PreferenceManager.setDefaultValues(this, R.xml.dev_preferences, false);
	}
	
	/**
	 * @see roboguice.application.RoboApplication#addApplicationModules(java.util.List)
	 */
	@Override
	protected void addApplicationModules(List<Module> modules) {
		modules.add(new AndroidModule());
	}
	
	public static AndroidApplication get() {
		return INSTANCE;
	}
	
	public abstract Class<? extends Activity> getHomeActivityClass();
	
	public BaseActivity createBaseActivity(Activity activity) {
		return new BaseActivity(activity);
	}
	
	public void setCurrentActivity(Activity activity) {
		currentActivity = activity;
	}
	
	public Activity getCurrentActivity() {
		return currentActivity;
	}
	
	/**
	 * @return the cacheDirectory
	 */
	public File getCacheDirectory() {
		return cacheDirectory;
	}
	
	/**
	 * @return the inBackground
	 */
	public boolean isInBackground() {
		return inBackground;
	}
	
	/**
	 * @param inBackground the inBackground to set
	 */
	public void setInBackground(boolean inBackground) {
		this.inBackground = inBackground;
	}
	
	/**
	 * 
	 */
	public void logout() {
		detachUser();
		
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ActivityLauncher.launchActivity(LoginActivity.class, intent);
	}
}
