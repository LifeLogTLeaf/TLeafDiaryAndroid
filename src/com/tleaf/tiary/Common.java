package com.tleaf.tiary;

import java.util.ArrayList;

public class Common {

	public static final int HOME = 0;
	public static final int MYPAGE = 1;
	public static final int WRITE = 2;
	public static final int FOLDER = 3;
	public static final int TAG = 4;
	public static final int EMOTION = 5;
	public static final int SHACK = 6;
	public static final int SETIING = 7;
	public static final int LOCATION = 8;
	
	
//	public static final int TAG = 10;
//	public static final int FOLDER = 20;
	
	public static final String ACTION_MULTIPLE_PICK = "luminous.ACTION_MULTIPLE_PICK";

	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

	public static final int REQUEST_CODE_GALLERY      = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;
	
	public static final ArrayList<String> arr = new ArrayList<String>();
	
	private static boolean initArray = false;

	
	private static void initArray() {
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
}
