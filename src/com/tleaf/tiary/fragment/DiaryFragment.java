package com.tleaf.tiary.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.drive.internal.m;
import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class DiaryFragment extends Fragment {

	private Diary diary;
	private Context mContext;
	private DataManager dataMgr;
	
	private Fragment fragment;
	
	
	public DiaryFragment(Diary diary) {
		this.diary = diary;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_diary, container, false);

		mContext = getActivity();
		dataMgr = new DataManager(mContext);


		TextView txt_date = (TextView)rootView.findViewById(R.id.txt_diary_date);
		String dateStr = MyTime.getLongToString(mContext, diary.getDate());
		txt_date.setText(dateStr);

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

		ImageView img_share = (ImageView) rootView
				.findViewById(R.id.img_diary_share);
		img_share.setOnClickListener(cl);

		ImageView img_shack = (ImageView) rootView
				.findViewById(R.id.img_diary_shack);
		img_shack.setOnClickListener(cl);
		return rootView;

	}


	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			onClickMenu(v.getId());
		}
	};

	private void onClickMenu(int viewId) {
		switch (viewId) {
		case R.id.img_diary_modify:
			Util.ll("일기 보기 id", diary.getNo());
			fragment = new DiaryEditFragment(diary);
			MainActivity.changeFragment(fragment);
			break;
		case R.id.img_diary_delete:
			new AlertDialog.Builder(mContext)
//			.setTitle("일기 삭제")
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
		case R.id.img_diary_share:
			Util.tst(mContext, "공유하기");
			break;
		case R.id.img_diary_shack:
			Util.tst(mContext, "shack으로 보내기(관련처리)");
			break;

		}

	}
}


//ImageView img = (ImageView)convertView.findViewById(R.id.item_img_diary);
//img.setImageResource(R.drawable.day);