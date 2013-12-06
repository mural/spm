package com.spm.common.usecase.listener;

import com.spm.common.exception.AndroidException;

/**
 * Default Use Case Listener
 * 
 * @author Maxi Rosson
 */
public interface DefaultUseCaseListener {
	
	/**
	 * Called before the use case starts
	 */
	public void onStartUseCase();
	
	/**
	 * Called after the use case fails
	 * 
	 * @param androidException The {@link AndroidException} with the error
	 */
	public void onFinishFailedUseCase(AndroidException androidException);
	
	/**
	 * Called when the use case finishes successfully
	 */
	public void onFinishUseCase();
	
}
