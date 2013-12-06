package com.spm.common.domain;

import android.preference.PreferenceManager;
import com.spm.android.common.AndroidApplication;
import com.spm.common.http.mock.AbstractMockWebService;
import com.spm.common.http.mock.XmlMockWebService;
import com.spm.common.utils.PropertiesUtils;
import com.spm.domain.User;
import com.spm.store.Db4oHelper;

/**
 * 
 * @author Maxi Rosson
 */
public interface Application {
	
	public static String SERVER_API_SUFFIX = "/spm-api";
	public static ApplicationProvider APPLICATION_PROVIDER = new ApplicationProvider();
	
	public static class ApplicationProvider {
		
		// The base URL of the API server
		public static final String SERVER_URL = PropertiesUtils.getStringProperty("server.url");
		
		// Whether the application is running on a production environment
		public static final Boolean PRODUCTION_ENVIRONMENT = PropertiesUtils.getBooleanProperty("production.environment");
		
		// Whether the application should display the development settings
		public static final Boolean DEV_SETTINGS = PropertiesUtils.getBooleanProperty("dev.settings");
		
		// Whether the error mail reporting service is enabled or not
		public static final Boolean MAIL_REPORTING = PropertiesUtils.getBooleanProperty("mail.reporting");
		
		// Whether the server responses should be logged or not
		public static final Boolean LOGGING_SERVER_RESPONSE = PropertiesUtils.getBooleanProperty("logging.server.response");
		
		// The sender id used by the c2dm framework
		public static final String C2DM_SENDER = PropertiesUtils.getStringProperty("c2dm.sender");
		
		private Application application;
		
		/**
		 * @param application
		 */
		public void set(Application application) {
			this.application = application;
		}
		
		/**
		 * @return Application
		 */
		public Application get() {
			return application;
		}
		
		public static Boolean isServerMockEnabled() {
			return !PRODUCTION_ENVIRONMENT
					&& PreferenceManager.getDefaultSharedPreferences(AndroidApplication.get()).getBoolean(
						"serverMockEnabled", false);
		}
		
		public static AbstractMockWebService getAbstractMockWebServiceInstance(String module, String action) {
			return new XmlMockWebService(module, action);
		}
		
		public static String getServerApiURL() {
			return SERVER_URL + SERVER_API_SUFFIX;
		}
	}
	
	/**
	 * 
	 */
	void detachUser();
	
	/**
	 * @param user
	 */
	void attach(User user);
	
	/**
	 * @return a SPM user
	 */
	User getUser();
	
	Db4oHelper dbHelper();
}
