package com.spm.repository;

import com.google.inject.ImplementedBy;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.common.repository.Repository;
import com.spm.domain.User;

/**
 * 
 * @author Agustin Sgarlata
 */
@ImplementedBy(DBUserRepository.class)
// @ImplementedBy(SQLUserRepository.class)
public interface UserRepository extends Repository<User> {
	
	/**
	 * @param name
	 * @return user
	 * @throws ObjectNotFoundException
	 */
	User getByName(String name) throws ObjectNotFoundException;
	
}
