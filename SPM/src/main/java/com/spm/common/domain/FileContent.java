package com.spm.common.domain;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import com.spm.common.utils.StringUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class FileContent implements Serializable {
	
	private transient ByteArrayInputStream localStream;
	private String externalFileURL;
	
	/**
	 * @param localStream
	 */
	public FileContent(ByteArrayInputStream localStream) {
		this.localStream = localStream;
	}
	
	/**
	 * @param externalFileURL
	 */
	public FileContent(String externalFileURL) {
		if (StringUtils.isNotEmpty(externalFileURL)) {
			this.externalFileURL = externalFileURL;
		}
	}
	
	public boolean hasLocalContent() {
		return localStream != null;
	}
	
	/**
	 * @return the localStream
	 */
	public ByteArrayInputStream getLocalStream() {
		return localStream;
	}
	
	/**
	 * @return the externalFileURL
	 */
	public String getExternalFileURL() {
		return externalFileURL;
	}
}
