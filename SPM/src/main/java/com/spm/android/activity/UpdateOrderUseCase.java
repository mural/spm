package com.spm.android.activity;

import com.google.inject.Inject;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.Order;
import com.spm.repository.OrderRepository;
import com.spm.service.APIService;

/**
 * Use case to handle the splatt screen.
 * 
 * @author Agustin Sgarlata
 */
public class UpdateOrderUseCase extends DefaultAbstractUseCase {
	
	private Order order;
	
	private OrderRepository orderRepository;
	
	@Inject
	public UpdateOrderUseCase(APIService apiService, OrderRepository orderRepository) {
		super(apiService);
		this.orderRepository = orderRepository;
	}
	
	/**
	 * @see com.splatt.common.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (order != null) {
			orderRepository.add(order);
		}
	}
	
	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}
	
}