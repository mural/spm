package com.spm.common.exception;

import java.util.List;
import com.spm.common.utils.StringUtils;

/**
 * 
 * @author Maxi Rosson
 */
public enum CommonErrorCode implements ErrorCode {
	
	SERVER_ERROR {
		
		@Override
		public ApplicationException newApplicationException(String message) {
			return new ServerHttpResponseException(this, message);
		}
		
		@Override
		public ApplicationException newApplicationException(Throwable throwable) {
			return new ServerHttpResponseException(this, throwable);
		}
	},
	INTERNAL_ERROR,
	CONNECTION_ERROR {
		
		@Override
		public ApplicationException newApplicationException(String message) {
			return new ConnectionException(this, message);
		}
		
		@Override
		public ApplicationException newApplicationException(Throwable throwable) {
			return new ConnectionException(this, throwable);
		}
	},
	USER_NOT_FOUND_ERROR,
	DB4O_ERROR,
	SYNC_DATE_ERROR,
	UPDATE_DATA_DATE_ERROR,
	NOT_SELECTED_ITEMS_ERROR,
	FACEBOOK_ERROR;
	
	/**
	 * @see com.spm.common.exception.ErrorCode#newLocalBusinessException()
	 */
	@Override
	public LocalBusinessException newLocalBusinessException() {
		return new LocalBusinessException(this);
	}
	
	/**
	 * @see com.spm.common.exception.ErrorCode#newLocalBusinessException(java.lang.Object)
	 */
	@Override
	public LocalBusinessException newLocalBusinessException(Object errorCodeParameter) {
		return new LocalBusinessException(this, errorCodeParameter);
	}
	
	/**
	 * @see com.spm.common.exception.ErrorCode#newApplicationException(java.lang.String)
	 */
	@Override
	public ApplicationException newApplicationException(Throwable throwable) {
		return new ApplicationException(this, throwable);
	}
	
	/**
	 * @see com.spm.common.exception.ErrorCode#newApplicationException(java.lang.String)
	 */
	@Override
	public ApplicationException newApplicationException(String message) {
		return new ApplicationException(this, message);
	}
	
	public void validateRequired(String value) {
		if (StringUtils.isEmpty(value)) {
			throw newLocalBusinessException();
		}
	}
	
	public void validateRequired(Object value) {
		if (value == null) {
			throw newLocalBusinessException();
		}
	}
	
	public void validateRequired(List<?> value) {
		if ((value == null) || value.isEmpty()) {
			throw newLocalBusinessException();
		}
	}
	
	public void validatePositive(Integer value) {
		if (value <= 0) {
			throw newLocalBusinessException();
		}
	}
	
	public void validatePositive(Float value) {
		if (value <= 0) {
			throw newLocalBusinessException();
		}
	}
	
	public void validateMaximum(Integer value, Integer maximum) {
		if (value > maximum) {
			throw newLocalBusinessException(maximum);
		}
	}
}
