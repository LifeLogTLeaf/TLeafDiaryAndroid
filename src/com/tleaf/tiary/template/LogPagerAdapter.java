package com.tleaf.tiary.template;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.fragment.lifelog.AsyncMyLogLoad;
import com.tleaf.tiary.fragment.lifelog.adapter.BookMarkLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.CallLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.CardLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.GalleryLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.LocationLogAdapter;
import com.tleaf.tiary.fragment.lifelog.adapter.SmsLogAdapter;
import com.tleaf.tiary.model.MyLog;

/** 채팅에 로그입력시 로그들을 pagerAdapter를 통해 뷰를 바인딩 해주는 어답터 클래스 **/
public class LogPagerAdapter extends PagerAdapter {

	public interface OnItemClickLogPagerListener {
		void onClick(MyLog myLog);
	}

	private LayoutInflater mInflater;
	private Activity mActivity;
	private Context mContext;

	private ListView lv_log;
	private CallLogAdapter callAdapter;
	private SmsLogAdapter smsAdapter;
	private CardLogAdapter cardAdapter;
//	private GalleryLogAdapter galleryAdapter;
	private LocationLogAdapter locationAdapter;
	private BookMarkLogAdapter bookmarkAdapter;

	
	private String[] titleArr = {Common.STRING_CALL, Common.STRING_SMS,
			Common.STRING_CARD, Common.STRING_LOCATION, Common.STRING_BOOKMARK,
			Common.STRING_GALLERY };

	private OnItemClickLogPagerListener mOnItemClickLogPager;

	public LogPagerAdapter(Activity activity, Context context,
			OnItemClickLogPagerListener onClick) {
		mActivity = activity;
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mOnItemClickLogPager = onClick;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titleArr[position];
	}

	@Override
	public int getCount() {
		return titleArr.length;
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
		View rootView = mInflater.inflate(R.layout.item_log, null);

		lv_log = (ListView) rootView.findViewById(R.id.list_template_log);

		switch (position) {
		case 0:
			callAdapter = new CallLogAdapter(mContext, R.layout.item_call);
			new AsyncMyLogLoad(mActivity, Common.CALL, callAdapter).execute();
			lv_log.setAdapter(callAdapter);
			lv_log.setOnItemClickListener(cl);
			break;
		case 1:
			smsAdapter = new SmsLogAdapter(mContext, R.layout.item_sms);
			new AsyncMyLogLoad(mActivity, Common.SMS, smsAdapter).execute();
			lv_log.setAdapter(smsAdapter);
			lv_log.setOnItemClickListener(cl);
			break;
		case 2:
			cardAdapter = new CardLogAdapter(mContext, R.layout.item_card);
			new AsyncMyLogLoad(mActivity, Common.CARD, cardAdapter).execute();
			lv_log.setAdapter(cardAdapter);
			lv_log.setOnItemClickListener(cl);
			break;
		case 3:
			locationAdapter = new LocationLogAdapter(mContext, R.layout.item_location);
			new AsyncMyLogLoad(mActivity, Common.LOCATION, locationAdapter).execute();
			lv_log.setAdapter(locationAdapter);
			lv_log.setOnItemClickListener(cl);
			break;
		case 4:
			bookmarkAdapter = new BookMarkLogAdapter(mContext, R.layout.item_bookmark);
			new AsyncMyLogLoad(mActivity, Common.BOOKMARK, bookmarkAdapter).execute();
			lv_log.setAdapter(bookmarkAdapter);
			lv_log.setOnItemClickListener(cl);
			break;
		case 5:
//			galleryAdapter = new GalleryLogAdapter(mContext, R.layout.item_gallery);
//			new AsyncMyLogLoad(mActivity, Common.GALLERY, cardAdapter).execute();
//			lv_log.setAdapter(cardAdapter);
//			lv_log.setOnItemClickListener(cl);
			break;
		}
		rootView.setTag(position);
		((ViewPager) container).addView(rootView);
		// (rootView, 0); //확인

		return rootView;
	}

	private AdapterView.OnItemClickListener cl = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mOnItemClickLogPager.onClick((MyLog) parent.getItemAtPosition(position));
		}

	};

}
