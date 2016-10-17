package com.spm.common.repository;

import java.util.Collection;
import com.spm.common.domain.Entity;
import com.spm.common.domain.EntityInterface;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public abstract class AbstractRepository<T extends EntityInterface> implements Repository<T> {
	
	// Refresh frequency (in milliseconds)
	// TODO: change this time to 5 * 60000 (5min) when Push Notifications is implemented
	private static final Integer REFRESH_FREQUENCY = 10000; // 10secs
	
	private Long lastUpdateTimestamp;

	@Override
	public void add(T entity) {
		refreshUpdateTimestamp();
	}
	
	/**
	 * @see com.spm.common.repository.Repository#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<T> entities) {
		refreshUpdateTimestamp();
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(java.lang.Long)
	 */
	@Override
	public void remove(Long id) {
		refreshUpdateTimestamp();
	}

	@Override
	public void remove(T entity) {
		refreshUpdateTimestamp();
	};
	
	protected void refreshUpdateTimestamp() {
		lastUpdateTimestamp = System.currentTimeMillis();
	}
	
}
