package com.spm.android.activity;

import java.util.Date;

import roboguice.inject.InjectView;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.internal.Nullable;
import com.spm.R;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.activity.AbstractActivity;
import com.spm.android.common.listener.AsyncOnClickListener;
import com.spm.android.common.listener.LaunchOnClickListener;
import com.spm.android.common.utils.AlertDialogUtils;
import com.spm.android.common.utils.LocalizationUtils;
import com.spm.android.common.utils.ToastUtils;
import com.spm.common.domain.Application;
import com.spm.domain.User;

/**
 * 
 * @author Agustin Sgarlata
 */
public class DashBoardActivity extends AbstractActivity {

	private UpdateDataUseCase updateDataUseCase;

	@Nullable
	@InjectView(R.id.userName)
	private TextView userName;

	@Nullable
	@InjectView(R.id.clientes)
	private Button clientes;

	@Nullable
	@InjectView(R.id.sync)
	private Button sync;

	@Nullable
	@InjectView(R.id.update)
	private Button update;

	@InjectView(R.id.dashPriceList)
	private TextView dashPriceList;

	@Nullable
	@InjectView(R.id.map)
	private Button map;

	boolean justCreated = true;

	/**
	 * @see com.spm.android.common.activity.AbstractActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dashboard_activity);

		clientes.setOnClickListener(new LaunchOnClickListener(
				ClientsActivity.class));
		sync.setOnClickListener(new LaunchOnClickListener(SyncActivity.class));
		// map.setOnClickListener(new
		// LaunchOnClickListener(MyMapActivity.class));

		update.setOnClickListener(new AsyncOnClickListener() {

			@Override
			public void onAsyncRun(View view) {
				// update();
				executeUseCase(updateDataUseCase);
			}
		});

		updateDataUseCase = (UpdateDataUseCase) getLastNonConfigurationInstance();
		if (updateDataUseCase == null) {
			updateDataUseCase = getInstance(UpdateDataUseCase.class);
		}
		updateDataUseCase.addListener(this);
		if (updateDataUseCase.isInProgress()) {
			onStartUseCase();
		} else if (updateDataUseCase.isFinishSuccessful()) {
			onFinishUseCase();
		} else if (updateDataUseCase.isFinishFailed()) {
			onFinishUseCase();
		}

		userName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// testApi();
			}
		});

	}

	private void testApi() {
		User user = Application.APPLICATION_PROVIDER.get().getUser();
		// APIServiceImpl api = new APIServiceImpl();
		// String result = api.lastOrderNumber(user).toString();

		// TelephonyManager tMgr =
		// (TelephonyManager)AndroidApplication.get().getSystemService(Context.TELEPHONY_SERVICE);
		// String result = tMgr.getLine1Number();

		ToastUtils.showToast(user.getId().toString());
	}

	/**
	 * @see roboguice.activity.RoboActivity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return updateDataUseCase;
	}

	private void update() {
		final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				executeUseCase(updateDataUseCase);
			}
		};

		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				AlertDialogUtils.showOkCancelDialog(dialogClickListener,
						R.string.confirmUpdateTitle, R.string.confirmUpdateMsg,
						R.string.update, R.string.notUpdate);
			}
		});
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {

		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				updateDate();
				dismissLoading();
				ToastUtils.showToastOnUIThread("Datos actualizados");
			}
		});
	}

	private void updateDate() {
		User user = AndroidApplication.get().getUser();
		if (user != null) {
			if (user.getUpdateDate() == null) {
				dashPriceList.setText(LocalizationUtils.getString(
						R.string.priceDate, "no actualizado"));
			} else {
				dashPriceList.setText(LocalizationUtils.getString(
						R.string.priceDate, user.getUpdateDate()
								.toLocaleString()));
			}
		}
	}

	/**
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		AlertDialogUtils.showExitOkCancelDialog();
	}

	/**
	 * @see com.spm.android.common.activity.AbstractActivity#onResume()
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
			user.setUpdateDate(new Date());
			Application.APPLICATION_PROVIDER.get().attach(user);
			// AndroidApplication.get().logout(); //TODO waht ? ?¡
		} else {
			userName.setText(user.getFirstName());
		}

		updateDate();

		if (justCreated) {
			update.performClick(); // only first time...
			justCreated = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, R.drawable.ic_sync, Menu.NONE, R.string.resync)
				.setIcon(R.drawable.ic_sync);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.drawable.ic_sync:
			update.performClick();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
