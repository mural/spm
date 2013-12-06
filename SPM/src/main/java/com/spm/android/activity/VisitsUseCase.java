package com.spm.android.activity;

import java.util.List;
import com.google.inject.Inject;
import com.google.inject.internal.Lists;
import com.spm.common.domain.Application;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.Client;
import com.spm.domain.User;
import com.spm.domain.Visit;
import com.spm.repository.VisitRepository;
import com.spm.service.APIService;

/**
 * Use case to handle the visits screen.
 * 
 * @author Agustin Sgarlata
 */
public class VisitsUseCase extends DefaultAbstractUseCase {
	
	private List<Visit> visits = Lists.newArrayList();
	private Client client;
	
	private VisitRepository visitRepository;
	
	@Inject
	public VisitsUseCase(APIService apiService, VisitRepository visitRepository) {
		super(apiService);
		this.visitRepository = visitRepository;
	}
	
	/**
	 * @see com.splatt.common.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		User user = Application.APPLICATION_PROVIDER.get().getUser();
		Visit visit = new Visit(null, client.getId(), Boolean.FALSE, user.getId());
		visits.clear();
		visits.addAll(visitRepository.get(visit));
	}
	
	/**
	 * @return the visits
	 */
	public List<Visit> getVisits() {
		return visits;
	}
	
	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}
	
	/**
	 * @param client the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}
	
	/**
	 * @param v
	 */
	public void deleteVisit(Visit v) {
		visits.remove(v);
		visitRepository.remove(v);
	}
	
}