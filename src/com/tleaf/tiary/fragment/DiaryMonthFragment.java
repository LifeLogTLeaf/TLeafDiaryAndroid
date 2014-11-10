package com.tleaf.tiary.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sangmuk.month.MonthSelectListener;
import com.sangmuk.month.ViewPager;
import com.sangmuk.month.ViewPager.OnPageChangeListener;
import com.tleaf.tiary.R;
import com.tleaf.tiary.calendar.MonthPagerAdapter;

public class DiaryMonthFragment extends Fragment {

	private Context mContext;
	private Activity mActivity;
	
	public DiaryMonthFragment(Activity activity) {
		mActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_month_diary_list, container, false);

		ViewPager viewpager = (ViewPager) rootView.findViewById(R.id.viewpager_month);
		final MonthPagerAdapter adapter = new MonthPagerAdapter(mActivity, Time.SUNDAY, new MonthSelectListener() {

			@Override
			public void selectDay(int julianDay)
			{

			}

			@Override
			public void longClickDay(long milliSecond)
			{

			}
		});
		
		Time time = new Time(Time.getCurrentTimezone());
		time.setToNow();

		final TextView txt = (TextView)rootView.findViewById(R.id.txt_month_title);
		txt.setText(time.format("%m"));
		
		adapter.setCurrentJulianDay(Time.getJulianDay(time.toMillis(false),
				time.gmtoff));
		viewpager.setAdapter(adapter);
		viewpager.setCurrentItem(adapter.getCurrentPosition());
		viewpager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int position)
			{
				Time time = MonthPagerAdapter.getTime(position);
				txt.setText(time.format("%m"));
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
				// TODO Auto-generated method stub

			}
		});

		return rootView;

	}


}
