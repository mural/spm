package com.spm.common.repository;

import com.spm.common.exception.AndroidException;

public class ObjectNotFoundException extends AndroidException {
	
	public ObjectNotFoundException(Class<?> entityClass, Long id) {
		super("The entity of type " + entityClass.getSimpleName() + " and id = " + id + " wasn't found.");
	}
	
	public ObjectNotFoundException(Class<?> entityClass) {
		super("The entity of type " + entityClass.getSimpleName() + " wasn't found.");
	}
}
