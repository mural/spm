package com.spm.android.activity;

import java.util.List;
import java.util.Set;
import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.spm.R;
import com.spm.android.activity.SyncAdapter.WorkViewHolder;
import com.spm.android.common.adapter.BaseHolderArrayAdapter;
import com.spm.domain.Order;
import com.spm.domain.Visit;
import com.spm.domain.Work;

/**
 * 
 * @author Agustin Sgarlata
 */
public class SyncAdapter extends BaseHolderArrayAdapter<Work, WorkViewHolder> {
	
	private Set<Work> selectedWorks;
	
	/**
	 * @param context categories
	 * @param works
	 * @param selectedWorks
	 */
	public SyncAdapter(Activity context, List<Work> works, Set<Work> selectedWorks) {
		super(context, works, R.layout.sync_row);
		this.selectedWorks = selectedWorks;
	}
	
	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#fillHolderFromItem(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected void fillHolderFromItem(final Work work, WorkViewHolder holder) {
		String type = "";
		if (work instanceof Order) {
			type = "P";
		}
		if (work instanceof Visit) {
			type = "N";
		}
		holder.workCode.setText(work.getName() + " " + type);
		holder.workDate.setText(work.getDate());
		
		if (isCheckeable(work)) {
			// We need to null the previous listener to prevent it from running with the former user.
			holder.checked.setOnCheckedChangeListener(null);
			holder.checked.setChecked(selectedWorks.contains(work));
			
			holder.checked.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						selectedWorks.add(work);
					} else {
						selectedWorks.remove(work);
					}
					onSelectionChanged();
				}
			});
			holder.checked.setVisibility(View.VISIBLE);
		} else {
			holder.checked.setVisibility(View.INVISIBLE);
			holder.checked.setChecked(false);
			selectedWorks.remove(work);
		}
	}
	
	protected Boolean isCheckeable(Work work) {
		if ((work.getSync() != null) && work.getSync().equals(Boolean.TRUE)) {
			return false;
		} else {
			return true;
		}
	}
	
	protected void onSelectionChanged() {
		// Do Nothing
	}
	
	/**
	 * @see com.spm.android.common.adapter.BaseHolderArrayAdapter#createViewHolderFromConvertView(android.view.View)
	 */
	@Override
	protected WorkViewHolder createViewHolderFromConvertView(View convertView) {
		WorkViewHolder holder = new WorkViewHolder();
		holder.workCode = findView(convertView, R.id.orderCode);
		holder.workDate = findView(convertView, R.id.orderDate);
		holder.checked = findView(convertView, R.id.check);
		
		return holder;
	}
	
	public static class WorkViewHolder {
		
		private TextView workCode;
		private TextView workDate;
		private CheckBox checked;
	}
	
}
