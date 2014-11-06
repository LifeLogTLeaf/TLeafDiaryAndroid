package com.tleaf.tiary.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.google.android.gms.internal.fr;
import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.model.Diary;

public class MyLifeLogFragement extends Fragment {

	private RelativeLayout layout_call;
	private RelativeLayout layout_msg;
	private RelativeLayout layout_galley;
	private RelativeLayout layout_card;
	private RelativeLayout layout_bookmark;
	private RelativeLayout layout_location;

	public MyLifeLogFragement() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mylifelog, container, false);


		layout_call = (RelativeLayout) rootView.findViewById(R.id.layout_call);
		layout_call.setOnClickListener(cl);
		layout_msg = (RelativeLayout) rootView.findViewById(R.id.layout_msg);
		layout_msg.setOnClickListener(cl);
		layout_galley = (RelativeLayout) rootView.findViewById(R.id.layout_gallery);
		layout_galley.setOnClickListener(cl);
		layout_card = (RelativeLayout) rootView.findViewById(R.id.layout_card);
		layout_card.setOnClickListener(cl);
		layout_bookmark = (RelativeLayout) rootView.findViewById(R.id.layout_bookmark);
		layout_bookmark.setOnClickListener(cl);
		layout_location = (RelativeLayout) rootView.findViewById(R.id.layout_location);
		layout_location.setOnClickListener(cl);


		return rootView;
	}

	private OnClickListener cl = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Fragment fragment = null;
			switch(v.getId()) {
			case R.id.layout_call:
				fragment = new CallFragment();
				break;
			case R.id.layout_msg:
				fragment = new SmsFragment();
				break;

			case R.id.layout_gallery:
				fragment = new GalleryFragment();
				break;

			case R.id.layout_card:
				fragment = new CardFragment();
				break;

			case R.id.layout_bookmark:
				fragment = new BookMarkFragment();
				break;
			case R.id.layout_location:
				fragment = new MyLocationFragment();
				break;
			}
			MainActivity.changeFragment(fragment);
		}
	};
}
