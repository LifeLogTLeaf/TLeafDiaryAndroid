package com.tleaf.tiary.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.adapter.ImagePagerAdapter;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

/** 다이어리를 보여주는 프래그먼트를 담당하는 클래스 **/
public class DiaryFragment extends BaseFragment {

	private Diary diary;
	private Context mContext;
	private DataManager dataMgr;

	private Fragment fragment;

	private ViewPager mPager;
	private ImageLoader imageLoader;
	private ImageView[] img_moving;
	private LinearLayout ll;
	private ImagePagerAdapter adapter;

	public DiaryFragment(Diary diary) {
		this.diary = diary;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_diary, container, false);

		mContext = getActivity();
		dataMgr = new DataManager(mContext);

		Util.ll("DiaryFragment setDate", diary.getDate());


		TextView txt_date = (TextView)rootView.findViewById(R.id.txt_diary_date);

		String dateStr = MyTime.getLongToString(mContext, diary.getDate());
		txt_date.setText(dateStr);

		TextView txt_date_time = (TextView)rootView.findViewById(R.id.txt_diary_date_time);

		String dateTimeStr = MyTime.getLongToOnlyTime(mContext, diary.getDate());
		txt_date_time.setText(dateTimeStr);

		ImageView img_emo = (ImageView) rootView.findViewById(R.id.img_edit_emotion);

		if(diary.getEmotion() != null) {
			int index = Common.getIndexByEmomtionName(diary.getEmotion());
			img_emo.setImageResource(getResources().getIdentifier(
					"emo" + (index + 1), "drawable",
					mContext.getPackageName()));
		}
		TextView txt_title = (TextView) rootView.findViewById(R.id.txt_diary_title);
		txt_title.setText(diary.getTitle());

		TextView txt_content = (TextView) rootView.findViewById(R.id.txt_diary_content);
		txt_content.setText(diary.getContent());

		TextView txt_tag = (TextView) rootView.findViewById(R.id.txt_diary_tag);
		ArrayList<String> tags = diary.getTags();
		if(tags != null && tags.size() != 0) {
			Util.ll("tags.size()", tags.size());
			Util.ll("Util.covertArrayToString(tags)", Util.covertArrayToString(tags));

			txt_tag.setText(Util.covertArrayToString(tags)); 
		}

		TextView txt_folder = (TextView) rootView.findViewById(R.id.txt_diary_folder);
		ArrayList<String> folders = diary.getFolders();
		if(folders != null && folders.size() != 0) {
			Util.ll("folders.size()", folders.size());
			Util.ll("Util.covertArrayToString(folders)", Util.covertArrayToString(folders));
			txt_folder.setText(Util.covertArrayToString(folders)); 
		}

		TextView txt_location = (TextView) rootView.findViewById(R.id.txt_diary_location);
		String location = diary.getLocation();
		if(location != null && !location.equals("null") && !location.equals(""))
			txt_location.setText(diary.getLocation());

		String tagStr = txt_tag.getText().toString();
		String tagFolder = txt_folder.getText().toString();
		String tagLocation = txt_location.getText().toString();

		LinearLayout layout_info = (LinearLayout) rootView
				.findViewById(R.id.layout_diary_user_add_info);
		if ((tagStr != null && !tagStr.isEmpty())
				|| (tagFolder != null && !tagFolder.isEmpty())
				|| (tagLocation != null && !tagLocation.isEmpty())) {
			layout_info.setVisibility(View.VISIBLE);
		} else {
			layout_info.setVisibility(View.GONE);
		}


		ImageView img_modify = (ImageView) rootView
				.findViewById(R.id.img_diary_modify);
		img_modify.setOnClickListener(cl);

		ImageView img_delete = (ImageView) rootView
				.findViewById(R.id.img_diary_delete);
		img_delete.setOnClickListener(cl);

		//		ImageView img_share = (ImageView) rootView
		//				.findViewById(R.id.img_diary_share);
		//		img_share.setOnClickListener(cl);
		//
		//		ImageView img_shack = (ImageView) rootView
		//				.findViewById(R.id.img_diary_shack);
		//		img_shack.setOnClickListener(cl);


		mPager = (ViewPager) rootView.findViewById(R.id.viewpager);

		ll = (LinearLayout) rootView.findViewById(R.id.layout_moving);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(20, 20);
		llp.setMargins(20, 20, 20, 20);

		RelativeLayout rl =  (RelativeLayout) rootView.findViewById(R.id.layout_diary_image);
		if (diary.getImages() != null & diary.getImages().size() > 0) {
			initImageLoader();
			adapter = new ImagePagerAdapter(mContext, diary.getImages(), imageLoader);
			mPager.setAdapter(adapter);
			mPager.setOnPageChangeListener(mPageListener);
			
			rl.setVisibility(View.VISIBLE);
			int size = diary.getImages().size();
			img_moving = new ImageView[size];
			for (int i = 0; i < size; i++) {
				img_moving[i] = new ImageView(mContext);
				img_moving[i].setLayoutParams(llp);
				img_moving[i].setImageResource(R.drawable.moving);
				img_moving[i].setColorFilter(R.color.bottom_menu);
				img_moving[i].setAlpha(0.5f);
				ll.addView(img_moving[i]);
			}

		}
		return rootView;

	}

	/** 이미지를 담고 있는 뷰페이저가 이동했을 때 하단에 이미지뷰 속성을 변경하는 메서드 **/
	private SimpleOnPageChangeListener mPageListener = new SimpleOnPageChangeListener() {
		public void onPageSelected(int position) {
			//			adapter.getItemPosition(object);
			if (position == 0) //현재 포지션이면
				img_moving[position].setAlpha(0);
			else 
				img_moving[position].setAlpha(0.5f);
		};
	};

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

	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			onClickMenu(v.getId());
		}
	};

	/** 다이어리 하단에 수정 또는 삭제 클릭을 받는 메서드 **/
	private void onClickMenu(int viewId) {
		switch (viewId) {
		case R.id.img_diary_modify:
			Util.ll("일기 보기 id", diary.getNo());
			fragment = new DiaryEditFragment(diary);
			MainActivity.changeFragment(fragment);
			break;
		case R.id.img_diary_delete:
			new AlertDialog.Builder(mContext)
			.setTitle("일기 삭제")
			.setMessage("이 일기를 삭제하시겠습니까?")
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Boolean result = dataMgr.deleteDiary(diary.getNo());
					if(result) {
						fragment = new DiaryListViewFragement();
						MainActivity.changeFragment(fragment);
					} else {
						//예외처리
					}
				}
			})
			.setNegativeButton("취소", null)
			.show();


			break;
			//		case R.id.img_diary_share:
			//			Util.tst(mContext, "공유하기");
			//			break;
			//		case R.id.img_diary_shack:
			//			Util.tst(mContext, "shack으로 보내기(관련처리)");
			//			break;

		}

	}


	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
}
