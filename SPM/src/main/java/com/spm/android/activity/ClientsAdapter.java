package com.spm.android.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.spm.R;
import com.spm.android.activity.ClientsAdapter.ClientViewHolder;
import com.spm.android.common.adapter.BaseHolderArrayAdapter;
import com.spm.domain.Client;

/**
 * 
 * @author Agustin Sgarlata
 */
public class ClientsAdapter extends
		BaseHolderArrayAdapter<Client, ClientViewHolder> implements
		SectionIndexer {

	HashMap<String, Integer> alphaIndexer;
	String[] sections;

	// private List<Client> itemList;
	// private Context context;
	// private static String sections = "abcdefghilmnopqrstuvz";

	/**
	 * @param context
	 * @param clients
	 */
	public ClientsAdapter(Activity context, List<Client> clients) {
		super(context, clients, R.layout.client_row);

		// itemList = clients;
		// this.context = context;

		alphaIndexer = new HashMap<String, Integer>();
		int size = clients.size();
		for (int x = 0; x < size; x++) {
			Client client = clients.get(x);

			// get the first letter of the store
			String ch = client.getFirstName().substring(0, 1);
			// convert to uppercase otherwise lowercase a -z will be sorted
			// after upper A-Z
			ch = ch.toUpperCase();

			// HashMap will prevent duplicates
			alphaIndexer.put(ch, x);
		}
		Set<String> sectionLetters = alphaIndexer.keySet();

		// create a list from the set to sort
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);
	}

	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	//
	// View v = convertView;
	// if (v == null) {
	// LayoutInflater inflater = (LayoutInflater) context
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// v = inflater.inflate(android.R.layout.simple_list_item_1, null);
	// }
	//
	// TextView text = (TextView) v.findViewById(android.R.id.text1);
	// text.setText(itemList.get(position).getFirstName());
	//
	// return v;
	//
	// }

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

	/**
	 * @see android.widget.SectionIndexer#getPositionForSection(int)
	 */
	@Override
	public int getPositionForSection(int section) {
		return alphaIndexer.get(sections[section]);
		// Log.d("ListView", "Get position for section");
		// for (int i = 0; i < this.getCount(); i++) {
		// String item = this.getItem(i).getFirstName().toLowerCase();
		// if (item.charAt(0) == sections.charAt(section)) {
		// return i;
		// }
		// }
		// return 0;
	}

	/**
	 * @see android.widget.SectionIndexer#getSectionForPosition(int)
	 */
	@Override
	public int getSectionForPosition(int arg0) {
		return 1;
		// Log.d("ListView", "Get section");
		// return 0;
	}

	/**
	 * @see android.widget.SectionIndexer#getSections()
	 */
	@Override
	public Object[] getSections() {
		return sections;
		// Log.d("ListView", "Get sections");
		// String[] sectionsArr = new String[sections.length()];
		// for (int i = 0; i < sections.length(); i++) {
		// sectionsArr[i] = "" + sections.charAt(i);
		// }
		//
		// return sectionsArr;
	}

}
