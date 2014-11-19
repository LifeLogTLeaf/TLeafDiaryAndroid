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
import com.tleaf.tiary.model.MyTemplate;
import com.tleaf.tiary.model.TemplateContent;
import com.tleaf.tiary.template.ChatFragement;
import com.tleaf.tiary.util.Util;

/** 템플릿 검색 및 템플릿 연결을 담당하는 프래그먼트 클래스
 * expandable listview로 구성된다  **/
public class TemplateSearchFragment extends BaseFragment {

	private ExpandableListView exListView;

	private ArrayList<String> mParent;
	private HashMap<String, ArrayList<MyTemplate>> mChild;

	public TemplateSearchFragment() {
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

		mParent = new ArrayList<String>();
		mParent.addAll(dataMgr.getDistinctTemplateCategory());


		mChild = new HashMap<String, ArrayList<MyTemplate>>();

		for(int i = 0; i < mParent.size(); i++) 
			mChild.put(mParent.get(i), dataMgr.getTemplateList(mParent.get(i)));

		TemplateExpandableListAdapter mAdapter = new TemplateExpandableListAdapter(mContext, R.layout.item_expandable_parent_template, R.layout.item_expandable_child_template, mParent, mChild); // this.getActivity()

		exListView.setAdapter(mAdapter);
		//				exListView.setItemChecked(mCurrentSelectedPosition, true);
		return rootView;

	}

	/** expandable listview에 차일드 뷰를 셋팅하는 메서드 **/
	private void selectChildItem(int position, int childPosition) {
		Util.tst(mContext, "position"+position+",childPosition"+childPosition);
		FragmentManager fragmentManager = getFragmentManager();

		ArrayList<TemplateContent> contentArr = new ArrayList<TemplateContent>();
		long no = mChild.get(mParent.get(position)).get(childPosition).getNo();
		contentArr.addAll(dataMgr.getTemplateContentByNo(no));

		if (contentArr != null && contentArr.size() != 0) {
			Fragment fragment = new ChatFragement(contentArr);

			fragmentManager.beginTransaction()
			.replace(R.id.container, fragment)
			.commit();
		} else {
			Util.tst(mContext, "아직 템플릿 준비중입니다");
		}

	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
}


