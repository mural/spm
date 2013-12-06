package com.spm.android.activity;

import java.util.Date;
import android.content.Intent;
import android.util.Log;
import com.google.inject.Inject;
import com.spm.android.common.AndroidApplication;
import com.spm.common.domain.Application;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.common.utils.DateUtils;
import com.spm.common.utils.ThreadUtils;
import com.spm.domain.User;
import com.spm.domain.Visit;
import com.spm.repository.UserRepository;
import com.spm.repository.VisitRepository;
import com.spm.service.APIService;

/**
 * Use case to handle the splatt screen.
 * 
 * @author Agustin Sgarlata
 */
public class DetailVisitUseCase extends DefaultAbstractUseCase {
	
	private Long clientId;
	private Visit visit;
	
	private VisitRepository visitRepository;
	private UserRepository userRepository;
	
	@Inject
	public DetailVisitUseCase(APIService apiService, VisitRepository visitRepository, UserRepository userRepository) {
		super(apiService);
		this.visitRepository = visitRepository;
		this.userRepository = userRepository;
	}
	
	/**
	 * @see com.spm.common.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		execute();
	}
	
	public void execute() {
		if (visit == null) {
			User user = Application.APPLICATION_PROVIDER.get().getUser();
			Long secuencial = user.getOrderNumber() + 1;
			user.setOrderNumber(secuencial);
			userRepository.add(user);
			
			Long newId = new Long(clientId.toString() + secuencial.toString());
			visit = new Visit(newId, secuencial.toString(), DateUtils.DDMMYYYY_DATE_FORMAT.format(new Date()),
					Visit.OTROS, clientId, Boolean.FALSE, user.getId());
			visit.modify(clientId.toString() + "." + secuencial.toString());
			visitRepository.add(visit); // TODO manage ID!
			
			Log.d("GPS for SPM", "--- pre thread ! ---");
			ThreadUtils.execute(new Runnable() {
				
				@Override
				public void run() {
					Log.d("GPS for SPM", "--- init de thread ! ---");
					Intent service = new Intent(AndroidApplication.get(), MyLocationService.class);
					service.putExtra("ID", visit.getId());
					AndroidApplication.get().startService(service);
				}
			});
			
		} else {
			visit = visitRepository.get(visit.getId());
		}
	}
	
	/**
	 * @return the visit
	 */
	public Visit getVisit() {
		return visit;
	}
	
	/**
	 * @param visit the visit to set
	 */
	public void setVisit(Visit visit) {
		this.visit = visit;
	}
	
	/**
	 * @param visit
	 */
	public void updateVisit(Visit visit) {
		visitRepository.add(visit);
	}
	
	/**
	 * @param clientId
	 */
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	
}