package com.spm.common.utils;

import java.util.List;

/**
 * 
 * @author Maxi Rosson
 */
public class StringUtils {
	
	public static Boolean isEmpty(String text) {
		return text != null ? text.length() == 0 : true;
	}
	
	public static Boolean isNotEmpty(String text) {
		return !isEmpty(text);
	}
	
	public static String capitalize(String s) {
		if (s.length() == 0) {
			return s;
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
	
	/**
	 * Joins all the strings in the list in a single one separated by the separator sequence.
	 * 
	 * @param stringsToJoin The strings to join.
	 * @param separator The separator sequence.
	 * @return The joined strings.
	 */
	public static String join(List<String> stringsToJoin, String separator) {
		StringBuilder builder = new StringBuilder();
		for (String string : stringsToJoin) {
			builder.append(string);
			builder.append(separator);
		}
		// Remove the last separator
		return builder.substring(0, builder.length() - separator.length());
	}
	
	/**
	 * Joins all the strings in the list in a single one separated by ','.
	 * 
	 * @param stringsToJoin The strings to join.
	 * @return The joined strings.
	 */
	public static String join(List<String> stringsToJoin) {
		return join(stringsToJoin, ",");
	}
}
