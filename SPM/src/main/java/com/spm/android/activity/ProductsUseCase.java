package com.spm.android.activity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.internal.Lists;
import com.google.inject.internal.Sets;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.Product;
import com.spm.service.APIService;

/**
 * Use case to handle the splatt screen.
 * 
 * @author Agustin Sgarlata
 */
public class ProductsUseCase extends DefaultAbstractUseCase {

	private List<Product> products = Lists.newArrayList();
	// private Long lineId;

	private Set<Product> selectedItems = Sets.newHashSet();


	@Inject
	public ProductsUseCase(APIService apiService){
		super(apiService);
	}

	@Override
	protected void doExecute() {
		// This logic in DETAIL ORDER ACTIVITY!!! (and use the common one for
		// this...)
		// List<Product> newProducts = productRepository.get(product);
//		List<Product> newProducts = productRepository.getAll();
//		for (Product productToAdd : newProducts) {
//			boolean exists = false;
//			for (Product existentProduct : products) {
//				if (productToAdd.getId().equals(existentProduct.getId())) {
//					exists = true;
//					break;
//				}
//			}
//			if (!exists) {
//				products.add(productToAdd);
//			}
//		}
	}

	/**
	 * @return the products
	 */
	public List<Product> getProducts() {
		Collections.sort(products);
		return products;
	}

	/**
	 * @return clear the products
	 */
	public void clearProducts() {
		for (Product product : products) {
			product.setQuantity(0);
		}
	}

	// /**
	// * @param lineId the lineId to set
	// */
	// public void setLineId(Long lineId) {
	// this.lineId = lineId;
	// }

	/**
	 * @return the selected Products
	 */
	public Set<Product> getSelectedItems() {
		return selectedItems;
	}

}