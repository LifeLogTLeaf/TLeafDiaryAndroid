package com.tleaf.tiary.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.model.MyMenuItem;
import com.tleaf.tiary.util.MyTime;

public class DiaryListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Diary> arrItem;
	private int mLayout;

	public DiaryListAdapter(Context context, int layout, ArrayList<Diary> item) {
		mContext = context;
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arrItem = item;
		mLayout = layout;
	}

	public int getCount() {
		return arrItem.size();
	}

	public Diary getItem(int position) {
		return arrItem.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	//	String table_diary = "create table diary (no integer primary key autoincrement, " +
	//			"date integer, " +
	//			"title text, " +
	//			"content text, " +
	//			"emotion text, " +
	//			"images text, " +
	//			"tags text, " +
	//			"folders text, " +
	//			"location text, " +
	//			"todayWeather text, " +
	//			"temperature real, " +
	//			"humidity real)";

	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		if (convertView == null) {
			convertView = mInflater.inflate(mLayout, parent, false);
		}

		TextView txt_date = (TextView)convertView.findViewById(R.id.item_txt_diary_date);
		String dateStr = MyTime.getLongToString(mContext, arrItem.get(position).getDate());
		txt_date.setText(dateStr);

		ImageView img = (ImageView)convertView.findViewById(R.id.item_img_diary);
		img.setImageResource(R.drawable.day);

		TextView txt_title = (TextView)convertView.findViewById(R.id.item_txt_diary_title);
		txt_title.setText(arrItem.get(position).getTitle());

		TextView txt_content = (TextView)convertView.findViewById(R.id.item_txt_diary_content);
		txt_content.setText(arrItem.get(position).getContent());

		
		
		ImageView img_tag = (ImageView)convertView.findViewById(R.id.item_img_diary_tag);

		TextView txt_tag = (TextView)convertView.findViewById(R.id.item_txt_diary_tag);
		ArrayList<String> tags = arrItem.get(position).getTags();
		if(tags != null && tags.size() != 0) {
			txt_tag.setVisibility(View.VISIBLE);
			txt_tag.setText(tags.get(0));
			img_tag.setVisibility(View.VISIBLE);

		} else {
			txt_tag.setVisibility(View.GONE);
			img_tag.setVisibility(View.GONE);
		}

		//사용자에게 더 있음을 알려주는 ui
		//		String tag = "";
		//		for(int i=0; i< tags.length; i++) {
		//			tag += tags[i] + ",";
		//		}

		
		ImageView img_folder = (ImageView)convertView.findViewById(R.id.item_img_diary_folder);

		TextView txt_folder = (TextView)convertView.findViewById(R.id.item_txt_diary_folder);
		ArrayList<String> folders = arrItem.get(position).getFolders();
		if(folders != null && folders.size() != 0) {
			txt_folder.setVisibility(View.VISIBLE);
			txt_folder.setText(folders.get(0)); 
			img_folder.setVisibility(View.VISIBLE);
		} else { 
			txt_folder.setVisibility(View.GONE);
			img_folder.setVisibility(View.GONE);
		}
		
		ImageView img_location = (ImageView)convertView.findViewById(R.id.item_img_diary_location);

		TextView txt_location = (TextView)convertView.findViewById(R.id.item_txt_diary_location);
		String location = arrItem.get(position).getLocation().trim();
		if(location != null && !location.equals("null") && !location.equals("")) {
			txt_location.setVisibility(View.VISIBLE);
			txt_location.setText(arrItem.get(position).getLocation());
			img_location.setVisibility(View.VISIBLE);
			
		} else {
			txt_location.setVisibility(View.GONE);
			img_location.setVisibility(View.GONE);
		}
		
		return convertView;
	}
}
