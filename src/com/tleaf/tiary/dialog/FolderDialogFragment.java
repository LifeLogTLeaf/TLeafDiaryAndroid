package com.tleaf.tiary.dialog;

import com.tleaf.tiary.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;


public class FolderDialogFragment extends DialogFragment {

	private DialogResultListener resultListener;
	public static FolderDialogFragment newInstace(DialogResultListener resultListener) {
		FolderDialogFragment fragment = new FolderDialogFragment();
		fragment.resultListener = resultListener;
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

		
		final EditText edit_folder = (EditText) mv
				.findViewById(R.id.edit_dialog_tag);

		TextView btn_ok = (TextView) mv.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resultListener.setResult(edit_folder.getText().toString());
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

		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);


		return mv;
	}
}
