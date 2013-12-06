package com.spm.android.common;

import java.io.Serializable;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.spm.android.common.activity.PictureMenuActivity;

/**
 * Launcher for all the activities of the application
 * 
 * @author Maxi Rosson
 */
public class ActivityLauncher {
	
	/**
	 * Launches the {@link AndroidApplication#getHomeActivityClass()}
	 */
	public static void launchHomeActivity() {
		Activity currentActivity = AndroidApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != AndroidApplication.get().getHomeActivityClass()) {
			Intent intent = new Intent(currentActivity, AndroidApplication.get().getHomeActivityClass());
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			currentActivity.startActivity(intent);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass) {
		Activity currentActivity = AndroidApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			currentActivity.startActivity(intent);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param requestCode If >= 0, this code will be returned in onActivityResult() when the activity exits.
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass, int requestCode) {
		Activity currentActivity = AndroidApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			currentActivity.startActivityForResult(intent, requestCode);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param extraName The extra name
	 * @param extraValue The extra value
	 * @param requestCode If >= 0, this code will be returned in onActivityResult() when the activity exits.
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass, String extraName,
			Serializable extraValue, int requestCode) {
		Activity currentActivity = AndroidApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			intent.putExtra(extraName, extraValue);
			currentActivity.startActivityForResult(intent, requestCode);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param extraName The extra name
	 * @param extraValue The extra value
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass, String extraName,
			Serializable extraValue) {
		Activity currentActivity = AndroidApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			intent.putExtra(extraName, extraValue);
			currentActivity.startActivity(intent);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param bundle The extra {@link Bundle}
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass, Bundle bundle) {
		Activity currentActivity = AndroidApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			intent.putExtras(bundle);
			currentActivity.startActivity(intent);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param bundle The extra {@link Bundle}
	 * @param requestCode If >= 0, this code will be returned in onActivityResult() when the activity exits.
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass, Bundle bundle, int requestCode) {
		Activity currentActivity = AndroidApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			intent.putExtras(bundle);
			currentActivity.startActivityForResult(intent, requestCode);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param intent
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass, Intent intent) {
		Activity currentActivity = AndroidApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			currentActivity.startActivity(intent);
		}
	}
	
	/**
	 * Launches a new {@link PictureMenuActivity}
	 */
	public static void launchPictureMenuActivity() {
		launchActivity(PictureMenuActivity.class, PictureMenuActivity.SET_PICTURE);
	}
	
}
