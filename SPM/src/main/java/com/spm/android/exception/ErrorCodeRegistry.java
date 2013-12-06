package com.spm.android.exception;

import java.util.Map;
import com.google.inject.internal.Maps;
import com.spm.R;
import com.spm.android.common.utils.LocalizationUtils;
import com.spm.common.exception.CommonErrorCode;
import com.spm.common.exception.ErrorCode;

/**
 * 
 * @author Maxi Rosson
 */
public class ErrorCodeRegistry {
	
	private final static ErrorCodeRegistry INSTANCE = new ErrorCodeRegistry();
	
	// Map with all the error codes as keys, and the android resources IDs as values
	private Map<ErrorCode, Integer> errorCodeMap = Maps.newHashMap();
	
	private ErrorCodeRegistry() {
		// Application Errors
		errorCodeMap.put(CommonErrorCode.INTERNAL_ERROR, R.string.internalError);
		errorCodeMap.put(CommonErrorCode.SERVER_ERROR, R.string.serverError);
		errorCodeMap.put(CommonErrorCode.CONNECTION_ERROR, R.string.connectionError);
		errorCodeMap.put(CommonErrorCode.FACEBOOK_ERROR, R.string.facebookError);
		errorCodeMap.put(CommonErrorCode.USER_NOT_FOUND_ERROR, R.string.userNotFoundError);
		errorCodeMap.put(CommonErrorCode.DB4O_ERROR, R.string.db4oError);
		errorCodeMap.put(CommonErrorCode.SYNC_DATE_ERROR, R.string.syncDateError);
		errorCodeMap.put(CommonErrorCode.UPDATE_DATA_DATE_ERROR, R.string.updateDataDateError);
		errorCodeMap.put(CommonErrorCode.NOT_SELECTED_ITEMS_ERROR, R.string.notSelectedItemError);
	}
	
	public static ErrorCodeRegistry get() {
		return INSTANCE;
	}
	
	public Integer getMessageIdFor(ErrorCode errorCode) {
		return errorCodeMap.get(errorCode);
	}
	
	public String getMessageFor(ErrorCode errorCode) {
		return LocalizationUtils.getString(this.getMessageIdFor(errorCode));
	}
	
	public String getMessageFor(ErrorCode errorCode, Object... args) {
		return LocalizationUtils.getString(this.getMessageIdFor(errorCode), args);
	}
}
