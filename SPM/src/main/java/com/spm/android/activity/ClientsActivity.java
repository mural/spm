package com.spm.android.activity;

import roboguice.inject.InjectView;
import android.os.Bundle;

import com.spm.R;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.activity.AbstractListActivity;
import com.spm.android.common.view.ActionBar;
import com.spm.common.domain.Application;
import com.spm.domain.Client;
import com.spm.domain.User;

/**
 * 
 * @author Agustin Sgarlata
 */
public class ClientsActivity extends AbstractListActivity<Client> {

	private ClientsUseCase categoriesUseCase;
	private ClientsAdapter categoriesAdapter;

	@InjectView(R.id.actionBar)
	private ActionBar actionBar;

	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clients_activity);

		actionBar.setTitle("Clientes");

		categoriesUseCase = (ClientsUseCase) getLastNonConfigurationInstance();
		if (categoriesUseCase == null) {
			categoriesUseCase = getInstance(ClientsUseCase.class);
		}

		categoriesUseCase.addListener(this);
		if (categoriesUseCase.isNotInvoked()) {
			executeUseCase(categoriesUseCase);
		} else if (categoriesUseCase.isInProgress()) {
			onStartUseCase();
		} else if (categoriesUseCase.isFinishSuccessful()) {
			onFinishUseCase();
		} else if (categoriesUseCase.isFinishFailed()) {
			onFinishUseCase();
		}
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		User user = AndroidApplication.get().getUser();
		if (user == null) {
			ActivityLauncher.launchActivity(LoginActivity.class);
			finish();
		} else if (!Application.APPLICATION_PROVIDER.get().getUser()
				.checkValidDate()) {
			AndroidApplication.get().logout();
		}
	}

	/**
	 * @see roboguice.activity.RoboListActivity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return categoriesUseCase;
	}

	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onListItemClick(java.lang.Object)
	 */
	@Override
	protected void onListItemClick(final Client client) {
		super.onListItemClick(client);

		// final DialogInterface.OnClickListener dialogClickListener1 = new
		// DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(OrdersActivity.CLIENT, client);
		ActivityLauncher.launchActivity(OrdersActivity.class, bundle);
		// }
		// };

		// final DialogInterface.OnClickListener dialogClickListener2 = new
		// DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// Bundle bundle = new Bundle();
		// bundle.putSerializable(VisitsActivity.CLIENT, client);
		// ActivityLauncher.launchActivity(VisitsActivity.class, bundle);
		// }
		// };

		// executeOnUIThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// AlertDialogUtils.show2OptionsDialog(dialogClickListener1,
		// dialogClickListener2, R.string.chooseAction,
		// R.string.chooseActionText, R.string.orders, R.string.visits);
		// }
		// });
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {

		categoriesAdapter = new ClientsAdapter(this,
				categoriesUseCase.getClients());
		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				setListAdapter(categoriesAdapter);
				dismissLoading();
			}
		});
	}

	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		categoriesUseCase.removeListener(this);
	}
}
