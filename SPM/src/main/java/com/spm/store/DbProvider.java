package com.spm.store;

import java.io.Serializable;
import java.util.List;
import android.content.Context;

/**
 * 
 * @author Agustin Sgarlata
 * @param <T>
 */
public class DbProvider<T extends Serializable> extends Db4oHelper {
	
	public Class<T> persistentClass;
	
	public DbProvider(Class<T> persistentClass, Context ctx) {
		super(ctx);
		this.persistentClass = persistentClass;
	}
	
	public void store(T obj) {
		db().store(obj);
		db().commit();
	}
	
	public void delete(T obj) {
		db().delete(obj);
		db().commit();
	}
	
	public List<T> findAll() {
		return db().query(persistentClass);
	}
	
	public List<T> get(T obj) {
		return db().queryByExample(obj);
	}
}