package com.spm.android.activity;

import java.util.List;

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

import com.google.inject.internal.Lists;
import com.spm.R;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.AndroidUseCaseListener;
import com.spm.android.common.activity.AbstractListActivity;
import com.spm.android.common.activity.ActivityIf;
import com.spm.android.common.utils.LocalizationUtils;
import com.spm.android.common.utils.ToastUtils;
import com.spm.android.common.view.ActionBar;
import com.spm.common.domain.Application;
import com.spm.domain.Order;
import com.spm.domain.User;
import com.spm.domain.Visit;
import com.spm.domain.Work;

/**
 * 
 * @author Agustin Sgarlata
 */
public class SyncActivity extends AbstractListActivity<Work> {

	private SyncUseCase syncUseCase;
	private SyncAdapter syncAdapter;

	private SyncDataUseCase syncDataUseCase;
	private SyncDataUseCaseListener syncDataUseCaseListener;

	@InjectView(R.id.syncPriceDate)
	private TextView syncPriceDate;

	@InjectView(R.id.actionBar)
	private ActionBar actionBar;

	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync_activity);

		User user = AndroidApplication.get().getUser();
		if (user == null) {
			ActivityLauncher.launchActivity(LoginActivity.class);
			finish();
		} else {

			actionBar.setTitle("Sincronizar");

			if (user.getUpdateDate() == null) {
				syncPriceDate.setText(LocalizationUtils.getString(
						R.string.priceDate, "no actualizado"));
			} else {
				syncPriceDate.setText(LocalizationUtils.getString(
						R.string.priceDate, user.getUpdateDate()
								.toLocaleString()));
			}

			syncUseCase = (SyncUseCase) getLastNonConfigurationInstance();
			if (syncUseCase == null) {
				syncUseCase = getInstance(SyncUseCase.class);
			}

			syncUseCase.addListener(this);
			if (syncUseCase != null) {
				executeUseCase(syncUseCase);
			} else if (syncUseCase.isInProgress()) {
				onStartUseCase();
			} else if (syncUseCase.isFinishSuccessful()) {
				onFinishUseCase();
			} else if (syncUseCase.isFinishFailed()) {
				onFinishUseCase();
			}

			actionBar.reset();
			actionBar.addImageViewAction(R.drawable.ic_check,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							List<Work> works = Lists.newArrayList();
							works.addAll(syncUseCase.getSelectedItems());

							// for (Order order :
							// syncUseCase.getSelectedItems()) {
							// // order.modifySync(Boolean.TRUE);
							// // syncUseCase.updateOrder(order);
							// }
							syncDataUseCase.works = syncUseCase
									.getSelectedItems();
							executeUseCase(syncDataUseCase);
						}
					});

			// TODO: what happens when interrupt ?
			// syncDataUseCase =
			// (SyncDataUseCase)getLastNonConfigurationInstance();
			if (syncDataUseCase == null) {
				syncDataUseCase = getInstance(SyncDataUseCase.class);
			}
			syncDataUseCaseListener = new SyncDataUseCaseListener();
			syncDataUseCase.addListener(syncDataUseCaseListener);

			registerForContextMenu(getListView());

		}
	}

	/**
	 * @see roboguice.activity.RoboListActivity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return syncUseCase;
	}

	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onListItemClick(java.lang.Object)
	 */
	@Override
	protected void onListItemClick(Work work) {
		super.onListItemClick(work);

		ToastUtils.showToast(work.getName());

		Bundle bundle = new Bundle();
		if (work instanceof Order) {
			bundle.putSerializable(DetailOrderActivity.ORDER, work);
			bundle.putSerializable(OrdersActivity.CLIENT,
					syncUseCase.getClient(work));
			ActivityLauncher.launchActivity(DetailOrderActivity.class, bundle);
		}
		if (work instanceof Visit) {
			bundle.putSerializable(DetailVisitActivity.VISIT, work);
			bundle.putSerializable(VisitsActivity.CLIENT,
					syncUseCase.getClient(work));
			ActivityLauncher.launchActivity(DetailVisitActivity.class, bundle);
		}
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {

		syncAdapter = new SyncAdapter(this, syncUseCase.getWorks(),
				syncUseCase.getSelectedItems());
		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				setListAdapter(syncAdapter);
				dismissLoading();
			}
		});
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
	 * @see com.splatt.android.common.activity.AbstractListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (syncUseCase != null) {
			syncUseCase.removeListener(this);
		}
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
			syncUseCase.deleteWork((Work) getListAdapter().getItem(
					info.position));
			executeUseCase(syncUseCase);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class SyncDataUseCaseListener extends AndroidUseCaseListener {

		@Override
		public void onFinishUseCase() {
			dismissLoading();
			ToastUtils.showToastOnUIThread("Datos sincronizados: "
					+ syncDataUseCase.result);
			executeUseCase(syncUseCase);
		}

		/**
		 * @see com.spm.android.common.AndroidUseCaseListener#getActivityIf()
		 */
		@Override
		protected ActivityIf getActivityIf() {
			return SyncActivity.this;
		}
	}
}
