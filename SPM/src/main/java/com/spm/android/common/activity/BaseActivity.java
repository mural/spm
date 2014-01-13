package com.spm.android.common.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.spm.android.common.AndroidApplication;
import com.spm.android.common.dialog.LoadingDialog;
import com.spm.android.common.view.ActionBar;
import com.spm.common.exception.AndroidException;
import com.spm.common.usecase.DefaultUseCase;
import com.spm.common.utils.ThreadUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class BaseActivity implements ActivityIf {

	private final static String TAG = BaseActivity.class.getSimpleName();

	private Activity activity;
	private LoadingDialog loadingDialog;
	private ActionBar actionBar;

	/**
	 * @param activity
	 */
	public BaseActivity(Activity activity) {
		this.activity = activity;
	}

	public ActivityIf getActivityIf() {
		return (ActivityIf) activity;
	}

	protected Activity getActivity() {
		return activity;
	}

	public void onCreate() {
		Log.v(TAG, "Executing onCreate on " + activity);
		AndroidApplication.get().setCurrentActivity(activity);
	}

	public void onSaveInstanceState(Bundle outState) {
		Log.v(TAG, "Executing onSaveInstanceState on " + activity);
		dismissLoading();
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.v(TAG, "Executing onRestoreInstanceState on " + activity);
	}

	public void onStart() {
		Log.v(TAG, "Executing onStart on " + activity);
		AndroidApplication.get().setCurrentActivity(activity);
	}

	public void onResume() {
		Log.v(TAG, "Executing onResume on " + activity);
		AndroidApplication.get().setInBackground(false);
		AndroidApplication.get().setCurrentActivity(activity);
	}

	public void onPause() {
		Log.v(TAG, "Executing onPause on " + activity);
		AndroidApplication.get().setInBackground(true);
	}

	public void onStop() {
		Log.v(TAG, "Executing onStop on " + activity);
	}

	public void onDestroy() {
		Log.v(TAG, "Executing onDestroy on " + activity);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		AndroidApplication.get().setCurrentActivity(activity);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		if (getActivityIf().hasOptionsMenu()) {
			MenuInflater inflater = activity.getMenuInflater();
			inflater.inflate(getActivityIf().getMenuResourceId(), menu);
			getActivityIf().doOnCreateOptionsMenu(menu);
		}
		return true;
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#hasOptionsMenu()
	 */
	@Override
	public boolean hasOptionsMenu() {
		return false;
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#getMenuResourceId()
	 */
	@Override
	public int getMenuResourceId() {
		return 0;
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#doOnCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		// Do Nothing by Default
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// TODO Uncomment on Android 3.0 or greater
		// case android.R.id.home:
		// ActivityLauncher.launchHomeActivity();
		// return true;
		default:
			return false;
		}
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#showLoadingOnUIThread()
	 */
	@Override
	public void showLoadingOnUIThread() {
		showLoadingOnUIThread(true);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#showLoadingOnUIThread(java.lang.Boolean)
	 */
	@Override
	public void showLoadingOnUIThread(final Boolean cancelable) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showLoading(cancelable);
			}
		});
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#showLoading()
	 */
	@Override
	public void showLoading() {
		showLoading(true);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#showLoading(java.lang.Boolean)
	 */
	@Override
	public void showLoading(Boolean cancelable) {
		try {
			if ((loadingDialog == null) || (!loadingDialog.isShowing())) {
				loadingDialog = new LoadingDialog(activity);
				loadingDialog.setOnCancelListener((OnCancelListener) activity);
				loadingDialog.setCancelable(cancelable);
				loadingDialog.show();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		try {
			if (loadingDialog != null) {

				loadingDialog.dismiss();
				loadingDialog = null;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#dismissLoadingOnUIThread()
	 */
	@Override
	public void dismissLoadingOnUIThread() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				dismissLoading();
			}
		});
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		if (activity.equals(AndroidApplication.get().getCurrentActivity())) {
			activity.runOnUiThread(runnable);
		}
	}

	/**
	 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		activity.setResult(Activity.RESULT_CANCELED);
		activity.finish();
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return AndroidApplication.get().getInjector().getInstance(clazz);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#getExtra(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <E> E getExtra(String key) {
		return (E) activity.getIntent().getExtras().get(key);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#executeUseCase(com.spm.common.usecase.DefaultUseCase)
	 */
	@Override
	public void executeUseCase(DefaultUseCase<?> useCase) {
		ThreadUtils.execute(useCase);
	}

	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		getActivityIf().showLoadingOnUIThread();
	}

	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		// Do nothing by default
	}

	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(com.spm.common.exception.AndroidException)
	 */
	@Override
	public void onFinishFailedUseCase(AndroidException androidException) {
		getActivityIf().dismissLoadingOnUIThread();
		throw androidException;
	}

	/**
	 * @see com.spm.android.common.fragment.FragmentIf#findView(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <V extends View> V findView(int id) {
		return (V) getActivity().findViewById(id);
	}

	/**
	 * @see com.spm.android.common.fragment.FragmentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return LayoutInflater.from(getActivity()).inflate(resource, null);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#isAuthenticated()
	 */
	@Override
	public Boolean isAuthenticated() {
		return true;
	}

	/**
	 * @return the ActionBar
	 */
	@Override
	public ActionBar getActionBarLegacy() {
		return actionBar;
	}
}
