package com.tleaf.tiary.dialog;

import com.tleaf.tiary.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FolderDialogFragment extends DialogFragment {

	private View.OnClickListener okListner = null;
	private View.OnClickListener cancelListner = null;

	public static FolderDialogFragment newInstace(View.OnClickListener okListner, View.OnClickListener cancelListner) {
		FolderDialogFragment fragment = new FolderDialogFragment();
		fragment.okListner = okListner;
		fragment.cancelListner = cancelListner;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mv = inflater.inflate(R.layout.dialog_folder, container, false);

		TextView btn_ok = (TextView) mv.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(okListner);
		TextView btn_cancel = (TextView) mv.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(cancelListner);
		
		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);


		return mv;
	}
}
