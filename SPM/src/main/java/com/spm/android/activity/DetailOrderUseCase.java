package com.spm.android.activity;

import java.util.Date;
import java.util.List;
import android.content.Intent;
import android.util.Log;
import com.google.inject.Inject;
import com.google.inject.internal.Lists;
import com.spm.android.common.AndroidApplication;
import com.spm.common.domain.Application;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.common.utils.DateUtils;
import com.spm.common.utils.ThreadUtils;
import com.spm.domain.Order;
import com.spm.domain.OrderItem;
import com.spm.domain.User;
import com.spm.repository.OrderRepository;
import com.spm.repository.UserRepository;
import com.spm.service.APIService;

/**
 * Use case to handle the splatt screen.
 * 
 * @author Agustin Sgarlata
 */
public class DetailOrderUseCase extends DefaultAbstractUseCase {
	
	private Long clientId;
	private Order order;
	private List<OrderItem> orderItems = Lists.newArrayList();
	
	private OrderRepository orderRepository;
	private UserRepository userRepository;
	
	@Inject
	public DetailOrderUseCase(APIService apiService, OrderRepository orderRepository, UserRepository userRepository) {
		super(apiService);
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
	}
	
	/**
	 * @see com.spm.common.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		execute();
	}
	
	public synchronized void execute() {
		if (order == null) {
			User user = Application.APPLICATION_PROVIDER.get().getUser();
			Long secuencial = user.getOrderNumber() + 1;
			user.setOrderNumber(secuencial);
			userRepository.add(user);
			
			Long newId = new Long(clientId.toString() + secuencial.toString());
			order = new Order(newId, secuencial.toString(), DateUtils.DDMMYYYY_DATE_FORMAT.format(new Date()),
					Order.NORMAL, Order.TO_DELIVER, clientId, orderItems, Boolean.FALSE, user.getId());
			order.modify(clientId.toString() + "." + secuencial.toString());
			orderRepository.add(order); // TODO manage ID!

			//todo!
//			Log.d("GPS for SPM", "--- pre thread ! ---");
//			ThreadUtils.execute(new Runnable() {
//
//				@Override
//				public void run() {
//					Log.d("GPS for SPM", "--- init de thread ! ---");
//					Intent service = new Intent(AndroidApplication.get(), MyLocationService.class);
//					service.putExtra("ID", order.getId());
//					AndroidApplication.get().startService(service);
//				}
//			});
			
		} else {
			order = orderRepository.get(order.getId());
			orderItems.clear();
			orderItems.addAll(order.getProducts());
		}
	}
	
	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}
	
	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}
	
	/**
	 * @return the packs
	 */
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	
	/**
	 * @param orderToAdd
	 */
	public void addProduct(OrderItem orderToAdd) {
		
		boolean exists = false;
		for (OrderItem existentOrderItem : order.getProducts()) {
			if (orderToAdd.getId().equals(existentOrderItem.getId())) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			order.getProducts().add(orderToAdd);
			// orderRepository.add(order);
		}
	}
	
	/**
	 * @param c
	 */
	public void deleteProduct(OrderItem c) {
		order.getProducts().remove(c);
		// orderRepository.add(order);
	}
	
	// /**
	// * @param order
	// */
	// public void updateOrder(Order order) {
	// orderRepository.add(order);
	// }
	
	/**
	 * @param clientId
	 */
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	
}