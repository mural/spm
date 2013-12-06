package com.spm.common.exception;

/**
 * Common interface for all the possible errors of the application
 * 
 * @author Maxi Rosson
 */
public interface ErrorCode {
	
	/**
	 * @return A new {@link LocalBusinessException} with this {@link ErrorCode}
	 */
	public LocalBusinessException newLocalBusinessException();
	
	/**
	 * @param errorCodeParameter A parameter for this {@link ErrorCode}'s message.
	 * @return A new {@link LocalBusinessException} with this {@link ErrorCode}
	 */
	public LocalBusinessException newLocalBusinessException(Object errorCodeParameter);
	
	/**
	 * @param throwable
	 * @return A new {@link ApplicationException} with this {@link ErrorCode}
	 */
	public ApplicationException newApplicationException(Throwable throwable);
	
	/**
	 * @param message
	 * @return A new {@link ApplicationException} with this message
	 */
	public ApplicationException newApplicationException(String message);
	
}
