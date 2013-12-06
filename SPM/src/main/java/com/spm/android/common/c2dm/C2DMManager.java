package com.spm.android.common.c2dm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * 
 * @author Maxi Rosson
 */
public class C2DMManager {
	
	private static final String TAG = C2DMManager.class.getSimpleName();
	
	private static final String GOOGLE_ACCOUNT_TYPE = "com.google";
	
	private static final String EXTRA_SENDER = "sender";
	private static final String EXTRA_APPLICATION_PENDING_INTENT = "app";
	private static final String REQUEST_UNREGISTRATION_INTENT = "com.google.android.c2dm.intent.UNREGISTER";
	private static final String REQUEST_REGISTRATION_INTENT = "com.google.android.c2dm.intent.REGISTER";
	private static final String GSF_PACKAGE = "com.google.android.gsf";
	
	public static final String C2DM_ENABLED_KEY = "c2dmEnabled";
	
	/**
	 * Register or unregister the C2DM based on the {@link #C2DM_ENABLED_KEY} preference key
	 * 
	 * @param context The {@link Context}
	 * @param senderId The sender id
	 */
	public static void initializeC2DM(Context context, String senderId) {
		Boolean c2dmEnabled = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(C2DM_ENABLED_KEY, true);
		if (c2dmEnabled) {
			AccountManager accountManager = AccountManager.get(context);
			Account[] accounts = accountManager.getAccountsByType(GOOGLE_ACCOUNT_TYPE);
			// C2DM needs a google account to work
			if (accounts.length > 0) {
				if (C2DMRepository.getRegistrationId() == null) {
					C2DMManager.register(context, senderId);
				}
			} else {
				// TODO Add support to devices that doesn't have a google account
			}
		}
	}
	
	/**
	 * Initiate C2DM registration for the current application
	 * 
	 * @param context The {@link Context}
	 * @param senderId The sender id
	 */
	public static void register(Context context, String senderId) {
		Intent registrationIntent = new Intent(REQUEST_REGISTRATION_INTENT);
		registrationIntent.setPackage(GSF_PACKAGE);
		registrationIntent.putExtra(EXTRA_APPLICATION_PENDING_INTENT,
			PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		registrationIntent.putExtra(EXTRA_SENDER, senderId);
		context.startService(registrationIntent);
		Log.d(TAG, "Starting C2DM registration");
	}
	
	/**
	 * Initiate C2DM unregistration for the current application. New messages will be blocked by server.
	 * 
	 * @param context The {@link Context}
	 */
	public static void unregister(Context context) {
		Intent regIntent = new Intent(REQUEST_UNREGISTRATION_INTENT);
		regIntent.setPackage(GSF_PACKAGE);
		regIntent.putExtra(EXTRA_APPLICATION_PENDING_INTENT, PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		context.startService(regIntent);
		Log.d(TAG, "Starting C2DM unregistration");
	}
	
}
