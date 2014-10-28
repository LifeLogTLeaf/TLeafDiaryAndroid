package com.tleaf.tiary.util;

import java.util.ArrayList;

import com.google.android.gms.internal.em;
import com.tleaf.tiary.Common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Util {
	
	private static ArrayList<String> arr;
	private static boolean initArray = false;
	
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

	public static String getEmomtionNameByIndex(int index) {
		if(!initArray) {
			initArray();
			initArray = true;
		}
		
		return arr.get(index);
		
	}
	
	public static int getIndexByEmomtionName(String emotionName) {
		if(!initArray) {
			initArray();
			initArray = true;
		}
		
		return arr.indexOf(emotionName);
		
	}
	
	private static void initArray() {
		arr = new ArrayList<String>();
		
		arr.add("활짝");
		arr.add("그저그런");
		arr.add("스마일");
		arr.add("호호");
		arr.add("힛");
		
		arr.add("실망");
		arr.add("울상");
		arr.add("궁금");
		arr.add("심통");
		arr.add("하트");
		
		arr.add("놀람");
		arr.add("심술");
		arr.add("노코멘트");
		arr.add("메롱");
		arr.add("화남");
		
		arr.add("윙크");
		arr.add("베트맨");
		arr.add("웃음");
		arr.add("사랑");
		arr.add("행복");
		
		arr.add("잉");
		arr.add("버블버블");
		arr.add("칫");
		arr.add("흠");
		arr.add("센스쟁이");
		
	}
}
