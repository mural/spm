package com.spm.android.common.listener;

import android.view.View;
import android.view.View.OnClickListener;
import com.spm.common.utils.ThreadUtils;

/**
 * Asynchronous {@link OnClickListener}
 * 
 * @author Maxi Rosson
 */
public abstract class AsyncOnClickListener implements OnClickListener {
	
	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public final void onClick(final View view) {
		ThreadUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				onAsyncRun(view);
			}
		});
	}
	
	/**
	 * Called when a view has been clicked.
	 * 
	 * @param view The view that was clicked.
	 */
	public abstract void onAsyncRun(View view);
	
}
