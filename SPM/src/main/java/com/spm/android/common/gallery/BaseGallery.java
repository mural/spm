package com.spm.android.common.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Gallery;

/**
 * Base {@link Gallery} class
 * 
 * @author Maxi Rosson
 */
public class BaseGallery extends Gallery {
	
	public BaseGallery(Context context) {
		super(context);
	}
	
	public BaseGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public BaseGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setSelected(Object item) {
		
		// TODO We need to investigate a better way to do this
		for (int i = 0; i < getCount(); i++) {
			if (getItemAtPosition(i).equals(item)) {
				setSelection(i, true);
				break;
			}
		}
	}
	
}
