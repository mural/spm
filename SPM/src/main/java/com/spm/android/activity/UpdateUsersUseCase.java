package com.spm.android.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.db4o.ext.DatabaseFileLockedException;
import com.google.inject.Inject;
import com.spm.common.domain.Application;
import com.spm.common.exception.CommonErrorCode;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.Order;
import com.spm.domain.User;
import com.spm.domain.Visit;
import com.spm.repository.ClientRepository;
import com.spm.repository.OrderRepository;
import com.spm.repository.ProductRepository;
import com.spm.repository.UserRepository;
import com.spm.repository.VisitRepository;
import com.spm.service.APIService;

/**
 * Use case to handle the {@link User}'s login.
 * 
 * @author Agustin Sgarlata
 */
public class UpdateUsersUseCase extends DefaultAbstractUseCase {

	UserRepository userRepository;
	private OrderRepository orderRepository;
	private VisitRepository visitRepository;
	ProductRepository productRepository;
	ClientRepository clientRepository;

	@Inject
	public UpdateUsersUseCase(APIService apiService,
			UserRepository userRepository, OrderRepository orderRepository,
			VisitRepository visitRepository,
			ProductRepository productRepository,
			ClientRepository clientRepository) {
		super(apiService);
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
		this.visitRepository = visitRepository;
		this.productRepository = productRepository;
		this.clientRepository = clientRepository;
	}

	/**
	 * @see com.splatt.common.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		try {
			List<User> users = getApiService().getContacts();
			userRepository.addAll(users);

			GregorianCalendar days = new GregorianCalendar();
			days.setTime(new Date());
			days.add(Calendar.DAY_OF_YEAR, -15);

			for (Order order : orderRepository.getAll()) {
				if (order.getSync()
						&& (order.getSyncDate().compareTo(days.getTime()) < 0)) {
					orderRepository.remove(order);
				}
			}
			for (Visit visit : visitRepository.getAll()) {
				if (visit.getSync()
						&& (visit.getSyncDate().compareTo(days.getTime()) < 0)) {
					visitRepository.remove(visit);
				}
			}

			// TODO necesito saber que usuario es primero antes de traer estos
			// datos..
			// User user = AndroidApplication.get().getUser();
			// Date dateOfPriceList = getApiService().getPriceListDate();
			// if ((user.getUpdateDate() == null)
			// || (dateOfPriceList.compareTo(user.getUpdateDate()) > 0)) {
			// UpdateDataUseCase updateDataUseCase = new UpdateDataUseCase(
			// getApiService(), productRepository, clientRepository,
			// userRepository);
			// updateDataUseCase.doExecute();
			// }

		} catch (DatabaseFileLockedException e) {
			throw CommonErrorCode.DB4O_ERROR.newLocalBusinessException();
		}

	}

	public void clearCanceledLogin() {
		Application.APPLICATION_PROVIDER.get().detachUser();
	}

}