package com.spm.android.common.utils;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * Manager that control the acquire and release of the different kinds of {@link WakeLock}s
 * 
 * @author Maxi Rosson
 */
public class WakeLockManager {
	
	private final static String TAG = WakeLockManager.class.getSimpleName();
	
	private static PowerManager.WakeLock screenDimWakeLock;
	private static PowerManager.WakeLock partialWakeLock;
	
	/**
	 * Makes sure the device is on at the {@link PowerManager#SCREEN_DIM_WAKE_LOCK} level when you created the wake
	 * lock.
	 * 
	 * @param context The {@link Context}
	 */
	public static void acquireScreenDimWakeLock(Context context) {
		screenDimWakeLock = aquire(context, screenDimWakeLock, PowerManager.SCREEN_DIM_WAKE_LOCK);
	}
	
	/**
	 * Release your claim to the {@link PowerManager#SCREEN_DIM_WAKE_LOCK} being on.
	 */
	public static void releaseScreenDimWakeLock() {
		screenDimWakeLock.release();
	}
	
	/**
	 * Makes sure the device is on at the {@link PowerManager#PARTIAL_WAKE_LOCK} level when you created the wake lock.
	 * 
	 * @param context The {@link Context}
	 */
	public static void acquirePartialWakeLock(Context context) {
		partialWakeLock = aquire(context, partialWakeLock, PowerManager.PARTIAL_WAKE_LOCK);
	}
	
	/**
	 * Release your claim to the {@link PowerManager#PARTIAL_WAKE_LOCK} being on.
	 */
	public static void releasePartialWakeLock() {
		partialWakeLock.release();
	}
	
	private static PowerManager.WakeLock aquire(Context context, PowerManager.WakeLock wakeLock, int flags) {
		if (wakeLock == null) {
			PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(flags, TAG);
		}
		wakeLock.acquire();
		return wakeLock;
	}
}
