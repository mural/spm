package com.spm.android.activity;

import android.content.Context;

import java.util.List;
import com.google.inject.Inject;
import com.google.inject.internal.Lists;
import com.spm.common.domain.Application;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.Client;
import com.spm.domain.User;
import com.spm.service.APIService;

import io.realm.Realm;

/**
 * Use case to handle the splatt screen.
 * 
 * @author Agustin Sgarlata
 */
public class ClientsUseCase extends DefaultAbstractUseCase {
	
	private List<Client> clients = Lists.newArrayList();


	private Realm realm;
	private Context context;

	@Inject
	public ClientsUseCase(APIService apiService,   Context context) {
		super(apiService);
		this.context = context;
	}

	@Override
	protected void doExecute() {
		
		User user = Application.APPLICATION_PROVIDER.get().getUser();
		
		// List<Client> allClients = clientRepository.getAll();
		// for (Client client : allClients) {
		// if (user.getClients().contains(client)) {
		// clients.add(client);
		// }
		// }
		//clients.addAll(clientRepository.getByUserId(user.getId()));
		//Collections.sort(clients);
	}
	
	/**
	 * @return the clients
	 */
	public List<Client> getClients() {
		return clients;
	}
}