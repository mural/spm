package com.spm.common.exception;

/**
 * This exception holds business errors provided by the server.
 * 
 * @author Estefania Caravatti
 */
public class ServerBusinessException extends BusinessException {
	
	/**
	 * @param errorMessage The error message provided by the server.
	 */
	public ServerBusinessException(String errorMessage) {
		super(errorMessage);
	}
	
}
