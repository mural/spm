package com.spm.common.exception;

/**
 * @author Luciano Rey
 */
public abstract class AndroidException extends RuntimeException {
	
	/**
	 * Constructor
	 */
	public AndroidException() {
		super();
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public AndroidException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * @param message
	 */
	public AndroidException(String message) {
		super(message);
	}
	
	/**
	 * @param cause
	 */
	public AndroidException(Throwable cause) {
		super(cause);
	}
	
}
