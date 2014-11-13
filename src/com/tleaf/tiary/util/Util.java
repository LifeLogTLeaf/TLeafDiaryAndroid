package com.tleaf.tiary.util;

import java.util.ArrayList;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
	
	public static void ll(String tag, boolean msg) {
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

	
	public static void hideKeyboard(Context context, IBinder windowToken)
	{
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(windowToken, 0);
	}
	
	public static void showKeyboard(Context context, View editView)
	{
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editView, 2);//0
	}
	

}
