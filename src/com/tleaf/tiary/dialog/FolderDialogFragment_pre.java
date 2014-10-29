package com.tleaf.tiary.dialog;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.util.Util;


public class FolderDialogFragment_pre extends DialogFragment {

	private DialogResultListener resultListener;
	private int dateType;
	private ArrayList<String> previousData;

	private Context mContext;
	private DataManager dataMgr;

	private EditText edit_folder;

	private ImageView img_add;

	private boolean select = false;

	private LinearLayout ll;
	private LinearLayout.LayoutParams llp;

	public static FolderDialogFragment_pre newInstace(DialogResultListener resultListener, int dataType, ArrayList<String> previousData) {
		FolderDialogFragment_pre fragment = new FolderDialogFragment_pre();
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

		ll = (LinearLayout) mv.findViewById(R.id.layout_folderDialog_txt);
		llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(5, 5, 5, 5);


		edit_folder = (EditText) mv.findViewById(R.id.edit_dialog_folder);
		img_add = (ImageView) mv.findViewById(R.id.img_dialog_folder_add);
		img_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String folder = edit_folder.getText().toString().trim();
				if(TextUtils.isEmpty(folder))
					Util.tst(mContext, "새로운 폴더를 입력해주세요");
				else {
					dataMgr.insertFolder(folder);
					setFolderList();
				}
			}
		});

		setFolderList();

		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		return mv;
	}

	private void setFolderList() {
		ArrayList<String> userFolders = new ArrayList<String>();
		userFolders = dataMgr.getDistinctFolderList();

		TextView txt_userfolder[] = new TextView[userFolders.size()];

		for(int i=0; i<userFolders.size() ;i++) {
			txt_userfolder[i] = new TextView(mContext);
			txt_userfolder[i].setText(userFolders.get(i));
			txt_userfolder[i].setLayoutParams(llp);

			//			if(previousData != null && previousData.contains(userFolders.get(i))) {
			//				txt_userfolder[i].setTextColor(getResources().getColor(R.color.point));			
			//			} else {
			//				txt_userfolder[i].setTextColor(getResources().getColor(R.color.text_gray_custom));
			//			}
			txt_userfolder[i].setTextSize(20);
			txt_userfolder[i].setOnClickListener(cl);
			ll.addView(txt_userfolder[i]);
		}

	}

	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//			TextView tv = (TextView) v;
			//			if (!select) { //셀렉트
			//				tv.setTextColor(getResources().getColor(R.color.point));
			//				if (!previousData.contains(tv.getText().toString())) {
			//					if (previousData == null && previousData.size() == 0)  
			//						previousData = new ArrayList<String>();
			//					previousData.add(tv.getText().toString());
			//					select = true;
			//				}
			//			} else { //셀렉트해제
			//				tv.setTextColor(getResources().getColor(R.color.text_gray_custom));
			//				previousData.remove(tv.getText());
			//				select = false;
			//			}
			//			edit_folder.setText(Util.covertArrayToString(previousData));
		}

	};

}

