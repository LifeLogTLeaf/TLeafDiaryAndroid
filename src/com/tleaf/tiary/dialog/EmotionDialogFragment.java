package com.tleaf.tiary.dialog;

import java.net.IDN;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.internal.ie;
import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.util.Util;

/** 이모티콘 선택을 위한 커스텀 다이어로그 프래그먼트 클래스 **/
public class EmotionDialogFragment extends DialogFragment {

	private DialogResultListener resultListener;

	private Context mContext;

	private int selectedIndex = -1;

	private ImageView[] iv;

//	private int previousIndex;
	
	public static EmotionDialogFragment newInstace(
			DialogResultListener resultListener, int previousIndex) {
		EmotionDialogFragment fragment = new EmotionDialogFragment();
		fragment.resultListener = resultListener;
		fragment.selectedIndex = previousIndex;
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
		View mv = inflater.inflate(R.layout.dialog_emotion, container, false);
		mContext = getActivity();

		iv = new ImageView[25];
		for (int i = 0; i < 25; i++) {
			iv[i] = (ImageView) mv.findViewById(getResources().getIdentifier(
					"img_dialog_emo" + (i + 1), "id",
					mContext.getPackageName()));
			iv[i].setTag(i);
			iv[i].setOnClickListener(cl);
			if (i == selectedIndex) {
				iv[i].setColorFilter(getResources().getColor(R.color.point));
			}
		}

		TextView btn_ok = (TextView) mv.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectedIndex != -1) { //선택했을 시에만 반영
					resultListener.setResult(selectedIndex);
				} else {
//					resultListener.setCancel();
					dismiss();
				}
			}
		});

		TextView btn_cancel = (TextView) mv.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resultListener.setCancel();
//				dismiss();
			}
		});

		getDialog().getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);

		return mv;
	}

	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ImageView mv = (ImageView) v;
			if (selectedIndex == -1) { //아무것도 선택 안했을 시
				mv.setColorFilter(getResources().getColor(R.color.point));
				selectedIndex = (Integer) mv.getTag(); 
			} else {
				int index = (Integer) mv.getTag();
				if(index == selectedIndex) { //선택한걸 다시 선택할 경우 해제로 인식
					mv.setColorFilter(null);
					selectedIndex = -1;
				} else { //선택한 것이 있는데 다른 것을 다시 선택할 경우
					iv[selectedIndex].setColorFilter(null);
//					iv[previousIndex].setColorFilter(null);
					iv[index].setColorFilter(getResources().getColor(R.color.point));
					selectedIndex = index;
				}
			}
		}

	};

}
