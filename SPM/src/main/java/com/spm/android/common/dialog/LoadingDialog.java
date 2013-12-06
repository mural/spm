package com.spm.android.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;
import com.spm.R;

/**
 * {@link Dialog} that displays a Loading text and a {@link ProgressBar} on indeterminate mode
 * 
 * @author Luciano Rey
 */
public class LoadingDialog extends Dialog {
	
	/**
	 * Constructor
	 * 
	 * @param context The Context in which the Dialog should run.
	 */
	public LoadingDialog(Context context) {
		this(context, R.style.CustomDialog);
	}
	
	/**
	 * Constructor
	 * 
	 * @param context The Context in which the Dialog should run.
	 * @param theme A style resource describing the theme to use for the window.
	 */
	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading_dialog);
	}
}
