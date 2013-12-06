package com.spm.common.repository;

import com.spm.android.common.utils.SharedPreferencesUtils;
import com.spm.domain.User;

/**
 * {@link UserRepository} implementation that saves and loads the user from the Android shared preferences
 * 
 * @author Maxi Rosson
 */
public class UserRepositoryImpl implements UserRepository {
	
	private static final String USER_ID = "user.id";
	private static final String USER_USER_NAME = "user.username";
	
	/**
	 * @see com.spm.common.repository.UserRepository#getUser()
	 */
	@Override
	public User getUser() {
		
		Long id = SharedPreferencesUtils.loadPreferenceAsLong(USER_ID);
		if (id != null) {
			String username = SharedPreferencesUtils.loadPreference(USER_USER_NAME);
			return new User(id, username);
		}
		return null;
	}
	
	/**
	 * @see com.spm.common.repository.UserRepository#saveUser(com.splatt.domain.User)
	 */
	@Override
	public void saveUser(User user) {
		SharedPreferencesUtils.savePreference(USER_ID, user.getId());
		SharedPreferencesUtils.savePreference(USER_USER_NAME, user.getFirstName());
	}
	
	/**
	 * @see com.spm.common.repository.UserRepository#removeUser()
	 */
	@Override
	public void removeUser() {
		SharedPreferencesUtils.removePreferences(USER_ID, USER_USER_NAME);
	}
}
