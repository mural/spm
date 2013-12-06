package com.spm.android.activity;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import com.google.inject.Inject;
import com.google.inject.internal.Lists;
import com.google.inject.internal.Sets;
import com.spm.common.domain.Application;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.Client;
import com.spm.domain.Order;
import com.spm.domain.User;
import com.spm.domain.Visit;
import com.spm.domain.Work;
import com.spm.repository.ClientRepository;
import com.spm.repository.OrderRepository;
import com.spm.repository.VisitRepository;
import com.spm.service.APIService;

/**
 * Use case to handle the splatt screen.
 * 
 * @author Agustin Sgarlata
 */
public class SyncUseCase extends DefaultAbstractUseCase {
	
	private List<Work> works = Lists.newArrayList();
	
	private Set<Work> selectedItems = Sets.newHashSet();
	
	private OrderRepository orderRepository;
	private VisitRepository visitRepository;
	private ClientRepository clientRepository;
	
	@Inject
	public SyncUseCase(APIService apiService, OrderRepository orderRepository, ClientRepository clientRepository,
			VisitRepository visitRepository) {
		super(apiService);
		this.orderRepository = orderRepository;
		this.clientRepository = clientRepository;
		this.visitRepository = visitRepository;
	}
	
	/**
	 * @see com.splatt.common.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		works.clear();
		User user = Application.APPLICATION_PROVIDER.get().getUser();
		works.addAll(orderRepository.getByUserId(user.getId()));
		works.addAll(visitRepository.getByUserId(user.getId()));
	}
	
	/**
	 * @return the works
	 */
	public List<Work> getWorks() {
		Collections.sort(works);
		return works;
	}
	
	/**
	 * @param work
	 * @return the client
	 */
	public Client getClient(Work work) {
		if (work instanceof Order) {
			return clientRepository.get(((Order)work).getClientId());
		} else {
			return clientRepository.get(((Visit)work).getClientId());
		}
	}
	
	/**
	 * @return the selected Products
	 */
	public Set<Work> getSelectedItems() {
		return selectedItems;
	}
	
	/**
	 * @param work
	 */
	public void updateWork(Work work) {
		if (work instanceof Order) {
			orderRepository.add((Order)work);
		} else {
			visitRepository.add((Visit)work);
		}
	}
	
	/**
	 * @param w
	 */
	public void deleteWork(Work w) {
		works.remove(w);
		if (w instanceof Order) {
			orderRepository.remove((Order)w);
		} else {
			visitRepository.remove((Visit)w);
		}
	}
	
}