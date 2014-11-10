package com.tleaf.tiary.fragment.lifelog;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.adapter.DiaryListAdapter;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.fragment.lifelog.adapter.BookMarkLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.CallLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.CardLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.LocationLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.MyLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.SmsLogAdapter;
import com.tleaf.tiary.model.BookMark;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.model.MySms;
import com.tleaf.tiary.util.Util;

public class LogListViewFragment extends Fragment {

	private Activity mContext;
	private DataManager dataMgr;

	private int type;
	
	private MyLogAdapter mAdapter;

	public LogListViewFragment(int type) {
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		dataMgr = new DataManager(mContext);

		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		ListView lv = (ListView) rootView.findViewById(R.id.list_gen);

		switch (type) {
		case Common.CARD:
//			mAdapter= new CardLogAdapter(mContext, R.layout.item_card, dataMgr.getCardList()); //this.getActivity()
			break;
		case Common.GALLERY:
			//			mAdapter = new GalleryLogAdapter(mContext, R.layout.item_diary_, get); //this.getActivity()
			break;
		case Common.LOCATION: 
//			mAdapter= new LocationLogAdapter(mContext, R.layout.item_location, dataMgr.getMyLogList()); //this.getActivity()
			break;
		case Common.BOOKMARK:
//			mAdapter = new BookMarkLogAdapter(mContext, R.layout.item_bookmark, dataMgr.getBookMarkList()); //this.getActivity()
			break;
		}

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



	


	private void getLogsInDb(final int type) { // 킨 시간에 다시 불러오기
		//		new AsyncTask<Void, Void, ArrayList<MyLog>>() {
		//
		//			@Override
		//			protected ArrayList<MyLog> doInBackground(Void... params) {
		//
		//				switch (type) {
		//				case value:
		//					
		//					break;
		//
		//				default:
		//					break;
		//				}
		//				ArrayList<MySms> smsArr = new ArrayList<MySms>();
		//
		//				smsArr.addAll(collectSmsByType(SMS_TYPE_INBOX));
		//				smsArr.addAll(collectSmsByType(SMS_TYPE_SENT));
		//				dataMgr.insertSmsList(smsArr);
		//
		//				return dataMgr.getSmsList();
		//			}
		//
		//			@Override
		//			protected void onPostExecute(ArrayList<MyLog> result) {
		//				super.onPostExecute(result);
		//				mAdapter.updateItem(result);
		//			}
		//
		//		}.execute();
	}		

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//			Diary diary = getDiaryListByType().get(position);
			//			Fragment fragment = new DiaryFragment(diary);
			//			MainActivity.changeFragment(fragment);
		}

	};
}


