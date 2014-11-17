package com.tleaf.tiary.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tleaf.tiary.R;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.SemiRoundImageView;
import com.tleaf.tiary.util.Util;

/** 넘겨받은 다이어리 array로 다이어리 리스트뷰를 채우는 리스트뷰 어답터 **/
public class DiaryListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Diary> arrItem;
	private int mLayout;
	private ImageLoader imageLoader;

	public DiaryListAdapter(Context context, int layout) {
		mContext = context;
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arrItem = new ArrayList<Diary>();
		mLayout = layout;
	}

	public DiaryListAdapter(Context context, int layout, ArrayList<Diary> item) {
		mContext = context;
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arrItem = item;
		mLayout = layout;

		if (item == null)
			arrItem = new ArrayList<Diary>();
	}

	public int getCount() {
		return arrItem.size();
	}

	public Diary getItem(int position) {
		return arrItem.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void updateItem(ArrayList<Diary> diaryArr) {
		Util.ll("DiaryListAdapter updateItem diaryArr", diaryArr.size());
		arrItem.clear();
		arrItem.addAll(diaryArr);
		this.notifyDataSetChanged();
	}

	//	String table_diary = "create table diary (no integer primary key autoincrement, " +
	//			"date integer, " +
	//			"title text, " +
	//			"content text, " +
	//			"emotion text, " +
	//			"images text, " +
	//			"tags text, " +
	//			"folders text, " +
	//			"location text, " +
	//			"todayWeather text, " +
	//			"temperature real, " +
	//			"humidity real)";

	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		if (convertView == null) {
			convertView = mInflater.inflate(mLayout, parent, false);
		}

		TextView txt_date = (TextView)convertView.findViewById(R.id.item_txt_diary_date);
		String dateStr = MyTime.getLongToString(mContext, arrItem.get(position).getDate());
		txt_date.setText(dateStr);
		
		SemiRoundImageView img = (SemiRoundImageView) convertView.findViewById(R.id.item_img_diary);
		ArrayList<String> imgArr = arrItem.get(position).getImages();

		initImageLoader();
		if (imgArr != null && imgArr.size() != 0) {
			//			img.setVisibility(View.VISIBLE);
			try {
				imageLoader.displayImage("file://" + imgArr.get(0),
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
		} else {
//						img.setVisibility(View.GONE);
		}

		TextView txt_title = (TextView)convertView.findViewById(R.id.item_txt_diary_title);
		txt_title.setText(arrItem.get(position).getTitle());

		TextView txt_content = (TextView)convertView.findViewById(R.id.item_txt_diary_content);
		txt_content.setText(arrItem.get(position).getContent());


		//사용자에게 더 있음을 알려주는 ui
		ImageView img_tag = (ImageView)convertView.findViewById(R.id.item_img_diary_tag);

		TextView txt_tag = (TextView)convertView.findViewById(R.id.item_txt_diary_tag);
		ArrayList<String> tags = arrItem.get(position).getTags();
		if(tags != null && tags.size() != 0) {
			txt_tag.setVisibility(View.VISIBLE);
			txt_tag.setText(tags.get(0));
			img_tag.setVisibility(View.VISIBLE);

		} else {
			txt_tag.setVisibility(View.GONE);
			img_tag.setVisibility(View.GONE);
		}

		ImageView img_folder = (ImageView)convertView.findViewById(R.id.item_img_diary_folder);

		TextView txt_folder = (TextView)convertView.findViewById(R.id.item_txt_diary_folder);
		ArrayList<String> folders = arrItem.get(position).getFolders();
		if(folders != null && folders.size() != 0) {
			txt_folder.setVisibility(View.VISIBLE);
			txt_folder.setText(folders.get(0)); 
			img_folder.setVisibility(View.VISIBLE);
		} else { 
			txt_folder.setVisibility(View.GONE);
			img_folder.setVisibility(View.GONE);
		}

		ImageView img_location = (ImageView)convertView.findViewById(R.id.item_img_diary_location);

		TextView txt_location = (TextView)convertView.findViewById(R.id.item_txt_diary_location);
		String location = arrItem.get(position).getLocation().trim();
		if(location != null && !location.equals("null") && !location.equals("")) {
			txt_location.setVisibility(View.VISIBLE);
			txt_location.setText(arrItem.get(position).getLocation());
			img_location.setVisibility(View.VISIBLE);

		} else {
			txt_location.setVisibility(View.GONE);
			img_location.setVisibility(View.GONE);
		}


		return convertView;
	}

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
}
