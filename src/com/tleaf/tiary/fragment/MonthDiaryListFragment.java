package com.tleaf.tiary.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tleaf.tiary.R;

public class MonthDiaryListFragment  extends Fragment {
	
	public MonthDiaryListFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_month_diary_list, container, false);
		return rootView;
	}

}
