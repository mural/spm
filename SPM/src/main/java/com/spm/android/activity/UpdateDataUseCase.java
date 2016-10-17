package com.spm.android.activity;

import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.spm.android.common.AndroidApplication;
import com.spm.common.domain.Application;
import com.spm.common.exception.CommonErrorCode;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.Client;
import com.spm.domain.Product;
import com.spm.domain.User;
import com.spm.repository.ClientRepository;
import com.spm.repository.ProductRepository;
import com.spm.repository.UserRepository;
import com.spm.service.APIService;

import hugo.weaving.DebugLog;

/**
 * Use case to handle the {@link User}'s login.
 * 
 * @author Agustin Sgarlata
 */
public class UpdateDataUseCase extends DefaultAbstractUseCase {

	ProductRepository productRepository;
	ClientRepository clientRepository;
	UserRepository userRepository;

	@Inject
	public UpdateDataUseCase(APIService apiService,
			ProductRepository productRepository,
			ClientRepository clientRepository, UserRepository userRepository) {
		super(apiService);
		this.productRepository = productRepository;
		this.clientRepository = clientRepository;
		this.userRepository = userRepository;

	}

	@DebugLog
	@Override
	protected void doExecute() {
		User user = Application.APPLICATION_PROVIDER.get().getUser();
		Date dateOfPriceList = getApiService().getPriceListDate();
//		if ((user != null) && (user.getUpdateDate() != null)
//				&& (dateOfPriceList.compareTo(user.getUpdateDate()) == 0)) {
//			// actualizada!
//		} else if (((user != null) && (user.getUpdateDate() != null) && (dateOfPriceList
//				.compareTo(user.getUpdateDate()) < 0))
//				|| ((user != null) && (user.getUpdateDate() == null))) {

			List<Client> clients = getApiService().getClients(user);
			clientRepository.addAll(clients);

			List<Product> products = getApiService().getProducts();
			productRepository.addAll(products); // muyy lento! 45s.

			user.setUpdateDate(dateOfPriceList);
			// Can be changed to // priceListDate, // and use updateDate for
			// other purpose
			userRepository.add(user);
//		}

	}

	public boolean isUpdatedData() {
		User user = Application.APPLICATION_PROVIDER.get().getUser();
		try {
			Date dateOfPriceList = getApiService().getPriceListDate();
			if ((user != null) && (user.getUpdateDate() != null)
					&& (dateOfPriceList.compareTo(user.getUpdateDate()) == 0)) {
				return true;
			} else if (((user != null) && (user.getUpdateDate() != null) && (dateOfPriceList
					.compareTo(user.getUpdateDate()) < 0))
					|| ((user != null) && (user.getUpdateDate() == null))) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}