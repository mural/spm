package com.spm.common.exception;

/**
 * 
 * @author Estefania Caravatti
 */
public class HttpResponseException extends ApplicationException {
	
	protected HttpResponseException() {
		// Nothing by default.
	}
	
	public HttpResponseException(ErrorCode errorCode, Throwable throwable) {
		super(errorCode, throwable);
	}
	
	public HttpResponseException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
	
}
