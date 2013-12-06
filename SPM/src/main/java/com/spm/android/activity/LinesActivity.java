package com.spm.android.activity;

import android.content.Intent;
import android.os.Bundle;
import com.spm.R;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.activity.AbstractListActivity;
import com.spm.common.utils.IdGenerator;
import com.spm.domain.Line;
import com.spm.domain.User;

/**
 * 
 * @author Agustin Sgarlata
 */
public class LinesActivity extends AbstractListActivity<Line> {
	
	private LinesUseCase linesUseCase;
	private LinesAdapter linesAdapter;
	
	public final static int REQUEST_CODE = IdGenerator.getIntId();
	
	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lines_activity);
		
		linesUseCase = (LinesUseCase)getLastNonConfigurationInstance();
		if (linesUseCase == null) {
			linesUseCase = getInstance(LinesUseCase.class);
		}
		
		linesUseCase.addListener(this);
		if (linesUseCase.isNotInvoked()) {
			executeUseCase(linesUseCase);
		} else if (linesUseCase.isInProgress()) {
			onStartUseCase();
		} else if (linesUseCase.isFinishSuccessful()) {
			onFinishUseCase();
		} else if (linesUseCase.isFinishFailed()) {
			onFinishUseCase();
		}
	}
	
	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		User user = AndroidApplication.get().getUser();
		if (user == null) {
			ActivityLauncher.launchActivity(LoginActivity.class);
			finish();
		}
	}
	
	/**
	 * @see roboguice.activity.RoboListActivity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return linesUseCase;
	}
	
	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onListItemClick(java.lang.Object)
	 */
	@Override
	protected void onListItemClick(Line line) {
		super.onListItemClick(line);
		
		// Bundle bundle = new Bundle();
		// bundle.putLong(ProductsActivity.LINE_ID, line.getId());
		// ActivityLauncher.launchActivity(ProductsActivity.class, bundle, ProductsActivity.REQUEST_CODE);
	}
	
	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		
		linesAdapter = new LinesAdapter(this, linesUseCase.getLines());
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				setListAdapter(linesAdapter);
				dismissLoading();
			}
		});
	}
	
	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		linesUseCase.removeListener(this);
	}
	
	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == ProductsActivity.REQUEST_CODE) && (resultCode == RESULT_OK)) {
			setResult(resultCode, data);
			finish();
		}
	}
}
