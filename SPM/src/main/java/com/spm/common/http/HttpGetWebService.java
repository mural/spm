package com.spm.common.http;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import android.net.Uri;

/**
 * @author Luciano Rey
 */
public class HttpGetWebService extends HttpWebService {
	
	public static HttpGetWebService getNew(String baseURL) {
		return new HttpGetWebService(baseURL);
	}
	
	private HttpGetWebService(String baseURL) {
		super(baseURL);
	}
	
	/**
	 * @see com.spm.common.http.HttpWebService#makeHttpUriRequest()
	 */
	@Override
	protected HttpUriRequest makeHttpUriRequest() throws URISyntaxException, UnsupportedEncodingException {
		HttpGet httpGet = new HttpGet(getBaseURL() + makeStringParameters());
		addHeaders(httpGet);
		return httpGet;
	}
	
	private String makeStringParameters() {
		String params = "";
		boolean isFirst = true;
		
		for (NameValuePair pair : getParameters()) {
			if (isFirst) {
				params = params + "?" + pair.getName() + "=" + pair.getValue();
				isFirst = false;
			} else {
				params = params + "&" + pair.getName() + "=" + pair.getValue();
			}
		}
		
		return params;
	}
	
	/**
	 * @see com.spm.common.http.HttpWebService#addParameter(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addParameter(String name, Object value) {
		addParameter(new BasicNameValuePair(name, Uri.encode(value.toString())));
	}
}
