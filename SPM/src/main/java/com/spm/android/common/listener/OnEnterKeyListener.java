package com.spm.android.common.listener;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import com.spm.android.common.utils.AndroidUtils;
import com.spm.common.utils.ThreadUtils;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class OnEnterKeyListener implements OnKeyListener {
	
	private Boolean async;
	
	public OnEnterKeyListener() {
		this(true);
	}
	
	/**
	 * @param async
	 */
	public OnEnterKeyListener(Boolean async) {
		this.async = async;
	}
	
	/**
	 * @see android.view.View.OnKeyListener#onKey(android.view.View, int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKey(final View v, int keyCode, KeyEvent event) {
		
		if (((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_UP))) {
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					AndroidUtils.hideSoftInput(v);
					onRun(v);
				}
			};
			if (async) {
				ThreadUtils.execute(runnable);
			} else {
				runnable.run();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Called when the search key on the keyword is released.
	 * 
	 * @param view The view the key has been dispatched to.
	 */
	public abstract void onRun(View view);
	
}
