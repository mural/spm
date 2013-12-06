package com.spm.common.parser;

import java.io.InputStream;

/**
 * @author Luciano Rey
 */
public interface WebServiceParser {
	
	/**
	 * Parse the inputStream
	 * 
	 * @param inputStream The inputStream to parse
	 * @return The parser response object
	 */
	public Object parse(InputStream inputStream);
	
}
