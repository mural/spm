package com.spm.android.activity;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.spm.R;
import com.spm.android.activity.DetailOrderAdapter.OrderItemViewHolder;
import com.spm.android.common.adapter.BaseHolderArrayAdapter;
import com.spm.domain.OrderItem;

/**
 * 
 * @author Agustin Sgarlata
 */
public class DetailOrderAdapter extends
		BaseHolderArrayAdapter<OrderItem, OrderItemViewHolder> {

	/**
	 * @param context
	 *            categories
	 * @param orderItems
	 */
	public DetailOrderAdapter(Activity context, List<OrderItem> orderItems) {
		super(context, orderItems, R.layout.order_item_row);
	}

	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#fillHolderFromItem(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected void fillHolderFromItem(OrderItem orderItem,
			OrderItemViewHolder holder) {
		// holder.product.setText(orderItem.getProduct());
		// usaba el nombre entero: id + desc
		holder.product.setText(orderItem.getId().toString());
		holder.quantity.setText(orderItem.getQuantity().toString());
		holder.discount.setText(orderItem.getDiscount().toString() + "%");
		holder.price.setText("$" + orderItem.getPrice().toString());
	}

	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#createViewHolderFromConvertView(android.view.View)
	 */
	@Override
	protected OrderItemViewHolder createViewHolderFromConvertView(
			View convertView) {
		OrderItemViewHolder holder = new OrderItemViewHolder();
		holder.product = findView(convertView, R.id.product);
		holder.quantity = findView(convertView, R.id.quantity);
		holder.discount = findView(convertView, R.id.discount);
		holder.price = findView(convertView, R.id.price);

		return holder;
	}

	public static class OrderItemViewHolder {

		private TextView product;
		private TextView quantity;
		private TextView discount;
		private TextView price;
	}

}
