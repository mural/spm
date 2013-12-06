package com.spm.android.demo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.spm.android.common.activity.AbstractActivity;
import com.spm.common.exception.ApplicationException;
import com.spm.common.exception.CommonErrorCode;
import com.spm.common.exception.LocalBusinessException;
import com.spm.common.exception.UnexpectedException;
import com.spm.common.utils.ThreadUtils;
import com.spm.R;

public class ExceptionsActivity extends AbstractActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exceptions_activity);
		
		Button businessUIThread = findView(R.id.businessUIThread);
		businessUIThread.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				throw new LocalBusinessException(CommonErrorCode.CONNECTION_ERROR);
			}
		});
		
		Button businessNewThread = findView(R.id.businessNewThread);
		businessNewThread.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ThreadUtils.execute(new Runnable() {
					
					@Override
					public void run() {
						throw new LocalBusinessException(CommonErrorCode.CONNECTION_ERROR);
						
					}
				});
			}
		});
		
		Button appUIThread = findView(R.id.applicationUIThread);
		appUIThread.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				throw new ApplicationException(CommonErrorCode.CONNECTION_ERROR, new RuntimeException("appUIThread"));
			}
		});
		
		Button appNewThread = findView(R.id.applicationNewThread);
		appNewThread.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ThreadUtils.execute(new Runnable() {
					
					@Override
					public void run() {
						throw new ApplicationException(CommonErrorCode.CONNECTION_ERROR, new RuntimeException(
								"appNewThread"));
						
					}
				});
			}
		});
		
		Button unexpectedUIThread = findView(R.id.unexpectedUIThread);
		unexpectedUIThread.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				throw new UnexpectedException("unexpectedUIThread");
			}
		});
		
		Button unexpectedNewThread = findView(R.id.unexpectedNewThread);
		unexpectedNewThread.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ThreadUtils.execute(new Runnable() {
					
					@Override
					public void run() {
						throw new UnexpectedException("unexpectedNewThread");
						
					}
				});
			}
		});
		
		Button otherUIThread = findView(R.id.otherUIThread);
		otherUIThread.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				throw new RuntimeException("otherUIThread");
			}
		});
		
		Button otherNewThread = findView(R.id.otherNewThread);
		otherNewThread.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ThreadUtils.execute(new Runnable() {
					
					@Override
					public void run() {
						throw new RuntimeException("otherNewThread");
						
					}
				});
			}
		});
	}
}