package com.tleaf.tiary.dialog;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.util.Util;


public class FolderDialogFragment extends DialogFragment {

	private DialogResultListener resultListener;
	private int dateType;
	private ArrayList<String> previousData;

	private Context mContext;
	private DataManager dataMgr;

	private EditText edit_folder;

	private boolean select = false;

	public static FolderDialogFragment newInstace(DialogResultListener resultListener, int dataType, ArrayList<String> previousData) {
		FolderDialogFragment fragment = new FolderDialogFragment();
		fragment.resultListener = resultListener;
		fragment.dateType = dataType;
		fragment.previousData = previousData;
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
		mContext = getActivity();
		dataMgr = new DataManager(mContext);

		edit_folder = (EditText) mv.findViewById(R.id.edit_dialog_folder);

		if (previousData != null && previousData.size() != 0) 
			edit_folder.setText(Util.covertArrayToString(previousData));
//		else 
//			previousData = new ArrayList<String>();

		LinearLayout ll = (LinearLayout) mv.findViewById(R.id.layout_folderDialog_txt);


		//        <TextView
		//            android:id="@+id/textView1"
		//            android:layout_width="wrap_content"
		//            android:layout_height="wrap_content"
		//            android:layout_margin="5dp"
		//            android:text="매일일기 "
		//            android:textColor="@android:color/darker_gray"
		//            android:textSize="16sp" />


		ArrayList<String> userFolders = new ArrayList<String>();
		userFolders = dataMgr.getDistinctFolderList();

		TextView txt_userfolder[] = new TextView[userFolders.size()];

		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(5, 5, 5, 5);

		for(int i=0; i<userFolders.size() ;i++) {
			txt_userfolder[i] = new TextView(mContext);
			txt_userfolder[i].setText(userFolders.get(i));
			txt_userfolder[i].setLayoutParams(llp);

			if(previousData != null && previousData.contains(userFolders.get(i))) {
				txt_userfolder[i].setTextColor(getResources().getColor(R.color.point));			
			} else {
				txt_userfolder[i].setTextColor(getResources().getColor(R.color.text_gray_custom));
			}
			txt_userfolder[i].setTextSize(20);
			txt_userfolder[i].setOnClickListener(cl);
			ll.addView(txt_userfolder[i]);
		}

		TextView btn_ok = (TextView) mv.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resultListener.setResult(edit_folder.getText().toString(), dateType);
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

	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TextView tv = (TextView) v;
			if (!select) { //셀렉트
				tv.setTextColor(getResources().getColor(R.color.point));
				if (!previousData.contains(tv.getText().toString())) {
					if (previousData == null && previousData.size() == 0)  
						previousData = new ArrayList<String>();
					previousData.add(tv.getText().toString());
					select = true;
				}
			} else { //셀렉트해제
				tv.setTextColor(getResources().getColor(R.color.text_gray_custom));
				previousData.remove(tv.getText());
				select = false;
			}
			edit_folder.setText(Util.covertArrayToString(previousData));
		}

	};

}
