package com.spm.android;

import com.google.inject.Singleton;
import com.spm.android.common.AndroidModule;
import com.spm.common.repository.UserRepository;
import com.spm.common.repository.UserRepositoryImpl;

/**
 * 
 * @author Maxi Rosson
 */
public class SPMAndroidModule extends AndroidModule {
	
	/**
	 * @see com.splatt.android.common.AndroidModule#configure()
	 */
	@Override
	protected void configure() {
		super.configure();
		
		// this.bind(APIService.class).to(APIServiceImpl.class).in(Singleton.class);
		this.bind(UserRepository.class).to(UserRepositoryImpl.class).in(Singleton.class);
	}
}
