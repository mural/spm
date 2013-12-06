package com.spm.android.activity;

import java.util.LinkedList;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.spm.R;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.AndroidUseCaseListener;
import com.spm.android.common.activity.AbstractActivity;
import com.spm.android.common.activity.ActivityIf;
import com.spm.android.common.utils.AlertDialogUtils;
import com.spm.android.common.utils.ToastUtils;
import com.spm.android.common.view.ActionBar;
import com.spm.common.domain.Application;
import com.spm.domain.Client;
import com.spm.domain.User;
import com.spm.domain.Visit;

/**
 * 
 * @author Agustin Sgarlata
 */
public class DetailVisitActivity extends AbstractActivity {

	private DetailVisitUseCase detailVisitUseCase;

	private UpdateVisitUseCase updateVisitUseCase;
	private UpdateVisitUseCaseListener updateVisitUseCaseListener;

	public final static String VISIT = "visit";
	public static final String CLIENT = "client";

	@InjectExtra(value = VISIT, optional = true)
	private Visit visit;

	@InjectExtra(value = CLIENT)
	private Client client;

	@InjectView(R.id.actionBar)
	private ActionBar actionBar;

	// @InjectView(R.id.visitNum) //TODO old version
	private TextView visitNum;

	// @InjectView(R.id.comment)
	private EditText comment;

	// @InjectView(R.id.tema)
	private Spinner tema;

	// @InjectView(R.id.other)
	private EditText other;

	private String initialTopic = "";
	private String initialComment = "";
	private String initialOther = "";

