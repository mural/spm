package com.spm.repository;

import com.google.inject.ImplementedBy;
import com.spm.common.repository.Repository;
import com.spm.domain.Line;

/**
 * 
 * @author Agustin Sgarlata
 */
@ImplementedBy(DBLineRepository.class)
public interface LineRepository extends Repository<Line> {
	
}
