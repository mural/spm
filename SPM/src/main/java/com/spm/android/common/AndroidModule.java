package com.spm.android.common;

import roboguice.config.AbstractAndroidModule;
import com.google.inject.Singleton;
import com.spm.common.domain.Application.ApplicationProvider;
import com.spm.common.mail.MailService;
import com.spm.common.mail.MailServiceImpl;
import com.spm.repository.DBOrderRepository;
import com.spm.repository.DBVisitRepository;
import com.spm.repository.OrderRepository;
import com.spm.repository.VisitRepository;
import com.spm.service.APIService;
import com.spm.service.APIServiceImpl;

public class AndroidModule extends AbstractAndroidModule {
	
	/**
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		
		if (ApplicationProvider.MAIL_REPORTING) {
			this.bind(MailService.class).to(MailServiceImpl.class).in(Singleton.class);
		}
		this.bind(APIService.class).to(APIServiceImpl.class).in(Singleton.class);
		this.bind(OrderRepository.class).to(DBOrderRepository.class).in(Singleton.class);
		this.bind(VisitRepository.class).to(DBVisitRepository.class).in(Singleton.class);
	}
	
}
