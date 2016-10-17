package com.spm.android.activity;

import java.util.Date;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.inject.internal.Nullable;
import com.spm.R;
import com.spm.android.activity.settings.DevSettingsActivity;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidUseCaseListener;
import com.spm.android.common.activity.AbstractActivity;
import com.spm.android.common.activity.ActivityIf;
import com.spm.android.common.listener.AsyncOnClickListener;
import com.spm.android.common.utils.SharedPreferencesUtils;
import com.spm.android.common.utils.ToastUtils;

/**
 * 
 * @author Agustin Sgarlata
 */
public class LoginActivity extends AbstractActivity {

	private LoginUseCase loginUseCase;
	private UpdateUsersUseCase updateUsersUseCase;
	private UpdateUsersUseCaseListener updateUsersUseCaseListener;

	@InjectView(R.id.username)
	private EditText username;

	// @InjectView(R.id.password)
	// private EditText password;

	@InjectView(R.id.loginButton)
	private Button loginButton;

	@InjectView(R.id.version)
	private TextView version;

	@Nullable
	@InjectView(R.id.update)
	private TextView update;

	Date updateUsersDate;

	/**
	 * @see com.spm.android.common.activity.AbstractActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_activity);

		String lastUser = SharedPreferencesUtils
				.loadPreference("lastUserLogged");
		if (lastUser != null) {
			username.setText(lastUser);
		}

		loginButton.setOnClickListener(new AsyncOnClickListener() {

			@Override
			public void onAsyncRun(View view) {
				login();
			}
		});

		update.setOnClickListener(new AsyncOnClickListener() {

			@Override
			public void onAsyncRun(View view) {
				update();
			}
		});

		version.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// version(); //TODO: version click
			}
		});

		loginUseCase = (LoginUseCase) getLastNonConfigurationInstance();
		if (loginUseCase == null) {
			loginUseCase = getInstance(LoginUseCase.class);
		}
		loginUseCase.addListener(this);
		if (loginUseCase.isInProgress()) {
			onStartUseCase();
		} else if (loginUseCase.isFinishSuccessful()) {
			onFinishUseCase();
		} else if (loginUseCase.isFinishFailed()) {
			dismissLoading();
		}

		// updateUsersUseCase =
		// (UpdateUsersUseCase)getLastNonConfigurationInstance();
		if (updateUsersUseCase == null) {
			updateUsersUseCase = getInstance(UpdateUsersUseCase.class);
		}
		updateUsersUseCaseListener = new UpdateUsersUseCaseListener();
		updateUsersUseCase.addListener(updateUsersUseCaseListener);

	}

	/**
	 * @see com.spm.android.common.activity.AbstractActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		checkUpdateUsers();
	}

	protected void checkUpdateUsers() {
		// Compruebo fecha actualizacion
		Long updateUsersLong = SharedPreferencesUtils
				.loadPreferenceAsLong("updateUsers");
		if (updateUsersLong != null) {
			updateUsersDate = new Date(updateUsersLong);
		}
		if ((updateUsersDate != null)
				&& ((updateUsersDate.getDay()) == (new Date().getDay()))) {
			update.setBackgroundResource(R.drawable.button_flat_inverted);
		} else {
			update.setBackgroundResource(R.drawable.button_cancel);
		}
	}

	/**
	 * @see roboguice.activity.RoboListActivity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return loginUseCase;
	}

	private void version() {
		// User user = Application.APPLICATION_PROVIDER.get().getUser();
		// APIServiceImpl api = new APIServiceImpl();
		// String result = api.lastOrderNumber(user).toString();
		//
		// // TelephonyManager tMgr =
		// //
		// (TelephonyManager)AndroidApplication.get().getSystemService(Context.TELEPHONY_SERVICE);
		// // String result = tMgr.getLine1Number();
		//
		// ToastUtils.showToast(result);
	}

	private void login() {
		loginUseCase.setUsername(username.getText().toString());
		executeUseCase(loginUseCase);
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {

		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				SharedPreferencesUtils.savePreference("lastUserLogged",
						username.getText().toString());
				dismissLoading();
				finish();
				//ActivityLauncher.launchActivity(DashBoardActivity.class);
				startDashboard();
			}
		});
	}

	private void startDashboard() {
		DashBoardActivity_.intent(this).start();
	}

	private void update() {
		executeUseCase(updateUsersUseCase);
	}

	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.devSettingsItem:
			ActivityLauncher.launchActivity(DevSettingsActivity.class);
			return true;
		default:
			return false;
		}
	}

	/**
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		finish();
	}

	private class UpdateUsersUseCaseListener extends AndroidUseCaseListener {

		@Override
		public void onFinishUseCase() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					checkUpdateUsers();
				}
			});
			dismissLoadingOnUIThread();
			ToastUtils.showToastOnUIThread("Usuarios actualizados");
		}

		/**
		 * @see com.spm.android.common.AndroidUseCaseListener#getActivityIf()
		 */
		@Override
		protected ActivityIf getActivityIf() {
			return LoginActivity.this;
		}
	}
}
