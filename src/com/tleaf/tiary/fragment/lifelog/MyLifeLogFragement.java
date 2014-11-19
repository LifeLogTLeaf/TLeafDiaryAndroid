package com.tleaf.tiary.fragment.lifelog;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tleaf.tiary.Common;
import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.fragment.BaseFragment;

/** 메뉴 중 마이 라이프 로그 뷰를 담당하는 프래그먼트 클래스
 * 여러 로그 프래그먼트를 연결해준다 **/
public class MyLifeLogFragement extends BaseFragment {

	private RelativeLayout layout_call;
	private RelativeLayout layout_sms;
	private RelativeLayout layout_galley;
	private RelativeLayout layout_card;
	private RelativeLayout layout_bookmark;
	private RelativeLayout layout_location;

	public MyLifeLogFragement() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mylifelog, container, false);


		layout_call = (RelativeLayout) rootView.findViewById(R.id.layout_call);
		layout_call.setOnClickListener(cl);
		layout_sms = (RelativeLayout) rootView.findViewById(R.id.layout_sms);
		layout_sms.setOnClickListener(cl);
		layout_galley = (RelativeLayout) rootView.findViewById(R.id.layout_gallery);
		layout_galley.setOnClickListener(cl);
		layout_card = (RelativeLayout) rootView.findViewById(R.id.layout_card);
		layout_card.setOnClickListener(cl);
		layout_bookmark = (RelativeLayout) rootView.findViewById(R.id.layout_bookmark);
		layout_bookmark.setOnClickListener(cl);
		layout_location = (RelativeLayout) rootView.findViewById(R.id.layout_location);
		layout_location.setOnClickListener(cl);


		return rootView;
	}

	/** 사용자가 선택한 로그 아이콘에 따른 로그 프래그먼트를 보여주는 메서드 **/
	private OnClickListener cl = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Fragment fragment = null;
			switch(v.getId()) {
			case R.id.layout_call:
				fragment = new CallListViewFragment();//CallListViewFragment
				break;
			case R.id.layout_sms:
				fragment = new SmsListViewFragment();
				break;
			case R.id.layout_gallery:
				fragment = new GalleryListViewFragment();
				break;
			case R.id.layout_card:
				fragment = new CardListViewFragment();
				break;
			case R.id.layout_bookmark:
				fragment = new BookMarkListView();
				break;
			case R.id.layout_location:
				fragment = new LocationListView();
				break;
			}
			MainActivity.changeFragment(fragment);
		}
	};

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
}
