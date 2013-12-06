package com.spm.common.http.mock;

/**
 * 
 * @author Maxi Rosson
 */
public class JsonMockWebService extends AbstractMockWebService {
	
	private final static String MOCKS_BASE_PATH = "mocks/json/";
	private final static String MOCKS_EXTENSION = ".json";
	
	public JsonMockWebService(String module, String action) {
		super(module, action);
	}
	
	/**
	 * @see com.spm.common.http.mock.AbstractMockWebService#getMocksBasePath()
	 */
	@Override
	protected String getMocksBasePath() {
		return MOCKS_BASE_PATH;
	}
	
	/**
	 * @see com.spm.common.http.mock.AbstractMockWebService#getMocksExtension()
	 */
	@Override
	protected String getMocksExtension() {
		return MOCKS_EXTENSION;
	}
	
}
