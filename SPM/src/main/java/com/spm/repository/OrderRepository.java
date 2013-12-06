package com.spm.repository;

import java.util.Collection;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.common.repository.Repository;
import com.spm.domain.Order;

/**
 * 
 * @author Agustin Sgarlata
 */
public interface OrderRepository extends Repository<Order> {
	
	/**
	 * @param userId
	 * @return
	 * @throws ObjectNotFoundException
	 */
	Collection<Order> getByUserId(Long userId) throws ObjectNotFoundException;
	
}
