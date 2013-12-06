package com.spm.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import com.spm.common.exception.UnexpectedException;

/**
 * Reflection related utilities
 * 
 * @author Luciano Rey
 */
public class ReflectionUtils {
	
	/**
	 * Create a class for the specified type,.
	 * 
	 * @param <T> the class to instantiate to be returned
	 * @param clazz the class to be instantiated
	 * @return an instance of the class specified
	 */
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new UnexpectedException("Unable to instantiate class [" + clazz.getSimpleName() + "]", e);
		}
	}
	
	/**
	 * Create a class for the specified type, using the specified constructor with the passed parameters.
	 * 
	 * @param <T> the class to instantiate to be returned
	 * @param clazz the class to be instantiated
	 * @param parameterTypes a constructor with this parameters will be used to instantiate the class
	 * @param parameterValues parameter values to be used when instantiating
	 * @return an instance of the class specified
	 */
	public static <T> T newInstance(Class<T> clazz, Collection<Class<?>> parameterTypes,
			Collection<Object> parameterValues) {
		try {
			Constructor<T> constructor = clazz.getConstructor(parameterTypes.toArray(new Class[0]));
			return constructor.newInstance(parameterValues.toArray(new Object[0]));
		} catch (Exception e) {
			throw new UnexpectedException("Unable to instantiate class [" + clazz.getSimpleName() + "]", e);
		}
	}
	
	/**
	 * @param object
	 * @param fieldName
	 * @param value
	 */
	public static void set(Object object, String fieldName, Object value) {
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
		} catch (SecurityException e) {
			throw new UnexpectedException(e);
		} catch (NoSuchFieldException e) {
			throw new UnexpectedException(e);
		} catch (IllegalArgumentException e) {
			throw new UnexpectedException(e);
		} catch (IllegalAccessException e) {
			throw new UnexpectedException(e);
		}
	}
	
	/**
	 * @param field
	 * @param object
	 * @return Object
	 */
	public static Object get(Field field, Object object) {
		field.setAccessible(true);
		try {
			return field.get(object);
		} catch (IllegalArgumentException e) {
			throw new UnexpectedException(e);
		} catch (IllegalAccessException e) {
			throw new UnexpectedException(e);
		}
	}
	
	/**
	 * @param object
	 * @param fieldName
	 * @return Object
	 */
	public static Object get(Object object, String fieldName) {
		Field field = getField(object, fieldName);
		field.setAccessible(true);
		return get(field, object);
	}
	
	/**
	 * @param object
	 * @param fieldName
	 * @return Field
	 */
	public static Field getField(Object object, String fieldName) {
		try {
			return object.getClass().getDeclaredField(fieldName);
		} catch (SecurityException e) {
			throw new UnexpectedException(e);
		} catch (NoSuchFieldException e) {
			throw new UnexpectedException(e);
		}
	}
	
	/**
	 * @param object
	 * @param fieldName
	 * @return Class<?>
	 */
	public static Class<?> getType(Object object, String fieldName) {
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			return field.getType();
		} catch (SecurityException e) {
			throw new UnexpectedException(e);
		} catch (NoSuchFieldException e) {
			throw new UnexpectedException(e);
		}
	}
	
}
