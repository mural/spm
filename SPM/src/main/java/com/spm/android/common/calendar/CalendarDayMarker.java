package com.spm.android.common.calendar;

import java.util.Calendar;
import java.util.Date;

public class CalendarDayMarker {
	
	private int year;
	private int month;
	private int day;
	private int color;
	
	public CalendarDayMarker(Date date, int color) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), color);
	}
	
	public CalendarDayMarker(Calendar c, int color) {
		init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), color);
	}
	
	private void init(int year, int month, int day, int color) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.color = color;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getColor() {
		return color;
	}
}
