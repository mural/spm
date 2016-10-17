package com.spm.android.activity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.inject.internal.Lists;
import com.spm.R;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.activity.AbstractListActivity;
import com.spm.android.common.utils.LocalizationUtils;
import com.spm.android.common.view.ActionBar;
import com.spm.common.utils.IdGenerator;
import com.spm.domain.Client;
import com.spm.domain.Product;
import com.spm.domain.User;

/**
 * 
 * @author Agustin Sgarlata
 */
public class ProductsActivity extends AbstractListActivity<Product> {

	// public static final String LINE_ID = "lineId";

	public final static String DTO = "dto";
	public final static String CLIENT = "client";

	@InjectExtra(value = DTO, optional = true)
	private Double dto;

	@InjectExtra(value = CLIENT)
	private Long clientId;
	private Client client;

	private ProductsUseCase productsUseCase;
	private ProductsAdapter productsAdapter;

	// @InjectExtra(value = LINE_ID)
	// private Long lineId;

	@InjectView(R.id.actionBar)
	private ActionBar actionBar;

	@InjectView(R.id.totales)
	private TextView totales;

	public final static int REQUEST_CODE = IdGenerator.getIntId();
	public final static String PRODUCT = "PRODUCT";

	Realm realm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products_activity);

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

		actionBar.setTitle(LocalizationUtils.getString(R.string.dtoClient,
				dto.toString()));

		// productsUseCase = (ProductsUseCase)
		// getLastNonConfigurationInstance();
		// if (productsUseCase == null) {
		productsUseCase = getInstance(ProductsUseCase.class);
		productsUseCase.clearProducts();
		// }

		productsUseCase.addListener(this);
		// if (productsUseCase.isNotInvoked()) {
		// productsUseCase.setLineId(lineId);
		executeUseCase(productsUseCase);
		if (productsUseCase.isInProgress()) {
			onStartUseCase();
		} else if (productsUseCase.isFinishSuccessful()) {
			onFinishUseCase();
		} else if (productsUseCase.isFinishFailed()) {
			onFinishUseCase();
		}

		actionBar.addImageViewAction(R.drawable.ic_check,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						List<Product> products = Lists.newArrayList();
						products.addAll(productsUseCase.getSelectedItems());

						Intent intent = new Intent();
						intent.putExtra(PRODUCT, (Serializable) products); // agregar
																			// la
																			// cantidad
						setResult(RESULT_OK, intent);
						finish();
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
		}
	}

	@Override
	protected void onListItemClick(Product product) {
		super.onListItemClick(product);

		// Intent intent = new Intent();
		// intent.putExtra(PRODUCT, product);
		// setResult(RESULT_OK, intent);
		// finish();

	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		//List<Product> products = productsUseCase.getProducts();

		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				// Initialize Realm
				Realm.init(getActivity());
				// Get a Realm instance for this thread
				realm = Realm.getDefaultInstance();
				// Build the query looking at all users:
				RealmQuery<Product> query = realm.where(Product.class);
				// Execute the query:
				RealmResults<Product> result1 = query.findAll();
				List<Product> productList = Lists.newArrayList(result1.subList(0, result1.size()));
				Collections.sort(productList);

				List<Product> newProducts = Lists.newArrayList();
				for (Product productToAdd : productList) {
					boolean exists = false;
					for (Product existentProduct : newProducts) {
						if (productToAdd.getId().equals(existentProduct.getId())) {
							exists = true;
							break;
						}
					}
					if (!exists) {
						newProducts.add(realm.copyFromRealm(productToAdd));
					}
				}

				for (Product product : newProducts) {
					product.setQuantity(0);
				}

				productsAdapter = new ProductsAdapter(getActivity(), newProducts,
						productsUseCase.getSelectedItems(), dto, client, totales);

				setListAdapter(productsAdapter);
				dismissLoading();
			}
		});
	}

	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		productsUseCase.removeListener(this);
		realm.close();
	}
}
