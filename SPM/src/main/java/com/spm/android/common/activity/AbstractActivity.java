package com.spm.android.common.activity;

import roboguice.activity.RoboActivity;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.spm.android.common.AndroidApplication;
import com.spm.android.common.view.ActionBar;
import com.spm.common.exception.AndroidException;
import com.spm.common.usecase.DefaultUseCase;

/**
 * Base {@link Activity}
 * 
 * @author Luciano Rey
 */
public abstract class AbstractActivity extends RoboActivity implements
		ActivityIf {

	private BaseActivity baseActivity;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseActivity = AndroidApplication.get().createBaseActivity(this);
		baseActivity.onCreate();
	}

	/**
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		baseActivity.onSaveInstanceState(outState);
	}

	/**
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		baseActivity.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * @see roboguice.activity.GuiceActivity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		baseActivity.onStart();
	}

	/**
	 * @see roboguice.activity.GuiceActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		baseActivity.onResume();
	}

	/**
	 * @see roboguice.activity.RoboActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		baseActivity.onPause();
	}

	/**
	 * @see roboguice.activity.RoboActivity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		baseActivity.onStop();
	}

	/**
	 * @see roboguice.activity.RoboActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		baseActivity.onDestroy();
	}

	/**
	 * @see android.app.Activity#onActivityResult(int, int,
	 *      android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		baseActivity.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return baseActivity.onCreateOptionsMenu(menu);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#hasOptionsMenu()
	 */
	@Override
	public boolean hasOptionsMenu() {
		return baseActivity.hasOptionsMenu();
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#getMenuResourceId()
	 */
	@Override
	public int getMenuResourceId() {
		return baseActivity.getMenuResourceId();
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#doOnCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		baseActivity.doOnCreateOptionsMenu(menu);
	}

	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// REVIEW See if this is the correct approach
		return baseActivity.onOptionsItemSelected(item) ? true : super
				.onOptionsItemSelected(item);
	}

	/**
	 * @see com.spm.android.common.fragment.FragmentIf#findView(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(int id) {
		return (V) findViewById(id);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#showLoadingOnUIThread()
	 */
	@Override
	public void showLoadingOnUIThread() {
		baseActivity.showLoadingOnUIThread();
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#showLoadingOnUIThread(java.lang.Boolean)
	 */
	@Override
	public void showLoadingOnUIThread(Boolean cancelable) {
		baseActivity.showLoadingOnUIThread(cancelable);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#showLoading()
	 */
	@Override
	public void showLoading() {
		baseActivity.showLoading();
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#showLoading(java.lang.Boolean)
	 */
	@Override
	public void showLoading(Boolean cancelable) {
		baseActivity.showLoading(cancelable);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		baseActivity.dismissLoading();
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#dismissLoadingOnUIThread()
	 */
	@Override
	public void dismissLoadingOnUIThread() {
		baseActivity.dismissLoadingOnUIThread();
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		baseActivity.executeOnUIThread(runnable);
	}

	/**
	 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		baseActivity.onCancel(dialog);
	}

	/**
	 * @see com.spm.android.common.fragment.FragmentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return baseActivity.inflate(resource);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return baseActivity.getInstance(clazz);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return baseActivity.<E> getExtra(key);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#executeUseCase(com.spm.common.usecase.DefaultUseCase)
	 */
	@Override
	public void executeUseCase(DefaultUseCase<?> useCase) {
		baseActivity.executeUseCase(useCase);
	}

	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		baseActivity.onStartUseCase();
	}

	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		baseActivity.onFinishUseCase();
	}

	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(com.spm.common.exception.AndroidException)
	 */
	@Override
	public void onFinishFailedUseCase(AndroidException androidException) {
		baseActivity.onFinishFailedUseCase(androidException);
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#isAuthenticated()
	 */
	@Override
	public Boolean isAuthenticated() {
		return baseActivity.isAuthenticated();
	}

	/**
	 * @see com.spm.android.common.activity.ActivityIf#getActionBarLegacy()
	 */
	@Override
	public ActionBar getActionBarLegacy() {
		return baseActivity.getActionBarLegacy();
	}
}
