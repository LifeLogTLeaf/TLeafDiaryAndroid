package com.tleaf.tiary.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.adapter.DiaryListAdapter;
import com.tleaf.tiary.model.Diary;

public class DiaryListViewFragement extends BaseFragment {

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

		View rootView = inflater.inflate(R.layout.fragment_list, container,
				false);
		ListView lv = (ListView) rootView.findViewById(R.id.list_gen);
		LinearLayout ll = (LinearLayout) rootView
				.findViewById(R.id.ll_no_diary);

		if (getDiaryListByType().size() == 0) {
			lv.setVisibility(View.GONE);
			ll.setVisibility(View.VISIBLE);
		} else {
			lv.setVisibility(View.VISIBLE);
			ll.setVisibility(View.GONE);
		}
		DiaryListAdapter mAdapter = new DiaryListAdapter(mContext,
				R.layout.item_diary_, getDiaryListByType()); // this.getActivity()
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(mItemClickListener);

		return rootView;
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Diary diary = getDiaryListByType().get(position);
			Fragment fragment = new DiaryFragment(diary);
			MainActivity.changeFragment(fragment);
		}
	};

	private ArrayList<Diary> getDiaryListByType() {
		arItem = new ArrayList<Diary>(); 
		if (type.equals("all")) {
			arItem = dataMgr.getDiaryList();
		} else { // folderName이 넘어오는 경우
			arItem = dataMgr.getDiaryListByFolderName(type);
		}
		return arItem;

	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}
