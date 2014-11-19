package com.tleaf.tiary.fragment;

import com.tleaf.tiary.db.DataManager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

/** 프래그먼트가 공통적인 정보를 갖는 상위 클래스로서 모든 프래그먼트가 상속한다 **/
public abstract class BaseFragment extends Fragment {
	protected Activity mActivity;
	protected Context mContext;
	protected DataManager dataMgr;

	// protected ActivityInterface mInterface;
	// protected MyPreference mPref;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		mContext = activity.getApplicationContext();
		dataMgr = new DataManager(mContext);
		
		// mPref = PreferenceSmartCalendar.getInstance(mContext);
		//
		// if (mActivity instanceof ActivityInterface) {
		// mInterface = (ActivityInterface) mActivity;
		// }
	}

	abstract public boolean onBackPressed();

}
