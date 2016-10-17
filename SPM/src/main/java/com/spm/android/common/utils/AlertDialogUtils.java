package com.spm.android.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Handler;
import com.spm.R;
import com.spm.android.common.AndroidApplication;

/**
 * @author Luciano Rey
 */
public final class AlertDialogUtils {
	
	private static final int DIALOG = 2;
	private static final int BUILDER = 3;
	
	private static final Handler HANDLER = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case DIALOG:
					AlertDialog dialog = (AlertDialog)msg.obj;
					dialog.show();
					break;
				case BUILDER:
					AlertDialog.Builder builder = (Builder)msg.obj;
					builder.create().show();
					break;
			}
		};
	};
	
	public static final void init() {
		// nothing...
	}
	
	/**
	 * @param builder
	 */
	public static void show(AlertDialog.Builder builder) {
		HANDLER.sendMessage(HANDLER.obtainMessage(BUILDER, builder));
	}
	
	/**
	 * @param alertDialog
	 */
	public static void show(AlertDialog alertDialog) {
		HANDLER.sendMessage(HANDLER.obtainMessage(DIALOG, alertDialog));
	}
	
	public static void showOKDialog(int titleResId, int messageResId) {
		showOKDialog(LocalizationUtils.getString(titleResId), LocalizationUtils.getString(messageResId), null,
			LocalizationUtils.getString(R.string.ok));
	}
	
	public static void showOKDialog(int titleResId, int messageResId, DialogInterface.OnClickListener onClickListener) {
		showOKDialog(LocalizationUtils.getString(titleResId), LocalizationUtils.getString(messageResId),
			onClickListener, LocalizationUtils.getString(R.string.ok));
	}
	
	public static void showOKDialog(String title, String message, DialogInterface.OnClickListener onClickListener,
			String buttonText) {
		Activity activity = AndroidApplication.get().getCurrentActivity();
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity, R.style.CustomAlertDialogTheme);
		downloadDialog.setTitle(title);
		downloadDialog.setMessage(message);
		downloadDialog.setPositiveButton(buttonText, onClickListener);
		AlertDialogUtils.show(downloadDialog);
	}
	
	public static void showUnsavedChangesDialog() {
		final Activity activity = AndroidApplication.get().getCurrentActivity();
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAlertDialogTheme);
		builder.setTitle(R.string.areYouSure);
		builder.setMessage(R.string.confirmExit);
		builder.setPositiveButton(R.string.yes, dialogClickListener);
		builder.setNegativeButton(R.string.no, null);
		builder.show();
	}
	
	public static void showOkCancelDialog(DialogInterface.OnClickListener dialogClickListener, int title, int message,
			int positiveButton, int negativeButton) {
		final Activity activity = AndroidApplication.get().getCurrentActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAlertDialogTheme);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(positiveButton, dialogClickListener);
		builder.setNegativeButton(negativeButton, null);
		builder.show();
	}
	
	public static void show2OptionsDialog(DialogInterface.OnClickListener dialogClickListener1,
			DialogInterface.OnClickListener dialogClickListener2, int title, int message, int option1Button,
			int option2Button) {
		final Activity activity = AndroidApplication.get().getCurrentActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(option1Button, dialogClickListener1);
		builder.setNegativeButton(option2Button, dialogClickListener2);
		builder.show();
	}
	
	public static void showExitOkCancelDialog() {
		final Activity activity = AndroidApplication.get().getCurrentActivity();
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAlertDialogTheme);
		builder.setTitle(R.string.confirmExitTitle);
		builder.setMessage(R.string.confirmExitMsg);
		builder.setPositiveButton(R.string.exit, dialogClickListener);
		builder.setNegativeButton(R.string.notExit, null);
		builder.show();
	}
}
