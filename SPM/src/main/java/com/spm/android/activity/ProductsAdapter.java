package com.spm.android.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.spm.R;
import com.spm.android.activity.ProductsAdapter.ProductViewHolder;
import com.spm.android.common.adapter.BaseHolderArrayAdapter;
import com.spm.domain.Client;
import com.spm.domain.Product;

/**
 * 
 * @author Agustin Sgarlata
 */
public class ProductsAdapter extends
		BaseHolderArrayAdapter<Product, ProductViewHolder> {

	private Set<Product> selectedProducts;
	private Double dto;
	private Client client;

	/**
	 * @param context
	 * @param products
	 * @param selectedProducts
	 * @param dto
	 * @param client
	 */
	public ProductsAdapter(Activity context, List<Product> products,
			Set<Product> selectedProducts, Double dto, Client client) {
		super(context, products, R.layout.product_row);
		this.selectedProducts = selectedProducts;
		this.dto = dto;
		this.client = client;
	}

	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#fillHolderFromItem(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected void fillHolderFromItem(final Product product,
			final ProductViewHolder holder) {
		// holder.productName.setText(product.getName());
		holder.productName.setText(product.getId().toString());

		Long priceList = client.getPriceList();
		Double price = product.getPrice1();
		if (priceList.compareTo(2L) == 0) {
			price = product.getPrice2();
		}
		if (priceList.compareTo(3L) == 0) {
			price = product.getPrice3();
		}
		if (priceList.compareTo(4L) == 0) {
			price = product.getPrice4();
		}
		if (priceList.compareTo(5L) == 0) {
			price = product.getPrice5();
		}
		if (price == null) {
			price = new Double(0);
		}

		Double priceDto = price * (1 - (dto / 100));

		BigDecimal priceDtoBig = new BigDecimal(priceDto.toString());
		priceDtoBig = priceDtoBig.setScale(2, RoundingMode.HALF_UP);

		holder.productPrice.setText("$ " + priceDtoBig.toString());

		if (isCheckeable(product)) {
			// We need to null the previous listener to prevent it from running
			// with the former user.
			holder.checked.setOnCheckedChangeListener(null);
			holder.checked.setChecked(selectedProducts.contains(product));

			holder.checked
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								selectedProducts.add(product);
							} else {
								selectedProducts.remove(product);
							}
							onSelectionChanged();
						}
					});
			holder.checked.setVisibility(View.VISIBLE);
		} else {
			holder.checked.setVisibility(View.GONE);
		}

		holder.minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Integer quantity = Integer.valueOf(holder.quantity.getText()
						.toString());
				if (quantity.intValue() > 0) {
					quantity--;
					holder.quantity.setText(quantity.toString());
				}
			}
		});

		holder.plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Integer quantity = Integer.valueOf(holder.quantity.getText()
						.toString());
				quantity++;
				holder.quantity.setText(quantity.toString());
			}
		});
	}

	protected Boolean isCheckeable(Product product) {
		return true;
	}

	protected void onSelectionChanged() {
		// Do Nothing
	}

	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#createViewHolderFromConvertView(android.view.View)
	 */
	@Override
	protected ProductViewHolder createViewHolderFromConvertView(View convertView) {
		ProductViewHolder holder = new ProductViewHolder();
		holder.productName = findView(convertView, R.id.productName);
		holder.minus = findView(convertView, R.id.minus);
		holder.quantity = findView(convertView, R.id.quantity);
		holder.plus = findView(convertView, R.id.plus);
		holder.productPrice = findView(convertView, R.id.productPrice);
		holder.checked = findView(convertView, R.id.check);

		return holder;
	}

	public static class ProductViewHolder {

		private TextView productName;
		private TextView minus;
		private TextView quantity;
		private TextView plus;
		private TextView productPrice;
		private CheckBox checked;
	}

}
