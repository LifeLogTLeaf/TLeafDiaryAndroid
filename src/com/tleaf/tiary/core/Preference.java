/**
 * 
 */
package com.tleaf.tiary.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created with Eclipse IDE
 * Author : RichardJ
 * Date : Oct 27, 2014 3:43:37 PM
 * Description :
 */
public class Preference {
	private static final String APP_NAME = "com.swimyoung.android_mqtt_communication";
	private SharedPreferences pref;
	private SharedPreferences.Editor prefEdit;

	private volatile static Preference preferenece;


	public static Preference getInstance(Context context) {
		if (preferenece == null) {
			synchronized (Preference.class) {
				if (preferenece == null) {
					preferenece = new Preference(context);
				}
			}
		}
		return preferenece;
	}

	public Preference(Context context) {
		pref = context.getSharedPreferences(APP_NAME, 0);
		prefEdit = pref.edit();
	}

	public void setStringPref(String key, String value) {
		prefEdit.putString(key, value);
		prefEdit.commit();
	}

	public String getStringPref(String key) {
		return pref.getString(key, null);
	}

	public void setBooleanPref(String key, Boolean value) {
		prefEdit.putBoolean(key, value);
		prefEdit.commit();
	}

	public boolean getBooleanPref(String key) {
		return pref.getBoolean(key, false);
	}

	public void setIntPref(String key, int value) {
		prefEdit.putInt(key, value);
		prefEdit.commit();
	}

	public int getIntPref(String key) {
		return pref.getInt(key, 0);
	}

}
