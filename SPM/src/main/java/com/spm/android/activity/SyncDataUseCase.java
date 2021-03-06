package com.spm.android.activity;

import android.content.Context;

import java.util.Date;
import java.util.Set;

import com.google.inject.Inject;
import com.spm.android.common.AndroidApplication;
import com.spm.common.exception.CommonErrorCode;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.Client;
import com.spm.domain.Order;
import com.spm.domain.User;
import com.spm.domain.Visit;
import com.spm.domain.Work;
import com.spm.repository.OrderRepository;
import com.spm.repository.VisitRepository;
import com.spm.service.APIService;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Use case to handle the {@link User}'s login.
 * 
 * @author Agustin Sgarlata
 */
public class SyncDataUseCase extends DefaultAbstractUseCase {

	public String result;

	OrderRepository orderRepository;
	VisitRepository visitRepository;
	protected Set<Work> works;
	Long clientId;
	Context context;

	@Inject
	public SyncDataUseCase(APIService apiService,
			OrderRepository orderRepository,
			VisitRepository visitRepository,
						  Context context) {
		super(apiService);
		this.orderRepository = orderRepository;
		this.visitRepository = visitRepository;
		this.context = context;
	}

	@Override
	protected void doExecute() {

		User user = AndroidApplication.get().getUser();
		Date dateOfPriceList = getApiService().getPriceListDate();
		if ((user.getUpdateDate() == null)
				|| (dateOfPriceList.compareTo(user.getUpdateDate()) > 0)) {
			throw CommonErrorCode.SYNC_DATE_ERROR.newLocalBusinessException();
		}

		if ((works == null) || works.isEmpty()) {
			throw CommonErrorCode.NOT_SELECTED_ITEMS_ERROR
					.newLocalBusinessException();
		}

		String sync; // OK or ERROR
		int ok = 0;
		int error = 0;
		int empty = 0;
		for (Work work : works) {
			if (work instanceof Order) {
				if (!((Order) work).getProducts().isEmpty()) {

					// Initialize Realm
					Realm.init(context);
					// Get a Realm instance for this thread
					Realm realm = Realm.getDefaultInstance();
					// Build the query looking at all users:
					RealmQuery<Client> query = realm.where(Client.class);
					query.equalTo("id", ((Order) work).getClientId());
					// Execute the query:
					Client client = query.findFirst();

					sync = getApiService().sync((Order) work, realm.copyFromRealm(client));

					if (sync.equals("OK")) {
						ok++;
						((Order) work).modifySync(Boolean.TRUE);
						((Order) work).setSyncDate(new Date());
						orderRepository.add((Order) work);
					} else {
						error++;
					}
				} else {
					empty++;
				}
			}
			if (work instanceof Visit) {
				if ((((Visit) work).getComment() != null)
						&& (((Visit) work).getComment().length() > 0)) {

					sync = getApiService().sync((Visit) work);

					if (sync.equals("OK")) {
						ok++;
						((Visit) work).modifySync(Boolean.TRUE);
						((Visit) work).setSyncDate(new Date());
						visitRepository.add((Visit) work);
					} else {
						error++;
					}
				} else {
					empty++;
				}
			}
		}

		result = ok + " OK";
		if (error > 0) {
			result = result.concat(" " + error + " Error");
		}
		if (empty > 0) {
			result = result
					.concat("\nLos Pedidos sin productos no se sincronizaron");
		}
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
}