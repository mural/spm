package com.spm.android.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.spm.android.common.activity.AbstractListActivity;
import com.spm.android.common.activity.ActivityIf;
import com.spm.android.common.dialog.NumDialog;
import com.spm.android.common.utils.LocalizationUtils;
import com.spm.android.common.utils.ToastUtils;
import com.spm.android.common.view.ActionBar;
import com.spm.common.domain.Application;
import com.spm.domain.Client;
import com.spm.domain.Order;
import com.spm.domain.OrderItem;
import com.spm.domain.Product;
import com.spm.domain.User;

/**
 * 
 * @author Agustin Sgarlata
 */
public class DetailOrderActivity extends AbstractListActivity<OrderItem> {

	private DetailOrderUseCase detailOrderUseCase;
	private DetailOrderAdapter detailOrdersAdapter;

	private UpdateOrderUseCase updateOrderUseCase;
	private UpdateOrderUseCaseListener updateOrderCaseListener;

	public final static String ORDER = "order";
	public static final String CLIENT = "client";

	@InjectExtra(value = ORDER, optional = true)
	private Order order;

	@InjectExtra(value = CLIENT)
	private Client client;

	@InjectView(R.id.actionBar)
	private ActionBar actionBar;

	@InjectView(R.id.dto)
	private TextView dto;

	private Double discount;

	@InjectView(R.id.subTotal)
	private TextView subTotal;

	@InjectView(R.id.total)
	private TextView total;

	@InjectView(R.id.estado)
	private Spinner estado;

	@InjectView(R.id.tipo)
	private Spinner tipo;

	OrderItem orderItemTop;
	EditText input;
	private int quantity;

	@InjectView(R.id.orderNum)
	private TextView orderNum;

