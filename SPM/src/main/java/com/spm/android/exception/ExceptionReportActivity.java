package com.spm.android.exception;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.spm.android.common.utils.AndroidUtils;
import com.spm.R;

/**
 * 
 * @author Maxi Rosson
 */
public class ExceptionReportActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_NoDisplay);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getString(R.string.exceptionReportDialogTitle, AndroidUtils.getApplicationName()));
		dialog.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
		dialog.setMessage(getString(R.string.exceptionReportDialogText, AndroidUtils.getApplicationName()));
		dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = getIntent();
				intent.setClass(ExceptionReportActivity.this, ExceptionReportService.class);
				startService(intent);
				dialog.dismiss();
				finish();
			}
		});
		dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		});
		dialog.show();
	}
}
