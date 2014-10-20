package com.tleaf.tiary.fragment;

import com.tleaf.tiary.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WriteFragement extends Fragment {
	
	public WriteFragement() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_write, container, false);
		return rootView;
	}

}
