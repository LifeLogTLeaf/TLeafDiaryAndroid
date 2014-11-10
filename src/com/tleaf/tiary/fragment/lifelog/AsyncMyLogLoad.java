package com.tleaf.tiary.fragment.lifelog;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;

import com.tleaf.tiary.Common;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.fragment.lifelog.adapter.MyLogAdapter;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.model.MySms;
import com.tleaf.tiary.util.MyPreference;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class AsyncMyLogLoad extends AsyncTask<Void, Void, ArrayList<MyLog>>
{
	private Context mContext;
	private DataManager dataMgr;

	private MyLogAdapter mAdapter;

	private ProgressDialog pDialog;
	private int mType;
	private String mSubType;

	public AsyncMyLogLoad(Context context, int type, MyLogAdapter adapter) {
		mContext =context; 
		dataMgr = new DataManager(mContext);
		mType = type;
		mSubType = null;
		mAdapter = adapter;

	}
	
	public AsyncMyLogLoad(Context context, int type, String subType, MyLogAdapter adapter) {
		mContext =context; 
		dataMgr = new DataManager(mContext);
		mType = type;
		mSubType = subType;
		mAdapter = adapter;
	}


	@Override
	protected void onPreExecute() {
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
			if (mSubType != null) {
				return dataMgr.getSmsListByType(mSubType);
			} 
			ArrayList<MySms> smsArr = new ArrayList<MySms>();
			smsArr.addAll(collectSmsByType(Common.KEY_SMSINBOX_BASETIME));
			dataMgr.insertSmsList(smsArr, Common.KEY_SMSINBOX_BASETIME);
			
			smsArr.clear();
			smsArr.addAll(collectSmsByType(Common.KEY_SMSSENT_BASETIME));
			dataMgr.insertSmsList(smsArr, Common.KEY_SMSSENT_BASETIME);
			
			return dataMgr.getSmsList();
		default:
			break;
		}

		return new ArrayList<MyLog>();
	}

	@Override
	protected void onPostExecute(ArrayList<MyLog> result) {
		super.onPostExecute(result);
		Util.ll("onPostExecute arr", result.size());
		mAdapter.updateItem(result);
		if(pDialog != null)
			pDialog.dismiss();
	}

	//pref set은 db 메소드에서
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

	private ArrayList<MySms> collectSmsByType(String typeIdx) {
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

			// 번호
			String num = cursor.getString(numidx);
			sms.setNumber(num);

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

}