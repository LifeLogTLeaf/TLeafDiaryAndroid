package com.tleaf.tiary.fragment.lifelog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.format.Time;

import com.tleaf.tiary.Common;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.fragment.lifelog.adapter.MyLogAdapter;
import com.tleaf.tiary.model.BookMark;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.model.Card;
import com.tleaf.tiary.model.Contact;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.model.MySms;
import com.tleaf.tiary.util.MyPreference;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

/** 사용자 로그들을 asyncTack를 이용해 주집하는 클래스 **/
public class AsyncMyLogLoad extends AsyncTask<Void, Void, ArrayList<MyLog>>
{
	private Context mContext;
	private DataManager dataMgr;

	private MyLogAdapter mAdapter;

	private ProgressDialog pDialog;
	private int mType;
	private String mSubType;

	public AsyncMyLogLoad(Context context) {
		dataMgr = new DataManager(context);
	}

	public AsyncMyLogLoad(Context context, int type, MyLogAdapter adapter) {
		mContext =context; 
		dataMgr = new DataManager(context);
		mType = type;
		mSubType = null;
		mAdapter = adapter;

	}

	public AsyncMyLogLoad(Context context, int type, String subType, MyLogAdapter adapter) {
		mContext =context; 
		dataMgr = new DataManager(context);
		mType = type;
		mSubType = subType;
		mAdapter = adapter;
	}


	@Override
	protected void onPreExecute() {
		if(mContext != null)
			pDialog = ProgressDialog.show(mContext, "로딩 중", "기다려주세요");	
	}

	@Override
	protected ArrayList<MyLog> doInBackground(Void... params) {
		switch (mType) {
		case Common.CALL:
			if (mSubType != null) {
				return dataMgr.getCallListByType(mSubType);
			} 
			ArrayList<Call> callArr = new ArrayList<Call>();
			callArr.addAll(collectCall());
			dataMgr.insertCallList(callArr);
			return dataMgr.getCallList();
		case Common.SMS:
			insertSmsInDb();
			if (mSubType != null) {
				return dataMgr.getSmsListByType(mSubType, -1);
			} else {
				return dataMgr.getSmsList();
			}
		case Common.CARD:
			//inbox를 업데이트 시킨 후 카드로그를 추출한다
			ArrayList<MySms> smsUpdatedArr = new ArrayList<MySms>();
			smsUpdatedArr.addAll(collectSmsByType(Common.KEY_SMSINBOX_BASETIME));
			dataMgr.insertSmsList(smsUpdatedArr, Common.KEY_SMSINBOX_BASETIME);

			ArrayList<Card> cardArr = new ArrayList<Card>();
			cardArr.addAll(collectCard());
			dataMgr.insertCardList(cardArr);
			return dataMgr.getCardList();
		case Common.LOCATION:
			ArrayList<MyLog> locationArr = new ArrayList<MyLog>();
			locationArr.add(new MyLog(0, 0, "서울시 강남구 역삼동 5번지", "아남타워", MyTime.getCurrentTime()));
			locationArr.add(new MyLog(0, 0, "서울시 강남구 역삼동 5번지", "아남타워", MyTime.getCurrentTime()));
			locationArr.add(new MyLog(0, 0, "서울시 강남구 역삼동 5번지", "아남타워", MyTime.getCurrentTime()));
			locationArr.add(new MyLog(0, 0, "서울시 강남구 역삼동 5번지", "아남타워", MyTime.getCurrentTime()));
			locationArr.add(new MyLog(0, 0, "서울시 강남구 역삼동 5번지", "아남타워", MyTime.getCurrentTime()));
			return locationArr;
		case Common.BOOKMARK:
			ArrayList<BookMark> bookMarkArr = new ArrayList<BookMark>();
			bookMarkArr.add(new BookMark("shack", "shack.tleaf.us", MyTime.getCurrentTime()));
			bookMarkArr.add(new BookMark("shack", "shack.tleaf.us", MyTime.getCurrentTime()));
			bookMarkArr.add(new BookMark("shack", "shack.tleaf.us", MyTime.getCurrentTime()));
			bookMarkArr.add(new BookMark("shack", "shack.tleaf.us", MyTime.getCurrentTime()));
			bookMarkArr.add(new BookMark("shack", "shack.tleaf.us", MyTime.getCurrentTime()));
			ArrayList<MyLog> returnArr = new ArrayList<MyLog>();
			returnArr.addAll(bookMarkArr);
			return returnArr;
		case Common.GALLERY:
			//			ArrayList<MyLog> locationArr = new ArrayList<MyLog>();
			//			locationArr.add(new MyLog(0, 0, "서울시 강남구 역삼동 5번지", "아남타워", MyTime.getCurrentTime()));
			//			locationArr.add(new MyLog(0, 0, "서울시 강남구 역삼동 5번지", "아남타워", MyTime.getCurrentTime()));
			//			locationArr.add(new MyLog(0, 0, "서울시 강남구 역삼동 5번지", "아남타워", MyTime.getCurrentTime()));
			//			locationArr.add(new MyLog(0, 0, "서울시 강남구 역삼동 5번지", "아남타워", MyTime.getCurrentTime()));
			//			locationArr.add(new MyLog(0, 0, "서울시 강남구 역삼동 5번지", "아남타워", MyTime.getCurrentTime()));
			//			return locationArr;
		}
		return new ArrayList<MyLog>();
	}

