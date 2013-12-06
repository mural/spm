package com.spm.android.activity;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.spm.R;
import com.spm.android.activity.LinesAdapter.LineViewHolder;
import com.spm.android.common.adapter.BaseHolderArrayAdapter;
import com.spm.domain.Line;

/**
 * 
 * @author Agustin Sgarlata
 */
public class LinesAdapter extends BaseHolderArrayAdapter<Line, LineViewHolder> {
	
	/**
	 * @param context categories
	 * @param lines
	 */
	public LinesAdapter(Activity context, List<Line> lines) {
		super(context, lines, R.layout.line_row);
	}
	
	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#fillHolderFromItem(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected void fillHolderFromItem(Line line, LineViewHolder holder) {
		holder.name.setText(line.getName());
	}
	
	/**
	 * @see com.splatt.android.common.adapter.BaseHolderArrayAdapter#createViewHolderFromConvertView(android.view.View)
	 */
	@Override
	protected LineViewHolder createViewHolderFromConvertView(View convertView) {
		LineViewHolder holder = new LineViewHolder();
		holder.name = findView(convertView, R.id.lineName);
		
		return holder;
	}
	
	public static class LineViewHolder {
		
		private TextView name;
	}
	
}
