package com.spm.android.common.calendar;

import java.util.Calendar;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

public class CalendarWrapper {
	
	private Calendar calendar;
	private String[] shortDayNames;
	private String[] shortMonthNames;
	private OnDateChangedListener onDateChangedListener;
	private Calendar visibleStartDate;
	private Calendar visibleEndDate;
	
	public interface OnDateChangedListener {
		
		public void onDateChanged(CalendarWrapper sc);
	}
	
	public CalendarWrapper() {
		calendar = Calendar.getInstance();
		
		shortDayNames = new String[calendar.getActualMaximum(Calendar.DAY_OF_WEEK)];
		shortMonthNames = new String[calendar.getActualMaximum(Calendar.MONTH) + 1]; // Months are 0-based so size is
																						// Max + 1
		
		for (int i = 0; i < shortDayNames.length; i++) {
			shortDayNames[i] = DateUtils.getDayOfWeekString(i + 1, DateUtils.LENGTH_SHORT);
		}
		
		for (int i = 0; i < shortMonthNames.length; i++) {
			shortMonthNames[i] = DateUtils.getMonthString(i, DateUtils.LENGTH_SHORT);
		}
	}
	
	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}
	
	public int getMonth() {
		return calendar.get(Calendar.MONTH);
	}
	
	public int getDayOfWeek() {
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	public int getDay() {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public void setYear(int value) {
		calendar.set(Calendar.YEAR, value);
		invokeDateChangedListener();
	}
	
	public void setYearAndMonth(int year, int month) {
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		invokeDateChangedListener();
	}
	
	public void setMonth(int value) {
		calendar.set(Calendar.MONTH, value);
		invokeDateChangedListener();
	}
	
	public void setDay(int value) {
		calendar.set(Calendar.DAY_OF_MONTH, value);
		invokeDateChangedListener();
	}
	
	public void addYear(int value) {
		if (value != 0) {
			calendar.add(Calendar.YEAR, value);
			invokeDateChangedListener();
		}
	}
	
	public void addMonth(int value) {
		if (value != 0) {
			calendar.add(Calendar.MONTH, value);
			invokeDateChangedListener();
		}
	}
	
	public void addMonthSetDay(int monthAdd, int day) {
		calendar.add(Calendar.MONTH, monthAdd);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		
		invokeDateChangedListener();
	}
	
	public void addDay(int value) {
		if (value != 0) {
			calendar.add(Calendar.DAY_OF_MONTH, value);
			invokeDateChangedListener();
		}
	}
	
	public String[] getShortDayNames() {
		return shortDayNames;
	}
	
	public String[] getShortMonthNames() {
		return shortMonthNames;
	}
	
	public int[] get7x6DayArray() {
		visibleStartDate = null;
		visibleEndDate = null;
		
		int[] days = new int[42];
		
		Calendar tempCal = (Calendar)calendar.clone();
		tempCal.set(Calendar.DAY_OF_MONTH, 1);
		
		int dayOfWeekOn1st = tempCal.get(Calendar.DAY_OF_WEEK);
		int maxDay = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int previousMonthCount = dayOfWeekOn1st - 1;
		int index = 0;
		
		if (previousMonthCount > 0) {
			tempCal.set(Calendar.DAY_OF_MONTH, -1);
			
			int previousMonthMax = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			for (int i = previousMonthCount; i > 0; i--) {
				int day = previousMonthMax - i + 1;
				
				if (i == previousMonthCount) {
					visibleStartDate = (Calendar)tempCal.clone();
					visibleStartDate.set(Calendar.DAY_OF_MONTH, day);
				}
				
				days[index] = day;
				index++;
			}
		}
		
		for (int i = 0; i < maxDay; i++) {
			if ((i == 0) && (visibleStartDate == null)) {
				visibleStartDate = (Calendar)tempCal.clone();
			}
			
			days[index] = (i + 1);
			index++;
		}
		
		int nextMonthDay = 1;
		
		for (int i = index; i < days.length; i++) {
			if (i == index) {
				days[index] = nextMonthDay;
			}
			nextMonthDay++;
			index++;
		}
		
		visibleEndDate = (Calendar)calendar.clone();
		visibleEndDate.add(Calendar.MONTH, 1);
		visibleEndDate.set(Calendar.DAY_OF_MONTH, days[41]);
		
		return days;
	}
	
	public Calendar getSelectedDay() {
		return (Calendar)calendar.clone();
	}
	
	public Calendar getVisibleStartDate() {
		return (Calendar)visibleStartDate.clone();
	}
	
	public Calendar getVisibleEndDate() {
		return (Calendar)visibleEndDate.clone();
	}
	
	public void setOnDateChangedListener(OnDateChangedListener l) {
		onDateChangedListener = l;
	}
	
	public String toString(CharSequence format) {
		return DateFormat.format(format, calendar).toString();
	}
	
	private void invokeDateChangedListener() {
		if (onDateChangedListener != null) {
			onDateChangedListener.onDateChanged(this);
		}
	}
}