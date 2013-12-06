package com.spm.repository;

import java.util.Collection;
import com.google.inject.ImplementedBy;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.common.repository.Repository;
import com.spm.domain.Client;

/**
 * 
 * @author Agustin Sgarlata
 */
@ImplementedBy(DBClientRepository.class)
public interface ClientRepository extends Repository<Client> {
	
	/**
	 * @param userId
	 * @return
	 * @throws ObjectNotFoundException
	 */
	Collection<Client> getByUserId(Long userId) throws ObjectNotFoundException;
	
}
