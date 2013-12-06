package com.spm.common.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.james.mime4j.util.CharsetUtil;
import com.spm.common.http.webservice.PostWebService;

/**
 * @author Luciano Rey
 */
public class HttpPostWebService extends HttpWebService implements PostWebService {
	
	private HttpEntity entity;
	
	public static HttpPostWebService getNew(String baseURL) {
		return new HttpPostWebService(baseURL);
	}
	
	private HttpPostWebService(String baseURL) {
		super(baseURL);
	}
	
	/**
	 * @see com.spm.common.http.HttpWebService#makeHttpUriRequest()
	 */
	@Override
	protected HttpUriRequest makeHttpUriRequest() throws URISyntaxException, UnsupportedEncodingException {
		// New post for send request.
		HttpPost httpPost = new HttpPost(new URI(getBaseURL()));
		addHeaders(httpPost);
		
		// REVIEW: Review the entity behavior
		// set parameters for request.
		if (entity == null) {
			httpPost.setEntity(new UrlEncodedFormEntity(getParameters(), CharsetUtil.UTF_8.name()));
		} else {
			httpPost.setEntity(entity);
		}
		
		return httpPost;
	}
	
	/**
	 * @see com.spm.common.http.webservice.PostWebService#setBody(org.apache.http.HttpEntity)
	 */
	@Override
	public void setBody(HttpEntity entity) {
		this.entity = entity;
	}
	
	/**
	 * @see com.spm.common.http.HttpWebService#addParameter(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addParameter(String name, Object value) {
		addParameter(new BasicNameValuePair(name, value.toString()));
	}
}
