package com.spm.android.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.spm.R;

/**
 * {@link Dialog} that displays a Loading text and a {@link ProgressBar} on indeterminate mode
 * 
 * @author Luciano Rey
 */
public class NumDialog extends Dialog {
	
	EditText num;
	Integer number;
	
	/**
	 * Constructor
	 * 
	 * @param context The Context in which the Dialog should run.
	 */
	
	/**
	 * Constructor
	 * 
	 * @param context The Context in which the Dialog should run.
	 * @param theme A style resource describing the theme to use for the window.
	 */
	public NumDialog(Context context, Integer q) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.num_dialog);
		
		num = (EditText)findViewById(R.id.num);
		num.setText(q.toString());
		
		Button confirm = (Button)findViewById(R.id.confirm);
		confirm.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("numero", num.getText().toString());
				getOwnerActivity().setResult(-1, intent);
				cancel();
			}
		});
		
	}
	
}
