package com.spm.android.activity;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.spm.R;
import com.spm.android.activity.ClientsAdapter.ClientViewHolder;
import com.spm.android.common.adapter.BaseHolderArrayAdapter;
import com.spm.domain.Client;

/**
 * 
 * @author Agustin Sgarlata
 */
public class ClientsAdapter extends BaseHolderArrayAdapter<Client, ClientViewHolder> {
	
	/**
	 * @param context categories
	 * @param clients
	 */
	public ClientsAdapter(Activity context, List<Client> clients) {
		super(context, clients, R.layout.client_row);
	}
	
	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#fillHolderFromItem(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected void fillHolderFromItem(Client client, ClientViewHolder holder) {
		holder.clientName.setText(client.getFirstName());
	}
	
	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#createViewHolderFromConvertView(android.view.View)
	 */
	@Override
	protected ClientViewHolder createViewHolderFromConvertView(View convertView) {
		ClientViewHolder holder = new ClientViewHolder();
		holder.clientName = findView(convertView, R.id.clientName);
		
		return holder;
	}
	
	public static class ClientViewHolder {
		
		private TextView clientName;
	}
	
}
