package com.tleaf.tiary.fragment.lifelog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.fragment.lifelog.adapter.CallLogAdapter;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.model.Diary;

public class CallListViewFragment extends Fragment {

	private Activity mContext;
	private DataManager dataMgr;

	private int type;
	private ArrayList<Diary> arItem;

	public CallListViewFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		dataMgr = new DataManager(mContext);

		View rootView = inflater.inflate(R.layout.fragment_call, container, false);
		ListView lv = (ListView) rootView.findViewById(R.id.list_call);

		TextView txt_incoming = (TextView) rootView.findViewById(R.id.txt_incoming);
		txt_incoming.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		TextView txt_outgoing = (TextView) rootView.findViewById(R.id.txt_outgoing);
		txt_incoming.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		TextView txt_missed = (TextView) rootView.findViewById(R.id.txt_missed);
		txt_incoming.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		collectCall();
		CallLogAdapter mAdapter = new CallLogAdapter(mContext, R.layout.item_call, collectCall());// dataMgr.getCallList()); //this.getActivity()
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(mItemClickListener);

		//	        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		//	            @Override
		//	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//	                selectItem(position);
		//	            }
		//	        });
		//	        

		//		lv.setItemChecked(mCurrentSelectedPosition, true);


		return rootView;
	}



	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//			Diary diary = getDiaryListByType().get(position);
			//			Fragment fragment = new DiaryFragment(diary);
			//			MainActivity.changeFragment(fragment);
		}

	};

	private ArrayList<Call> collectCall() { //킨 시간에 다시 불러오기
		ContentResolver cr = mContext.getContentResolver();
		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI,null,null,null,
				CallLog.Calls.DATE + " DESC");

		int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
		int dateidx = cursor.getColumnIndex(CallLog.Calls.DATE);
		int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION);
		int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE);

		//		StringBuilder result = new StringBuilder();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
		//		result.append("총 기록 개수 : " + cursor.getCount() + "개\n");

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
			//			String sdate = formatter.format(new Date(date));

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
		dataMgr.insertCallList(callArr);
		return callArr;
	}

}


