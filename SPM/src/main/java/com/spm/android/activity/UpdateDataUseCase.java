package com.spm.android.activity;

import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
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

	/**
	 * @see com.splatt.common.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		User user = Application.APPLICATION_PROVIDER.get().getUser();
		Date dateOfPriceList = getApiService().getPriceListDate();
		if ((user != null) && (user.getUpdateDate() != null)
				&& (dateOfPriceList.compareTo(user.getUpdateDate()) == 0)) {
			throw CommonErrorCode.UPDATE_DATA_DATE_ERROR
					.newLocalBusinessException();
		}

		List<Client> clients = getApiService().getClients(user);
		clientRepository.addAll(clients);

		List<Product> products = getApiService().getProducts();
		productRepository.addAll(products);

		user.setUpdateDate(dateOfPriceList); // Can be changed to priceListDate,
												// and use updateDate for other
												// purpose
		userRepository.add(user);
	}

}