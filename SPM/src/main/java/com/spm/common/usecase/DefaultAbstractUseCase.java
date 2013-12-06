package com.spm.common.usecase;

import com.spm.common.exception.AndroidException;
import com.spm.common.usecase.listener.DefaultUseCaseListener;
import com.spm.service.APIService;

/**
 * Abstract use case that handles the calls to {@link DefaultUseCaseListener#onStartUseCase()},
 * {@link DefaultUseCaseListener#onFinishUseCase()} and
 * {@link DefaultUseCaseListener#onFinishFailedUseCase(AndroidException)} when executing.
 * 
 * @author Maxi Rosson
 */
public abstract class DefaultAbstractUseCase extends AbstractUseCase<DefaultUseCaseListener> implements
		DefaultUseCase<DefaultUseCaseListener> {
	
	public DefaultAbstractUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * Executes the use case.
	 */
	@Override
	public final void run() {
		
		setUseCaseStatus(UseCaseStatus.IN_PROGRESS);
		for (DefaultUseCaseListener listener : getListeners()) {
			listener.onStartUseCase();
		}
		try {
			doExecute();
			for (DefaultUseCaseListener listener : getListeners()) {
				listener.onFinishUseCase();
			}
			setUseCaseStatus(UseCaseStatus.FINISHED_SUCCESSFUL);
		} catch (AndroidException e) {
			setUseCaseStatus(UseCaseStatus.FINISHED_FAILED);
			for (DefaultUseCaseListener listener : getListeners()) {
				listener.onFinishFailedUseCase(e);
			}
		}
	}
	
	/**
	 * Override this method with the login to be executed between {@link NoResultUseCaseListener#onStartUseCase()} and
	 * before {@link NoResultUseCaseListener#onFinishUseCase()}
	 */
	protected abstract void doExecute();
}
