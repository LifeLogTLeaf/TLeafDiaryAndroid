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
		
		TextView txt_tag = (TextView)convertView.findViewById(R.id.item_txt_diary_tag);
		ArrayList<String> tags = arrItem.get(position).getTags();
		if(tags != null && tags.size() != 0)
			txt_tag.setText(tags.get(0)); 
				
		//사용자에게 더 있음을 알려주는 ui
//		String tag = "";
//		for(int i=0; i< tags.length; i++) {
//			tag += tags[i] + ",";
//		}
		
		TextView txt_folder = (TextView)convertView.findViewById(R.id.item_txt_diary_folder);
		ArrayList<String> folders = arrItem.get(position).getFolders();
		if(folders != null && folders.size() != 0)
			txt_folder.setText(folders.get(0)); 

		TextView txt_location = (TextView)convertView.findViewById(R.id.item_txt_diary_location);
		txt_location.setText(arrItem.get(position).getLocation());
		
		return convertView;
	}
	
}

//d.setDate(20141011);
//d.setImage("image");
//d.setTitle("내생일 "); 
//d.setContent("꼭 한번 가보고 싶었던 이태원 All that jazz에 다녀왔다. 공연이 정말 멋졌다.");
//d.setTags(tags);
//d.setFolder("daily");
//d.setLocaton("우리집");
//d.setEmotion("기쁨"); -> 그림으로    