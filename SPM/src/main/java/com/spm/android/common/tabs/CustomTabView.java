package com.spm.android.common.tabs;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Custom view for TabWidgets.
 */
public class CustomTabView extends LinearLayout {
	
	/**
	 * @param context - Context.
	 * @param drawableEnabledId - Image to display.
	 * @param drawableSelectedId - Image on selected state.
	 * @param label - The text to display.
	 * @param textSize - Text size.
	 * @param drawableTextColor - Text color.
	 * @param drawableBackgroundColor - Background color.
	 */
	public CustomTabView(Context context, int drawableEnabledId, int drawableSelectedId, String label, int textSize,
			int drawableTextColor, int drawableBackgroundColor) {
		super(context);
		
		ImageView imageView = new ImageView(context);
		StateListDrawable listDrawable = new StateListDrawable();
		listDrawable.addState(SELECTED_STATE_SET, this.getResources().getDrawable(drawableSelectedId));
		listDrawable.addState(ENABLED_STATE_SET, this.getResources().getDrawable(drawableEnabledId));
		imageView.setImageDrawable(listDrawable);
		imageView.setBackgroundColor(Color.TRANSPARENT);
		addView(imageView);
		
		TextView textView = new TextView(context);
		textView.setText(label);
		textView.setGravity(Gravity.CENTER);
		textView.setBackgroundColor(Color.TRANSPARENT);
		
		XmlResourceParser parser = getResources().getXml(drawableTextColor);
		ColorStateList colors = null;
		try {
			colors = ColorStateList.createFromXml(getResources(), parser);
			textView.setTextColor(colors);
		} catch (XmlPullParserException e) {
			textView.setTextColor(Color.WHITE);
		} catch (IOException e) {
			textView.setTextColor(Color.WHITE);
		}
		textView.setTextSize(textSize);
		textView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
		addView(textView);
		
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		Drawable bkgColor = getResources().getDrawable(drawableBackgroundColor);
		setBackgroundDrawable(bkgColor);
	}
}