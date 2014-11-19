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

import com.tleaf.tiary.R;
import com.tleaf.tiary.adapter.MenuListAdapter;
import com.tleaf.tiary.adapter.MyExpandableListAdapter;
import com.tleaf.tiary.fragment.lifelog.MyLifeLogFragement;
import com.tleaf.tiary.model.ExpandableItem;
import com.tleaf.tiary.util.Util;

/** 설정을 보여주는 프래그먼트를 담당하는 클래스 **/
public class ExpandableListFragment extends BaseFragment {

	private Context mContext;
	private ExpandableListView exListView;

	public ExpandableListFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.fragment_expandable, container, false);

		exListView = (ExpandableListView) rootView.findViewById(R.id.list_expand);


		exListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				selectChildItem(groupPosition, childPosition);
				return false;
			}
		});

		ArrayList<String> mParent = new ArrayList<String>();
		mParent.add("내 정보");
		mParent.add("잠금");
		mParent.add("일기 관리");
		mParent.add("안내");

		ArrayList<ExpandableItem> mChildItem1 = new ArrayList<ExpandableItem>();

		mChildItem1.add(new ExpandableItem("로그인", "설정설명입니다"));
		mChildItem1.add(new ExpandableItem("비밀번호 수정", "설정설명입니다"));
		mChildItem1.add(new ExpandableItem("계정 탈퇴", "설정설명입니다"));

		ArrayList<ExpandableItem> mChildItem2 = new ArrayList<ExpandableItem>();

		mChildItem2.add(new ExpandableItem("잠금 설정", "설정설명입니다"));
		mChildItem2.add(new ExpandableItem("암호 변경", "설정설명입니다"));

		ArrayList<ExpandableItem> mChildItem3 = new ArrayList<ExpandableItem>();

		mChildItem3.add(new ExpandableItem("일기 내보내기", "설정설명입니다"));
		mChildItem3.add(new ExpandableItem("일기 가져오기", "설정설명입니다"));
		mChildItem3.add(new ExpandableItem("동기화", "설정설명입니다"));

		ArrayList<ExpandableItem> mChildItem4 = new ArrayList<ExpandableItem>();

		mChildItem4.add(new ExpandableItem("Tiary 정보", "설정설명입니다"));

		HashMap<String, ArrayList<ExpandableItem>> mChild = new HashMap<String, ArrayList<ExpandableItem>>();


//		for(int i = 0; i < mParent.size(); i++) 
//			mChild.put(mParent.get(i), mChildItem);
		
		mChild.put(mParent.get(0), mChildItem1);
		mChild.put(mParent.get(1), mChildItem2);
		mChild.put(mParent.get(2), mChildItem3);
		mChild.put(mParent.get(3), mChildItem4);
		

		MyExpandableListAdapter mAdapter = new MyExpandableListAdapter(mContext, R.layout.item_expandable_parent, R.layout.item_expandable_child, mParent, mChild); // this.getActivity()
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

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
}


