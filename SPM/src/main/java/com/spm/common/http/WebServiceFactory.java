package com.spm.common.http;

import com.spm.common.domain.Application.ApplicationProvider;
import com.spm.common.http.webservice.PostWebService;
import com.spm.common.http.webservice.WebService;

/**
 * @author Luciano Rey
 */
public class WebServiceFactory {
	
	private static final String SEPARATOR = "/";
	
	public static WebService newGet(String serverUrl, String module, String action) {
		return WebServiceFactory.newGet(serverUrl, module, action, false);
	}
	
	public static WebService newGet(String serverUrl, String module, String action, Boolean mocked) {
		if (ApplicationProvider.isServerMockEnabled() || mocked) {
			return ApplicationProvider.getAbstractMockWebServiceInstance(module, action);
		} else {
			String baseURL = getBaseURL(serverUrl, module, action);
			//todo return HttpGetWebService.getNew(baseURL);
			return ApplicationProvider.getAbstractMockWebServiceInstance(module, action);
		}
	}
	
	public static PostWebService newPost(String serverUrl, String module, String action) {
		return WebServiceFactory.newPost(serverUrl, module, action, false);
	}
	
	public static PostWebService newPost(String serverUrl, String module, String action, Boolean mocked) {
		if (ApplicationProvider.isServerMockEnabled() || mocked) {
			return ApplicationProvider.getAbstractMockWebServiceInstance(module, action);
		} else {
			String baseURL = getBaseURL(serverUrl, module, action);
			//todo return HttpPostWebService.getNew(baseURL);
			return ApplicationProvider.getAbstractMockWebServiceInstance(module, action);
		}
	}
	
	/**
	 * @param module
	 * @param action
	 * @return
	 */
	private static String getBaseURL(String serverUrl, String module, String action) {
		StringBuilder builder = new StringBuilder();
		builder.append(serverUrl);
		builder.append(SEPARATOR);
		builder.append(module);
		builder.append(SEPARATOR);
		builder.append(action);
		return builder.toString();
	}
	
}
