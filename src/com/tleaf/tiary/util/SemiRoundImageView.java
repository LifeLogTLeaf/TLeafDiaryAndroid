package com.tleaf.tiary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tleaf.tiary.R;

public class SemiRoundImageView  extends ImageView
{

	public SemiRoundImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public SemiRoundImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public SemiRoundImageView(Context context)
	{
		super(context);
		init(context);
	}

	private void init(Context context)
	{
		setBackgroundResource(R.drawable.shape);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// super.onDraw(canvas);
		Drawable drawable = getDrawable();
		if (drawable == null || ((BitmapDrawable) drawable).getBitmap() == null)
		{
			super.onDraw(canvas);
			return;
		}

		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
		int w = getWidth(), h = getHeight();

		int pixels = (int) (3 * getResources().getDisplayMetrics().density);

		Bitmap roundBitmap = RoundImageView.getRoundedCornerBitmap(
				getContext(), bitmap, pixels, w, h, false, false, true, true);
		canvas.drawBitmap(roundBitmap, 0, 0, null);
	}

	public static Bitmap getRoundedCornerBitmap(Context context, Bitmap input,
			int pixels, int w, int h, boolean squareTL, boolean squareTR,
			boolean squareBL, boolean squareBR)
	{

		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final float densityMultiplier = context.getResources()
				.getDisplayMetrics().density;

		final int color = context.getResources().getColor(
				R.color.background_skyblue);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);

		// make sure that our rounded corner is scaled appropriately
		final float roundPx = pixels * densityMultiplier;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		// draw rectangles over the corners we want to be square
		if (squareTL)
		{
			canvas.drawRect(0, 0, w / 2, h / 2, paint);
		}
		if (squareTR)
		{
			canvas.drawRect(w / 2, 0, w, h / 2, paint);
		}
		if (squareBL)
		{
			canvas.drawRect(0, h / 2, w / 2, h, paint);
		}
		if (squareBR)
		{
			canvas.drawRect(w / 2, h / 2, w, h, paint);
		}

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		int sourceWidth = input.getWidth();
		int sourceHeight = input.getHeight();
		float xScale = (float) w / sourceWidth;
		float yScale = (float) h / sourceHeight;
		float scale;// = Math.max(xScale, yScale);

		scale = Math.max(xScale, yScale);

		// Now get the size of the source bitmap when scaled
		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		// Let's find out the upper left coordinates if the scaled bitmap
		// should be centered in the new size give by the parameters
		float left = (w - scaledWidth) / 2;
		float top = (h - scaledHeight) / 2;

		// The target rectangle for the new, scaled version of the source bitmap
		// will now
		// be
		RectF targetRect = new RectF(left, top, left + scaledWidth, top
				+ scaledHeight);

		// Finally, we create a new bitmap of the specified size and draw our
		// new,
		// scaled bitmap onto it.
		canvas.drawBitmap(input, null, targetRect, paint);

		// Bitmap scaled = Bitmap.createScaledBitmap(input, w, h, true);
		// canvas.drawBitmap(scaled, 0, 0, paint);

		return output;
	}
}