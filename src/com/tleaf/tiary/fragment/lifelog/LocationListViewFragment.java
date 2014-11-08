package com.tleaf.tiary.fragment.lifelog;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.fragment.lifelog.adapter.LocationLogAdapter;
import com.tleaf.tiary.model.MyLog;


public class LocationListViewFragment extends Fragment {

	private Activity mContext;
	private DataManager dataMgr;

	public LocationListViewFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		dataMgr = new DataManager(mContext);

		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		ListView lv = (ListView) rootView.findViewById(R.id.list_gen);

		
		LocationLogAdapter mAdapter = new LocationLogAdapter(mContext, R.layout.item_location, collectLocation());//dataMgr.getMyLogList());// dataMgr.getCallList()); //this.getActivity()
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

	private ArrayList<MyLog> collectLocation() { //킨 시간에 다시 불러오기

		ArrayList<MyLog> locationArr = new ArrayList<MyLog>();

//		dataMgr.insertMyLogList(callArr);
		return locationArr;
	}

}


