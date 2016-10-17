package com.spm.common.http;

import java.io.InputStream;
import android.util.Log;
import com.spm.common.http.webservice.PostWebService;
import com.spm.common.http.webservice.WebService;
import com.spm.common.parser.WebServiceParser;

/**
 * Mocked {@link WebService} and {@link PostWebService} implementation that returns mocked responses
 * 
 * @author Maxi Rosson
 */
public class MockWebService implements PostWebService {
	
	private static final String TAG = MockWebService.class.getSimpleName();
	
	private final static String MOCKS_BASE_PATH = "mocks/json/";
	private final static String MOCKS_EXTENSION = ".json";
	private static final String MOCK_SEPARATOR = "_";
	private static final String URL_SEPARATOR = "/";
	
	private String filePath;
	
	public MockWebService(String module, String action) {
		StringBuilder sb = new StringBuilder(getMocksBasePath());
		sb.append(module.replaceAll(URL_SEPARATOR, MOCK_SEPARATOR));
		sb.append(MOCK_SEPARATOR);
		sb.append(action.replaceAll(URL_SEPARATOR, MOCK_SEPARATOR));
		sb.append(getMocksExtension());
		filePath = sb.toString();
	}
	
	/**
	 * @see com.spm.common.http.webservice.WebService#execute(com.spm.common.parser.WebServiceParser)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T execute(WebServiceParser parser) {
		Log.w(TAG, "Request: " + filePath);
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
		
		// parse and return response.
		return (T)(parser != null ? parser.parse(inputStream) : null);
	}
	
	/**
	 * @see com.spm.common.http.webservice.WebService#execute()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T execute() {
		return (T)execute(null);
	}
	
	/**
	 * @see com.spm.common.http.webservice.WebService#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) {
		// Do Nothing
	}
	
	/**
	 * @see com.spm.common.http.webservice.WebService#addParameter(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addParameter(String name, Object value) {
		// Do Nothing
	}
	
	/**
	 * @see com.spm.common.http.webservice.WebService#setConnectionTimeout(java.lang.Integer)
	 */
	@Override
	public void setConnectionTimeout(Integer connectionTimeout) {
		// Do Nothing
	}
	
	/**
	 * @return The mocks base path
	 */
	protected String getMocksBasePath() {
		return MOCKS_BASE_PATH;
	}
	
	/**
	 * @return The mocks extension
	 */
	protected String getMocksExtension() {
		return MOCKS_EXTENSION;
	}
	
}
