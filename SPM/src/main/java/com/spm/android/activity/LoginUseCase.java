package com.spm.android.activity;

import com.google.inject.Inject;
import com.spm.common.domain.Application;
import com.spm.common.exception.CommonErrorCode;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.User;
import com.spm.repository.UserRepository;
import com.spm.service.APIService;

/**
 * Use case to handle the {@link User}'s login.
 * 
 * @author Agustin Sgarlata
 */
public class LoginUseCase extends DefaultAbstractUseCase {

	private String username;

	User user;

	private UserRepository userRepository;

	@Inject
	public LoginUseCase(APIService apiService, UserRepository userRepository) {
		super(apiService);
		this.userRepository = userRepository;
	}

	/**
	 * @see com.splatt.common.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		// String name = getApiService().getContacts();
		try {
			user = userRepository.getByName(username.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TelephonyManager tMgr = (TelephonyManager) AndroidApplication.get()
		// .getSystemService(Context.TELEPHONY_SERVICE);
		// String phoneNumber = tMgr.getLine1Number();
		// if ((user != null) && (user.getFirstName().length() > 0) &&
		// (phoneNumber != null)
		// && phoneNumber.equals(user.getPhoneNumber()) &&
		// user.checkValidDate()) {
		if ((user != null) && (user.getFirstName().length() > 0)
				&& user.checkValidDate()) { // for rapid access

			user = new User(user.getId(), user.getFirstName(),
					user.getClients(), user.getUpdateDate(),
					user.getOrderNumber(), user.getUsersUpdateDate());
			Application.APPLICATION_PROVIDER.get().attach(user);
		} else {
			throw CommonErrorCode.USER_NOT_FOUND_ERROR
					.newLocalBusinessException();
		}
	}

	public void clearCanceledLogin() {
		Application.APPLICATION_PROVIDER.get().detachUser();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

}