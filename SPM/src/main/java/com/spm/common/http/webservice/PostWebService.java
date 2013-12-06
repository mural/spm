package com.spm.common.http.webservice;

import org.apache.http.HttpEntity;

/**
 * @author Luciano Rey
 */
public interface PostWebService extends WebService {
	
	/**
	 * @param entity
	 */
	public void setBody(HttpEntity entity);
	
}
