package com.spm.android.activity;

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

import com.spm.R;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.activity.AbstractListActivity;
import com.spm.android.common.utils.LocalizationUtils;
import com.spm.android.common.view.ActionBar;
import com.spm.common.domain.Application;
import com.spm.domain.Client;
import com.spm.domain.User;
import com.spm.domain.Visit;

/**
 * 
 * @author Agustin Sgarlata
 */
public class VisitsActivity extends AbstractListActivity<Visit> {

	public static final String CLIENT = "client";

	private VisitsUseCase visitsUseCase;
	private VisitsAdapter visitsAdapter;

	// @InjectView(R.id.visitsPriceDate) //TODO old version
	private TextView visitsPriceDate;

	@InjectView(R.id.actionBar)
	private ActionBar actionBar;

	@InjectExtra(value = CLIENT)
	private Client client;

	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visits_activity);

		actionBar.addImageViewAction(R.drawable.add, new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(VisitsActivity.CLIENT,
						visitsUseCase.getClient());
				ActivityLauncher.launchActivity(DetailVisitActivity.class,
						bundle);
			}
		});

		registerForContextMenu(getListView());
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

		if (user.getUpdateDate() == null) {
			visitsPriceDate.setText(LocalizationUtils.getString(
					R.string.priceDate, "no actualizado"));
		} else {
			visitsPriceDate.setText(LocalizationUtils.getString(
					R.string.priceDate, user.getUpdateDate().toLocaleString()));
		}

		visitsUseCase = (VisitsUseCase) getLastNonConfigurationInstance();
		if (visitsUseCase == null) {
			visitsUseCase = getInstance(VisitsUseCase.class);
		}

		visitsUseCase.addListener(this);
		if (visitsUseCase != null) {
			visitsUseCase.setClient(client);
			executeUseCase(visitsUseCase);
		} else if (visitsUseCase.isInProgress()) {
			onStartUseCase();
		} else if (visitsUseCase.isFinishSuccessful()) {
			onFinishUseCase();
		} else if (visitsUseCase.isFinishFailed()) {
			onFinishUseCase();
		}
	}

	/**
	 * @see roboguice.activity.RoboListActivity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return visitsUseCase;
	}

	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onListItemClick(java.lang.Object)
	 */
	@Override
	protected void onListItemClick(Visit visit) {
		super.onListItemClick(visit);

		Bundle bundle = new Bundle();
		bundle.putSerializable(DetailVisitActivity.VISIT, visit);
		bundle.putSerializable(VisitsActivity.CLIENT, visitsUseCase.getClient());
		ActivityLauncher.launchActivity(DetailVisitActivity.class, bundle);
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {

		visitsAdapter = new VisitsAdapter(this, visitsUseCase.getVisits());
		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				setListAdapter(visitsAdapter);
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
		visitsUseCase.removeListener(this);
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
			visitsUseCase.deleteVisit((Visit) getListAdapter().getItem(
					info.position));
			executeUseCase(visitsUseCase);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
