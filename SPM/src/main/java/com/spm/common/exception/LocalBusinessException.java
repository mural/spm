package com.spm.common.exception;

/**
 * Business exceptions that are related to the app's business logic.
 * 
 * @author Estefania Caravatti
 */
public class LocalBusinessException extends BusinessException {
	
	private ErrorCode errorCode;
	
	private Object errorCodeParameter;
	
	public LocalBusinessException(ErrorCode errorCode, Object errorCodeParameter) {
		this.errorCode = errorCode;
		this.errorCodeParameter = errorCodeParameter;
	}
	
	public LocalBusinessException(ErrorCode errorCode) {
		this(errorCode, null);
	}
	
	/**
	 * @return the errorCode
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	
	/**
	 * @return the errorCodeParameter
	 */
	public Object getErrorCodeParameter() {
		return errorCodeParameter;
	}
}
