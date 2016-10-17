package com.spm.common.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.google.inject.internal.Lists;
import com.google.inject.internal.Maps;
import com.spm.common.domain.EntityInterface;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public class InMemoryRepository<T extends EntityInterface> extends AbstractRepository<T> {
	
	private Map<Long, T> items = Maps.newHashMap();

	@Override
	public void add(T entity) {
		super.add(entity);
		items.put(entity.getId(), entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<T> entities) {
		super.addAll(entities);
		for (T entity : entities) {
			add(entity);
		}
	}

	@Override
	public void remove(T entity) {
		super.remove(entity);
		items.remove(entity.getId());
	}
	
	/**
	 * @see com.spm.common.repository.Repository#getAll()
	 */
	@Override
	public List<T> getAll() {
		return Lists.newArrayList(items.values());
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public T get(Long id) throws ObjectNotFoundException {
		return items.get(id);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#removeAll()
	 */
	@Override
	public void removeAll() {
		items.clear();
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(java.lang.Long)
	 */
	@Override
	public void remove(Long id) {
		super.remove(id);
		items.remove(id);
	}

	@Override
	public List<T> get(T entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
