package com.tleaf.tiary.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.model.MyTemplate;
import com.tleaf.tiary.util.Util;

/** 템플릿 검색 뷰에서 템플릿 항목을 expandablelistview로 보여줄 때 뷰를 바인딩해주는 expandablelistview adapter **/

public class TemplateExpandableListAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<String> mParent;
	private HashMap<String, ArrayList<MyTemplate>> mChild;
	private int mParentLayout;
	private int mChildLayout;

	public TemplateExpandableListAdapter(Context context, int layoutParent,
			int layoutChild, ArrayList<String> parent,
			HashMap<String, ArrayList<MyTemplate>> child) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		if (mChild.get(mParent.get(groupPosition)) == null)
			return 0;
		return mChild.get(mParent.get(groupPosition)).size();
	}

	@Override
	public String getGroup(int groupPosition) {
		return mParent.get(groupPosition);
	}

	@Override
	public MyTemplate getChild(int groupPosition, int childPosition) {
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
		// ((ExpandableListView) parent).expandGroup(groupPosition);
		final int pos = groupPosition;
		if (convertView == null) {
			convertView = mInflater.inflate(mParentLayout, parent, false);
		}

		TextView txt = (TextView) convertView
				.findViewById(R.id.txt_expand_template_title);
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

		TextView txt_template_name = (TextView) convertView
				.findViewById(R.id.txt_expand_child_template_title);
		txt_template_name.setText(mChild.get(mParent.get(groupPosition))
				.get(childPosition).getName());

		TextView txt_template_info = (TextView) convertView
				.findViewById(R.id.txt_expand_child_template_info);
		txt_template_info.setText(mChild.get(mParent.get(groupPosition))
				.get(childPosition).getInformation());

		// 임시
		String category = getChild(groupPosition, childPosition).getCategory();
		Util.ll("child view category", category);
		ImageView img = (ImageView) convertView
				.findViewById(R.id.img_expand_child_template);

		if (category.equals("Money")) {
			img.setImageResource(R.drawable.money);
			txt_template_info.setTextColor(mContext.getResources().getColor(
					R.color.background_white));
			txt_template_info.setAlpha(1);
		} else if (category.equals("Location")) {
			img.setImageResource(R.drawable.location_template);
			txt_template_info.setTextColor(mContext.getResources().getColor(
					R.color.background_white));
			txt_template_info.setAlpha(0.8f);
		} else if (category.equals("Diet")) {
			img.setImageResource(R.drawable.food);
			txt_template_info.setTextColor(mContext.getResources().getColor(
					R.color.diary_edit_edittext_hint));
			txt_template_info.setAlpha(1);
		} else {
			img.setImageResource(R.drawable.template_daily);
			txt_template_info.setTextColor(mContext.getResources().getColor(
					R.color.background_white));
			txt_template_info.setAlpha(1);
		}

		// TextView txt_title =
		// (TextView)convertView.findViewById(R.id.txt_expand_child_title);
		// txt_title.setText(mChild.get(mParent.get(groupPosition)).get(childPosition).getTitle());

		//
		// <com.tleaf.tiary.util.RoundImageView
		// android:id="@+id/img_expand_child_template"
		// android:layout_width="match_parent"
		// android:layout_height="80dp"
		// android:scaleType="centerCrop"
		// android:src="@drawable/template_daily" />

		// TextView txt_content = (TextView)
		// convertView.findViewById(R.id.txt_expand_child_template_title);
		// txt_content.setText(mChild.get(mParent.get(groupPosition)).get(childPosition).getTitle());

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
}