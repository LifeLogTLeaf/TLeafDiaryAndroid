package com.tleaf.tiary.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.format.DateUtils;
import android.text.format.Time;

public class MyTime {


	static public Time getCurrentTimeToTime() {
		Time time = new Time(Time.getCurrentTimezone());
		time.setToNow();
		return time;
	}

//	Time time = new Time(Time.getCurrentTimezone());
//	time.setToNow();
//	time.toMillis(false);
	
	static public long getCurrentTime() {
		long time = System.currentTimeMillis();
		return time;
	}

	
	//호출형식 : MyTime.getTodayToString(mContext, time.toMillis(false))
	static public String getLongToString(Context context, long millis) {
		return DateUtils.formatDateTime(context,
				millis, DateUtils.FORMAT_SHOW_DATE
				| DateUtils.FORMAT_SHOW_WEEKDAY
				| DateUtils.FORMAT_SHOW_YEAR);
	}

	static public String getLongToString(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(date);
        return currentDate;
	}
	
	static public String getLongToStringWithTime(Context context, long millis) {
		return DateUtils.formatDateTime(context,
				millis, DateUtils.FORMAT_SHOW_DATE
				| DateUtils.FORMAT_SHOW_WEEKDAY
				| DateUtils.FORMAT_SHOW_YEAR
				| DateUtils.FORMAT_SHOW_TIME);
	}

	static public String getLongToOnlyTime(Context context, long millis) {
		return DateUtils.formatDateTime(context,
				millis, DateUtils.FORMAT_SHOW_TIME);
	}
	
	//호출형식 : MyTime.getTodayToString(mContext, time.toMillis(false))
	//	static public String getTodayToString(Context context, long millis) {
	//		return DateUtils.formatDateTime(context,
	//				millis, DateUtils.FORMAT_SHOW_DATE
	//						| DateUtils.FORMAT_SHOW_WEEKDAY
	//						| DateUtils.FORMAT_SHOW_YEAR);
	//	}


	static public String  convertSecondToString(int second) {

		String str = "";

		int hour = second / 3600; 
		int min = second % 3600 / 60;
		int sec =  second % 3600 % 60; 

		if (hour != 0) {
			str += hour;
			str += "시간 ";
		}
		if (min != 0) {
			str += min;
			str += "분 ";
		}
		str += sec;
		str += "초 ";

		return str;
	}
}
