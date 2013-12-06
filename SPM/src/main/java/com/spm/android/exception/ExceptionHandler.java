package com.spm.android.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import android.os.Handler;
import android.util.Log;
import com.spm.android.common.utils.ToastUtils;
import com.spm.common.domain.Application;
import com.spm.common.domain.Application.ApplicationProvider;
import com.spm.common.exception.ApplicationException;
import com.spm.common.exception.LocalBusinessException;
import com.spm.common.exception.ServerBusinessException;

/**
 * 
 * @author Maxi Rosson
 */
public class ExceptionHandler extends Handler implements UncaughtExceptionHandler {
	
	private static final String TAG = ExceptionHandler.class.getSimpleName();
	private static final String MAIN_THREAD_NAME = "main";
	private static final ExceptionHandler INSTANCE = new ExceptionHandler();
	
	private UncaughtExceptionHandler defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	private ExceptionReporter reporter = new ExceptionReporter();
	
	/**
	 * Constructor
	 */
	private ExceptionHandler() {
		// nothing...
	}
	
	/**
	 * @return The singleton instance
	 */
	public static ExceptionHandler getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		
		Boolean mainThread = thread.getName().equals(MAIN_THREAD_NAME);
		
		if (mainThread) {
			handleException(thread, throwable);
		} else if (throwable instanceof LocalBusinessException) {
			handleException(thread, (LocalBusinessException)throwable);
		} else if (throwable instanceof ServerBusinessException) {
			handleException(thread, (ServerBusinessException)throwable);
		} else if (throwable instanceof ApplicationException) {
			handleException(thread, (ApplicationException)throwable);
		} else {
			handleException(thread, throwable);
		}
	}
	
	public void handleException(LocalBusinessException businessException) {
		handleException(Thread.currentThread(), businessException);
	}
	
	public void handleException(Thread thread, LocalBusinessException businessException) {
		String message = businessException.getMessage();
		if (businessException.getErrorCode() != null) {
			message = ErrorCodeRegistry.get().getMessageFor(businessException.getErrorCode(),
				businessException.getErrorCodeParameter());
		}
		ToastUtils.showToastOnUIThread(message);
	}
	
	public void handleException(Thread thread, ServerBusinessException businessException) {
		ToastUtils.showToastOnUIThread(businessException.getMessage());
	}
	
	public void handleException(Thread thread, ApplicationException applicationException) {
		String message = ErrorCodeRegistry.get().getMessageFor(applicationException.getErrorCode());
		Log.e(TAG, message, applicationException);
		ToastUtils.showToastOnUIThread(message);
	}
	
	private void handleException(Thread thread, Throwable throwable) {
		try {
			String user = "";
			if (Application.APPLICATION_PROVIDER.get().getUser() != null) {
				user = Application.APPLICATION_PROVIDER.get().getUser().getUserName();
			}
			if (ApplicationProvider.MAIL_REPORTING) {
				reporter.reportException(thread, throwable, user);
			}
		} catch (Exception e) {
			Log.e(TAG, "Unexepected error from the exception reporter", e);
		} finally {
			defaultExceptionHandler.uncaughtException(thread, throwable);
		}
	}
}
