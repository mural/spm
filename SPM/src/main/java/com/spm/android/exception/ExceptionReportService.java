package com.spm.android.exception;

import roboguice.service.RoboIntentService;
import android.content.Intent;
import android.util.Log;
import com.google.inject.Inject;
import com.spm.android.common.utils.AndroidUtils;
import com.spm.common.mail.MailException;
import com.spm.common.mail.MailService;

/**
 * 
 * @author Maxi Rosson
 */
public class ExceptionReportService extends RoboIntentService {
	
	private static final String TAG = ExceptionReportService.class.getSimpleName();
	
	public static final String EXTRA_STACK_TRACE = "stackTrace";
	public static final String EXTRA_EXCEPTION_TIME = "exceptionTime";
	public static final String EXTRA_THREAD_NAME = "threadName";
	public static final String EXTRA_USER_NAME = "userName";
	
	// Used internally to count retries.
	private static final String EXTRA_CURRENT_RETRY_COUNT = "currentRetryCount";
	
	// The maximum number of tries to send a report.
	private static final int MAXIMUM_RETRY_COUNT = 5;
	
	private static final String NEW_LINE = "\n";
	
	@Inject
	private MailService mailService;
	
	public ExceptionReportService() {
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			sendReport(intent);
		} catch (Exception e) {
			Log.e(TAG, "Error while sending an error report", e);
		}
	}
	
	private void sendReport(Intent intent) {
		Log.v(TAG, "Got request to report error: " + intent.toString());
		
		try {
			StringBuilder builder = new StringBuilder();
			
			builder.append("User: ");
			builder.append(intent.getStringExtra(EXTRA_USER_NAME));
			builder.append(NEW_LINE);
			
			builder.append("Date: ");
			builder.append(intent.getStringExtra(EXTRA_EXCEPTION_TIME));
			builder.append(NEW_LINE);
			
			builder.append("Thread Name: ");
			builder.append(intent.getStringExtra(EXTRA_THREAD_NAME));
			builder.append(NEW_LINE);
			
			builder.append("App Version Code: ");
			builder.append(AndroidUtils.getVersionCode());
			builder.append(NEW_LINE);
			
			String versionName = AndroidUtils.getVersionName();
			builder.append("App Version Name: ");
			builder.append(versionName);
			builder.append(NEW_LINE);
			
			String packageName = AndroidUtils.getPackageName();
			builder.append("App Package Name: ");
			builder.append(packageName);
			builder.append(NEW_LINE);
			
			builder.append("Available Data: ");
			builder.append(AndroidUtils.getAvailableInternalDataSize());
			builder.append(" MB");
			builder.append(NEW_LINE);
			
			builder.append("Total Data: ");
			builder.append(AndroidUtils.getTotalInternalDataSize());
			builder.append(" MB");
			builder.append(NEW_LINE);
			
			builder.append("Heap Size: ");
			builder.append(AndroidUtils.getHeapSize());
			builder.append(" MB");
			builder.append(NEW_LINE);
			
			builder.append("Device Model: ");
			builder.append(AndroidUtils.getDeviceModel());
			builder.append(NEW_LINE);
			
			builder.append("API Level: ");
			builder.append(AndroidUtils.getApiLevel());
			builder.append(NEW_LINE);
			
			builder.append("Platform Version: ");
			builder.append(AndroidUtils.getPlatformVersion());
			builder.append(NEW_LINE);
			builder.append(NEW_LINE);
			
			builder.append(intent.getStringExtra(EXTRA_STACK_TRACE));
			builder.append(NEW_LINE);
			
			mailService.sendMail("[Android Error] " + packageName + " v" + versionName, builder.toString());
			
		} catch (MailException e) {
			// Retry at a later point in time
			Log.e(TAG, "Error while sending an e-Mail error report...", e);
			// int count = intent.getIntExtra(EXTRA_CURRENT_RETRY_COUNT, 0);
			// intent.putExtra(EXTRA_CURRENT_RETRY_COUNT, count + 1);
			// PendingIntent operation = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			// if (count >= MAXIMUM_RETRY_COUNT) {
			// // Discard error
			// Log.w(TAG, "Error report reached the maximum retry count and will be discarded.", e);
			// return;
			// }
			// // Retry every one hour
			// long backoff = (count + 1) * 3600000;
			// AlarmManagerUtils.scheduleAlarm(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + backoff,
			// operation);
		}
	}
}
