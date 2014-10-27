package com.tleaf.tiary.dialog;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.util.Util;

public class TagDialogFragment extends DialogFragment {

	private DialogResultListener resultListener;
	private int dateType;
	private ArrayList<String> previousData;
	
	public static TagDialogFragment newInstace(
			DialogResultListener resultListener, int dataType, ArrayList<String> previousData) {
		TagDialogFragment fragment = new TagDialogFragment();
		fragment.resultListener = resultListener;
		fragment.dateType = dataType;
		fragment.previousData = previousData;
 		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE,
				android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mv = inflater.inflate(R.layout.dialog_tag, container, false);

		final EditText edit_tag = (EditText) mv
				.findViewById(R.id.edit_dialog_tag);
		
		if (previousData != null && previousData.size() != 0) 
			edit_tag.setText(Util.covertArrayToString(previousData));
		
		TextView btn_ok = (TextView) mv.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resultListener.setResult(edit_tag.getText().toString(), dateType);
			}
		});
		
		TextView btn_cancel = (TextView) mv.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resultListener.setCancel();
				dismiss();
			}
		});

		getDialog().getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);

		return mv;
	}
}