	/**
	 * @see com.spm.android.common.activity.AbstractActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_order_activity);

		// boolean giro = savedInstanceState.getBoolean("GIRO");
		if (savedInstanceState != null) {
			order = (Order) savedInstanceState.getSerializable(ORDER);
		}

		actionBar.setTitle(client.getFirstName());

		detailOrderUseCase = (DetailOrderUseCase) getLastNonConfigurationInstance();
		if (detailOrderUseCase == null) {
			detailOrderUseCase = getInstance(DetailOrderUseCase.class);
		}
		detailOrderUseCase.addListener(this);

		// updateOrderUseCase =
		// (UpdateOrderUseCase)getLastNonConfigurationInstance();
		if (updateOrderUseCase == null) {
			updateOrderUseCase = getInstance(UpdateOrderUseCase.class);
		}

		updateOrderCaseListener = new UpdateOrderUseCaseListener();
		updateOrderUseCase.addListener(updateOrderCaseListener);

		detailOrderUseCase.setOrder(order);
		detailOrderUseCase.setClientId(client.getId());
		executeUseCase(detailOrderUseCase);

		// Creamos la lista
		LinkedList<String> estados = new LinkedList<String>();
		// La poblamos con los ejemplos
		estados.add(Order.TO_DELIVER);
		estados.add(Order.DELIVERED);
		// Creamos el adaptador
		ArrayAdapter<String> estadoAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, estados);
		// Añadimos el layout para el menú y se lo damos al spinner
		estadoAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		estado.setAdapter(estadoAdapter);
		if ((order != null) && (order.getStatus() != null)
				&& order.getStatus().equals(Order.DELIVERED)) {
			estado.setSelection(1);
		} else {
			estado.setSelection(0);
		}
		if (!canEdit()) {
			estado.setEnabled(false);
		}

		estado.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if ((order != null) && !estado.getAdapter().isEmpty()
						&& (estado.getSelectedItem() != null)) {
					order.modifyStatus(estado.getSelectedItem().toString());
					updateOrderUseCase.setOrder(order);
					// executeUseCase(updateOrderUseCase);
					// detailOrderUseCase.updateOrder(order);
					// executeUseCase(detailOrderUseCase);
					updatePrices();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});

		// Creamos la lista
		LinkedList<String> tipos = new LinkedList<String>();
		// La poblamos con los ejemplos
		tipos.add(Order.NORMAL);
		tipos.add(Order.SPECIAL);
		// Creamos el adaptador
		ArrayAdapter<String> tipoAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, tipos);
		// Añadimos el layout para el menú y se lo damos al spinner
		tipoAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tipo.setAdapter(tipoAdapter);
		if ((order != null) && (order.getType() != null)
				&& order.getType().equals(Order.SPECIAL)) {
			tipo.setSelection(1);
		} else {
			tipo.setSelection(0);
		}

		dto.setText(LocalizationUtils.getString(R.string.dtoClient, client
				.getDiscount().toString()));
		discount = client.getDiscount();

		if (!canEdit()) {
			tipo.setEnabled(false);
		}
		tipo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if ((order != null) && !tipo.getAdapter().isEmpty()
						&& (tipo.getSelectedItem() != null)) {
					order.modifyType(tipo.getSelectedItem().toString());
					if (tipo.getSelectedItem().equals(Order.SPECIAL)) {
						discount = client.getDiscount2();
						dto.setText(LocalizationUtils.getString(
								R.string.dtoClient, discount.toString()));
					} else {
						discount = client.getDiscount();
						dto.setText(LocalizationUtils.getString(
								R.string.dtoClient, discount.toString()));
					}

					updateOrderUseCase.setOrder(order);
					// executeUseCase(updateOrderUseCase);
					// detailOrderUseCase.updateOrder(order);
					// executeUseCase(detailOrderUseCase);
					updatePrices();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});

		if (canEdit()) {
			actionBar.addImageViewAction(R.drawable.add, new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putDouble(ProductsActivity.DTO, discount);
					bundle.putSerializable(ProductsActivity.CLIENT, client);
					ActivityLauncher.launchActivity(ProductsActivity.class,
							bundle, ProductsActivity.REQUEST_CODE);
				}
			});
		}

		registerForContextMenu(getListView());
	}

	private Boolean canEdit() {
		return (!((order != null) && ((order.getSync() != null) && order
				.getSync().equals(Boolean.TRUE))));
	}

	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(DetailOrderActivity.ORDER, order);
	}

	/**
	 * @see roboguice.activity.RoboListActivity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		// FIXME ! how ? map ?
		return detailOrderUseCase;
	}

	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onListItemClick(java.lang.Object)
	 */
	@Override
	protected void onListItemClick(OrderItem orderItem) {

		if (canEdit()) {
			orderItemTop = orderItem;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			input = new EditText(this);
			input.setText(orderItem.getQuantity().toString());
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
			builder.setView(input);
			builder.setMessage("Cantidad")
					.setCancelable(false)
					.setPositiveButton("Aceptar",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									if ((input.getText() != null)
											&& !input.getText().toString()
													.equals("")) {
										orderItemTop.setQuantity(Integer
												.valueOf(input.getText()
														.toString()));
									} else {
										orderItemTop.setQuantity(0);
									}
									// detailOrderUseCase.updateOrder(order);
									// executeUseCase(detailOrderUseCase);
									updateOrderUseCase.setOrder(order);
									executeUseCase(updateOrderUseCase);
									updatePrices();
								}
							})
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			return new NumDialog(this, quantity);
		}
		return null;
	}

	private void updatePrices() {
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.addAll(order.getProducts());
		Collections.sort(orderItems);
		Double subTotalInt = Double.valueOf(0);
		for (OrderItem orderItem : orderItems) {
			subTotalInt += orderItem.getPrice() * orderItem.getQuantity();
		}
		detailOrdersAdapter = new DetailOrderAdapter(DetailOrderActivity.this,
				orderItems);
		setListAdapter(detailOrdersAdapter);
		// subTotalInt = new BigDecimal(subTotalInt.multiply(
		// new BigDecimal(1 - (new Double(user.getDiscount()) /
		// 100))).toString()).setScale(2, 2);

		Double discount = Double.valueOf(1 - (client.getDiscount()
				.doubleValue() / 100));
		Double discount2 = Double.valueOf(1 - (client.getDiscount2()
				.doubleValue() / 100));
		if (tipo.getSelectedItem().equals(Order.SPECIAL)) {
			subTotalInt *= discount2;
		} else {
			subTotalInt *= discount;
		}
		BigDecimal subTotalBig = new BigDecimal(subTotalInt.toString());
		subTotalBig = subTotalBig.setScale(2, RoundingMode.HALF_UP);
		subTotal.setText("Subtotal: $" + subTotalBig.toString());

		BigDecimal totalBig = new BigDecimal(Double.valueOf(subTotalInt * 1.21)
				.toString());
		totalBig = totalBig.setScale(2, RoundingMode.HALF_UP);
		total.setText("Total: $" + totalBig.toString());
		if (tipo.getSelectedItem().equals(Order.SPECIAL)) {
			total.setText("Total: $" + subTotalBig.toString());
		}
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
				order = detailOrderUseCase.getOrder();
				orderNum.setText(order.getId().toString());

				updatePrices();
				dismissLoading();
			}
		});
	}

	/**
	 * @see com.splatt.android.common.activity.AbstractActivity#onActivityResult(int,
	 *      int, android.content.Intent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == ProductsActivity.REQUEST_CODE)
				&& (resultCode == RESULT_OK)) {
			List<Product> products = (List<Product>) data
					.getSerializableExtra(ProductsActivity.PRODUCT);
			User user = AndroidApplication.get().getUser();
			Long priceList = client.getPriceList();
			Date syncDate = order.getSyncDate();
			boolean old = false;
			if ((syncDate != null)
					&& (user.getUpdateDate().compareTo(order.getSyncDate()) > 0)) {
				old = true;
			}

			for (Product product : products) { // FECHA ACTUAL LISTA PRECIOS
												// COMPARADA CON ACTUALIZACION
												// !!!
				Double price = product.getPrice1();
				if (old) {
					price = product.getPrice1ant();
				}

				if (priceList.compareTo(2L) == 0) {
					price = product.getPrice2();
					if (old) {
						price = product.getPrice2ant();
					}
				}

				if (priceList.compareTo(3L) == 0) {
					price = product.getPrice3();
					if (old) {
						price = product.getPrice3ant();
					}
				}

				if (priceList.compareTo(4L) == 0) {
					price = product.getPrice4();
					if (old) {
						price = product.getPrice4ant();
					}
				}

				if (priceList.compareTo(5L) == 0) {
					price = product.getPrice5();
					if (old) {
						price = product.getPrice5ant();
					}
				}

				if (price == null) {
					price = Double.valueOf(0);
				}
				OrderItem orderItem = new OrderItem(product.getId(),
						product.getName(), product.getQuantity(), 0, price);
				detailOrderUseCase.addProduct(orderItem);
			}
			// executeUseCase(detailOrderUseCase);
			updateOrderUseCase.setOrder(detailOrderUseCase.getOrder());
			executeUseCase(updateOrderUseCase);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
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

		// if (detailOrderUseCase.isNotInvoked()) {

		// if (detailOrderUseCase != null) {
		// if (order != null) {
		// detailOrderUseCase.setOrder(order);
		// }
		// detailOrderUseCase.setClientId(client.getId());
		// executeUseCase(detailOrderUseCase);
		// } else if (detailOrderUseCase.isInProgress()) {
		// onStartUseCase();
		// } else if (detailOrderUseCase.isFinishSuccessful()) {
		// onFinishUseCase();
		// } else if (detailOrderUseCase.isFinishFailed()) {
		// onFinishUseCase();
		// }
	}

	/**
	 * @see com.splatt.android.common.activity.AbstractListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		detailOrderUseCase.removeListener(this);
		updateOrderUseCase.removeListener(updateOrderCaseListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add(Menu.NONE, R.drawable.ic_menu_save, Menu.NONE,
		// R.string.menuSave).setIcon(R.drawable.ic_menu_save);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.drawable.ic_menu_save:
			ToastUtils.showToast("Guardado");
			executeUseCase(detailOrderUseCase);
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
		if (canEdit()) {
			menu.add(Menu.NONE, R.string.delete, Menu.NONE, R.string.delete);
		}
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
			detailOrderUseCase.deleteProduct((OrderItem) getListAdapter()
					.getItem(info.position));
			// executeUseCase(detailOrderUseCase);
			updateOrderUseCase.setOrder(detailOrderUseCase.getOrder());
			executeUseCase(updateOrderUseCase);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class UpdateOrderUseCaseListener extends AndroidUseCaseListener {

		@Override
		public void onFinishUseCase() {
			executeOnUIThread(new Runnable() {

				@Override
				public void run() {
					updatePrices();
					dismissLoading();
				}
			});
		}

		/**
		 * @see com.spm.android.common.AndroidUseCaseListener#getActivityIf()
		 */
		@Override
		protected ActivityIf getActivityIf() {
			return DetailOrderActivity.this;
		}
	}
}