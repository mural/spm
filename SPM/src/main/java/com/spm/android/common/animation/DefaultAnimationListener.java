package com.spm.android.common.animation;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * {@link AnimationListener} that does nothing by default.
 * 
 * @author Estefania Caravatti
 */
public class DefaultAnimationListener implements AnimationListener {
	
	/**
	 * @see android.view.animation.Animation.AnimationListener#onAnimationStart(android.view.animation.Animation)
	 */
	@Override
	public void onAnimationStart(Animation animation) {
		// nothing by default
	}
	
	/**
	 * @see android.view.animation.Animation.AnimationListener#onAnimationEnd(android.view.animation.Animation)
	 */
	@Override
	public void onAnimationEnd(Animation animation) {
		// nothing by default
	}
	
	/**
	 * @see android.view.animation.Animation.AnimationListener#onAnimationRepeat(android.view.animation.Animation)
	 */
	@Override
	public void onAnimationRepeat(Animation animation) {
		// nothing by default
	}
	
}
