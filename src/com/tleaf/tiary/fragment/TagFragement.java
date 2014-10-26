package com.tleaf.tiary.fragment;

import java.util.ArrayList;

import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.adapter.DiaryListAdapter;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.model.Diary;

import android.app.Fragment;
import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

public class TagFragement extends Fragment {

	private Context mContext;
	private DataManager dataMgr;

	private String type;
	private ArrayList<Diary> arItem;


	public TagFragement() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mContext = getActivity();
		dataMgr = new DataManager(mContext);


		View rootView = inflater.inflate(R.layout.fragment_search, container, false);

		Spinner spin = (Spinner) rootView.findViewById(R.id.spin_search);
		spin.setVisibility(View.GONE);

		ListView lv = (ListView) rootView.findViewById(R.id.list_diary_search);
		lv.setOnItemClickListener(mItemClickListener);


		DiaryListAdapter mAdapter = new DiaryListAdapter(mContext, R.layout.item_diary, getDiaryListByType()); //this.getActivity()
		lv.setAdapter(mAdapter);


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
	
	private ArrayList<Diary> getDiaryListByType() {
		arItem = new ArrayList<Diary>();
		/*
		ArrayList<String> arrData = new ArrayList<String>();
		arrData.add("생일");
		arrData.add("생일");
		arrData.add("생일");
		
		type = "all";
		if(type.equals("all")) {
			String[] tags = {"생일", "이태원", "홍석천"};
			Diary d = new Diary();
			d.setDate(20141011);
			d.setImage("image");
			d.setTitle("내생일 "); 
			d.setContent("꼭 한번 가보고 싶었던 이태원 All that jazz에 다녀왔다. 공연이 정말 멋졌다.");
			d.setTags(arrData);
			d.setFolder("daily");
			d.setLocaton("우리집");
			d.setEmotion("기쁨");
			
			for(int i=0; i < 20; i++)
				arItem.add(d);
			
//			dataMgr.getDiaryList();
		} else {
			String[] tags = {"생일", "이태원", "홍석천"};
			Diary d = new Diary();
			d.setDate(20141011);
			d.setImage("image");
			d.setTitle("내생일 "); 
			d.setContent("안드로이드 개발.....템플릿 일기.....너이자식......");
			d.setTags(arrData);
			d.setFolder("daily");
			d.setLocaton("우리집");
			d.setEmotion("기쁨");
			
			for(int i=0; i < 20; i++)
				arItem.add(d);
//			dataMgr.getDiaryListByFolderName(type);
		}
	
		*/
		return arItem;
	}

}
