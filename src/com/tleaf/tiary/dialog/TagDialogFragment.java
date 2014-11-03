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


public class TagDialogFragment extends DialogFragment {

	private final int initialize = 1; //true;
	private final int refresh = 2;//false;

	private DialogResultListener resultListener;
	private int dataType;
	private ArrayList<String> previousData;

	private Context mContext;
	private DataManager dataMgr;

	private EditText edit_tag;
	private ImageView img_add;
	private TextView[] txt_userTag;

	private ArrayList<String> selectedTags = new ArrayList<String>();

	private LinearLayout ll;
	private LinearLayout.LayoutParams llp;

	public static TagDialogFragment newInstace(DialogResultListener resultListener, int dataType, ArrayList<String> previousData) {
		TagDialogFragment fragment = new TagDialogFragment();
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
		View mv = inflater.inflate(R.layout.dialog_tag, container, false);
		mContext = getActivity();
		dataMgr = new DataManager(mContext);

		ll = (LinearLayout) mv.findViewById(R.id.layout_tagDialog_txt);
		llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(5, 5, 5, 5);

		edit_tag = (EditText) mv.findViewById(R.id.edit_dialog_tag);
		img_add = (ImageView) mv.findViewById(R.id.img_dialog_tag_add);
		img_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tag = edit_tag.getText().toString().trim();
				if(TextUtils.isEmpty(tag))
					Util.tst(mContext, "새로운 태그를 입력해주세요");
				else {
					boolean result = dataMgr.insertTag(tag);
					if(!result)
						Util.tst(mContext, "이미 있는 태그입니다");
					edit_tag.setText("");
					setTagList(refresh);
				}
			}
		});

		setTagList(initialize);

		TextView btn_ok = (TextView) mv.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.ll("확인 버튼 클릭", Util.covertArrayToString(selectedTags));
				resultListener.setResult(selectedTags, Common.TAG);
				Util.hideKeyboard(mContext, edit_tag.getApplicationWindowToken());
			}
		});

		TextView btn_cancel = (TextView) mv.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resultListener.setCancel();
				Util.hideKeyboard(mContext, edit_tag.getApplicationWindowToken());
			}
		});
		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		return mv;
	}

	private void setTagList(int type) {
		ll.removeAllViews();
		ArrayList<String> userTags = new ArrayList<String>();
		userTags = dataMgr.getDistinctTagList();

		txt_userTag = new TextView[userTags.size()];

		for(int i=0; i<userTags.size() ;i++) {
			txt_userTag[i] = new TextView(mContext);
			txt_userTag[i].setText(userTags.get(i));
			txt_userTag[i].setLayoutParams(llp);
			txt_userTag[i].setTag(i);

			if(previousData != null && previousData.contains(userTags.get(i)) || selectedTags != null && selectedTags.contains(userTags.get(i))) { //이전에 선택한 태그
				txt_userTag[i].setTextColor(getResources().getColor(R.color.point));		
				if (type == initialize)
					selectedTags.add(userTags.get(i));
			} else {
				txt_userTag[i].setTextColor(getResources().getColor(R.color.diary_content));
			}
			txt_userTag[i].setTextSize(25);
			txt_userTag[i].setOnClickListener(cl);
			ll.addView(txt_userTag[i]);
		}

	}


	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TextView tv = (TextView) v;
			String str = tv.getText().toString();

			if(selectedTags != null && selectedTags.size() != 0) { //배열에 선택된 것이 들어가 있는 경우
				if (selectedTags.contains(str)) { //이미 선택된 경우
					tv.setTextColor(getResources().getColor(R.color.diary_content));
					selectedTags.remove(str);
				} else { //처음 선택인 경우
					tv.setTextColor(getResources().getColor(R.color.point));
					selectedTags.add(str);
				}
			} else { //아무것도 선택된 것이 없는 경우
				tv.setTextColor(getResources().getColor(R.color.point));
				selectedTags.add(str);
			}

		}
	};

}
