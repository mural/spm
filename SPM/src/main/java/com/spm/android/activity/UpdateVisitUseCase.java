package com.spm.android.activity;

import com.google.inject.Inject;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.Visit;
import com.spm.repository.VisitRepository;
import com.spm.service.APIService;

/**
 * Use case to handle the splatt screen.
 * 
 * @author Agustin Sgarlata
 */
public class UpdateVisitUseCase extends DefaultAbstractUseCase {
	
	private Visit visit;
	
	private VisitRepository visitRepository;
	
	@Inject
	public UpdateVisitUseCase(APIService apiService, VisitRepository visitRepository) {
		super(apiService);
		this.visitRepository = visitRepository;
	}
	
	/**
	 * @see com.splatt.common.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (visit != null) {
			visitRepository.add(visit);
		}
	}
	
	/**
	 * @param visit the visit to set
	 */
	public void setOrder(Visit visit) {
		this.visit = visit;
	}
	
}