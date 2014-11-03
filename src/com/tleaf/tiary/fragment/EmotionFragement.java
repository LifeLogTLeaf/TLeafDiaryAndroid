package com.tleaf.tiary.fragment;

import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EmotionFragement extends Fragment {

	
	public EmotionFragement() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_emo, container,
				false);


		return rootView;
	}

	


}
