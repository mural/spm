package com.spm.android.common.adapter;

import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.google.inject.internal.Lists;

/**
 * {@link BaseArrayAdapter} for lists that takes care of reusing the items' views.
 * 
 * @param <ITEM> An item in the list.
 * @param <VIEWHOLDER> A view holder containing all the necessary {@link View}s for an item's layout.
 * 
 * @author Estefania Caravatti
 */
public abstract class BaseHolderArrayAdapter<ITEM, VIEWHOLDER> extends BaseArrayAdapter<ITEM> {
	
	/** The {@link LayoutInflater} used to inflate the items in the list */
	private LayoutInflater layoutInflater;
	
	/** Layout resource used to inflate each row in the list. */
	private int rowLayoutResource;
	
	public BaseHolderArrayAdapter(Activity context, int rowLayoutResource) {
		this(context, Lists.<ITEM>newArrayList(), rowLayoutResource);
	}
	
	public BaseHolderArrayAdapter(Activity context, List<ITEM> items, int rowLayoutResource) {
		super(context, items);
		layoutInflater = context.getLayoutInflater();
		this.rowLayoutResource = rowLayoutResource;
	}
	
	/**
	 * @see ArrayAdapter#getView(int, View, ViewGroup)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		VIEWHOLDER holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(rowLayoutResource, parent, false);
			
			holder = createViewHolderFromConvertView(convertView);
			convertView.setTag(holder);
			
		} else {
			holder = (VIEWHOLDER)convertView.getTag();
		}
		
		ITEM item = getItem(position);
		fillHolderFromItem(item, holder);
		if (getCount() == 1) {
			onUniqueListItem(convertView, holder);
		} else {
			if (position == 0) {
				onFirstListItem(convertView, holder);
			} else if ((position + 1) == getCount()) {
				onLastListItem(convertView, holder);
			} else {
				onMiddleListItem(convertView, holder);
			}
		}
		
		if ((position % 2) == 0) {
			onEvenListItem(convertView, holder);
		} else {
			onOddListItem(convertView, holder);
		}
		onListItem(convertView, holder);
		return convertView;
	}
	
	/**
	 * Called when the view of the unique item of the list is created
	 * 
	 * @param convertView The convertView from the list.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onUniqueListItem(View convertView, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of the first item of the list is created
	 * 
	 * @param convertView The convertView from the list.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onFirstListItem(View convertView, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of the middle item of the list is created
	 * 
	 * @param convertView The convertView from the list.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onMiddleListItem(View convertView, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of the last item of the list is created
	 * 
	 * @param convertView The convertView from the list.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onLastListItem(View convertView, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of an odd item of the list is created
	 * 
	 * @param convertView The convertView from the list.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onOddListItem(View convertView, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of an even item of the list is created
	 * 
	 * @param convertView The convertView from the list.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onEvenListItem(View convertView, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of an item of the list is created
	 * 
	 * @param convertView The convertView from the list.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onListItem(View convertView, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Fills the VIEWHOLDER with the ITEM's data.
	 * 
	 * @param item The ITEM.
	 * @param holder The VIEWHOLDER.
	 */
	protected abstract void fillHolderFromItem(ITEM item, VIEWHOLDER holder);
	
	/**
	 * Creates a VIEWHOLDER from the given convertView. Please declare the VIEWHOLDER class as static when possible
	 * 
	 * @param convertView The convertView from the list.
	 * @return The new VIEWHOLDER.
	 */
	protected abstract VIEWHOLDER createViewHolderFromConvertView(View convertView);
	
}