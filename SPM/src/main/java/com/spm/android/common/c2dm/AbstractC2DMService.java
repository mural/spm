package com.spm.android.common.c2dm;

import roboguice.service.RoboIntentService;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.spm.android.common.utils.WakeLockManager;

/**
 * Base {@link IntentService} to handle Android Cloud to Data Messaging (AC2DM) messages.
 * 
 * You need to add the following permissions to your manifest:
 * 
 * <pre>
 * &lt;permission android:name="[app package].permission.C2D_MESSAGE" android:protectionLevel="signature" />
 * &lt;uses-permission android:name="[app package].permission.C2D_MESSAGE" />
 * &lt;uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
 * &lt;uses-permission android:name="android.permission.WAKE_LOCK" />
 * &lt;uses-permission android:name="android.permission.GET_ACCOUNTS" />
 * </pre>
 */
public abstract class AbstractC2DMService extends RoboIntentService {
	
	private static final String TAG = AbstractC2DMService.class.getSimpleName();
	
	private static final String C2DM_RETRY = "com.google.android.c2dm.intent.RETRY";
	private static final String C2DM_REGISTRATION = "com.google.android.c2dm.intent.REGISTRATION";
	private static final String C2DM_RECEIVE = "com.google.android.c2dm.intent.RECEIVE";
	
	// Extras in the registration callback intents.
	private static final String EXTRA_UNREGISTERED = "unregistered";
	private static final String EXTRA_ERROR = "error";
	private static final String EXTRA_REGISTRATION_ID = "registration_id";
	
	// Error codes
	private static final String ERR_SERVICE_NOT_AVAILABLE = "SERVICE_NOT_AVAILABLE";
	private static final String ERR_ACCOUNT_MISSING = "ACCOUNT_MISSING";
	private static final String ERR_AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";
	private static final String ERR_TOO_MANY_REGISTRATIONS = "TOO_MANY_REGISTRATIONS";
	private static final String ERR_INVALID_SENDER = "INVALID_SENDER";
	private static final String ERR_PHONE_REGISTRATION_ERROR = "PHONE_REGISTRATION_ERROR";
	
	private String senderId;
	
	/**
	 * @param senderId the sender id to be used for registration.
	 */
	public AbstractC2DMService(String senderId) {
		// senderId is used as base name for threads, etc.
		super(senderId);
		this.senderId = senderId;
	}
	
	/**
	 * Called when a cloud message has been received.
	 * 
	 * @param context The {@link Context}
	 * @param intent The {@link Intent} with the C2DM message
	 */
	public abstract void onMessage(Context context, Intent intent);
	
	/**
	 * Called when a registration token has been received.
	 * 
	 * @param context The {@link Context}
	 * @param registrationId The registration id
	 */
	public abstract void onRegistered(Context context, String registrationId);
	
	/**
	 * Called when the device has been unregistered.
	 * 
	 * @param context The {@link Context}
	 */
	public abstract void onUnregistered(Context context);
	
	/**
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	public final void onHandleIntent(Intent intent) {
		try {
			Context context = getApplicationContext();
			if (intent.getAction().equals(C2DM_REGISTRATION)) {
				handleRegistration(context, intent);
			} else if (intent.getAction().equals(C2DM_RECEIVE)) {
				onMessage(context, intent);
			} else if (intent.getAction().equals(C2DM_RETRY)) {
				C2DMManager.register(context, senderId);
			}
		} finally {
			// Release the power lock, so phone can get back to sleep.
			// The lock is reference counted by default, so multiple messages are ok.
			// If the onMessage() needs to spawn a thread or do something else, it should use it's own lock.
			WakeLockManager.releasePartialWakeLock();
		}
	}
	
	/**
	 * Called from the {@link AbstractC2DMBroadcastReceiver}. Will process the received intent, call handleMessage(),
	 * registered(), etc. in background threads, with a wake lock, while keeping the service alive.
	 * 
	 * @param context The {@link Context}
	 * @param intent The {@link Intent} being received.
	 * @param serviceClass The {@link Class} of the {@link Service} to start
	 */
	public static void runIntentInService(Context context, Intent intent, Class<?> serviceClass) {
		WakeLockManager.acquirePartialWakeLock(context);
		intent.setClass(context, serviceClass);
		context.startService(intent);
		
	}
	
