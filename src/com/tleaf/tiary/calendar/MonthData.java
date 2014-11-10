package com.tleaf.tiary.calendar;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

import com.sangmuk.month.AbstractMonthData;

public class MonthData extends AbstractMonthData
{

	public MonthData(Context context, Rect mRect, RectF mRectF, Paint mPaint,
			TextPaint textPaint)
	{
		super(context, mRect, mRectF, mPaint, textPaint);
	}

}
