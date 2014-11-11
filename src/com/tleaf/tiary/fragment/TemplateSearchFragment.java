package com.tleaf.tiary.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.tleaf.tiary.R;
import com.tleaf.tiary.adapter.MyExpandableListAdapter;
import com.tleaf.tiary.adapter.TemplateExpandableListAdapter;
import com.tleaf.tiary.fragment.lifelog.MyLifeLogFragement;
import com.tleaf.tiary.model.TemplateExpandableItem;
import com.tleaf.tiary.util.Util;

public class TemplateSearchFragment extends Fragment {

	private Context mContext;
	private ExpandableListView exListView;

	public TemplateSearchFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.fragment_template_search, container, false);

		exListView = (ExpandableListView) rootView.findViewById(R.id.list_expand_search);

		exListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		exListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				selectChildItem(groupPosition, childPosition);
				return false;
			}
		});

		ArrayList<String> mParent = new ArrayList<String>();
		mParent.add("전체");
		mParent.add("일상");
		mParent.add("다이어트");
		mParent.add("커플 일기");
		mParent.add("우정 일기");

		ArrayList<TemplateExpandableItem> mChildItem = new ArrayList<TemplateExpandableItem>();

		mChildItem.add(new TemplateExpandableItem("매일일기", "설정설명입니다"));
		mChildItem.add(new TemplateExpandableItem("음식기록", "설정설명입니다"));
		mChildItem.add(new TemplateExpandableItem("오늘의 소비", "설정설명입니다"));

		HashMap<String, ArrayList<TemplateExpandableItem>> mChild = new HashMap<String, ArrayList<TemplateExpandableItem>>();


		for(int i = 0; i < mParent.size(); i++) 
			mChild.put(mParent.get(i), mChildItem);

		TemplateExpandableListAdapter mAdapter = new TemplateExpandableListAdapter(mContext, R.layout.item_expandable_parent_template, R.layout.item_expandable_child_template, mParent, mChild); // this.getActivity()
		
		exListView.setAdapter(mAdapter);
		//		exListView.setItemChecked(mCurrentSelectedPosition, true);
		return rootView;

	}


	private void selectChildItem(int position, int childPosition) {
		
		Util.tst(mContext, "position"+position+",childPosition"+childPosition);
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();

		Fragment fragment = null;

		if(position == 0) {
			switch(childPosition) {
			case 0:
				fragment = new DiaryListViewFragement();//HomeFragement();
				break;
			case 1:
				fragment = new MyLifeLogFragement();
				break;
			case 2:
				fragment = new DiaryEditFragment();//WriteFragement();
				break;
			} 
		} else if(position == 1) {
			switch(childPosition) {
			case 0:
				fragment = new DiaryListViewFragement();//HomeFragement();
				break;
			case 1:
				fragment = new MyLifeLogFragement();
				break;
			case 2:
				fragment = new DiaryEditFragment();//WriteFragement();
				break;
			} 
		} 

		fragmentManager.beginTransaction()
		.replace(R.id.container, fragment)//PlaceholderFragment.newInstance(position + 1))
		.commit();

	}
}


