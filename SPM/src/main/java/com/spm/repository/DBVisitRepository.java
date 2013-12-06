package com.spm.repository;

import java.util.Collection;
import java.util.List;
import android.content.Context;
import com.db4o.query.Query;
import com.google.inject.Inject;
import com.spm.common.domain.Entity;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.domain.Visit;
import com.spm.store.DbProvider;

/**
 * 
 * @author Agustin Sgarlata
 */
public class DBVisitRepository extends DbProvider<Visit> implements VisitRepository {
	
	@Inject
	public DBVisitRepository(Context ctx) {
		super(Visit.class, ctx);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public Visit get(Long id) throws ObjectNotFoundException {
		Visit visit = new Visit(id);
		return super.get(visit).get(0);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public Collection<Visit> getByUserId(Long userId) throws ObjectNotFoundException {
		Query query = db().query();
		query.constrain(Visit.class);
		query.descend("userId").constrain(userId);
		Collection<Visit> result = query.execute();
		return result;
	}
	
	/**
	 * @see com.spm.common.repository.Repository#add(com.spm.common.domain.Entity)
	 */
	@Override
	public void add(Visit entity) {
		super.store(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<Visit> entities) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(com.spm.common.domain.Entity)
	 */
	@Override
	public void remove(Visit entity) {
		super.delete(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#removeAll()
	 */
	@Override
	public void removeAll() {
		// super.
	}
	
	/**
	 * @see com.spm.common.repository.Repository#getAll()
	 */
	@Override
	public List<Visit> getAll() {
		return super.findAll();
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(Entity)
	 */
	@Override
	public List<Visit> get(Visit entity) {
		return super.get(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(java.lang.Long)
	 */
	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	public void loadInitialData() {
		removeAll();
		db().commit();
		if (super.findAll().isEmpty()) {
			
		}
	}
}
