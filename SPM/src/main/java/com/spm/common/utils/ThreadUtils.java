package com.spm.common.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.util.Log;

/**
 * @author Luciano Rey
 */
public final class ThreadUtils {
	
	private static final String TAG = ThreadUtils.class.getSimpleName();
	
	// Default amount of thread inside the pool
	private static final int DEFAULT_THREAD_POOL_SIZE = 10;
	
	private static final String THREAD_POOL_SIZE_PROPERTY = "thread.pool.size";
	
	private static final Executor executor = Executors.newFixedThreadPool(PropertiesUtils.getIntegerProperty(
		THREAD_POOL_SIZE_PROPERTY, DEFAULT_THREAD_POOL_SIZE));
	
	/**
	 * @param runnable The {@link Runnable} task
	 */
	public static void execute(Runnable runnable) {
		ThreadUtils.executor.execute(runnable);
	}
	
	/**
	 * @param seconds The time to sleep in seconds.
	 */
	public static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			Log.e(TAG, "Error sleep in millis", e);
		}
	}
	
	/**
	 * @param millis The time to sleep in milliseconds.
	 */
	public static void sleepInMillis(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Log.e(TAG, "Error sleep in millis", e);
		}
	}
	
}
