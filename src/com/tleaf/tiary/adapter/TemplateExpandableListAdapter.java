package com.tleaf.tiary.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.model.ExpandableItem;
import com.tleaf.tiary.model.TemplateExpandableItem;

public class TemplateExpandableListAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<String> mParent;
	private HashMap<String, ArrayList<TemplateExpandableItem>> mChild;
	private int mParentLayout;
	private int mChildLayout;

	public TemplateExpandableListAdapter(Context context, int layoutParent, int layoutChild, ArrayList<String> parent, HashMap<String, ArrayList<TemplateExpandableItem>> child) {
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
		if(mChild.get(mParent.get(groupPosition)) == null) 
			return 0;
 		return mChild.get(mParent.get(groupPosition)).size();
	}

	@Override
	public String getGroup(int groupPosition) {
		return mParent.get(groupPosition);
	}

	@Override
	public TemplateExpandableItem getChild(int groupPosition, int childPosition) {
		return mChild.get(mParent.get(groupPosition)).get(childPosition);
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
	
		TextView txt = (TextView)convertView.findViewById(R.id.txt_expand_template_title);
		txt.setText(mParent.get(groupPosition));

		return convertView;

	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final int pos = groupPosition;
		if (convertView == null) {
			convertView = mInflater.inflate(mChildLayout, parent, false);
		}

//		RoundImageView rImg = (RoundImageView)  convertView.findViewById(R.id.img_expand_child_template);
//		rImg.setImageRounding(false, false, false, false);
		
//		TextView txt_title = (TextView)convertView.findViewById(R.id.txt_expand_child_title);
//		txt_title.setText(mChild.get(mParent.get(groupPosition)).get(childPosition).getTitle());
		
//		
//        <com.tleaf.tiary.util.RoundImageView
//        android:id="@+id/img_expand_child_template"
//        android:layout_width="match_parent"
//        android:layout_height="80dp"
//        android:scaleType="centerCrop"
//     	android:src="@drawable/template_daily" />
		
		
//		TextView txt_content = (TextView) convertView.findViewById(R.id.txt_expand_child_template_title);
//		txt_content.setText(mChild.get(mParent.get(groupPosition)).get(childPosition).getTitle());

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
}