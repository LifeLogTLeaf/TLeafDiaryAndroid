package com.tleaf.tiary.util;

import android.content.Context;
import android.text.format.DateUtils;
import android.text.format.Time;

public class MyTime {
	

	static public Time getCurrentTimeToTime() {
		Time time = new Time(Time.getCurrentTimezone());
		time.setToNow();
		return time;
	}
	
	
	//호출형식 : MyTime.getTodayToString(mContext, time.toMillis(false))
	static public String getTodayToString(Context context, long millis) {
		return DateUtils.formatDateTime(context,
				millis, DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_SHOW_WEEKDAY
						| DateUtils.FORMAT_SHOW_YEAR);
	}
}
