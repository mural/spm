package com.spm.common.exception;

/**
 * Expected Application exception not related with business logic. <br>
 * For example a time out or I/O error.
 * 
 * @author Luciano Rey
 */
public class ApplicationException extends AndroidException {
	
	private ErrorCode errorCode;
	
	protected ApplicationException() {
		
	}
	
	protected ApplicationException(String errorMessage) {
		super(errorMessage);
	}
	
	public ApplicationException(ErrorCode errorCode, Throwable throwable) {
		super(throwable);
		this.errorCode = errorCode;
	}
	
	public ApplicationException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	/**
	 * @return the errorCode
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