	/**
	 * @see com.spm.android.common.activity.AbstractActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_visit_activity);

		if (savedInstanceState != null) {
			visit = (Visit) savedInstanceState.getSerializable(VISIT);
		}

		actionBar.setTitle(client.getFirstName());

		detailVisitUseCase = (DetailVisitUseCase) getLastNonConfigurationInstance();
		if (detailVisitUseCase == null) {
			detailVisitUseCase = getInstance(DetailVisitUseCase.class);
		}
		detailVisitUseCase.addListener(this);

		// updateVisitUseCase =
		// (UpdateVisitUseCase)getLastNonConfigurationInstance();
		if (updateVisitUseCase == null) {
			updateVisitUseCase = getInstance(UpdateVisitUseCase.class);
		}

		updateVisitUseCase.addListener(updateVisitUseCaseListener);

		// Creamos la lista
		LinkedList<String> temas = new LinkedList<String>();
		// La poblamos con los ejemplos
		temas.add(Visit.PRIMER_CONTACTO);
		temas.add(Visit.NEGOCIACION);
		temas.add(Visit.SUPERVISION);
		temas.add(Visit.COBRANZA);
		temas.add(Visit.OTROS);
		// Creamos el adaptador
		ArrayAdapter<String> temasAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, temas);
		// Añadimos el layout para el menú y se lo damos al spinner
		temasAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tema.setAdapter(temasAdapter);
		if ((visit != null) && (visit.getTopic() != null)
				&& visit.getTopic().equals(Visit.PRIMER_CONTACTO)) {
			tema.setSelection(0);
		} else if ((visit != null) && (visit.getTopic() != null)
				&& visit.getTopic().equals(Visit.NEGOCIACION)) {
			tema.setSelection(1);
		} else if ((visit != null) && (visit.getTopic() != null)
				&& visit.getTopic().equals(Visit.SUPERVISION)) {
			tema.setSelection(2);
		} else if ((visit != null) && (visit.getTopic() != null)
				&& visit.getTopic().equals(Visit.COBRANZA)) {
			tema.setSelection(3);
		} else {
			tema.setSelection(4);
			other.setVisibility(View.VISIBLE);
			if (visit != null) {
				if ((visit.getTopic() != null)
						&& !visit.getTopic().equals(Visit.OTROS)) {
					other.setText(visit.getTopic());
				}
			}
		}
		if (!canEdit()) {
			tema.setEnabled(false);
			tema.setFocusable(false);
			other.setEnabled(false);
			other.setFocusable(false);
		}

		tema.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if ((visit != null) && !tema.getAdapter().isEmpty()
						&& (tema.getSelectedItem() != null)) {

					if (tema.getSelectedItem().toString().equals(Visit.OTROS)) {
						other.setVisibility(View.VISIBLE);
					} else {
						other.setVisibility(View.INVISIBLE);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});

		if (!canEdit()) {
			comment.setEnabled(false);
		}
	}

	private Boolean canEdit() {
		return (!((visit != null) && ((visit.getSync() != null) && visit
				.getSync().equals(Boolean.TRUE))));
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(DetailVisitActivity.VISIT, visit);
	}

	/**
	 * @see roboguice.activity.RoboListActivity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		// FIXME ! how ? map ?
		return detailVisitUseCase;
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {

		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				// User user = Application.APPLICATION_PROVIDER.get().getUser();
				visit = detailVisitUseCase.getVisit();
				visitNum.setText(visit.getId().toString());
				comment.setText(visit.getComment());

				dismissLoading();
				initialTopic = tema.getAdapter()
						.getItem(tema.getSelectedItemPosition()).toString();
				initialComment = comment.getText().toString();
				initialOther = other.getText().toString();
			}
		});
	}

	protected void refresh() {
		onFinishUseCase();
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

		detailVisitUseCase = (DetailVisitUseCase) getLastNonConfigurationInstance();
		if (detailVisitUseCase == null) {
			detailVisitUseCase = getInstance(DetailVisitUseCase.class);
		}

		detailVisitUseCase.addListener(this);
		// if (detailVisitUseCase.isNotInvoked()) {
		if (detailVisitUseCase != null) {
			if (visit != null) {
				detailVisitUseCase.setVisit(visit);
			}
			detailVisitUseCase.setClientId(client.getId());
			executeUseCase(detailVisitUseCase);
		} else if (detailVisitUseCase.isInProgress()) {
			onStartUseCase();
		} else if (detailVisitUseCase.isFinishSuccessful()) {
			onFinishUseCase();
		} else if (detailVisitUseCase.isFinishFailed()) {
			onFinishUseCase();
		}
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		detailVisitUseCase.removeListener(this);
		updateVisitUseCase.removeListener(updateVisitUseCaseListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (canEdit()) {
			menu.add(Menu.NONE, R.drawable.ic_menu_save, Menu.NONE,
					R.string.menuSave).setIcon(R.drawable.ic_menu_save);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.drawable.ic_menu_save:
			if (tema.getSelectedItem().toString().equals(Visit.OTROS)
					&& !(other.getText().toString().length() > 0)) {
				AlertDialogUtils.showOKDialog(R.string.fillTopicTitle,
						R.string.fillTopicMessage);
			} else {
				ToastUtils.showToast("Guardado");
				// executeUseCase(updateVisitUseCase);
				visit.modifyComment(comment.getText().toString());
				if (tema.getSelectedItem().toString().equals(Visit.OTROS)) {
					visit.modifyTopic(other.getText().toString());
				} else {
					visit.modifyTopic(tema.getSelectedItem().toString());
				}
				detailVisitUseCase.updateVisit(visit);
				executeUseCase(detailVisitUseCase);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (canEdit()
				&& (!tema.getAdapter().getItem(tema.getSelectedItemPosition())
						.toString().equals(initialTopic)
						|| !comment.getText().toString().equals(initialComment) || !other
						.getText().toString().equals(initialOther))) {
			AlertDialogUtils.showUnsavedChangesDialog();
		} else {
			super.onBackPressed();
		}
	}

	private class UpdateVisitUseCaseListener extends AndroidUseCaseListener {

		@Override
		public void onFinishUseCase() {
			executeOnUIThread(new Runnable() {

				@Override
				public void run() {
					dismissLoading();
				}
			});
		}

		/**
		 * @see com.spm.android.common.AndroidUseCaseListener#getActivityIf()
		 */
		@Override
		protected ActivityIf getActivityIf() {
			return DetailVisitActivity.this;
		}
	}
}