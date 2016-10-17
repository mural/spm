package com.spm.android.activity;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.spm.R;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.activity.AbstractListActivity;
import com.spm.android.common.utils.LocalizationUtils;
import com.spm.android.common.view.ActionBar;
import com.spm.common.domain.Application;
import com.spm.domain.Client;
import com.spm.domain.Order;
import com.spm.domain.User;

import java.util.List;

/**
 * 
 * @author Agustin Sgarlata
 */
public class OrdersActivity extends AbstractListActivity<Order> {

	public static final String CLIENT = "client";

	private OrdersUseCase ordersUseCase;
	private OrdersAdapter ordersAdapter;

	@InjectView(R.id.ordersPriceDate)
	private TextView ordersPriceDate;

	@InjectView(R.id.actionBar)
	private ActionBar actionBar;

	@InjectExtra(value = CLIENT)
	private Long clientId;

	private Client client;
	Realm realm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders_activity);

		// Initialize Realm
		Realm.init(this);
		// Get a Realm instance for this thread
		realm = Realm.getDefaultInstance();
		// Build the query looking at all users:
		RealmQuery<Client> query = realm.where(Client.class);
		// Add query conditions:
		query.equalTo("id", clientId);
		// Execute the query:
		client = query.findFirst();

		actionBar.setTitle(client.getFirstName());

		actionBar.addImageViewAction(R.drawable.ic_add_order,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putSerializable(OrdersActivity.CLIENT, clientId);
						ActivityLauncher.launchActivity(
								DetailOrderActivity.class, bundle);
					}
				});

		actionBar.addImageViewAction(R.drawable.ic_sync_bl,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						ActivityLauncher.launchActivity(SyncActivity.class,
								bundle);
					}
				});

		registerForContextMenu(getListView());
	}

	public boolean checkUser(User user) {
		if (user == null) {
			ActivityLauncher.launchActivity(LoginActivity.class);
			finish();
			return false;
		} else if (!Application.APPLICATION_PROVIDER.get().getUser()
				.checkValidDate()) {
			AndroidApplication.get().logout();
			return false;
		}
		return true;
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		User user = AndroidApplication.get().getUser();
		if (checkUser(user)) {
			if (user.getUpdateDate() == null) {
				ordersPriceDate.setText(LocalizationUtils.getString(
						R.string.priceDate, "no actualizado"));
			} else {
				ordersPriceDate.setText(LocalizationUtils.getString(
						R.string.priceDate, user.getUpdateDate()
								.toLocaleString()));
			}

			ordersUseCase = (OrdersUseCase) getLastNonConfigurationInstance();
			if (ordersUseCase == null) {
				ordersUseCase = getInstance(OrdersUseCase.class);
			}

			ordersUseCase.addListener(this);
			if (ordersUseCase != null) {
				ordersUseCase.setClient(clientId);
				executeUseCase(ordersUseCase);
			} else if (ordersUseCase.isInProgress()) {
				onStartUseCase();
			} else if (ordersUseCase.isFinishSuccessful()) {
				onFinishUseCase();
			} else if (ordersUseCase.isFinishFailed()) {
				onFinishUseCase();
			}
		}
	}

	/**
	 * @see roboguice.activity.RoboListActivity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return ordersUseCase;
	}

	@Override
	protected void onListItemClick(Order order) {
		super.onListItemClick(order);

		// ActivityLauncher.launchActivity(SplattAnimationsActivity.class,
		// SplattAnimationsActivity.CATEGORY_ID,
		// category.getId());
		Bundle bundle = new Bundle();
		bundle.putSerializable(DetailOrderActivity.ORDER, order);
		bundle.putSerializable(OrdersActivity.CLIENT, ordersUseCase.getClient());
		ActivityLauncher.launchActivity(DetailOrderActivity.class, bundle);
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {

		ordersAdapter = new OrdersAdapter(this, ordersUseCase.getOrders());
		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				setListAdapter(ordersAdapter);
				dismissLoading();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (ordersUseCase != null) {
			ordersUseCase.removeListener(this);
		}
		realm.close();
	}

	/**
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
	 *      android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, R.string.delete, Menu.NONE, R.string.delete);
	}

	/**
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.string.delete:
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
					.getMenuInfo();
			ordersUseCase.deleteOrder((Order) getListAdapter().getItem(
					info.position));
			executeUseCase(ordersUseCase);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
