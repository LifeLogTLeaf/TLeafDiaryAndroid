package com.tleaf.tiary.fragment;

import java.util.ArrayList;

import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.adapter.DiaryListAdapter;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.util.Util;

public class DiaryListViewFragement extends Fragment implements OnNavigationListener {

	private Activity mContext;
	private DataManager dataMgr;

	private String type;
	private ArrayList<Diary> arItem;

	public DiaryListViewFragement() {
		type = "all";
	}

	public DiaryListViewFragement(String type) {
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		dataMgr = new DataManager(mContext);

		View rootView = inflater.inflate(R.layout.fragment_diary_list, container, false);
		ListView lv = (ListView) rootView.findViewById(R.id.list_diary);

		DiaryListAdapter mAdapter = new DiaryListAdapter(mContext, R.layout.item_diary, getDiaryListByType()); //this.getActivity()
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
			Diary diary = getDiaryListByType().get(position);
			Fragment fragment = new DiaryFragment(diary);
			MainActivity.changeFragment(fragment);
		}

	};




	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		//		Fragment fragment = new HomeFragement();
		//	    getFragmentManager().beginTransaction()
		//        .replace(R.id.container, fragment)
		//        .commit();
		//		return true;
		return false;
	}



	private ArrayList<Diary> getDiaryListByType() {
		arItem = new ArrayList<Diary>(); //확인
		if(type.equals("all")) {
			arItem = dataMgr.getDiaryList();
		} else { //folderName이 넘어오는 경우
			arItem = dataMgr.getDiaryListByFolderName(type);
		}
		return arItem;

	}


}


