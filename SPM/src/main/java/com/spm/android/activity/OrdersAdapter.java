package com.spm.android.activity;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.spm.R;
import com.spm.android.activity.OrdersAdapter.OrderViewHolder;
import com.spm.android.common.adapter.BaseHolderArrayAdapter;
import com.spm.domain.Order;

/**
 * 
 * @author Agustin Sgarlata
 */
public class OrdersAdapter extends BaseHolderArrayAdapter<Order, OrderViewHolder> {
	
	/**
	 * @param context categories
	 * @param orders
	 */
	public OrdersAdapter(Activity context, List<Order> orders) {
		super(context, orders, R.layout.order_row);
	}
	
	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#fillHolderFromItem(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected void fillHolderFromItem(Order order, OrderViewHolder holder) {
		holder.orderName.setText(order.getName());
		holder.orderDate.setText(order.getDate());
	}
	
	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#createViewHolderFromConvertView(android.view.View)
	 */
	@Override
	protected OrderViewHolder createViewHolderFromConvertView(View convertView) {
		OrderViewHolder holder = new OrderViewHolder();
		holder.orderName = findView(convertView, R.id.orderName);
		holder.orderDate = findView(convertView, R.id.orderDate);
		
		return holder;
	}
	
	public static class OrderViewHolder {
		
		private TextView orderName;
		private TextView orderDate;
	}
	
}
