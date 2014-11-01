package com.tleaf.tiary.dialog;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.util.Util;


public class LocationDialogFragment extends DialogFragment {

	//	private final int initialize = 1;
	//	private final int refresh = 2;

	private DialogResultListener resultListener;
	private int dateType;
	private String previousData;

	private Context mContext;
	private DataManager dataMgr;

	private EditText edit_location;



	private TextView[] txt_userLocation;

	private int selectedIndex = -1;
	private String selectLocation;

	private LinearLayout ll;
	private LinearLayout.LayoutParams llp;

	public static LocationDialogFragment newInstace(DialogResultListener resultListener, int dataType, String previousData) {
		LocationDialogFragment fragment = new LocationDialogFragment();
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
		View mv = inflater.inflate(R.layout.dialog_location, container, false);
		mContext = getActivity();
		dataMgr = new DataManager(mContext);

		ll = (LinearLayout) mv.findViewById(R.id.layout_locationDialog_txt);
		llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(5, 5, 5, 5);


		edit_location = (EditText) mv.findViewById(R.id.edit_dialog_location);
		edit_location.setSelection(edit_location.getText().length());
		if(previousData != null && !previousData.equals("")) {
			selectLocation = previousData;
			edit_location.setText(previousData);
		}
		edit_location.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String editedText = s.toString();
				if (editedText != null && editedText.equals("") && selectedIndex != -1) { //사용자가 리스트에 있는 장소를 에디트텍스트에서 지웠을 경우
					txt_userLocation[selectedIndex].setTextColor(getResources().getColor(R.color.diary_content));
				}  
				//						else if (editedText != null && editedText.length() > 0) { //사용자가 에디트텍스트에서 새로운 내용을 입력했을 경우
				//							setLocationList(editedText);
				//						}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		//사용자가 입력을 다 지운 경우 -> 리스트에서 해제
		//사용자가 입력을 완료한 경우 -> 리스트에서 표시 => 잘안쓰는 기
		//		edit_location.setOnEditorActionListener(new OnEditorActionListener() {
		//			@Override
		//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		//				EditText et = (EditText) v;
		//				if (actionId == EditorInfo.IME_ACTION_NONE) { //사용자가 입력을 지움
		//					Util.tst(mContext, "입력을 지움");
		//					if (selectedIndex != -1) { //리스트에서 선택한 위치을 지움
		//						txt_userLocation[selectedIndex].setTextColor(getResources().getColor(R.color.diary_content));
		//					}
		//				} else if (actionId == EditorInfo.IME_ACTION_DONE) { //사용자가 새롭게 입력을 함
		//					Util.tst(mContext, "입력 완료");
		//					if (selectedIndex == -1) { //리스트에서 선택한 항목이 아닌 새로운 위치를 입력 
		//						setLocationList(et.getText().toString());
		//					}
		//				}
		//				return false;
		//			}
		//		});


		setLocationList(null);

		TextView btn_ok = (TextView) mv.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String location = edit_location.getText().toString().trim();
				resultListener.setResult(location);
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

	private void setLocationList(String location) {
		ll.removeAllViews();
		ArrayList<String> userLocations = new ArrayList<String>();
		userLocations = dataMgr.getDistinctLocationList();

		txt_userLocation = new TextView[userLocations.size()];

		for(int i=0; i<userLocations.size() ;i++) {
			txt_userLocation[i] = new TextView(mContext);
			txt_userLocation[i].setText(userLocations.get(i));
			txt_userLocation[i].setLayoutParams(llp);
			txt_userLocation[i].setTag(i);

			if(selectLocation != null && selectLocation.equals(userLocations.get(i)) || location != null && location.equals(userLocations.get(i))) { 
				selectedIndex = i;
				txt_userLocation[i].setTextColor(getResources().getColor(R.color.point));
				Integer index = Integer.valueOf(i);
				txt_userLocation[i].setTag(index);
				edit_location.setText(selectLocation);
			} else {
				txt_userLocation[i].setTextColor(getResources().getColor(R.color.diary_content));
			}
			txt_userLocation[i].setTextSize(25);
			txt_userLocation[i].setOnClickListener(cl);
			ll.addView(txt_userLocation[i]);
		}

	}

	//	if(previousData != null && previousData.equals(userLocations.get(i))) {// || selectLocation != null && selectLocation.equals(userLocations.get(i))) { 
	//		txt_userLocation[i].setTextColor(getResources().getColor(R.color.point));		
	//		if (type == initialize) {
	//			selectLocation = userLocations.get(i);
	//		} 
	//		edit_location.setText(selectLocation);
	//	} else {
	//		txt_userLocation[i].setTextColor(getResources().getColor(R.color.diary_content));
	//	}



	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TextView tv = (TextView) v;
			String str = tv.getText().toString().trim();
			int index = (Integer) tv.getTag();
			if(selectLocation != null && !selectLocation.equals("") && selectedIndex != -1) { 
				if(selectLocation.equals(str) && selectedIndex == index) { //동일한 것을 선택 -> 선택해제
					tv.setTextColor(getResources().getColor(R.color.diary_content));
					selectedIndex = -1;
					selectLocation = "";
					edit_location.setText(""); 
				} else { //선택한 위치가 있는 데 새롭게 선택
					txt_userLocation[selectedIndex].setTextColor(getResources().getColor(R.color.diary_content));
					tv.setTextColor(getResources().getColor(R.color.point));
					selectedIndex = index;
					selectLocation = str;
					edit_location.setText(selectLocation); //해줘야하나
				}
			} else { //아무것도 선택된 것이 없는 경우
				tv.setTextColor(getResources().getColor(R.color.point));
				selectedIndex = index;
				selectLocation = str;
				edit_location.setText(selectLocation); //해줘야하나
			}

		}
	};

}


//if (!select) { //셀렉트
//tv.setTextColor(getResources().getColor(R.color.point));
//if (!previousData.contains(tv.getText().toString())) {
//	if (previousData == null && previousData.size() == 0)  
//		previousData = new ArrayList<String>();
//	previousData.add(tv.getText().toString());
//	select = true;
//}
//			} else { //셀렉트해제
//				tv.setTextColor(getResources().getColor(R.color.text_gray_custom));
//				previousData.remove(tv.getText());
//				select = false;
//			}
//			edit_folder.setText(Util.covertArrayToString(previousData));
