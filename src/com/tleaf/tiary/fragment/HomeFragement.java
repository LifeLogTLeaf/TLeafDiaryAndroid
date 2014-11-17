package com.tleaf.tiary.fragment;

import com.tleaf.tiary.R;
import com.tleaf.tiary.adapter.HomePagerAdapter;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.model.Card;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.model.MySms;
import com.tleaf.tiary.template.LogPagerAdapter;
import com.tleaf.tiary.template.LogPagerAdapter.OnItemClickLogPagerListener;
import com.tleaf.tiary.util.Util;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

/** 홈 뷰를 담당하는 홈프래금먼트 클래스
 * 메인뷰와 다이어리 리스트 뷰를 갖는다**/
public class HomeFragement extends BaseFragment {
	
	private ViewPager vp_home;
	private HomePagerAdapter homePagerAdapter;
	
	public HomeFragement() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		vp_home = (ViewPager) rootView.findViewById(R.id.viewPager_home);
		homePagerAdapter = new HomePagerAdapter(mActivity, mContext, dataMgr);
		vp_home.setAdapter(homePagerAdapter);

		return rootView;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}
