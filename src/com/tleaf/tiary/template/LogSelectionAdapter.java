package com.tleaf.tiary.template;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tleaf.tiary.R;
import com.tleaf.tiary.util.Util;

public class LogSelectionAdapter extends PagerAdapter {
	
	private LayoutInflater mInflater;
	private Context mContext;

	private ArrayList<String> imgArr;
//	private ImageLoader imageLoader;

	public LogSelectionAdapter(Context context, ArrayList<String> images, ImageLoader imageLoader) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		imgArr = images;
//		this.imageLoader = imageLoader;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean isViewFromObject(View v, Object obj) {
		return v == obj;
	}



//	@Override
//	public int getCount() {
//		return imgArr.size();
//	}
//

//
//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		((ViewPager) container).removeView((View) object);
//	}
//
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View v = mInflater.inflate(R.layout.item_photo_diary, null);
//		ImageView img = (ImageView) v.findViewById(R.id.img_photo_diary); 
//		img.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Util.tst(mContext, "뷰확대");
//			}
//		});
//
//		try {
//			imageLoader.displayImage("file://" + imgArr.get(position),
//					img, new SimpleImageLoadingListener() {
//				@Override
//				public void onLoadingStarted(String imageUri, View view) {
//					//						img_photo.setImageResource(R.drawable.no_media);
//					super.onLoadingStarted(imageUri, view);
//				}
//			});
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		((ViewPager)container).addView(v, 0);

		return v; 
	}

}



