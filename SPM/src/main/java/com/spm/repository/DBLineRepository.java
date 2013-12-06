package com.spm.repository;

import java.util.Collection;
import java.util.List;
import android.content.Context;
import com.google.inject.Inject;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.domain.Line;
import com.spm.store.DbProvider;

/**
 * 
 * @author Agustin Sgarlata
 */
public class DBLineRepository extends DbProvider<Line> implements LineRepository {
	
	@Inject
	public DBLineRepository(Context ctx) {
		super(Line.class, ctx);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public Line get(Long id) throws ObjectNotFoundException {
		Line line = new Line(id, null);
		return (Line)super.get(line);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#add(com.spm.common.domain.Entity)
	 */
	@Override
	public void add(Line entity) {
		super.store(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<Line> entities) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(com.spm.common.domain.Entity)
	 */
	@Override
	public void remove(Line entity) {
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
	public List<Line> getAll() {
		return super.findAll();
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(java.lang.Long)
	 */
	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	public void loadInitialData() {
		if (super.findAll().isEmpty()) {
			add(new Line(1L, "CHUPETIN JELLY"));
			add(new Line(2L, "CARAMELOS JELLY"));
			add(new Line(3L, "FIGURAS LARGAS"));
			add(new Line(4L, "INTERACTIVA"));
			add(new Line(5L, "Jugos"));
			add(new Line(9L, "PROMOCION"));
		}
	}
	
}
