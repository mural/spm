package com.spm.common.http.webservice;

import com.spm.common.parser.WebServiceParser;

/**
 * @author Luciano Rey
 */
public interface WebService {
	
	/**
	 * @param <T>
	 * @param parser
	 * @return WebServiceResponse
	 */
	public <T> T execute(WebServiceParser parser);
	
	/**
	 * @param <T>
	 * @return WebServiceResponse
	 */
	public <T> T execute();
	
	/**
	 * @param name The header name.
	 * @param value The header value.
	 */
	public void addHeader(String name, String value);
	
	/**
	 * @param name The parameter name.
	 * @param value The parameter value.
	 */
	public void addParameter(String name, Object value);
	
	/**
	 * @param connectionTimeout The connection timeout in milliseconds.
	 */
	public void setConnectionTimeout(Integer connectionTimeout);
	
}
