package com.tleaf.tiary.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.model.Diary;

public class DiaryFragment extends Fragment {
	
	private Diary diary;

	public DiaryFragment(Diary diary) {
		this.diary = diary;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_diary, container, false);


		TextView txt_date = (TextView) rootView.findViewById(R.id.txt_diary_date);
//		txt_date.setOnClickListener(cl);

		TextView txt_title = (TextView) rootView.findViewById(R.id.txt_diary_title);
		txt_title.setText(diary.getTitle());
		TextView txt_content = (TextView) rootView.findViewById(R.id.txt_diary_content);
		txt_content.setText(diary.getContent());
		
		return rootView;

	}
}

