package com.tleaf.tiary.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.model.MyMenuItem;

public class MenuListAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<MyMenuItem> mParent;
	private HashMap<String, ArrayList<String>> mChild;
	private int mParentLayout;
	private int mChildLayout;

	public MenuListAdapter(Context context, int layoutParent, int layoutChild, ArrayList<MyMenuItem> parent, HashMap<String, ArrayList<String>> child) {
		mContext = context;
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		mParent = parent;
		mChild = child;
		mParentLayout = layoutParent;
		mChildLayout = layoutChild;
	}


	@Override
	public int getGroupCount() {
		return mParent.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(mChild.get(mParent.get(groupPosition).getMyMenuTitle()) == null) 
			return 0;
 		return mChild.get(mParent.get(groupPosition).getMyMenuTitle()).size();
	}

	@Override
	public MyMenuItem getGroup(int groupPosition) {
		return mParent.get(groupPosition);
	}

	@Override
	public String getChild(int groupPosition, int childPosition) {
		return mChild.get(mParent.get(groupPosition).getMyMenuTitle()).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
//		((ExpandableListView) parent).expandGroup(groupPosition);
		final int pos = groupPosition;
		if (convertView == null) {
			convertView = mInflater.inflate(mParentLayout, parent, false);
		}
		ImageView img = (ImageView)convertView.findViewById(R.id.img_drawer);
		img.setImageResource(mParent.get(groupPosition).getMyMenuIcon());

		TextView txt = (TextView)convertView.findViewById(R.id.txt_drawer_title);
		txt.setText(mParent.get(groupPosition).getMyMenuTitle());

		return convertView;

	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final int pos = groupPosition;
		if (convertView == null) {
			convertView = mInflater.inflate(mChildLayout, parent, false);
		}

		TextView txt = (TextView)convertView.findViewById(R.id.txt_drawer_title);
		txt.setText(mChild.get(mParent.get(groupPosition).getMyMenuTitle()).get(childPosition));

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void refreshData() {
		DataManager dataMgr = new DataManager(mContext);
		mChild.get(mContext.getString(R.string.folder)).clear();
		mChild.put(mContext.getString(R.string.folder), dataMgr.getDistinctFolderList());
		notifyDataSetChanged();
	}
}