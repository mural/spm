package com.spm.common.exception;

/**
 * @author Luciano Rey
 */
public class UnexpectedException extends AndroidException {
	
	/**
	 * @param message
	 * @param cause
	 */
	public UnexpectedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * @param message
	 */
	public UnexpectedException(String message) {
		super(message);
	}
	
	/**
	 * @param cause
	 */
	public UnexpectedException(Throwable cause) {
		super(cause);
	}
	
}
