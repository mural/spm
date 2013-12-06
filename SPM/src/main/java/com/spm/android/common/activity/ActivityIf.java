package com.spm.android.common.activity;

import android.app.Activity;
import android.content.DialogInterface.OnCancelListener;
import android.view.Menu;
import com.spm.android.common.fragment.FragmentIf;
import com.spm.android.common.view.ActionBar;
import com.spm.common.usecase.listener.DefaultUseCaseListener;

/**
 * 
 * @author Maxi Rosson
 */
public interface ActivityIf extends FragmentIf, OnCancelListener, DefaultUseCaseListener {
	
	/**
	 * @return Whether the {@link Activity} has an options menu
	 */
	public boolean hasOptionsMenu();
	
	public int getMenuResourceId();
	
	public void doOnCreateOptionsMenu(Menu menu);
	
	/**
	 * @return Whether this {@link Activity} requires authentication or not
	 */
	public Boolean isAuthenticated();
	
	public ActionBar getActionBarLegacy();
}
