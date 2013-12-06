package com.spm.repository;

import java.util.Collection;
import com.spm.common.repository.ObjectNotFoundException;
import com.spm.common.repository.Repository;
import com.spm.domain.Visit;

/**
 * 
 * @author Agustin Sgarlata
 */
public interface VisitRepository extends Repository<Visit> {
	
	/**
	 * @param userId
	 * @return
	 * @throws ObjectNotFoundException
	 */
	Collection<Visit> getByUserId(Long userId) throws ObjectNotFoundException;
	
}
