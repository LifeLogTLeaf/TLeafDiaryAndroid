package com.tleaf.tiary.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.common.api.Releasable;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.fragment.DiaryFragment;
import com.tleaf.tiary.fragment.lifelog.BookMarkListView;
import com.tleaf.tiary.fragment.lifelog.CallListViewFragment;
import com.tleaf.tiary.fragment.lifelog.CardListViewFragment;
import com.tleaf.tiary.fragment.lifelog.GalleryListViewFragment;
import com.tleaf.tiary.fragment.lifelog.LocationListView;
import com.tleaf.tiary.fragment.lifelog.SmsListViewFragment;
import com.tleaf.tiary.fragment.lifelog.adapter.CallLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.CardLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.SmsLogAdapter;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.template.LogPagerAdapter.OnItemClickLogPagerListener;
import com.tleaf.tiary.util.Util;

/** 홈메뉴에 해당되는 홈프래그먼트에서 뷰페이져 뷰에 해당 뷰들을 연결하는 pager adapter **/
public class HomePagerAdapter extends PagerAdapter {
	
//	public interface OnItemClickHomePagerListener {
//		void onClick();
//	}

	private LayoutInflater mInflater;
	private Activity mActivity;
	private Context mContext;

	private ListView lv_log;
	private CallLogAdapter callAdapter;
	private SmsLogAdapter smsAdapter;
	private CardLogAdapter cardAdapter;


	private ArrayList<Diary> arItem;
	private DataManager dataMgr;

//	private OnItemClickLogPagerListener mOnItemClickhomePager;
	
	public HomePagerAdapter(Activity activity, Context context, DataManager dataMgr) {
		mActivity = activity;
		mContext = context;
		mInflater = LayoutInflater.from(context);
		this.dataMgr = dataMgr;
//		this.mOnItemClickhomePager = mOnItemClickhomePager;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public boolean isViewFromObject(View v, Object obj) {
		return v == obj;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int view = ( position == 0 ) ? R.layout.item_home : R.layout.fragment_list; 
		View rootView = mInflater.inflate(view, null);

		switch (position) {
		case 0:
			rootView = setFirstLayout(rootView);
			break;
		case 1:
			rootView = setSecondLayout(rootView);
			break;
		}

		rootView.setTag(position);
		((ViewPager) container).addView(rootView);
		// (rootView, 0); //확인

		return rootView;
	}

	/** 첫번째 뷰를 얻는 메서드 **/
	private View setFirstLayout(View rootView) {
		ImageView img = (ImageView) rootView.findViewById(R.id.img_background);

		TextView txt_call = (TextView) rootView.findViewById(R.id.txt_cnt_call);
		txt_call.setText(""+dataMgr.getLogCountByType(Common.STRING_CALL));
		RelativeLayout rl_call = (RelativeLayout) rootView.findViewById(R.id.rl_home_call);
		rl_call.setOnClickListener(logClick);

		//		txt_call.setOnClickListener(logClickListener);

		TextView txt_sms = (TextView) rootView.findViewById(R.id.txt_cnt_sms);
		txt_sms.setText(""+dataMgr.getLogCountByType(Common.STRING_SMS));
		RelativeLayout rl_sms = (RelativeLayout) rootView.findViewById(R.id.rl_home_sms);
		rl_sms.setOnClickListener(logClick);
		
		//		txt_sms.setOnClickListener(logClickListener);


		TextView txt_card = (TextView) rootView.findViewById(R.id.txt_cnt_card);
		txt_card.setText(""+dataMgr.getLogCountByType(Common.STRING_CARD));
		RelativeLayout rl_card = (RelativeLayout) rootView.findViewById(R.id.rl_home_card);
		rl_card.setOnClickListener(logClick);
		
		//		txt_card.setOnClickListener(logClickListener);


		TextView txt_gallery = (TextView) rootView.findViewById(R.id.txt_cnt_gallery);
		RelativeLayout rl_gallery = (RelativeLayout) rootView.findViewById(R.id.rl_home_gallery);
		rl_gallery.setOnClickListener(logClick);
		
		//		txt_gallery.setText(""+dataMgr.getLogCountByType(Common.STRING_GALLERY));
		//		txt_gallery.setOnClickListener(logClickListener);


		TextView txt_location = (TextView) rootView.findViewById(R.id.txt_cnt_location);
		RelativeLayout rl_location = (RelativeLayout) rootView.findViewById(R.id.rl_home_location);
		rl_location.setOnClickListener(logClick);
		
		//		txt_location.setText(""+dataMgr.getLogCountByType(Common.STRING_LOCATION));
		//		txt_location.setOnClickListener(logClickListener);


		TextView txt_bookmark = (TextView) rootView.findViewById(R.id.txt_cnt_bookmark);
		RelativeLayout rl_bookmark = (RelativeLayout) rootView.findViewById(R.id.rl_home_bookmark);
		rl_bookmark.setOnClickListener(logClick);
		
		//		txt_bookmark.setText(""+dataMgr.getLogCountByType(Common.STRING_BOOKMARK));
		//		txt_bookmark.setOnClickListener(logClickListener);
		LinearLayout ll_next = (LinearLayout) rootView.findViewById(R.id.item_layout_next);
		ll_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.tst(mContext, "swipe!");
			}
		});


		return rootView;
	}

	/** 첫번째 뷰에서 각 로그 레이아웃에 클릭 이벤트를 받은 온클릭리스너 **/
	private View.OnClickListener logClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Fragment fragment = null;
			switch(v.getId()) {
			case R.id.rl_home_call:
				fragment = new CallListViewFragment();//CallListViewFragment
				break;
			case R.id.rl_home_sms:
				fragment = new SmsListViewFragment();
				break;
			case R.id.rl_home_gallery:
				fragment = new GalleryListViewFragment();
				break;
			case R.id.rl_home_card:
				fragment = new CardListViewFragment();
				break;
			case R.id.rl_home_bookmark:
				fragment = new BookMarkListView();
				break;
			case R.id.rl_home_location:
				fragment = new LocationListView();
				break;
			}
			MainActivity.changeFragment(fragment);			

		}
	};


	/** 두번째 뷰를 얻는 메서드 **/
	private View setSecondLayout(View rootView) {
		ListView lv = (ListView) rootView.findViewById(R.id.list_gen);
		LinearLayout ll = (LinearLayout) rootView
				.findViewById(R.id.ll_no_diary);

		if (getDiaryListByType().size() == 0) {
			lv.setVisibility(View.GONE);
			ll.setVisibility(View.VISIBLE);
		} else {
			lv.setVisibility(View.VISIBLE);
			ll.setVisibility(View.GONE);
		}
		DiaryListAdapter mAdapter = new DiaryListAdapter(mContext,
				R.layout.item_diary_, getDiaryListByType()); // this.getActivity()
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(mItemClickListener);
		return rootView;
	}

	
	private ArrayList<Diary> getDiaryListByType() {
		arItem = new ArrayList<Diary>(); 
		arItem = dataMgr.getDiaryList();
		return arItem;

	}

	/** 두번째 뷰에서 다이어리 아이템 클릭을 받는 클릭 리스너 **/
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Diary diary = getDiaryListByType().get(position);
			Fragment fragment = new DiaryFragment(diary);
			MainActivity.changeFragment(fragment);
		}
	};

	private AdapterView.OnItemClickListener cl = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		}

	};

}