	private void insertSmsInDb() {
		ArrayList<MySms> smsArr = new ArrayList<MySms>();
		smsArr.addAll(collectSmsByType(Common.KEY_SMSINBOX_BASETIME));
		dataMgr.insertSmsList(smsArr, Common.KEY_SMSINBOX_BASETIME);

		smsArr.clear();
		smsArr.addAll(collectSmsByType(Common.KEY_SMSSENT_BASETIME));
		dataMgr.insertSmsList(smsArr, Common.KEY_SMSSENT_BASETIME);
	}

	/** 각 어답터에게 결과 리스트를 보내는 메서드 **/
	@Override
	protected void onPostExecute(ArrayList<MyLog> result) {
		super.onPostExecute(result);
		Util.ll("onPostExecute arr", result.size());
		mAdapter.updateItem(result);
		if(pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	//pref set은 db 메소드에서
	/** 안드로이드 자원에서 전화 목록을 가져오는 메서드 **/
	private ArrayList<Call> collectCall() { 
		MyPreference pref = new MyPreference(mContext);
		long callBaseTime = pref.getLongPref(Common.KEY_CALL_BASETIME, pref.getLongPref(Common.KEY_INSTALL_TIME));

		String where = "date > ?";

		ContentResolver cr = mContext.getContentResolver();
		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, where,
				new String[] { String.valueOf(callBaseTime) },
				CallLog.Calls.DATE + " DESC");

		int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
		int dateidx = cursor.getColumnIndex(CallLog.Calls.DATE);
		int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION);
		int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE);


