package com.tleaf.tiary.photo;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tleaf.tiary.R;
import com.tleaf.tiary.util.Util;

/** 다이어리 에디터 뷰에서 이미지를 horizontal lisview로 보여줄 수 이미지를 바인딩 해주는 리스트뷰 어답터 **/
public class PhotoEditAdapter extends BaseAdapter {

//	private boolean isAdding = false;
	private final int max = 10;
	private Context mContext;
	private LayoutInflater infalter;
	private ArrayList<String> imagePath = new ArrayList<String>();
	private ImageLoader imageLoader;

	private ImageView img_photo;
	private ImageView img_photo_delete;


	public PhotoEditAdapter(Context context, ImageLoader imageLoader) {
		infalter = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
		this.imageLoader = imageLoader;
		// clearCache();
	}

	@Override
	public int getCount() {
		return imagePath.size();
	}

	@Override
	public String getItem(int position) {
		return imagePath.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public boolean isMaxSelected() {
		boolean isMaxSelected = false;
		if (imagePath.size() > max)
			isMaxSelected = true;
		return isMaxSelected;
	}

	public void addAll(ArrayList<String> files) {
		if (files == null && files.size() == 0) 
			return;

		for(int i=0; i < files.size(); i++) {
			Util.ll("addAll", files.get(i));
		}
		try {
			this.imagePath.clear();
			this.imagePath.addAll(files);
		} catch (Exception e) {
			e.printStackTrace();
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = infalter.inflate(R.layout.item_photo, parent, false); //null
		}

		img_photo = (ImageView) convertView.findViewById(R.id.img_user_add);

		img_photo_delete = (ImageView) convertView.findViewById(R.id.img_user_add_delete);
		img_photo_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mContext)
				.setTitle("사진 삭제")
				.setMessage("이 사진 삭제하시겠습니까?")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deletePhoto(position);
					}
				})
				.setNegativeButton("취소", null)
				.show();					
			}
		});

		try {
			imageLoader.displayImage("file://" + imagePath.get(position),
					img_photo, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					//						img_photo.setImageResource(R.drawable.no_media);
					super.onLoadingStarted(imageUri, view);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		RelativeLayout layout_delete = (RelativeLayout) convertView.findViewById(R.id.layout_img_delete);
		layout_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.tst(mContext, position + " 이미지 확대");
			}
		});

		return convertView;
	}

	private void deletePhoto(int position) {
		this.imagePath.remove(position);
		notifyDataSetChanged();
		//부르는 곳에 알려줘야
	}
	//	public void clearCache() {
	//		imageLoader.clearDiscCache();
	//		imageLoader.clearMemoryCache();
	//	}

	public void clear() {
		imagePath.clear();
		notifyDataSetChanged();
	}
	
	public ArrayList<String> arrAfterdelete() {
		return imagePath;
	}

//	public void isAdding(boolean isAdding) {
//		this.isAdding = isAdding;
//	}
}
