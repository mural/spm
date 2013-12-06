package com.spm.android.common.calendar;

import java.util.Calendar;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.spm.android.common.calendar.CalendarWrapper.OnDateChangedListener;
import com.spm.R;

public class CalendarView extends LinearLayout {
	
	private final int CENTURY_VIEW = 5;
	private final int DECADE_VIEW = 4;
	private final int YEAR_VIEW = 3;
	private final int MONTH_VIEW = 2;
	private final int DAY_VIEW = 1;
	private final int ITEM_VIEW = 0;
	
	private CalendarWrapper calendar;
	private TableLayout days;
	private TableLayout months;
	private TableLayout years;
	private LinearLayout events;
	private Button up;
	private ImageView previous;
	private ImageView next;
	private OnMonthChangedListener onMonthChangedListener;
	private OnSelectedDayChangedListener onSelectedDayChangedListener;
	private int currentView;
	private int currentYear;
	private int currentMonth;
	
	public CalendarView(Context context) {
		super(context);
		init(context);
	}
	
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public interface OnMonthChangedListener {
		
		public void onMonthChanged(CalendarView view);
	}
	
	public void setOnMonthChangedListener(OnMonthChangedListener l) {
		onMonthChangedListener = l;
	}
	
	public interface OnSelectedDayChangedListener {
		
		public void onSelectedDayChanged(CalendarView view);
	}
	
	public void setOnSelectedDayChangedListener(OnSelectedDayChangedListener l) {
		onSelectedDayChangedListener = l;
	}
	
	public Calendar getVisibleStartDate() {
		return calendar.getVisibleStartDate();
	}
	
	public Calendar getVisibleEndDate() {
		return calendar.getVisibleEndDate();
	}
	
	public Calendar getSelectedDay() {
		return calendar.getSelectedDay();
	}
	
	public void setDaysWithEvents(List<CalendarDayMarker> markers) {
		int hits = 0;
		int dayItemsInGrid = 42;
		int row = 1; // Skip weekday header row
		int col = 0;
		Calendar tempCal = calendar.getVisibleStartDate();
		
		for (int i = 0; i < dayItemsInGrid; i++) {
			if (hits == markers.size()) {
				break;
			}
			
			TableRow tr = (TableRow)days.getChildAt(row);
			TextView tv = (TextView)tr.getChildAt(col);
			int[] tag = (int[])tv.getTag();
			int day = tag[1];
			
			for (CalendarDayMarker marker : markers) {
				if ((tempCal.get(Calendar.YEAR) == marker.getYear())
						&& (tempCal.get(Calendar.MONTH) == marker.getMonth()) && (day == marker.getDay())) {
					tv.setBackgroundColor(marker.getColor());
					hits++;
					break;
				}
			}
			
			tempCal.add(Calendar.DAY_OF_MONTH, 1);
			
			col++;
			
			if (col == 7) {
				col = 0;
				row++;
			}
		}
	}
	
	public void setListViewItems(View[] views) {
		events.removeAllViews();
		
		for (int i = 0; i < views.length; i++) {
			events.addView(views[i]);
		}
	}
	
	private void init(Context context) {
		View v = LayoutInflater.from(context).inflate(R.layout.calendar, this, true);
		
		calendar = new CalendarWrapper();
		days = (TableLayout)v.findViewById(R.id.days);
		months = (TableLayout)v.findViewById(R.id.months);
		years = (TableLayout)v.findViewById(R.id.years);
		up = (Button)v.findViewById(R.id.up);
		previous = (ImageView)v.findViewById(R.id.previous);
		next = (ImageView)v.findViewById(R.id.next);
		events = (LinearLayout)v.findViewById(R.id.events);
		
		refreshCurrentDate();
		
		// Days Table
		String[] shortWeekDayNames = calendar.getShortDayNames();
		
		for (int i = 0; i < 7; i++) { // Rows
			TableRow tr = (TableRow)days.getChildAt(i);
			
			for (int j = 0; j < 7; j++) { // Columns
				Boolean header = i == 0; // First row is weekday headers
				TextView tv = (TextView)tr.getChildAt(j);
				
				if (header) {
					tv.setText(shortWeekDayNames[j]);
				} else {
					if (isDayClickable()) {
						tv.setOnClickListener(dayClicked);
					}
				}
			}
		}
		
		refreshDayCells();
		
		// Months Table
		String[] shortMonthNames = calendar.getShortMonthNames();
		int monthNameIndex = 0;
		
		for (int i = 0; i < 3; i++) { // Rows
			TableRow tr = (TableRow)months.getChildAt(i);
			
			for (int j = 0; j < 4; j++) { // Columns
				TextView tv = (TextView)tr.getChildAt(j);
				tv.setOnClickListener(monthClicked);
				tv.setText(shortMonthNames[monthNameIndex]);
				tv.setTag(monthNameIndex);
				
				monthNameIndex++;
			}
		}
		
		// Years Table
		for (int i = 0; i < 3; i++) { // Rows
			TableRow tr = (TableRow)years.getChildAt(i);
			
			for (int j = 0; j < 4; j++) { // Columns
				TextView tv = (TextView)tr.getChildAt(j);
				tv.setOnClickListener(yearClicked);
			}
		}
		
		// Listeners
		calendar.setOnDateChangedListener(dateChanged);
		if (isMonthClickable()) {
			up.setOnClickListener(upClicked);
		}
		previous.setOnClickListener(incrementClicked);
		next.setOnClickListener(incrementClicked);
		
		setView(MONTH_VIEW);
	}
	
