package com.tleaf.tiary.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tleaf.tiary.R;
import com.tleaf.tiary.util.Util;

/** 다이어리뷰에서 사용자가 선택한 여러장의 이미지를 pagerAdpater를 통해 보여준다 **/
public class ImagePagerAdapter extends PagerAdapter {
	private LayoutInflater mInflater;
	private Context mContext;

	private ArrayList<String> imgArr;
	private ImageLoader imageLoader;
	
	public ImagePagerAdapter(Context context, ArrayList<String> images, ImageLoader imageLoader) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		imgArr = images;
		this.imageLoader = imageLoader;
	}

	@Override
	public int getCount() {
		return imgArr.size();
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
		View v = mInflater.inflate(R.layout.item_photo_diary, null);
		ImageView img = (ImageView) v.findViewById(R.id.img_photo_diary); 
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.tst(mContext, "뷰확대");
			}
		});
		
		try {
			imageLoader.displayImage("file://" + imgArr.get(position),
					img, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					//						img_photo.setImageResource(R.drawable.no_media);
					super.onLoadingStarted(imageUri, view);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		((ViewPager)container).addView(v, 0);

		return v; 
	}

}



