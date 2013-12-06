package com.spm.android.activity;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.spm.R;
import com.spm.android.activity.VisitsAdapter.VisitViewHolder;
import com.spm.android.common.adapter.BaseHolderArrayAdapter;
import com.spm.domain.Visit;

/**
 * 
 * @author Agustin Sgarlata
 */
public class VisitsAdapter extends BaseHolderArrayAdapter<Visit, VisitViewHolder> {
	
	/**
	 * @param context categories
	 * @param visits
	 */
	public VisitsAdapter(Activity context, List<Visit> visits) {
		super(context, visits, R.layout.visit_row);
	}
	
	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#fillHolderFromItem(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected void fillHolderFromItem(Visit visit, VisitViewHolder holder) {
		holder.visitName.setText(visit.getName());
		holder.visitDate.setText(visit.getDate());
	}
	
	/**
	 * @see com.spm.android.common.adapter.BaseHolderArrayAdapter#createViewHolderFromConvertView(android.view.View)
	 */
	@Override
	protected VisitViewHolder createViewHolderFromConvertView(View convertView) {
		VisitViewHolder holder = new VisitViewHolder();
		holder.visitName = findView(convertView, R.id.visitName);
		holder.visitDate = findView(convertView, R.id.visitDate);
		
		return holder;
	}
	
	public static class VisitViewHolder {
		
		private TextView visitName;
		private TextView visitDate;
	}
	
}
