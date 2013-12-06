package com.spm.android.common.intent;

import java.io.Serializable;

/**
 * 
 * @author Maxi Rosson
 */
public class IntentContext implements Serializable {
	
	private static Object data;
	
	public static <T> IntentContext newContext(T data) {
		IntentContext.data = data;
		return new IntentContext();
	}
	
	/**
	 * @param <T>
	 * @return Return the data cleaning it from the context after returning it.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData() {
		Object returnedData = IntentContext.data;
		data = null;
		return (T)returnedData;
	}
}
