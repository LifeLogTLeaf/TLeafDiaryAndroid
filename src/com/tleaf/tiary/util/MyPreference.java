package com.tleaf.tiary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MyPreference {
	private SharedPreferences pref;
	private SharedPreferences.Editor edit;

	public MyPreference(Context context) {
		pref = context.getSharedPreferences("tiary", 0);
		edit = pref.edit();
	}

	public void setStringPref(String key, String value) {
		edit.putString(key, value); // 저장
		edit.commit();
		Util.ll("prefrence", "string" + key + "/" + value);

	}

	public String getStringPref(String key) {
		return pref.getString(key, "no data"); // defalutVaule
	}

	public void setBooleanPref(String key, Boolean value) {
		edit.putBoolean(key, value); // 저장
		edit.commit();
		Util.ll("prefrence", "boolean" + key + "/" + value);
	}

	public boolean getBooleanPref(String key) {
		return pref.getBoolean(key, false);
	}

	public void setLongPref(String key, long value) {
		edit.putLong(key, value);
		edit.commit();
	}

	public long getLongPref(String key) {
		return pref.getLong(key, 0); // defalutVaule
	}

	public long getLongPref(String key, long defaultValue) {
		return pref.getLong(key, defaultValue); // defalutVaule
	}

}