	private OnDateChangedListener dateChanged = new OnDateChangedListener() {
		
		@Override
		public void onDateChanged(CalendarWrapper sc) {
			Boolean monthChanged = (currentYear != sc.getYear()) || (currentMonth != sc.getMonth());
			
			if (monthChanged) {
				refreshDayCells();
				invokeMonthChangedListener();
			}
			
			refreshCurrentDate();
			refreshUpText();
		}
	};
	
	private OnClickListener incrementClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int inc = (v == next ? 1 : -1);
			
			if (currentView == MONTH_VIEW) {
				calendar.addMonth(inc);
			} else if (currentView == DAY_VIEW) {
				calendar.addDay(inc);
				invokeSelectedDayChangedListener();
			} else if (currentView == YEAR_VIEW) {
				currentYear += inc;
				refreshUpText();
			}
		}
	};
	
	private OnClickListener dayClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int[] tag = (int[])v.getTag();
			calendar.addMonthSetDay(tag[0], tag[1]);
			invokeSelectedDayChangedListener();
			setView(DAY_VIEW);
		}
	};
	
	private OnClickListener monthClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			calendar.setYearAndMonth(currentYear, (Integer)v.getTag());
			setView(MONTH_VIEW);
		}
	};
	
	private OnClickListener yearClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			currentYear = (Integer)v.getTag();
			setView(YEAR_VIEW);
		}
	};
	
	private OnClickListener upClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setView(currentView + 1);
		}
	};
	
	private void refreshDayCells() {
		int[] dayGrid = calendar.get7x6DayArray();
		int monthAdd = -1;
		int row = 1; // Skip weekday header row
		int col = 0;
		
		for (int i = 0; i < dayGrid.length; i++) {
			int day = dayGrid[i];
			
			if (day == 1) {
				monthAdd++;
			}
			
			TableRow tr = (TableRow)days.getChildAt(row);
			TextView dayTextView = (TextView)tr.getChildAt(col);
			
			// Clear current markers, if any.
			dayTextView.setBackgroundDrawable(null);
			
			dayTextView.setText(dayGrid[i] + "");
			
			if (monthAdd == 0) {
				dayTextView.setTextColor(getCurrentMonthColor());
			} else {
				dayTextView.setTextColor(getCurrentOutsideMonthColor());
			}
			
			dayTextView.setTag(new int[] { monthAdd, dayGrid[i] });
			
			col++;
			
			if (col == 7) {
				col = 0;
				row++;
			}
		}
	}
	
	protected Boolean isDayClickable() {
		return false;
	}
	
	protected Boolean isMonthClickable() {
		return false;
	}
	
	protected int getCurrentMonthColor() {
		return Color.BLACK;
	}
	
	protected int getCurrentOutsideMonthColor() {
		return Color.GRAY;
	}
	
	private void setView(int view) {
		if (currentView != view) {
			currentView = view;
			events.setVisibility(currentView == DAY_VIEW ? View.VISIBLE : View.GONE);
			years.setVisibility(currentView == DECADE_VIEW ? View.VISIBLE : View.GONE);
			months.setVisibility(currentView == YEAR_VIEW ? View.VISIBLE : View.GONE);
			days.setVisibility(currentView == MONTH_VIEW ? View.VISIBLE : View.GONE);
			up.setEnabled(currentView != YEAR_VIEW);
			
			refreshUpText();
		}
	}
	
	private void refreshUpText() {
		switch (currentView) {
			case MONTH_VIEW:
				up.setText(calendar.toString("MMMM yyyy"));
				break;
			case YEAR_VIEW:
				up.setText(currentYear + "");
				break;
			case CENTURY_VIEW:
				up.setText("CENTURY_VIEW");
				break;
			case DECADE_VIEW:
				up.setText("DECADE_VIEW");
				break;
			case DAY_VIEW:
				up.setText(calendar.toString("EEEE, MMMM dd, yyyy"));
				break;
			case ITEM_VIEW:
				up.setText("ITEM_VIEW");
				break;
			default:
				break;
		}
	}
	
	private void refreshCurrentDate() {
		currentYear = calendar.getYear();
		currentMonth = calendar.getMonth();
		calendar.getDay();
	}
	
	private void invokeMonthChangedListener() {
		if (onMonthChangedListener != null) {
			onMonthChangedListener.onMonthChanged(this);
		}
	}
	
	private void invokeSelectedDayChangedListener() {
		if (onSelectedDayChangedListener != null) {
			onSelectedDayChangedListener.onSelectedDayChanged(this);
		}
	}
}
