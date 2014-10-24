package com.tleaf.tiary.adapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.tleaf.tiary.R;
import com.tleaf.tiary.R.id;
import com.tleaf.tiary.model.MyMenuItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<MyMenuItem> arrItem;
	private int mLayout;

	public SpinnerListAdapter(Context context, int layout, ArrayList<MyMenuItem> item) {
		mContext = context;
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arrItem = item;
		mLayout = layout;
	}

	public int getCount() {
		return arrItem.size();
	}

	public MyMenuItem getItem(int position) {
		return arrItem.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	// 각 항목의 뷰 생성
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		if (convertView == null) {
			convertView = mInflater.inflate(mLayout, parent, false);
		}
		ImageView img = (ImageView)convertView.findViewById(R.id.img_drawer);
		img.setImageResource(arrItem.get(position).getMyMenuIcon());

		TextView txt = (TextView)convertView.findViewById(R.id.txt_drawer_title);
		txt.setText(arrItem.get(position).getMyMenuTitle());

		return convertView;
	}
}