		int count = 0;
		ArrayList<Call> callArr = new ArrayList<Call>();
		while (cursor.moveToNext()) {
			Call mCall = new Call();
			// 통화 대상자
			String name = cursor.getString(nameidx);
			if (name == null) {
				name = "등록안됨";
			}
			mCall.setName(name);

			// 통화 번호
			String num = cursor.getString(numidx);
			mCall.setNumber(num);

			// 통화 종류
			int type = cursor.getInt(typeidx);
			String stype;
			switch (type) {
			case CallLog.Calls.INCOMING_TYPE:
				stype = "수신";
				break;
			case CallLog.Calls.OUTGOING_TYPE:
				stype = "발신";
				break;
			case CallLog.Calls.MISSED_TYPE:
				stype = "부재중";
				break;
				//			case 14:
				//				stype = "문자보냄";
				//				break;
				//			case 13:
				//				stype = "문자받음";
				//				break;
			default:
				//				stype = "기타";// + type;
				continue;
			}
			mCall.setType(stype);

			// 통화 날짜
			long date = cursor.getLong(dateidx);
			mCall.setDate(date);

			// 통화 시간
			int duration = cursor.getInt(duridx);
			mCall.setDuration(duration);

			//최대 100개까지만 -> 더보기 기능
			if (count++ == 100) {
				break;
			}
			callArr.add(mCall);
		}
		cursor.close();
		Util.ll("callArr collectCall", callArr.size());
		return callArr;
	}

	/** 안드로이드 자원에서 문자 목록을 가져오는 메서드 **/
	private ArrayList<MySms> collectSmsByType(String typeIdx) {
		saveContact();
		MyPreference pref = new MyPreference(mContext);

		long smsInBoxBaseTime = pref.getLongPref(Common.KEY_SMSINBOX_BASETIME, pref.getLongPref(Common.KEY_INSTALL_TIME));
		long smsSentBaseTime = pref.getLongPref(Common.KEY_SMSSENT_BASETIME, pref.getLongPref(Common.KEY_INSTALL_TIME));

		Util.ll("collectSmsByType smsInBoxBaseTime", MyTime.getLongToString(smsInBoxBaseTime));
		Util.ll("collectSmsByType smsSentBaseTime", MyTime.getLongToString(smsSentBaseTime));

		String where = "date > ?";

		String path = null;
		long baseTime = 0;

		if (typeIdx.equals(Common.KEY_SMSINBOX_BASETIME)) {
			path = "content://sms/inbox";
			baseTime = smsInBoxBaseTime;
		}
		else if (typeIdx.equals(Common.KEY_SMSSENT_BASETIME)) {
			path = "content://sms/sent";
			baseTime = smsSentBaseTime;
		}

		ContentResolver cr = mContext.getContentResolver();
		Cursor cursor = cr.query(Uri.parse(path), null, where, new String[] { String.valueOf(baseTime) }, "date"+ " DESC");

		Util.ll("cursor getCount", cursor.getCount());


		// int nameidx = cursor.getColumnIndex("person");
		int numidx = cursor.getColumnIndex("address");
		int dateidx = cursor.getColumnIndex("date");
		int bodyidx = cursor.getColumnIndex("body");
		int typeidx = cursor.getColumnIndex("type");

		int count = 0;
		ArrayList<MySms> smsArr = new ArrayList<MySms>();
		while (cursor.moveToNext()) {
			MySms sms = new MySms();
			// 이름
			// Util.ll("MySms nameidx", nameidx);
			// String name = cursor.getString(nameidx);
			// sms.setName(name);
			// Util.ll("MySms name", ""+name);

			// for (int i = 0; i < cursor.getColumnCount(); i++) {
			// Util.ll("MySms", "What ? " + cursor.getString(i) + ", "
			// + cursor.getColumnName(i));
			// }

			// 타입
			String type = cursor.getString(typeidx);
			String typeStr = (type.equals(Common.SMS_INBOX_IDNEX)) ? Common.INCOMING : Common.OUTGOING;
			sms.setType(typeStr);

			Util.ll("MySms type", typeStr);

			// 번호analysis
			String num = cursor.getString(numidx);
			sms.setNumber(num);
			Util.ll("collectSmsByType num", num);

			// 번호로 주소록에서 이름을 가져온다
			String name = dataMgr.getContactNameByContactNumber(num).getName();
			if (name == null || (name != null && name.equals("null")))
				name = "";
			sms.setName(name);
			Util.ll("collectSmsByType name", name);

			// 날짜
			long date = cursor.getLong(dateidx);
			sms.setDate(date);

			// 내용
			String body = cursor.getString(bodyidx);
			sms.setMessage(body);

			// 최대 100개까지만
			if (count++ == 100) {
				break;
			}
			smsArr.add(sms);
		}
		// Util.ll("smsArr count", count);
		cursor.close();
		// Util.ll("smsArr size", smsArr.size());
		return smsArr;
	}

	/** 사용자 내부 디비에 전화 테이블에서 수신 목록만 가져와서 카드 문자만 모으는 메서드 **/
	private ArrayList<Card> collectCard() {
		MyPreference pref = new MyPreference(mContext);

		long cardBaseTime = pref.getLongPref(Common.KEY_CARD_BASETIME, pref.getLongPref(Common.KEY_INSTALL_TIME));
		Util.ll("collectCard cardBaseTime", MyTime.getLongToString(cardBaseTime));

		HashMap<String, String> cardInfoArr = Common.getCardArr();
		ArrayList<MyLog> smsArr = dataMgr.getSmsListByType(Common.INCOMING, cardBaseTime);

		//		private long no;
		//		private String name;
		//		private String number;
		//		private String type;
		//		private String message;

		ArrayList<Card> cardArr = new ArrayList<Card>();
		Card card;
		for (int i=0; i <smsArr.size(); i++) {
			MySms sms = (MySms)(smsArr.get(i));
			if (cardInfoArr.containsKey(sms.getNumber())) {
				//				Util.ll("collectCard sms getDate", MyTime.getLongToString(sms.getDate()));
				//				Util.ll("collectCard sms getNo", sms.getNo());
				////				Util.ll("collectCard sms getName", sms.getName()); -> null
				//				Util.ll("collectCard sms getNumber", sms.getNumber());
				//				Util.ll("collectCard sms getType", sms.getType());
				//				Util.ll("collectCard sms getMessage", sms.getMessage());

				//				card = (Card) sms; //확인
				card = new Card();
				card.setDate(sms.getDate());
				card.setNo(sms.getNo());
				//				card.setName(sms.getName());
				card.setNumber(sms.getNumber());
				//				card.setType(sms.getType());
				card.setMessage(sms.getMessage());

				card.setCardType(cardInfoArr.get(card.getNumber()));

				//				Util.ll("collectCard card getDate", MyTime.getLongToString(card.getDate()));
				//				Util.ll("collectCard card getNo", card.getNo());
				////				Util.ll("collectCard card getName", card.getName()); -> null
				//				Util.ll("collectCard card getNumber", card.getNumber());
				//				Util.ll("collectCard card getType", card.getType());
				//				Util.ll("collectCard card getMessage", card.getMessage());

				card = analyzeSmsForCard(card);
				cardArr.add(card);
			}
		}
		return cardArr;
	}

	//	private long cardNo;
	//	private String cardType;
	//	private long cardDate;
	//	private int spendedMoney;
	//	private String spendedPlace;
	//	private int leftMoney;

	/** 신한카드 문자를 파싱하는 메서드 **/
	/** [Web발신]
		신한체크승인    08/12 11:18     4,100원         롯데쇼핑(주)롭  잔액89,800원**/
	private Card parseShinHan(Card card) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(card.getMessage());
			tokenizer.nextToken(); //엔터
			tokenizer.nextToken();

			//			Date spendTime = new Date();
			Time mTime = new Time();

			String day = tokenizer.nextToken();
			StringTokenizer dayTokenizer = new StringTokenizer(day, "/");
			int month = Integer.parseInt(dayTokenizer.nextToken()); //0?
			int monthDay = Integer.parseInt(dayTokenizer.nextToken());
			//			spendTime.setMonth(month);
			//			spendTime.setDate(date);

			String time = tokenizer.nextToken();
			StringTokenizer timeTokenizer = new StringTokenizer(time, ":");
			int hour = Integer.parseInt(timeTokenizer.nextToken());
			int minute = Integer.parseInt(timeTokenizer.nextToken());
			//			spendTime.setHours(hour);
			//			spendTime.setMinutes(minute);
			Time t = new Time();
			t.setToNow();
			mTime.set(0, minute, hour, monthDay, month-1, t.year); 
			card.setCardDate(mTime.toMillis(false));
			Util.ll("파싱결과 setCardDate", MyTime.getLongToString(card.getCardDate()));

			String money = tokenizer.nextToken();
			money = money.trim();
			money = money.replace(",", "");
			money = money.replace("원", "");
			card.setSpendedMoney(Integer.parseInt(money));
			Util.ll("파싱결과 setSpendedMoney", card.getSpendedMoney());

			card.setSpendedPlace(tokenizer.nextToken());
			Util.ll("파싱결과 setSpendedPlace", card.getSpendedPlace());

			String leftMoney = tokenizer.nextToken();
			leftMoney = leftMoney.trim();
			leftMoney = leftMoney.replace("잔액", "");
			leftMoney = leftMoney.replace(",", "");
			leftMoney = leftMoney.replace("원", "");
			card.setLeftMoney((Integer.parseInt(leftMoney)));

			Util.ll("파싱결과 setLeftMoney", card.getLeftMoney());
			Util.ll("파싱결과", card.toString());

			Util.ll("파싱결과 card getDate", MyTime.getLongToString(card.getDate()));
			Util.ll("파싱결과 card getNo", card.getNo());
			Util.ll("파싱결과 card getNumber", card.getNumber());
			Util.ll("파싱결과 card getCardType", card.getCardType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return card;
	}

	/** 수집된 문자중에서 전화번호로 어떤 카드의 문자인지 확인하는 메서드 **/
	private Card analyzeSmsForCard (Card card) {
		Card parsedCard = null;
		if(card.getCardType().equals(Common.NAME_SHINHAN)) {
			parsedCard = parseShinHan(card);
		}
		return parsedCard;
	}

	private String getNameByNumberInDevice(String myNumber) {	
		String name = "";
		ContentResolver cr = mContext.getContentResolver();

		Cursor cursor = cr.query(
				ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

		int ididx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
		int nameidx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

		while (cursor.moveToNext()) {
			String tmpName = cursor.getString(nameidx); 

			// 전화 번호는 서브 쿼리로 조사해야 함.
			String id = cursor.getString(ididx);
			Cursor cursor2 = cr.query(ContactsContract.CommonDataKinds.
					Phone.CONTENT_URI, null, 
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
					new String[]{id}, null);

			//			int typeidx = cursor2.getColumnIndex(
			//					ContactsContract.CommonDataKinds.Phone.TYPE);
			int numidx = cursor2.getColumnIndex(
					ContactsContract.CommonDataKinds.Phone.NUMBER);

			// 전화의 타입에 따라 여러 개가 존재한다.
			while (cursor2.moveToNext()) {
				String num = cursor2.getString(numidx);
				if (num.equals(myNumber)) {
					name = tmpName;
					break;
				}
				//				switch (cursor2.getInt(typeidx)) {
				//				case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
				//					result.append(" Mobile:" + num);
				//					break;
				//				case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
				//					result.append(" Home:" + num);
				//					break;
				//				case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
				//					result.append(" Work:" + num);
				//					break;
				//				}
			}
			cursor2.close();
		}
		cursor.close();

		Util.ll("getNameByNumber name", name);
		return name;
	}

	public void saveContact() {
		MyPreference pref = new MyPreference(mContext);
		boolean isSavedContacts = pref.getBooleanPref(Common.IS_SAVEDCONTACTS);

		Util.ll("isSavedContacts", isSavedContacts);
		if (isSavedContacts)
			return;
		else 
			pref.setBooleanPref(Common.IS_SAVEDCONTACTS, true);

		ArrayList<Contact> contactArr = new ArrayList<Contact>();

		ContentResolver cr = mContext.getContentResolver();
		Cursor cursor = cr.query(
				ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

		int ididx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
		int nameidx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

		String name = "", num = "";

		while (cursor.moveToNext()) {
			Contact contact = new Contact();
			name = cursor.getString(nameidx); 

			// 전화 번호는 서브 쿼리로 조사해야 함.
			String id = cursor.getString(ididx);
			Cursor cursor2 = cr.query(ContactsContract.CommonDataKinds.
					Phone.CONTENT_URI, null, 
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
					new String[]{id}, null);

			//			int typeidx = cursor2.getColumnIndex(
			//					ContactsContract.CommonDataKinds.Phone.TYPE);
			int numidx = cursor2.getColumnIndex(
					ContactsContract.CommonDataKinds.Phone.NUMBER);

			// 전화의 타입에 따라 여러 개가 존재한다.
			while (cursor2.moveToNext()) {
				num = cursor2.getString(numidx);
				num = num.replace("+", "");
				num = num.replace("-", "");
				num = num.replace("*", "");
				num = num.replace("#", "");
				num = num.replace("82", "0");

				//				switch (cursor2.getInt(typeidx)) {
				//				case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
				//					result.append(" Mobile:" + num);
				//					break;
				//				case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
				//					result.append(" Home:" + num);
				//					break;
				//				case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
				//					result.append(" Work:" + num);
				//					break;
				//				}
			}

			cursor2.close();
			contact.setName(name);
			contact.setNumber(num);
			contactArr.add(contact);
		}
		cursor.close();

		if (contactArr == null) {
			Util.ll("contactArr", "null");
		} else { 
			Util.ll("contactArr size", contactArr.size());
		}
		dataMgr.insertContact(contactArr);

	}
}
