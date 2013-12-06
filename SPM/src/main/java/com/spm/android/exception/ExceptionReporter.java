package com.spm.android.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import android.content.Context;
import android.os.Bundle;
import com.spm.R;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.utils.AndroidUtils;
import com.spm.android.common.utils.NotificationUtils;
import com.spm.common.utils.DateUtils;
import com.spm.common.utils.IdGenerator;

/**
 * 
 * @author Maxi Rosson
 */
public class ExceptionReporter {
	
	/**
	 * Sends an error report.
	 * 
	 * @param thread The thread where the exception occurred (e.g. {@link java.lang.Thread#currentThread()})
	 * @param ex The exception
	 */
	public void reportException(Thread thread, Throwable ex, String user) {
		
		Context context = AndroidApplication.get();
		
		Writer writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		
		Date now = DateUtils.now();
		Bundle bundle = new Bundle();
		bundle.putString(ExceptionReportService.EXTRA_THREAD_NAME, thread.getName());
		bundle.putString(ExceptionReportService.EXTRA_EXCEPTION_TIME,
			DateUtils.unTransform(now, DateUtils.YYYYMMDDHHMMSSZ_DATE_FORMAT));
		bundle.putString(ExceptionReportService.EXTRA_STACK_TRACE, writer.toString());
		if (user != null) {
			bundle.putString(ExceptionReportService.EXTRA_USER_NAME, user);
		}
		
		String notificationTitle = context.getString(R.string.exceptionReportNotificationTitle,
			AndroidUtils.getApplicationName());
		String notificationText = context.getString(R.string.exceptionReportNotificationText);
		
		NotificationUtils.sendNotification(IdGenerator.getRandomIntId(), android.R.drawable.stat_notify_error,
			notificationTitle, notificationTitle, notificationText, ExceptionReportActivity.class, now.getTime(),
			bundle);
	}
}
