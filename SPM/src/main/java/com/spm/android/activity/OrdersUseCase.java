package com.spm.android.activity;

import java.util.List;
import com.google.inject.Inject;
import com.google.inject.internal.Lists;
import com.spm.common.domain.Application;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.Client;
import com.spm.domain.Order;
import com.spm.domain.User;
import com.spm.repository.OrderRepository;
import com.spm.service.APIService;

/**
 * Use case to handle the splatt screen.
 * 
 * @author Agustin Sgarlata
 */
public class OrdersUseCase extends DefaultAbstractUseCase {
	
	private List<Order> orders = Lists.newArrayList();
	private Long clientId;
	
	private OrderRepository orderRepository;
	
	@Inject
	public OrdersUseCase(APIService apiService, OrderRepository orderRepository) {
		super(apiService);
		this.orderRepository = orderRepository;
	}
	

	@Override
	protected void doExecute() {
		User user = Application.APPLICATION_PROVIDER.get().getUser();
		Order order = new Order(null, clientId, Boolean.FALSE, user.getId());
		orders.clear();
		orders.addAll(orderRepository.get(order));
	}
	
	/**
	 * @return the orders
	 */
	public List<Order> getOrders() {
		return orders;
	}
	
	/**
	 * @return the client
	 */
	public Long getClient() {
		return clientId;
	}
	
	/**
	 * @param clientId the client to set
	 */
	public void setClient(Long clientId) {
		this.clientId = clientId;
	}
	
	/**
	 * @param o
	 */
	public void deleteOrder(Order o) {
		orders.remove(o);
		orderRepository.remove(o);
	}
	
}