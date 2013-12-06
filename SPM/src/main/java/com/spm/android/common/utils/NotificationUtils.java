package com.spm.android.common.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import com.spm.android.common.AndroidApplication;
import com.spm.R;

/**
 * 
 * @author Maxi Rosson
 */
public class NotificationUtils {
	
	private final static NotificationManager NOTIFICATION_MANAGER = (NotificationManager)AndroidApplication.get().getSystemService(
		Context.NOTIFICATION_SERVICE);
	
	public static void sendNotification(int id, int icon, int tickerText, int contentTitle, int contentText) {
		sendNotification(id, icon, tickerText, contentTitle, contentText, null);
	}
	
	public static void sendNotification(int id, int icon, int tickerText, int contentTitle, int contentText,
			Class<?> clazz) {
		sendNotification(id, icon, tickerText, contentTitle, contentText, clazz, System.currentTimeMillis(), null);
	}
	
	public static void sendNotification(int id, int icon, int tickerText, int contentTitle, int contentText,
			Class<?> clazz, long when) {
		sendNotification(id, icon, tickerText, contentTitle, contentText, clazz, when, null);
	}
	
	public static void sendNotification(int id, int icon, int tickerText, int contentTitle, int contentText,
			Class<?> clazz, long when, Bundle notificationBundle) {
		Context context = AndroidApplication.get();
		sendNotification(id, icon, context.getString(tickerText), context.getString(contentTitle),
			context.getString(contentText), clazz, when, notificationBundle);
	}
	
	public static void sendNotification(int id, int icon, String tickerText, String contentTitle, String contentText,
			Class<?> clazz, long when, Bundle notificationBundle) {
		
		Notification notification = createNotification(icon, tickerText, clazz, when, notificationBundle);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		Context context = AndroidApplication.get();
		notification.setLatestEventInfo(context, contentTitle, contentText, notification.contentIntent);
		
		sendNotification(id, notification);
	}
	
	public static void sendNotification(int id, int statusBarIcon, int notificationIcon, int tickerText,
			int contentTitle, int contentText, Class<?> clazz) {
		sendNotification(id, statusBarIcon, notificationIcon, tickerText, contentTitle, contentText, clazz,
			System.currentTimeMillis(), null);
	}
	
	public static void sendNotification(int id, int statusBarIcon, int notificationIcon, int tickerText,
			int contentTitle, int contentText, Class<?> clazz, long when) {
		sendNotification(id, statusBarIcon, notificationIcon, tickerText, contentTitle, contentText, clazz, when, null);
	}
	
	public static void sendNotification(int id, int statusBarIcon, int notificationIcon, int tickerText,
			int contentTitle, int contentText, Class<?> clazz, long when, Bundle notificationBundle) {
		
		Notification notification = createNotification(statusBarIcon, tickerText, clazz, when, notificationBundle);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		Context context = AndroidApplication.get();
		notification.contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
		notification.contentView.setImageViewResource(R.id.icon, notificationIcon);
		notification.contentView.setTextViewText(R.id.title, context.getString(contentTitle));
		notification.contentView.setTextViewText(R.id.text, context.getString(contentText));
		notification.contentView.setLong(R.id.time, "setTime", when);
		
		sendNotification(id, notification);
	}
	
	public static void sendInProgressNotification(int id, int statusBarIcon, int notificationIcon, int progress,
			int tickerText, int contentTitle, int actionText, Class<?> clazz) {
		sendInProgressNotification(id, statusBarIcon, notificationIcon, progress, tickerText, contentTitle, actionText,
			clazz, null);
	}
	
	public static void sendInProgressNotification(int id, int statusBarIcon, int notificationIcon, int progress,
			int tickerText, int contentTitle, int actionText, Class<?> clazz, Bundle notificationBundle) {
		
		Notification notification = createNotification(statusBarIcon, tickerText, clazz, 0, notificationBundle);
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		
		Context context = AndroidApplication.get();
		notification.contentView = new RemoteViews(context.getPackageName(), R.layout.notification_inprogress);
		notification.contentView.setTextViewText(R.id.title, context.getString(contentTitle));
		notification.contentView.setTextViewText(R.id.action, context.getString(actionText));
		notification.contentView.setTextViewText(R.id.progressPercentage, progress + "%");
		notification.contentView.setProgressBar(R.id.progressBar, 100, progress, false);
		notification.contentView.setImageViewResource(R.id.icon, notificationIcon);
		
		sendNotification(id, notification);
	}
	
	private static Notification createNotification(int statusBarIcon, int tickerText, Class<?> clazz, long when,
			Bundle notificationBundle) {
		return createNotification(statusBarIcon, AndroidApplication.get().getString(tickerText), clazz, when,
			notificationBundle);
	}
	
	private static Notification createNotification(int statusBarIcon, String tickerText, Class<?> clazz, long when,
			Bundle notificationBundle) {
		Context context = AndroidApplication.get();
		Notification notification = new Notification(statusBarIcon, tickerText, when);
		
		Intent notificationIntent = clazz != null ? new Intent(context, clazz) : new Intent();
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (notificationBundle != null) {
			notificationIntent.replaceExtras(notificationBundle);
		}
		notification.contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		return notification;
	}
	
	public static void sendNotification(int id, Notification notification) {
		NOTIFICATION_MANAGER.notify(id, notification);
	}
	
	public static void cancelNotification(int id) {
		NOTIFICATION_MANAGER.cancel(id);
	}
}