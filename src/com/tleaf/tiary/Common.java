package com.tleaf.tiary;

import java.util.ArrayList;

public class Common {

	public static final int HOME = 0;
	public static final int MYLIFTLOG = 1;
	public static final int TEMPLATE = 2;
	public static final int WRITE = 3;
	public static final int FOLDER = 4;
	public static final int TAG = 5;
	public static final int EMOTION = 6;
	public static final int SHACK = 7;
	public static final int SETIING = 8;
	public static final int LOCATION = 9;

	public static final int CALL = 10;
	public static final int SMS = 11;
	public static final int CARD = 12;
	public static final int GALLERY = 13;
	public static final int BOOKMARK = 14;

	
	public static final int KEYBOARD = 20;
	public static final int LOG = 21;

	
	public static final String INCOMING = "수신";
	public static final String OUTGOING = "발신";
	public static final String MISSED = "부재중";

	public static final String SMS_INBOX_IDNEX = "1";
	public static final String SMS_SENT_IDNEX = "2";

	//	public static final int TAG = 10;
	//	public static final int FOLDER = 20;

	public static final String KEY_CALL_BASETIME = "callBaseTime";
	public static final String KEY_INSTALL_TIME = "installTime";

	public static final String KEY_INSTALLATION= "installation";

	public static final String KEY_SMSINBOX_BASETIME= "smsInBoxBaseTime";
	public static final String KEY_SMSSENT_BASETIME = "smsSentBaseTime";

	
	
	
	public static final String MYGALLERY = "myGallery";

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
