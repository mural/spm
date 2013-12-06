package com.spm.android.common.view;

import java.util.Date;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import com.spm.common.utils.DateUtils;
import com.spm.R;

/**
 * 
 * @author Maxi Rosson
 */
public class DateEditText extends EditText {
	
	private static final int DATE_DIALOG_ID = 0;
	
	private Activity activity;
	private Date birthDate;
	
	/**
	 * @param context
	 * @param attrs
	 */
	public DateEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void init(final Activity activity, Date defaultDate) {
		this.activity = activity;
		setDate(defaultDate);
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.showDialog(DATE_DIALOG_ID);
			}
		});
		setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					activity.showDialog(DATE_DIALOG_ID);
				}
			}
		});
		setLongClickable(false);
	}
	
	public void setDate(Date date) {
		birthDate = date;
		setText(DateUtils.unTransform(date));
	}
	
	/**
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}
	
	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	
	public static Dialog onCreateDialog(final DateEditText dateEditText, int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				OnDateSetListener dateSetListener = new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						dateEditText.setDate(DateUtils.getDate(year, monthOfYear, dayOfMonth));
					}
				};
				return new DatePickerDialog(dateEditText.getActivity(), R.style.CustomDialog, dateSetListener,
						DateUtils.getYear(dateEditText.getBirthDate()),
						DateUtils.getMonth(dateEditText.getBirthDate()), DateUtils.getDay(dateEditText.getBirthDate()));
		}
		return null;
	}
	
	public static Boolean onPrepareDialog(final DateEditText dateEditText, int id, Dialog dialog) {
		
		switch (id) {
			case DATE_DIALOG_ID:
				((DatePickerDialog)dialog).updateDate(DateUtils.getYear(dateEditText.getBirthDate()),
					DateUtils.getMonth(dateEditText.getBirthDate()), DateUtils.getDay(dateEditText.getBirthDate()));
				return true;
			default:
				return false;
		}
	}
}
