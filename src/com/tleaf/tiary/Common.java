package com.tleaf.tiary;

import java.util.ArrayList;
import java.util.HashMap;

/** 전역변수 및 전역 메서드를 담고 있는 클래스 **/
public class Common {

	public static final int HOME = 0;
	public static final int MYLIFTLOG = 1;
	public static final int TEMPLATE = 2;
	public static final int WRITE = 3;
	public static final int FOLDER = 4;
	public static final int TAG = 5;
	public static final int EMOTION = 6;
	public static final int SETIING = 7;
	public static final int LOCATION = 8;

	public static final int CALL = 10;
	public static final int SMS = 11;
	public static final int CARD = 12;
	public static final int GALLERY = 13;
	public static final int BOOKMARK = 14;

	public static final String STRING_CALL = "call";
	public static final String STRING_SMS = "sms";
	public static final String STRING_CARD = "card";
	public static final String STRING_GALLERY = "gallery";
	public static final String STRING_BOOKMARK = "bookmark";
	public static final String STRING_LOCATION = "location";

	
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

	public static final String KEY_CARD_BASETIME = "cardBaseTime";

	public static final String ALL = "all";
	
	
	
	public static final String MYGALLERY = "myGallery";

	public static final String ACTION_MULTIPLE_PICK = "luminous.ACTION_MULTIPLE_PICK";

	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

	public static final int REQUEST_CODE_GALLERY      = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;

	public static final String NUMBER_SHINHAN = "01024504006";//"15447200";
	public static final String NAME_SHINHAN = "신한체크승인";
	
	public static String IS_SAVEDCONTACTS = "isSavedContacts";
	
	
	public static final ArrayList<String> emotionArr = new ArrayList<String>();
//	private static boolean isInitEmotionArray = false;

	private static HashMap<String, String> cardArr = new HashMap<String, String>();

	private static void initEmotionArray() {
		emotionArr.add("활짝");
		emotionArr.add("그저그런");
		emotionArr.add("스마일");
		emotionArr.add("호호");
		emotionArr.add("힛");

		emotionArr.add("실망");
		emotionArr.add("울상");
		emotionArr.add("궁금");
		emotionArr.add("심통");
		emotionArr.add("하트");

		emotionArr.add("놀람");
		emotionArr.add("심술");
		emotionArr.add("노코멘트");
		emotionArr.add("메롱");
		emotionArr.add("화남");

		emotionArr.add("윙크");
		emotionArr.add("베트맨");
		emotionArr.add("웃음");
		emotionArr.add("사랑");
		emotionArr.add("행복");

		emotionArr.add("잉");
		emotionArr.add("버블버블");
		emotionArr.add("칫");
		emotionArr.add("흠");
		emotionArr.add("센스쟁이");

	}

	public static String getEmomtionNameByIndex(int index) {
		if(emotionArr.size() == 0) {
			initEmotionArray();
//			isInitEmotionArray = true;
		}

		return emotionArr.get(index);

	}

	public static int getIndexByEmomtionName(String emotionName) {
		if(emotionArr.size() == 0) {
//		if(!isInitEmotionArray) {
			initEmotionArray();
//			isInitEmotionArray = true;
		}

		return emotionArr.indexOf(emotionName);

	}
	

	
	public static HashMap<String, String> getCardArr() {
		if (cardArr.isEmpty())
			initCardArray();
		return cardArr;
	}


	private final static void initCardArray() {
		cardArr.put(NUMBER_SHINHAN, NAME_SHINHAN);
	}
}