	private void handleRegistration(final Context context, Intent intent) {
		String registrationId = intent.getStringExtra(EXTRA_REGISTRATION_ID);
		String error = intent.getStringExtra(EXTRA_ERROR);
		String removed = intent.getStringExtra(EXTRA_UNREGISTERED);
		
		if (removed != null) {
			// Remember we are unregistered
			C2DMRepository.clearRegistrationId();
			Log.d(TAG, "Device unregistered from " + removed);
			onUnregistered(context);
		} else if (error != null) {
			// Registration failed
			C2DMRepository.clearRegistrationId();
			handleError(context, error);
		} else {
			C2DMRepository.updateRegistrationId(registrationId);
			Log.d(TAG, "Device registered. Registration id: " + registrationId);
			onRegistered(context, registrationId);
		}
	}
	
	private void handleError(final Context context, String error) {
		if (ERR_SERVICE_NOT_AVAILABLE.equals(error)) {
			
			// TODO Verify if this is working properly
			
			// The device can't read the response, or there was a 500/503 from the server that can be retried later. The
			// application should use exponential back off and retry.
			long backoffTimeMs = C2DMRepository.getBackoff();
			
			Log.d(TAG, "Scheduling registration retry, backoff = " + backoffTimeMs);
			Intent retryIntent = new Intent(C2DM_RETRY);
			PendingIntent retryPendingIntent = PendingIntent.getBroadcast(context, 0, retryIntent, 0);
			
			AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			am.set(AlarmManager.ELAPSED_REALTIME, backoffTimeMs, retryPendingIntent);
			
			// Next retry should wait longer.
			backoffTimeMs *= 2;
			C2DMRepository.setBackoff(backoffTimeMs);
		} else if (ERR_ACCOUNT_MISSING.equals(error)) {
			Log.w(TAG, "There is no Google account on the phone.");
			onMissingGoogleAccountError(context);
		} else if (ERR_AUTHENTICATION_FAILED.equals(error)) {
			Log.w(TAG, "Authentication failed.");
			onAuthenticationFailedError(context);
		} else if (ERR_TOO_MANY_REGISTRATIONS.equals(error)) {
			Log.w(TAG, "The user has too many applications registered.");
			onTooManyRegistrationsError(context);
		} else if (ERR_INVALID_SENDER.equals(error)) {
			Log.w(TAG, "The sender account is not recognized.");
			onInvalidSenderError(context);
		} else if (ERR_PHONE_REGISTRATION_ERROR.equals(error)) {
			Log.w(TAG, "This phone doesn't currently support C2DM.");
			onC2DMNotSupportedError(context);
		}
	}
	
	/**
	 * Called on registration error. There is no Google account on the phone. The application should ask the user to
	 * open the account manager and add a Google account. Fix on the device side.
	 * 
	 * This is called in the context of a Service - no dialog or UI.
	 * 
	 * @param context The {@link Context}
	 */
	public void onMissingGoogleAccountError(Context context) {
		// Do nothing by default
	}
	
	/**
	 * Called on registration error. Bad password. The application should ask the user to enter his/her password, and
	 * let user retry manually later. Fix on the device side.
	 * 
	 * This is called in the context of a Service - no dialog or UI.
	 * 
	 * @param context The {@link Context}
	 */
	public void onAuthenticationFailedError(Context context) {
		// Do nothing by default
	}
	
	/**
	 * Called on registration error. The user has too many applications registered. The application should tell the user
	 * to uninstall some other applications, let user retry manually. Fix on the device side.
	 * 
	 * This is called in the context of a Service - no dialog or UI.
	 * 
	 * @param context The {@link Context}
	 */
	public void onTooManyRegistrationsError(Context context) {
		// Do nothing by default
	}
	
	/**
	 * Called on registration error. The sender account is not recognized.
	 * 
	 * This is called in the context of a Service - no dialog or UI.
	 * 
	 * @param context The {@link Context}
	 */
	public void onInvalidSenderError(Context context) {
		// Do nothing by default
	}
	
	/**
	 * Called on registration error. Incorrect phone registration with Google. This phone doesn't currently support
	 * C2DM.
	 * 
	 * This is called in the context of a Service - no dialog or UI.
	 * 
	 * @param context The {@link Context}
	 */
	public void onC2DMNotSupportedError(Context context) {
		// Do nothing by default
	}
}
