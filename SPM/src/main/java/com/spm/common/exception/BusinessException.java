package com.spm.common.exception;

/**
 * Expected Business exception
 * 
 * @author Luciano Rey
 */
public class BusinessException extends AndroidException {
	
	/**
	 * Default constructor
	 */
	protected BusinessException() {
		// nothing by default.
	}
	
	/**
	 * @param errorMessage The error message related to this exception.
	 */
	protected BusinessException(String errorMessage) {
		super(errorMessage);
	}
}
