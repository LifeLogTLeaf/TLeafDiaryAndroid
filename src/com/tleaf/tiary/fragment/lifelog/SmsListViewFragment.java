package com.tleaf.tiary.fragment.lifelog;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
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
import com.tleaf.tiary.fragment.lifelog.adapter.CallLogAdapter;
import com.tleaf.tiary.model.BookMark;
import com.tleaf.tiary.model.Diary;

public class SmsListViewFragment extends Fragment {

	private Activity mContext;
	private DataManager dataMgr;

	private int type;
	private ArrayList<Diary> arItem;

	public SmsListViewFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		dataMgr = new DataManager(mContext);

		View rootView = inflater.inflate(R.layout.fragment_sms, container, false);
		ListView lv = (ListView) rootView.findViewById(R.id.list_sms);

		CallLogAdapter	mAdapter = new CallLogAdapter(mContext, R.layout.item_sms, dataMgr.getCallList()); //this.getActivity()
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



	//	private ArrayList<Diary> getDiaryListByType() {
	//		arItem = new ArrayList<Diary>(); //확인
	//		if(type.equals("all")) {
	//			arItem = dataMgr.getDiaryList();
	//		} else { //folderName이 넘어오는 경우
	//			arItem = dataMgr.getDiaryListByFolderName(type);
	//		}
	//		return arItem;
	//
	//	}


}


