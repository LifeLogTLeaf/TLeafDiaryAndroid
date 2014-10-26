package com.tleaf.tiary.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class DiaryFragment extends Fragment {
	
	private Diary diary;
	private Context mContext;
	
	public DiaryFragment(Diary diary) {
		this.diary = diary;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_diary, container, false);
		
		mContext = getActivity();
		
		TextView txt_date = (TextView)rootView.findViewById(R.id.txt_diary_date);
		String dateStr = MyTime.getLongToString(mContext, diary.getDate());
		txt_date.setText(dateStr);
		
		TextView txt_title = (TextView) rootView.findViewById(R.id.txt_diary_title);
		txt_title.setText(diary.getTitle());
		
		TextView txt_content = (TextView) rootView.findViewById(R.id.txt_diary_content);
		txt_content.setText(diary.getContent());

		TextView txt_tag = (TextView) rootView.findViewById(R.id.txt_diary_tag);
		ArrayList<String> tags = diary.getTags();
		if(tags != null && tags.size() != 0) {
			Util.ll("tags.size()", tags.size());
			Util.ll("Util.covertArrayToString(tags)", Util.covertArrayToString(tags));

			txt_tag.setText(Util.covertArrayToString(tags)); 
		}
		
		TextView txt_folder = (TextView) rootView.findViewById(R.id.txt_diary_folder);
		ArrayList<String> folders = diary.getFolders();
		if(folders != null && folders.size() != 0) {
			Util.ll("folders.size()", folders.size());
			Util.ll("Util.covertArrayToString(folders)", Util.covertArrayToString(folders));
			txt_folder.setText(Util.covertArrayToString(folders)); 
		}
		
		TextView txt_location = (TextView) rootView.findViewById(R.id.txt_diary_location);
		txt_location.setText(diary.getLocation());

		String tagStr = txt_tag.getText().toString();
		String tagFolder = txt_folder.getText().toString();
		String tagLocation = txt_location.getText().toString();

		LinearLayout layout_info = (LinearLayout) rootView
				.findViewById(R.id.layout_diary_user_add_info);
		if ((tagStr != null && !tagStr.isEmpty())
				|| (tagFolder != null && !tagFolder.isEmpty())
				|| (tagLocation != null && !tagLocation.isEmpty())) {
			layout_info.setVisibility(View.VISIBLE);
		} else {
			layout_info.setVisibility(View.GONE);
		}

		return rootView;

	}
}

//ImageView img = (ImageView)convertView.findViewById(R.id.item_img_diary);
//img.setImageResource(R.drawable.day);
