package com.spm.android.common.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import com.spm.android.common.AndroidApplication;

/**
 * 
 * @author Maxi Rosson
 */
public class AlarmManagerUtils {
	
	public static void scheduleAlarm(int type, long triggerAtTime, PendingIntent operation) {
		Context context = AndroidApplication.get();
		AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmMgr.set(type, triggerAtTime, operation);
	}
	
}
