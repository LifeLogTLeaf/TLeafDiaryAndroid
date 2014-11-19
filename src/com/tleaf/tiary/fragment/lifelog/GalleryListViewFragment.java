package com.tleaf.tiary.fragment.lifelog;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.fragment.BaseFragment;
import com.tleaf.tiary.fragment.lifelog.adapter.LocationLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.SmsLogAdapter;

/** 갤러리 로그 리스트 뷰를 담당하는 프래그먼트 클래스 **/
public class GalleryListViewFragment extends BaseFragment {
	private Activity mContext;

	public GalleryListViewFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.fragment_gallery, container,
				false);


		return rootView;
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Diary diary = getDiaryListByType().get(position);
			// Fragment fragment = new DiaryFragment(diary);
			// MainActivity.changeFragment(fragment);
		}

	};

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}



