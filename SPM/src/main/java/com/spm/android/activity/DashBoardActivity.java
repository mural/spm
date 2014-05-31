package com.spm.android.activity;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import roboguice.inject.InjectView;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import com.spm.common.utils.ThreadUtils;
import com.spm.domain.User;
import com.spm.domain.Work;

/**
 * 
 * @author Agustin Sgarlata
 */
public class DashBoardActivity extends AbstractActivity {

	private UpdateDataUseCase updateDataUseCase;
	private SyncUseCase syncUseCase;

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

	@Nullable
	@InjectView(R.id.dashPriceList)
	private TextView dashPriceList;

	@Nullable
	@InjectView(R.id.map)
	private Button map;

	boolean updated = false;
	boolean justCreated;

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

		updateDataUseCase = (UpdateDataUseCase) getLastNonConfigurationInstance();
		if (updateDataUseCase == null) {
			updateDataUseCase = getInstance(UpdateDataUseCase.class);
		}

		updateDataUseCase.addListener(this);
		update.setOnClickListener(new AsyncOnClickListener() {

			@Override
			public void onAsyncRun(View view) {
				updateDataUseCase();
			}
		});

		userName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// testApi();
			}
		});
		justCreated = true;
	}

	private void updateDataUseCase() {
		if ((updateDataUseCase != null) && !updateDataUseCase.isInProgress()) {
			// onStartUseCase();
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					dashPriceList.setText("Actualizando...");
				}
			});

			ToastUtils.showToastOnUIThread("Actualizando...");
			executeUseCase(updateDataUseCase);
		} else if (updateDataUseCase.isInProgress()) {
			ToastUtils.showToastOnUIThread("Actualizando, espere por favor.");
		}
	}

	private void testApi() {
		User user = Application.APPLICATION_PROVIDER.get().getUser();
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

	/**
	 * @see com.spm.android.common.activity.AbstractActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		try {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					updateDate();
					updated = true;
					// datos actualizados a la ultima version de la base
					clientes.setBackgroundResource(R.drawable.button_flat_inverted);
					update.setVisibility(View.INVISIBLE);

					// dismissLoading();
					ToastUtils.showToastOnUIThread("Datos actualizados");
				}
			});
		} catch (Exception e) {
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
			userName.setText("Bienvenido/a: " + user.getFirstName());
		}

		updateDate();

		checkSync();

		checkUpdatedData();

		if (justCreated) {
			// update.performClick(); // only first time...
			justCreated = false;
		}
	}

	private void updateDate() {
		try {
			User user = AndroidApplication.get().getUser();
			if (user != null) {
				if (dashPriceList == null) {
					dashPriceList = (TextView) findViewById(R.id.dashPriceList);
				}
				if (user.getUpdateDate() == null) {
					dashPriceList.setText(LocalizationUtils.getString(
							R.string.priceDate, "no actualizado"));
				} else {
					dashPriceList.setText(LocalizationUtils.getString(
							R.string.priceDate, user.getUpdateDate()
									.toLocaleString()));
				}
			}
		} catch (Exception e) {
		}

	}

	protected void checkSync() {
		syncUseCase = getInstance(SyncUseCase.class);
		syncUseCase.doExecute();
		List<Work> works = syncUseCase.getWorks();
		boolean synced = true;
		for (Iterator<Work> iterator = works.iterator(); iterator.hasNext();) {
			Work work = iterator.next();
			if (work.getSync() == false) {
				synced = false;
			}
		}

		if (synced) {
			sync.setBackgroundResource(R.drawable.button_flat_inverted);
		} else {
			sync.setBackgroundResource(R.drawable.button_cancel);
		}
	}

	protected void checkUpdatedData() {
		updateDataUseCase = getInstance(UpdateDataUseCase.class);
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				updated = updateDataUseCase.isUpdatedData();

				// Get a handler that can be used to post to the main thread
				Handler mainHandler = new Handler(getApplicationContext()
						.getMainLooper());
				Runnable myRunnable = new Runnable() {

					@Override
					public void run() {
						if (updated) {
							// datos actualizados a la ultima version de la base
							clientes.setBackgroundResource(R.drawable.button_flat_inverted);
							update.setVisibility(View.INVISIBLE);
						} else {
							clientes.setBackgroundResource(R.drawable.button_cancel);
							update.setVisibility(View.VISIBLE);
						}
					}
				};
				mainHandler.post(myRunnable);
			}
		};
		ThreadUtils.execute(runnable);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add(Menu.NONE, R.drawable.ic_sync, Menu.NONE, R.string.resync)
		// .setIcon(R.drawable.ic_sync);
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

}
