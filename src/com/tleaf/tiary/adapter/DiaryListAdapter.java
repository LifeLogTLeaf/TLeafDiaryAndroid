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
	
//	d.setDate(20141011);
//	d.setImage("image");
//	d.setTitle("내생일 "); 
//	d.setContent("꼭 한번 가보고 싶었던 이태원 All that jazz에 다녀왔다. 공연이 정말 멋졌다.");
//	d.setTags(tags);
//	d.setFolder("daily");
//	d.setLocaton("우리집");
//	d.setEmotion("기쁨"); -> 그림으로    
	
	
	
	// 각 항목의 뷰 생성
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		if (convertView == null) {
			convertView = mInflater.inflate(mLayout, parent, false);
		}

		TextView txt_date = (TextView)convertView.findViewById(R.id.item_txt_diary_date);
		txt_date.setText(""+arrItem.get(position).getDate());
		
		
		ImageView img = (ImageView)convertView.findViewById(R.id.item_img_diary);
		img.setImageResource(R.drawable.day);

		TextView txt_title = (TextView)convertView.findViewById(R.id.item_txt_diary_title);
		txt_title.setText(arrItem.get(position).getTitle());
		
		TextView txt_content = (TextView)convertView.findViewById(R.id.item_txt_diary_content);
		txt_content.setText(arrItem.get(position).getContent());
		
		TextView txt_tag = (TextView)convertView.findViewById(R.id.item_txt_diary_tag);
//		ImageView img_tag = (ImageView)convertView.findViewById(R.id.item_img_diary_tag);
//		img_tag.setImageResource(R.drawable.tag2);
		
		String[] tags = arrItem.get(position).getTags();
		//사용자에게 더 있음을 알려주는 ui
//		String tag = "";
//		for(int i=0; i< tags.length; i++) {
//			tag += tags[i] + ",";
//		}
//		txt_tag.setText(tag);
		txt_tag.setText(tags[0]); 
		
		
		TextView txt_folder = (TextView)convertView.findViewById(R.id.item_txt_diary_folder);
		txt_folder.setText(arrItem.get(position).getFolder());
//		ImageView img_folder = (ImageView)convertView.findViewById(R.id.item_img_diary_folder);
//		img_folder.setImageResource(R.drawable.folder_list);
		
		TextView txt_location = (TextView)convertView.findViewById(R.id.item_txt_diary_location);
		txt_location.setText(arrItem.get(position).getLocation());
//		ImageView img_location = (ImageView)convertView.findViewById(R.id.item_img_diary_location);
//		img_location.setImageResource(R.drawable.location);
		
		
		return convertView;
	}
}