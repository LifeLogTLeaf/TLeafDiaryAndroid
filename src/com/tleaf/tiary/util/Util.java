package com.tleaf.tiary.util;

import java.util.ArrayList;

import com.tleaf.tiary.Common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Util {
	public static void tst(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ll(String tag, String msg) {
		Log.d(tag, msg);
	}
	
	public static void ll(String tag, long msg) {
		Log.d(tag, ""+msg);
	}

	public static void ll(String tag, int msg) {
		Log.d(tag, ""+msg);
	}

	public static String covertArrayToString(ArrayList<String> arr) {
		String str = "";
		for (int i = 0; i < arr.size(); i++) {
			str += arr.get(i);
			if (i != arr.size() - 1)
				str += ", ";
		}
		return str; 
	}
	
	public static ArrayList<String> covertStringToArray(String str) {

		ArrayList<String> arr = new ArrayList<String>();
		String[] splitStr = str.split(",");

		for (int i = 0; i < splitStr.length; i++) {
			splitStr[i] = splitStr[i].trim();
			if (splitStr[i] == null && splitStr[i] == "")
				break;
			arr.add(splitStr[i]);
		}
		return arr;
	}

}
