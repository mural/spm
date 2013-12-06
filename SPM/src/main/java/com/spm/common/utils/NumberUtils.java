package com.spm.common.utils;


/**
 * 
 * @author Maxi Rosson
 */
public class NumberUtils {
	
	public static Float getFloat(String value) {
		return StringUtils.isNotEmpty(value) ? new Float(value) : null;
	}
	
	public static Integer getInteger(String value) {
		return StringUtils.isNotEmpty(value) ? new Integer(value) : null;
	}
	
	public static Long getLong(String value) {
		return StringUtils.isNotEmpty(value) ? new Long(value) : null;
	}
	
	public static Double getDouble(String value) {
		return StringUtils.isNotEmpty(value) ? new Double(value) : null;
	}
	
	public static Boolean getBooleanFromNumber(String value) {
		return StringUtils.isNotEmpty(value) ? "1".equals(value) : null;
	}
	
	public static Boolean getBoolean(String value) {
		return Boolean.parseBoolean(value);
	}
	
	public static String getString(Integer value) {
		return value != null ? value.toString() : null;
	}
	
	public static String getOrdinalSuffix(int n) {
		// REVIEW Add internationalization support
		if ((n >= 11) && (n <= 13)) {
			return "th";
		}
		switch (n % 10) {
			case 1:
				return "st";
			case 2:
				return "nd";
			case 3:
				return "rd";
			default:
				return "th";
		}
	}
}
