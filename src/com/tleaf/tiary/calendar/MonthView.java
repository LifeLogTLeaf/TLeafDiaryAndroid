package com.tleaf.tiary.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.text.TextPaint;

import com.sangmuk.month.AbstractMonthView;
import com.tleaf.tiary.R;

public class MonthView extends AbstractMonthView
{

	public MonthView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setEventClear()
	{

	}

	@Override
	protected void initData()
	{
		data = new MonthData(mContext, new Rect(), new RectF(), new Paint(),
				new TextPaint());
	}

	@Override
	protected void initItems()
	{
		data.setColor(Color.RED, Color.BLUE, Color.RED, Color.BLUE,
				Color.BLACK, Color.BLACK, Color.GRAY);
		data.setSize(
				(int) mResource.getDimension(R.dimen.text_size_month_week),
				(int) mResource.getDimension(R.dimen.height_day_of_week),
				mResource.getDimensionPixelSize(R.dimen.text_size_month),
				(int) mResource.getDimension(R.dimen.height_month_item),
				mResource.getDimension(R.dimen.padding_item),
				mResource.getDimension(R.dimen.padding_item), 0);

		data.setDrawable(new ColorDrawable(getResources().getColor(R.color.background_white)), new ColorDrawable(
				getResources().getColor(R.color.background_skyblue_dark)), new ColorDrawable(getResources().getColor(R.color.background_skyblue)), new ColorDrawable(
						getResources().getColor(R.color.background_grey)), new ColorDrawable(Color.TRANSPARENT),
						new ColorDrawable(Color.TRANSPARENT));
	}

}
