package com.tleaf.tiary.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.adapter.DiaryListAdapter;
import com.tleaf.tiary.adapter.MenuListAdapter;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.model.MyMenuItem;

public class DiaryListViewFragement extends Fragment {

	public DiaryListViewFragement() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_diary_list, container, false);

		ListView lv = (ListView) rootView.findViewById(R.id.list_diary);

		ArrayList<Diary> arItem = new ArrayList<Diary>();
	
		String[] tags = {"#생일", "#이태원", "#홍석천"};
		Diary d = new Diary();
		d.setDate(20141011);
		d.setImage("image");
		d.setTitle("내생일 "); 
		d.setContent("꼭 한번 가보고 싶었던 이태원 All that jazz에 다녀왔다. 공연이 정말 멋졌다.");
		d.setTags(tags);
		d.setFolder("daily");
		d.setLocaton("우리집");
		d.setEmotion("기쁨");
		
		for(int i=0; i < 20; i++)
			arItem.add(d);
		
		DiaryListAdapter mAdapter = new DiaryListAdapter(this.getActivity(), R.layout.item_diary, arItem); //this.getActivity()
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

}
