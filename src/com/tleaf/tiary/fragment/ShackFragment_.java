package com.tleaf.tiary.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tleaf.tiary.R;
import com.tleaf.tiary.model.MyGallery;
import com.tleaf.tiary.photo.GalleryAdapter_;
import com.tleaf.tiary.util.Util;


public class ShackFragment_ extends Fragment {

	private GridView gridGallery;
	private Handler handler;
	private GalleryAdapter_ adapter;

	private ImageView imgSinglePick;
	private Button btnGalleryPickMul;

	private String action;
	private ViewSwitcher viewSwitcher;
	private ImageLoader imageLoader;

	private Context mContext;
	private View rootView;


	public ShackFragment_() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_shack, container, false);

		mContext = getActivity();
//		initImageLoader();
//		init();

		return rootView;
	}

/*
	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				mContext).defaultDisplayImageOptions(defaultOptions).memoryCache(
						new WeakMemoryCache());

		ImageLoaderConfiguration config = builder.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}

	private void init() {

		handler = new Handler();
//		gridGallery = (GridView) rootView.findViewById(R.id.gridGallery);
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(mContext, imageLoader);
		adapter.setMultiplePick(false);
		gridGallery.setAdapter(adapter);

//		viewSwitcher = (ViewSwitcher) rootView.findViewById(R.id.viewSwitcher);
//		viewSwitcher.setDisplayedChild(1);

		btnGalleryPickMul = (Button) rootView.findViewById(R.id.btnGalleryPickMul);
		btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent("luminous.ACTION_MULTIPLE_PICK");
				startActivityForResult(i, 200);
			}
		});

//		imgSinglePick = (ImageView) rootView.findViewById(R.id.imgSinglePick);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
			Util.tst(mContext, data.getStringExtra("type"));
			String type = data.getStringExtra("type");
			if (type.equals("allPath")) {
				String[] all_path = data.getStringArrayExtra("all_path");

				ArrayList<MyGallery> dataT = new ArrayList<MyGallery>();

				for (String string : all_path) {
					MyGallery item = new MyGallery();
					item.sdcardPath = string;

					dataT.add(item);
				}

				viewSwitcher.setDisplayedChild(0);
				adapter.addAll(dataT);
			} else if (type.equals("crop")) {
				Util.tst(mContext, "crop");
				String path = data.getStringExtra("path");
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				
				gridGallery.setVisibility(View.INVISIBLE);
				imgSinglePick.setVisibility(View.VISIBLE);
				imgSinglePick.setImageBitmap(bitmap);
				adapter.clear();

				viewSwitcher.setDisplayedChild(1);
				String single_path = data.getStringExtra("single_path");
				imageLoader.displayImage("file://" + path, imgSinglePick);
			}
		}
	}*/
}
