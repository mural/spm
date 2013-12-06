package com.spm.repository;

import com.google.inject.ImplementedBy;
import com.spm.common.repository.Repository;
import com.spm.domain.Product;

/**
 * 
 * @author Agustin Sgarlata
 */
@ImplementedBy(DBProductRepository.class)
public interface ProductRepository extends Repository<Product> {
	
}
