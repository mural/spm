package com.spm.common.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

/**
 * Factory class to create {@link HttpClient}s.
 * 
 * @author luciano.rey@teracode.com
 */
public abstract class HttpClientFactory {
	
	// 10 seconds
	private static final int DEFAULT_TIMEOUT = 10000;
	// 60 seconds
	private static final int DEFAULT_SO_TIMEOUT = 60000;
	
	/**
	 * Creates a {@link DefaultHttpClient} and sets a timeout for it.
	 * 
	 * @return {@link DefaultHttpClient} The created client.
	 */
	public static DefaultHttpClient createDefaultHttpClient() {
		return createDefaultHttpClient(null);
	}
	
	/**
	 * Creates a {@link DefaultHttpClient} and sets a timeout for it.
	 * 
	 * @param timeout The connection timeout in milliseconds. If null a default timeout of 10 seconds will be used.
	 * 
	 * @return {@link DefaultHttpClient} The created client.
	 */
	public static DefaultHttpClient createDefaultHttpClient(Integer timeout) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
			timeout != null ? timeout : DEFAULT_TIMEOUT);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, DEFAULT_SO_TIMEOUT);
		return client;
	}
}
