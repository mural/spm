package com.spm.repository;

import java.util.Collection;
import java.util.List;
import android.content.Context;
import com.db4o.query.Query;
import com.google.inject.Inject;
import com.spm.common.domain.Entity;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.domain.Order;
import com.spm.store.DbProvider;

/**
 * 
 * @author Agustin Sgarlata
 */
public class DBOrderRepository extends DbProvider<Order> implements OrderRepository {
	
	@Inject
	public DBOrderRepository(Context ctx) {
		super(Order.class, ctx);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public Order get(Long id) throws ObjectNotFoundException {
		Order order = new Order(id);
		return super.get(order).get(0);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public Collection<Order> getByUserId(Long userId) throws ObjectNotFoundException {
		Query query = db().query();
		query.constrain(Order.class);
		query.descend("userId").constrain(userId);
		Collection<Order> result = query.execute();
		return result;
	}
	
	/**
	 * @see com.spm.common.repository.Repository#add(com.spm.common.domain.Entity)
	 */
	@Override
	public void add(Order entity) {
		super.store(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<Order> entities) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(com.spm.common.domain.Entity)
	 */
	@Override
	public void remove(Order entity) {
		super.delete(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#removeAll()
	 */
	@Override
	public void removeAll() {
		// super.
	}
	
	/**
	 * @see com.spm.common.repository.Repository#getAll()
	 */
	@Override
	public List<Order> getAll() {
		return super.findAll();
	}
	
	/**
	 * @see com.spm.common.repository.Repository#get(Entity)
	 */
	@Override
	public List<Order> get(Order entity) {
		return super.get(entity);
	}
	
	/**
	 * @see com.spm.common.repository.Repository#remove(java.lang.Long)
	 */
	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	public void loadInitialData() {
		removeAll();
		db().commit();
		if (super.findAll().isEmpty()) {
			// List<OrderItem> items1 = Lists.newArrayList();
			// OrderItem orderItem11 = new OrderItem(2012L, "2012 TEMBLEKE SIMPSONS C/STICKERS 16X40", 0, 0,
			// new Double(30));
			// items1.add(orderItem11);
			// Order order1 = new Order(052100035333L, "052.100035.333", 100035L, items1);
			// Order order2 = new Order(000111111334L, "000-111111-334", 100036L, null);
			
			// add(order1);
			// add(order2);
		}
	}
}
