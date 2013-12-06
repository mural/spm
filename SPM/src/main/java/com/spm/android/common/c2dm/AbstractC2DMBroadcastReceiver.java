package com.spm.android.common.c2dm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * {@link BroadcastReceiver} to receive C2DM {@link Intent}s.
 * 
 * You need to add the following receiver xml inside the application tag of your manifest:
 * 
 * <pre>
 * &lt;receiver android:name=".android.c2dm.C2DMBroadcastReceiver"
 * 	android:permission="com.google.android.c2dm.permission.SEND">
 * 	&lt;!-- Receive actual messages -->
 * 	&lt;intent-filter>
 * 		&lt;action android:name="com.google.android.c2dm.intent.RECEIVE" />
 * 		&lt;category android:name="[app package]" />
 * 	&lt;/intent-filter>
 * 	&lt;!-- Receive registration ids -->
 * 	&lt;intent-filter>
 * 		&lt;action android:name="com.google.android.c2dm.intent.REGISTRATION" />
 * 		&lt;category android:name="[app package]" />
 * 	&lt;/intent-filter>
 * &lt;/receiver>
 * </pre>
 * 
 */
public abstract class AbstractC2DMBroadcastReceiver extends BroadcastReceiver {
	
	/**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public final void onReceive(Context context, Intent intent) {
		// This receiver can only run for a limited amount of time. It must start a real
		// service for longer activity, must get the power lock, must make sure it's released when all done.
		AbstractC2DMService.runIntentInService(context, intent, getC2DMServiceClass());
		setResultCode(Activity.RESULT_OK);
	}
	
	protected abstract Class<? extends AbstractC2DMService> getC2DMServiceClass();
}
