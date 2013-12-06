package com.spm.android.common;

import com.spm.android.common.activity.ActivityIf;
import com.spm.common.exception.AndroidException;
import com.spm.common.usecase.listener.DefaultUseCaseListener;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AndroidUseCaseListener implements DefaultUseCaseListener {
	
	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		getActivityIf().onStartUseCase();
	}
	
	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(com.spm.common.exception.AndroidException)
	 */
	@Override
	public void onFinishFailedUseCase(AndroidException androidException) {
		getActivityIf().onFinishFailedUseCase(androidException);
	}
	
	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		getActivityIf().onFinishUseCase();
	}
	
	protected abstract ActivityIf getActivityIf();
}
