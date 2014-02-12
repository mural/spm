package com.spm.android.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spm.R;

/**
 * Action Bar custom view
 * 
 * @author Agustin Sgarlata
 */
public class ActionBar extends LinearLayout {

	// private ImageView logo;
	private TextView titleView;
	private ProgressBar loading;
	private LinearLayout actionsViewgroup;

	public ActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.action_bar, this);

		// logo = (ImageView)this.findViewById(R.id.actionBarLogo);
		titleView = (TextView) this.findViewById(R.id.actionBarTitle);
		loading = (ProgressBar) this.findViewById(R.id.actionBarLoading);
		actionsViewgroup = (LinearLayout) this
				.findViewById(R.id.actionsViewgroup);

		// logo.setOnClickListener(new LaunchOnClickListener());

		TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
				R.styleable.actionBarStyleable);
		String title = typedArray
				.getString(R.styleable.actionBarStyleable_title1);
		if (title != null) {
			titleView.setText(title);
		}

		typedArray.recycle();
	}

	/**
	 * Show or not the loading
	 * 
	 * @param enabled
	 */
	public void displayLoading(boolean enabled) {
		if (enabled) {
			loading.setVisibility(VISIBLE);
		} else {
			loading.setVisibility(GONE);
		}
	}

	public void reset() {
		actionsViewgroup.removeAllViews();
	}

	/**
	 * Set the action bar title
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		titleView.setText(text);
	}

	/**
	 * Set the action bar title
	 * 
	 * @param textResId
	 */
	public void setTitle(int textResId) {
		setTitle(getContext().getString(textResId));
	}

	/**
	 * Add a view action to the action bar
	 * 
	 * @param actionView
	 */
	public void addViewAction(View actionView) {
		actionsViewgroup.addView(actionView);
	}

	/**
	 * Add a image view action to the action bar
	 * 
	 * @param imageResource
	 * @param clickListener
	 */
	public void addImageViewAction(int imageResource,
			OnClickListener clickListener) {
		ImageView imageAction = new ImageView(this.getContext());
		imageAction.setImageDrawable(this.getResources().getDrawable(
				imageResource));
		imageAction.setOnClickListener(clickListener);
		actionsViewgroup.addView(imageAction);
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageAction
				.getLayoutParams();
		lp.setMargins(20, 0, 20, 0);
	}

	/**
	 * Add a text view action to the action bar
	 * 
	 * @param textResource
	 * @param clickListener
	 */
	public void addTextViewAction(int textResource,
			OnClickListener clickListener) {
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Button textAction = (Button) layoutInflater.inflate(
				R.layout.action_bar_text_button, this, false);
		textAction.setText(this.getContext().getString(textResource));
		textAction.setOnClickListener(clickListener);
		actionsViewgroup.addView(textAction);

	}

}
