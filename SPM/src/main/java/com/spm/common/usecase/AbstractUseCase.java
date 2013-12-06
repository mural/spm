package com.spm.common.usecase;

import java.util.List;
import com.google.inject.internal.Lists;
import com.spm.service.APIService;

/**
 * 
 * @param <T> The Listener type
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractUseCase<T> {
	
	public enum UseCaseStatus {
		NOT_INVOKED,
		IN_PROGRESS,
		FINISHED_SUCCESSFUL,
		FINISHED_FAILED;
	}
	
	private APIService apiService;
	private List<T> listeners = Lists.newArrayList();
	private UseCaseStatus useCaseStatus = UseCaseStatus.NOT_INVOKED;
	
	public AbstractUseCase(APIService apiService) {
		this.apiService = apiService;
	}
	
	/**
	 * @return the listeners
	 */
	protected List<T> getListeners() {
		return listeners;
	}
	
	/**
	 * @param listener the listener to add
	 */
	public void addListener(T listener) {
		if (!listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}
	
	/**
	 * @param listener the listener to remove
	 */
	public void removeListener(T listener) {
		this.listeners.remove(listener);
	}
	
	public Boolean isNotInvoked() {
		return UseCaseStatus.NOT_INVOKED.equals(useCaseStatus);
	}
	
	public Boolean isInProgress() {
		return UseCaseStatus.IN_PROGRESS.equals(useCaseStatus);
	}
	
	public Boolean isFinishSuccessful() {
		return UseCaseStatus.FINISHED_SUCCESSFUL.equals(useCaseStatus);
	}
	
	public Boolean isFinishFailed() {
		return UseCaseStatus.FINISHED_SUCCESSFUL.equals(useCaseStatus);
	}
	
	/**
	 * @param useCaseStatus the useCaseStatus to set
	 */
	protected void setUseCaseStatus(UseCaseStatus useCaseStatus) {
		this.useCaseStatus = useCaseStatus;
	}
	
	/**
	 * @return the apiService
	 */
	protected APIService getApiService() {
		return apiService;
	}
}
