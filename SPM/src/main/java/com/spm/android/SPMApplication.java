package com.spm.android;

import java.util.List;

import android.app.Activity;

import com.google.inject.Module;
import com.spm.android.activity.DashBoardActivity;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.activity.BaseActivity;
import com.spm.domain.User;
import com.spm.repository.UserRepository;
import com.spm.store.Db4oHelper;

/**
 * 
 * @author Agustin Sgarlata
 */
public class SPMApplication extends AndroidApplication {

	private UserRepository userRepository;
	private Db4oHelper db4oHelper;

	public boolean dashboardUpdate = true;

	private User user;

	/**
	 * @return the db4oHelper
	 */
	public Db4oHelper getDb4oHelper() {
		return db4oHelper;
	}

	/**
	 * @see com.spm.android.common.AndroidApplication#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		userRepository = getInjector().getInstance(UserRepository.class);
		// user = userRepository.getUser();

		// dbHelper();
	}

	/**
	 * Create Db4oHelper instance
	 */
	@Override
	public Db4oHelper dbHelper() {
		if (db4oHelper == null) {
			db4oHelper = new Db4oHelper(this);
			db4oHelper.db();
		}
		return db4oHelper;
	}

	/**
	 * @see com.spm.common.domain.Application#attach(com.spm.domain.User)
	 */
	@Override
	public void attach(User user) {
		this.user = user;
		// userRepository.saveUser(user);
	}

	/**
	 * @see com.spm.common.domain.Application#detachUser()
	 */
	@Override
	public void detachUser() {
		// userRepository.removeUser();
		user = null;
	}

	/**
	 * @see com.spm.common.domain.Application#getUser()
	 */
	@Override
	public User getUser() {
		return user;
	}

	/**
	 * @see roboguice.application.RoboApplication#addApplicationModules(java.util.List)
	 */
	@Override
	protected void addApplicationModules(List<Module> modules) {
		modules.add(new SPMAndroidModule());
	}

	/**
	 * @see com.spm.android.common.AndroidApplication#getHomeActivityClass()
	 */
	@Override
	public Class<? extends Activity> getHomeActivityClass() {
		return DashBoardActivity.class;
	}

	/**
	 * @see com.spm.android.common.AndroidApplication#createBaseActivity(android.app.Activity)
	 */
	@Override
	public BaseActivity createBaseActivity(Activity activity) {
		return new SPMBaseActivity(activity);
	}

}