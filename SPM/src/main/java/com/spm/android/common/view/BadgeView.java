package com.spm.android.common.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import com.spm.R;

/**
 * Text view used to show a badge with a number of notifications. If the notifications are less than zero, the badge
 * isn't visible.
 * 
 * @author Estefania Caravatti
 */
public class BadgeView extends TextView {
	
	public BadgeView(Context context) {
		this(context, null);
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public BadgeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		if (isInEditMode()) {
			setText("1");
		} else {
			// hide by default
			setVisibility(GONE);
		}
		
		// Adding styles
		setBackgroundResource(R.drawable.badge);
		setTextColor(Color.WHITE);
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	/**
	 * Sets a notification number in the badge.
	 * 
	 * @param notifications
	 */
	public void setNotifications(Integer notifications) {
		
		if ((notifications != null) && (notifications > 0)) {
			setVisibility(VISIBLE);
			if (notifications > 99) {
				this.setText("99+");
			} else {
				this.setText(notifications.toString());
			}
		} else {
			setVisibility(GONE);
		}
	}
}
