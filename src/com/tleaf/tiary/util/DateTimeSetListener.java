//package com.tleaf.tiary.util;
//
//import android.app.DatePickerDialog;
//import android.app.DatePickerDialog.OnDateSetListener;
//import android.app.TimePickerDialog.OnTimeSetListener;
//import android.text.format.Time;
//import android.widget.DatePicker;
//import android.widget.TimePicker;
//
//
//class DateTimeSetListener implements OnDateSetListener, OnTimeSetListener {
//
//	private static final int START_TIME = 1;
//	private static final int END_TIME = 2;
//	private int select;
//	
//
//	private long mBegin;
//	private long mEnd;
//	
//	public DateTimeSetListener(int select) {
//		this.select = select;
//	}
//
//	public long getSelectedTime() {
//		long time;
//		if (select == START_TIME) {
//			time = mBegin;
//		} else {
//			time = mEnd;
//		}
//		return time;
//	}
//
//	public void setSelectedTime(long time) {
//		if (select == START_TIME) {
//			mBegin = time;
//			if (mEnd < mBegin)
//				mEnd = mBegin;
//		} else {
//			mEnd = time;
//			if (mEnd < mBegin)
//				mBegin = mEnd;
//		}
//	}
//
////	@Override
////	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
////
////		long time = getSelectedTime();
////		Time _time = new Time(Time.getCurrentTimezone());
////		_time.set(time);
////		_time.hour = hourOfDay;
////		_time.minute = minute;
////		setSelectedTime(_time.toMillis(true));
////		setTime();
////
////	}
//
//	public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
//
//		long time = getSelectedTime();
//		Time _time = new Time(Time.getCurrentTimezone());
//		_time.set(time);
//		_time.year = year;
//		_time.month = monthOfYear;
//		_time.monthDay = dayOfMonth;
////		setSelectedTime(_time.toMillis(true));
////		setTime();
//	}
//
//
//
//
//}