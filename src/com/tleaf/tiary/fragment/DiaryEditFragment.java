package com.tleaf.tiary.fragment;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.google.android.gms.internal.hn;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.MapActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.dialog.DialogResultListener;
import com.tleaf.tiary.dialog.FolderDialogFragment;
import com.tleaf.tiary.dialog.TagDialogFragment;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class DiaryEditFragment extends Fragment {

	private Context mContext;

	private DialogFragment dFragment;

	private FragmentManager fm;
	private Time mTime;

	private TextView txt_date;
	private TextView txt_title;
	private TextView txt_content;

	private TextView txt_tag;
	private TextView txt_folder;
	
	private LinearLayout layout_menu;
	private LinearLayout layout_add;
	private LinearLayout layout_template;

	private View rootView;

	private String userTag;
	private String userFolder;

	private ArrayList<String> handledTags = null;
	private ArrayList<String> handledFolders = null;

	public DiaryEditFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_diary_edit, container,
				false);

		mContext = getActivity();
		fm = getFragmentManager();
		mTime = MyTime.getCurrentTimeToTime();

		setComponent();

		return rootView;
	}

	private void setComponent() {

		layout_menu = (LinearLayout) rootView
				.findViewById(R.id.layout_edit_menu);
		layout_add = (LinearLayout) rootView.findViewById(R.id.layout_edit_add);
		layout_template = (LinearLayout) rootView
				.findViewById(R.id.layout_edit_template);

		((GridView) rootView.findViewById(R.id.sticker_gridview))
		.setAdapter(new StickerAdapter());

		txt_date = (TextView) rootView.findViewById(R.id.txt_edit_date);
		txt_date.setText(MyTime.getTodayToString(mContext,
				mTime.toMillis(false)));
		txt_date.setOnClickListener(cl);

		txt_title = (TextView) rootView.findViewById(R.id.txt_edit_title);
		txt_content = (TextView) rootView.findViewById(R.id.txt_edit_content);
		
		
		txt_tag = (TextView) rootView.findViewById(R.id.txt_edit_tag);
		txt_folder = (TextView) rootView.findViewById(R.id.txt_edit_folder);

		ImageView img_photo = (ImageView) rootView
				.findViewById(R.id.img_edit_gallery);
		img_photo.setOnClickListener(cl);

		ImageView img_tag = (ImageView) rootView
				.findViewById(R.id.img_edit_tag);
		img_tag.setOnClickListener(cl);

		ImageView img_folder = (ImageView) rootView
				.findViewById(R.id.img_edit_folder);
		img_folder.setOnClickListener(cl);

		ImageView img_location = (ImageView) rootView
				.findViewById(R.id.img_edit_location);
		img_location.setOnClickListener(cl);

		ImageView img_add = (ImageView) rootView
				.findViewById(R.id.img_edit_add);
		img_add.setOnClickListener(cl);

		ImageView img_template = (ImageView) rootView
				.findViewById(R.id.img_edit_template);
		img_template.setOnClickListener(cl);

		ImageView img_setting = (ImageView) rootView
				.findViewById(R.id.img_edit_setting);
		img_setting.setOnClickListener(cl);

		ImageView img_save = (ImageView) rootView
				.findViewById(R.id.img_edit_save);
		img_setting.setOnClickListener(cl);

	}

	private void onClickMenu(int viewId){
		switch (viewId) {
		case R.id.txt_edit_date:
			DatePickerDialog datepicker = DatePickerDialog.newInstance(
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePickerDialog dialog,
								int year, int monthOfYear, int dayOfMonth) {
							mTime.year = year;
							mTime.month = monthOfYear;
							mTime.monthDay = dayOfMonth;
							mTime.normalize(false);
							txt_date.setText(MyTime.getTodayToString(
									mContext, mTime.toMillis(false)));

						}
					}, mTime.year, mTime.month, mTime.monthDay);
			datepicker.show(fm, "dialog");
			break;
		case R.id.img_edit_gallery:
			Util.tst(mContext, "갤러리 호출 ");

			break;

		case R.id.img_edit_tag:
			Util.tst(mContext, "태그 호출 ");
			dFragment = TagDialogFragment.newInstace(tagDialogResultListener);
			dFragment.show(fm, "dialog");
			break;

		case R.id.img_edit_folder: 
			Util.tst(mContext, "폴더 호출 ");
			dFragment = FolderDialogFragment.newInstace(folderDialogResultListener);
			dFragment.show(fm, "dialog");
			break;

		case R.id.img_edit_location:
			Util.tst(mContext, "지도 호출 ");
			Intent intent = new Intent(mContext, MapActivity.class);
			// Log.e("arItem.get(pos).isbn",
			// ""+arItem.get(pos).getDealLocation());
			// intent.putExtra("location",
			// arItem.get(pos).getDealLocation());
			startActivity(intent);
			break;

		case R.id.img_edit_add:
			Util.tst(mContext, "add 호출 ");
			layout_menu.setVisibility(View.GONE);
			layout_template.setVisibility(View.GONE);
			layout_add.setVisibility(View.VISIBLE);
			break;

		case R.id.img_edit_template:
			Util.tst(mContext, "탬플릿 호출 ");
			layout_menu.setVisibility(View.GONE);
			layout_template.setVisibility(View.VISIBLE);
			layout_add.setVisibility(View.GONE);
			break;

		case R.id.img_edit_setting:
			Util.tst(mContext, "갤러리 호출 ");

			break;

		case R.id.img_edit_save:
			Util.tst(mContext, "갤러리 호출 ");
			saveDiary();
		default:
			break;
		}
	}

	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			onClickMenu(v.getId());
		}
	};


	private DialogResultListener tagDialogResultListener = new DialogResultListener() {

		@Override
		public void setResult(String result) {
			if (result == null || result == "") 
				Util.tst(mContext, "원하는 태그를 입력해주세요");
			else { 
				userTag = result;
				dFragment.dismiss();
				txt_tag.setText(handleComma(userTag, Common.TAG));
			}
		}

		@Override
		public void setCancel() {
			dFragment.dismiss();			
		}

	};

	private DialogResultListener folderDialogResultListener = new DialogResultListener() {

		@Override
		public void setResult(String result) {
			if (result == null || result == "") 
				Util.tst(mContext, "원하는 폴더를 입력해주세요");
			else { 
				userFolder = result;
				dFragment.dismiss();			
				txt_tag.setText(handleComma(userTag, Common.FOLDER));
			}
		}

		@Override
		public void setCancel() {
			dFragment.dismiss();			
		}

	};

	
	private String handleComma(String userData, int dataType) { 
		
		ArrayList<String> arr = new ArrayList<String>();
		
		String handledDataStr = "";
		
		String[] splitStr = userData.split(",");
		for(int i=0; i<splitStr.length; i++) {
			splitStr[i] = splitStr[i].trim();
			if (splitStr[i] == null || splitStr[i] == "")
				break;
			arr.add(splitStr[i]);
			if(dataType == Common.TAG)
				handledDataStr += "#";
			handledDataStr += splitStr[i];
			if (i != splitStr.length-1) 
				handledDataStr += ", ";
		}
		
		if (dataType == Common.TAG)
			handledTags = arr;
		else if (dataType == Common.FOLDER)
			handledFolders = arr;
		
		return handledDataStr;
	}
	
	
	private void saveDiary() {
		Diary mDiary = new Diary();
		mDiary.setDate(mTime.toMillis(false));
		mDiary.setTitle(txt_title.getText().toString());
		mDiary.setContent((txt_title.getContext().toString()));
		//이미지
		if (handledTags != null || handledTags.size() != 0)
			mDiary.setTags(handledTags);
		if (handledFolders != null || handledFolders.size() != 0)
			mDiary.setTags(handledFolders);

	}


	private class StickerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 50;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView image = (ImageView) convertView;
			if (image == null) {
				image = new ImageView(mContext);
			}

			image.setImageResource(R.drawable.ic_launcher);
			return image;
		}

	}

	// public void mOnClick(View v) {
	// final LinearLayout linear = (LinearLayout)
	// View.inflate(this, R.layout.order, null);
	//
	// new AlertDialog.Builder(this)
	// .setTitle("주문 정보를 입력하시오.")
	// .setIcon(R.drawable.androboy)
	// .setView(linear)
	// .setPositiveButton("확인", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// EditText product = (EditText)linear.findViewById(R.id.product);
	// EditText number = (EditText)linear.findViewById(R.id.number);
	// CheckBox paymethod = (CheckBox)linear.findViewById(R.id.paymethod);
	// TextView text = (TextView)findViewById(R.id.text);
	// text.setText("주문 정보 " + product.getText() + " 상품 " +
	// number.getText() + "개." +
	// (paymethod.isChecked() ? "착불결제":""));
	// }
	// })
	// .setNegativeButton("취소", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// TextView text = (TextView)findViewById(R.id.text);
	// text.setText("주문을 취소했습니다.");
	// }
	// })
	// .show();
	// }
}
