package com.tleaf.tiary.calendar;

import android.app.Activity;
import android.view.View;

import com.sangmuk.month.AbstractVerticalPagerAdapter;
import com.sangmuk.month.MonthSelectListener;

public class MonthPagerAdapter extends AbstractVerticalPagerAdapter
{

	public MonthPagerAdapter(Activity activity, int firstDayOfWeek,
			MonthSelectListener monthSelectListener)
	{
		super(activity, firstDayOfWeek, monthSelectListener);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View newMonthView()
	{
		return new MonthView(mContext);
	}

}
