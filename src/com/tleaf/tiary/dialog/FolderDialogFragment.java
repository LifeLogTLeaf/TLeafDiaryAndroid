package com.tleaf.tiary.dialog;

import java.util.ArrayList;
import java.util.Map;

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

import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.util.Util;
 
/** 폴더 선택을 위한 커스텀 다이어로그 프래그먼트 클래스 **/
public class FolderDialogFragment extends DialogFragment {

	private final int initialize = 1; //true;
	private final int refresh = 2;//false;
	
	private DialogResultListener resultListener;
	private int dataType;
	private ArrayList<String> previousData;

	private Context mContext;
	private DataManager dataMgr;

	private EditText edit_folder;
	private ImageView img_add;
	private TextView[] txt_userfolder;
	
	private ArrayList<String> selectFolders = new ArrayList<String>();

	private LinearLayout ll;
	private LinearLayout.LayoutParams llp;

	public static FolderDialogFragment newInstace(DialogResultListener resultListener, int dataType, ArrayList<String> previousData) {
		FolderDialogFragment fragment = new FolderDialogFragment();
		fragment.resultListener = resultListener;
		fragment.dataType = dataType;
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
					boolean result = dataMgr.insertFolder(folder);
					if(!result)
						Util.tst(mContext, "이미 있는 폴더입니다");
					edit_folder.setText("");
					setFolderList(refresh);
				}
			}
		});

		setFolderList(initialize);

		TextView btn_ok = (TextView) mv.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.ll("확인 버튼 클릭", Util.covertArrayToString(selectFolders));
				resultListener.setResult(selectFolders, Common.FOLDER);
			}
		});

		TextView btn_cancel = (TextView) mv.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resultListener.setCancel();
			}
		});
		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		return mv;
	}

	/** 다이어로그에 폴더 목록을 동적으로 생성하는 메서드 **/
	private void setFolderList(int type) {
		ll.removeAllViews();
		ArrayList<String> userFolders = new ArrayList<String>();
		userFolders = dataMgr.getDistinctFolderList();

		txt_userfolder = new TextView[userFolders.size()];

		for(int i=0; i<userFolders.size() ;i++) {
			txt_userfolder[i] = new TextView(mContext);
			txt_userfolder[i].setText(userFolders.get(i));
			txt_userfolder[i].setLayoutParams(llp);
			txt_userfolder[i].setTag(i);

			if(previousData != null && previousData.contains(userFolders.get(i)) || selectFolders != null && selectFolders.contains(userFolders.get(i))) { //이전에 선택한 태그
				txt_userfolder[i].setTextColor(getResources().getColor(R.color.point));		
				if (type == initialize)
					selectFolders.add(userFolders.get(i));
			} else {
				txt_userfolder[i].setTextColor(getResources().getColor(R.color.diary_content));
			}
			txt_userfolder[i].setTextSize(25);
			txt_userfolder[i].setOnClickListener(cl);
			ll.addView(txt_userfolder[i]);
		}

	}
	
	/** 다이어로그 선택에 따른 사용자 선택값 관리 **/
	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TextView tv = (TextView) v;
			String str = tv.getText().toString();

			if(selectFolders != null && selectFolders.size() != 0) { //배열에 선택된 것이 들어가 있는 경우
				if (selectFolders.contains(str)) { //이미 선택된 경우
					tv.setTextColor(getResources().getColor(R.color.diary_content));
					selectFolders.remove(str);
				} else { //처음 선택인 경우
					tv.setTextColor(getResources().getColor(R.color.point));
					selectFolders.add(str);
				}
			} else { //아무것도 선택된 것이 없는 경우
				tv.setTextColor(getResources().getColor(R.color.point));
				selectFolders.add(str);
			}

		}
	};
}
