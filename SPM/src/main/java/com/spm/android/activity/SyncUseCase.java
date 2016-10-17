package com.spm.android.activity;

import android.content.Context;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.internal.Lists;
import com.google.inject.internal.Sets;
import com.spm.common.domain.Application;
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
import io.realm.RealmResults;

/**
 * Use case to handle the Sync screen.
 * 
 * @author Agustin Sgarlata
 */
public class SyncUseCase extends DefaultAbstractUseCase {

	private List<Work> works = Lists.newArrayList();

	private Set<Work> selectedItems = Sets.newHashSet();

	private OrderRepository orderRepository;
	private VisitRepository visitRepository;

	Context context;

	@Inject
	public SyncUseCase(APIService apiService, OrderRepository orderRepository,
			  VisitRepository visitRepository, Context context) {
		super(apiService);
		this.orderRepository = orderRepository;
		this.visitRepository = visitRepository;
		this.context = context;
	}

	@Override
	protected void doExecute() {
		works.clear();
		User user = Application.APPLICATION_PROVIDER.get().getUser();
		works.addAll(orderRepository.getByUserId(user.getId()));
		works.addAll(visitRepository.getByUserId(user.getId()));
	}

	/**
	 * @return the works
	 */
	public List<Work> getWorks() {
		Collections.sort(works);
		return works;
	}

	/**
	 * @param work
	 * @return the client
	 */
	public Client getClient(Work work) {
			// Initialize Realm
			Realm.init(context);
			// Get a Realm instance for this thread
			Realm realm = Realm.getDefaultInstance();
			// Build the query looking at all users:
			RealmQuery<Client> query = realm.where(Client.class);
			query.equalTo("id", ((Order) work).getClientId());
			// Execute the query:
			Client client = query.findFirst();

			return realm.copyFromRealm(client);
	}

	/**
	 * @return the selected Products
	 */
	public Set<Work> getSelectedItems() {
		return selectedItems;
	}

	/**
	 * @param work
	 */
	public void updateWork(Work work) {
		if (work instanceof Order) {
			orderRepository.add((Order) work);
		} else {
			visitRepository.add((Visit) work);
		}
	}

	/**
	 * @param w
	 */
	public void deleteWork(Work w) {
		works.remove(w);
		if (w instanceof Order) {
			orderRepository.remove((Order) w);
		} else {
			visitRepository.remove((Visit) w);
		}
	}

}