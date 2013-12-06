package com.spm.common.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import com.spm.common.exception.UnexpectedException;

/**
 * @author Luciano Rey
 */
public final class PropertiesUtils {
	
	private static final String PROPERTIES_FILE = "settings.properties";
	private static final String LOCAL_PROPERTIES_FILE = "settings.local.properties";
	
	private static PropertiesUtils INSTANCE = new PropertiesUtils();
	
	private Properties properties;
	
	private PropertiesUtils() {
		try {
			URL url = this.getClass().getClassLoader().getResource(LOCAL_PROPERTIES_FILE);
			if (url == null) {
				url = this.getClass().getClassLoader().getResource(PROPERTIES_FILE);
			}
			properties = new Properties();
			properties.load(url.openStream());
		} catch (IOException e) {
			throw new UnexpectedException("Cannot read from file: " + PROPERTIES_FILE, e);
		}
	}
	
	public static String getStringProperty(String name) {
		return INSTANCE.properties.getProperty(name);
	}
	
	public static String getStringProperty(String name, String defaultValue) {
		String value = getStringProperty(name);
		return value != null ? value : defaultValue;
	}
	
	public static Integer getIntegerProperty(String name) {
		return PropertiesUtils.getIntegerProperty(name, null);
	}
	
	public static Integer getIntegerProperty(String name, Integer defaultValue) {
		String value = getStringProperty(name);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}
	
	public static Boolean getBooleanProperty(String name) {
		return PropertiesUtils.getBooleanProperty(name, null);
	}
	
	public static Boolean getBooleanProperty(String name, Boolean defaultValue) {
		String value = getStringProperty(name);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}
}
