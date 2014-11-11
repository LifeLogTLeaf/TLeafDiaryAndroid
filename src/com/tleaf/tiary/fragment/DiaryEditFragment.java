package com.tleaf.tiary.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.google.android.gms.internal.ho;
import com.google.android.gms.internal.ll;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.dialog.DialogResultListener;
import com.tleaf.tiary.dialog.EmotionDialogFragment;
import com.tleaf.tiary.dialog.FolderDialogFragment;
import com.tleaf.tiary.dialog.LocationDialogFragment;
import com.tleaf.tiary.dialog.TagDialogFragment;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.photo.HorizontalListView;
import com.tleaf.tiary.photo.PhotoEditAdapter;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class DiaryEditFragment extends Fragment {

	private boolean edit;
	private int selectedEmoIndex;
	private String selectedLocation;

	private Context mContext;

	private DialogFragment dFragment;

	private FragmentManager fm;
	private Time mTime;
	private DataManager dataMgr;

	private ImageView img_emo;
	private TextView txt_date;
	private EditText txt_title;
	private EditText txt_content;

	private TextView txt_tag;
	private TextView txt_folder;
	private TextView txt_location;

	private LinearLayout layout_menu;
	private LinearLayout layout_add;
	private LinearLayout layout_template;
	private LinearLayout layout_info;

	private View rootView;

	private ArrayList<String> selectedFolders;
	private ArrayList<String> selectedTags;
	private Diary editedDiary;

	private ImageLoader imageLoader;
	private PhotoEditAdapter adapter;
	private HorizontalListView horiView;

	private ArrayList<String> selectedImages;


	public DiaryEditFragment() {
		edit = false;
	}

	public DiaryEditFragment(Diary diary) {
		editedDiary = diary;
		edit = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_diary_edit, container,
				false);

		mContext = getActivity();
		fm = getFragmentManager();
		mTime = MyTime.getCurrentTimeToTime();
		dataMgr = new DataManager(mContext);

		//image
		initImageLoader();

		selectedImages = new ArrayList<String>();
		selectedFolders = new ArrayList<String>();
		selectedTags = new ArrayList<String>();
		selectedEmoIndex = -1;

		setComponent();
		if(edit) {
			setEditedDairy();
		} else {
			setCreatedDiary();
		}
		setInfoLayout();
		setImageLayout();

		return rootView;
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


	private void setComponent() {

		layout_menu = (LinearLayout) rootView
				.findViewById(R.id.layout_edit_menu);
		layout_add = (LinearLayout) rootView.findViewById(R.id.layout_edit_add);
		layout_template = (LinearLayout) rootView
				.findViewById(R.id.layout_edit_template);

		layout_info = (LinearLayout) rootView
				.findViewById(R.id.layout_edit_user_add_info);

		//스티커
		((GridView) rootView.findViewById(R.id.sticker_gridview))
		.setAdapter(new StickerAdapter());

		txt_date = (TextView) rootView.findViewById(R.id.txt_edit_date);
		txt_date.setOnClickListener(cl);

		img_emo = (ImageView) rootView.findViewById(R.id.img_edit_emotion);
		img_emo.setOnClickListener(cl);

		txt_title = (EditText) rootView.findViewById(R.id.txt_edit_title);
		txt_content = (EditText) rootView.findViewById(R.id.txt_edit_content);

		txt_tag = (TextView) rootView.findViewById(R.id.txt_edit_tag);
		txt_folder = (TextView) rootView.findViewById(R.id.txt_edit_folder);
		txt_location = (TextView) rootView.findViewById(R.id.txt_edit_location);


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
		img_save.setOnClickListener(cl);


		adapter = new PhotoEditAdapter(mContext, imageLoader);

		horiView = (HorizontalListView) rootView.findViewById(R.id.list_photo);
		horiView.setAdapter(adapter);
	}

	private void setCreatedDiary() {
		txt_date.setText(MyTime.getLongToStringWithTime(mContext,
				mTime.toMillis(false)));
		selectedFolders.clear();
		selectedTags.clear();
		selectedImages.clear();
		//		adapter.isAdding(false);
	}

	private void setEditedDairy() {
		String dateStr = MyTime.getLongToStringWithTime(mContext, editedDiary.getDate());
		txt_date.setText(dateStr);

		if(editedDiary.getEmotion() != null) {
			int index = Common.getIndexByEmomtionName(editedDiary.getEmotion());
			img_emo.setImageResource(getResources().getIdentifier(
					"emo" + (index + 1), "drawable",
					mContext.getPackageName()));
			selectedEmoIndex = index; //이전에 선택했던 이모티콘을 얼럿에서 포인터 색깔로 표시해줌
		}

		txt_title.setText(editedDiary.getTitle());
		txt_content.setText(editedDiary.getContent());

		selectedImages = editedDiary.getImages();
		displayPhoto();

		selectedFolders = editedDiary.getFolders();
		if(selectedFolders != null && selectedFolders.size() != 0) {
			Util.ll("folders.size()", selectedFolders.size());
			Util.ll("Util.covertArrayToString(folders)", Util.covertArrayToString(selectedFolders));
			txt_folder.setText(Util.covertArrayToString(selectedFolders)); 
		}

		selectedTags = editedDiary.getTags();
		if(selectedTags != null && selectedTags.size() != 0) {
			Util.ll("tags.size()", selectedTags.size());
			Util.ll("Util.covertArrayToString(tags)", Util.covertArrayToString(selectedTags));
			txt_tag.setText(Util.covertArrayToString(selectedTags)); 
		}

		selectedLocation = editedDiary.getLocation();
		if(selectedLocation != null && !selectedLocation.equals("null") && !selectedLocation.equals(""))
			txt_location.setText(selectedLocation);

		selectedImages = editedDiary.getImages();
		if(selectedImages != null && selectedImages.size() != 0) {
			Util.ll("selectedImages.size()", selectedImages.size());
			//			adapter.isAdding(false);
			adapter.addAll(selectedImages);
		}

	}

	private void setImageLayout() {
		if(selectedImages != null && selectedImages.size() != 0) {
			horiView.setVisibility(View.VISIBLE);
		} else {
			horiView.setVisibility(View.GONE);
		}
	}

	private void setInfoLayout() {
		String tagStr = txt_tag.getText().toString();
		String tagFolder = txt_folder.getText().toString();
		String tagLocation = txt_location.getText().toString();

		if ((tagStr != null && !tagStr.trim().isEmpty())
				|| (tagFolder != null && !tagFolder.trim().isEmpty())
				|| (tagLocation != null && !tagLocation.trim().isEmpty())) {
			layout_info.setVisibility(View.VISIBLE);
		} else {
			layout_info.setVisibility(View.GONE);
		}
	}

	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			onClickMenu(v.getId());
		}
	};

	private void onClickMenu(int viewId) {
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
							txt_date.setText(MyTime.getLongToStringWithTime(mContext,
									mTime.toMillis(false)));

						}
					}, mTime.year, mTime.month, mTime.monthDay);
			datepicker.show(fm, "dialog");
			break;
		case R.id.img_edit_emotion: 
			dFragment = EmotionDialogFragment.newInstace(dialogResultListener, selectedEmoIndex);
			dFragment.show(fm, "dialog");
			break;

		case R.id.img_edit_gallery:
			//			if (selectedImages != null && selectedImages.size() != 0) {
			//				adapter.isAdding(true);
			//			}
			Intent i = new Intent(Common.MYGALLERY);
			startActivityForResult(i, 200);
			break;

		case R.id.img_edit_tag:
			if(selectedTags != null && selectedTags.size() != 0)
				Util.ll("FolderDialogFragment newInstace", Util.covertArrayToString(selectedTags));
			dFragment = TagDialogFragment.newInstace(dialogResultListener,
					Common.TAG, selectedTags);
			dFragment.show(fm, "dialog");
			break;

		case R.id.img_edit_folder:
			if(selectedFolders != null && selectedFolders.size() != 0)
				Util.ll("FolderDialogFragment newInstace", Util.covertArrayToString(selectedFolders));
			dFragment = FolderDialogFragment.newInstace(dialogResultListener,
					Common.FOLDER, selectedFolders);
			dFragment.show(fm, "dialog");
			break;

		case R.id.img_edit_location:
			if(selectedLocation != null && !selectedLocation.equals(""))
				Util.ll("지도 아이콘 선택", selectedLocation);
			dFragment = LocationDialogFragment.newInstace(dialogResultListener,
					Common.FOLDER, selectedLocation);
			dFragment.show(fm, "dialog");

			//			Util.tst(mContext, "지도 호출 ");
			//			Intent intent = new Intent(mContext, MapActivity.class);
			// Log.e("arItem.get(pos).isbn",
			// ""+arItem.get(pos).getDealLocation());
			// intent.putExtra("location",
			// arItem.get(pos).getDealLocation());
			//			startActivity(intent);
			break;

		case R.id.img_edit_add:
			Util.tst(mContext, "add 호출 ");
			layout_menu.setVisibility(View.GONE);
			layout_template.setVisibility(View.GONE);
			layout_add.setVisibility(View.VISIBLE);
			break;

		case R.id.img_edit_template:
			Util.tst(mContext, "template 호출 ");
			//			layout_menu.setVisibility(View.GONE);
			//			layout_template.setVisibility(View.VISIBLE);
			//			layout_add.setVisibility(View.GONE);
			Fragment fragment = new TemplateSearchFragment();
			MainActivity.changeFragment(fragment);
			break;

		case R.id.img_edit_setting:
			Util.tst(mContext, "setting 호출 ");

			break;

		case R.id.img_edit_save:
			saveDiary();
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//		Util.tst(mContext, "requestCode"+requestCode+"resultCode"+resultCode);
		//		horiView.removeAllViews();
		if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
			String[] all_path = data.getStringArrayExtra("all_path");
			for (String string : all_path) {
				selectedImages.add(string);
				Util.ll("all_path", string);
			}
			displayPhoto();
		}
	}

	private void displayPhoto() {
		adapter.addAll(selectedImages);
		setImageLayout();
	}

	private DialogResultListener dialogResultListener = new DialogResultListener() {

		@Override
		public void setResult(ArrayList<String> result, int type) {
			dFragment.dismiss();
			if(type == Common.TAG) {
				if(selectedTags != null && !selectedTags.isEmpty()) {
					Util.ll("setResult clear 전", Util.covertArrayToString(selectedTags));
					selectedTags.clear();
					Util.ll("setResult clear 후", Util.covertArrayToString(selectedTags));
				}
				selectedTags = result;
				Util.ll("setResult 대입 후", Util.covertArrayToString(selectedTags));
				txt_tag.setText(Util.covertArrayToString(result));

			} else if (type == Common.FOLDER) {
				if(selectedFolders != null && !selectedFolders.isEmpty()) {
					Util.ll("setResult clear 전", Util.covertArrayToString(selectedFolders));
					selectedFolders.clear();
					Util.ll("setResult clear 후", Util.covertArrayToString(selectedFolders));
				}
				selectedFolders = result;
				Util.ll("setResult 대입 후", Util.covertArrayToString(selectedFolders));
				txt_folder.setText(Util.covertArrayToString(result));
			}
			setInfoLayout();
		}

		@Override
		public void setCancel() {
			dFragment.dismiss();
		}

		@Override
		public void setResult(int selectedIndex) {
			dFragment.dismiss();
			Util.tst(mContext, "스티커id " + selectedIndex);
			selectedEmoIndex = selectedIndex;
			img_emo.setImageResource(getResources().getIdentifier(
					"emo" + (selectedEmoIndex + 1), "drawable",
					mContext.getPackageName()));
		}


		@Override
		public void setResult(String result) {
			dFragment.dismiss();
			Util.tst(mContext, "사용자가 입력한 장소 result " + result);
			selectedLocation = result;
			txt_location.setText(selectedLocation);
			setInfoLayout();
		}

	};


	private void saveDiary() {
		Util.hideKeyboard(mContext, txt_content.getApplicationWindowToken());
		Diary mDiary = new Diary();
		if (edit) 
			mDiary.setNo(editedDiary.getNo());

		mDiary.setDate(mTime.toMillis(false));
		Util.ll("saveDiary setDate", mTime.toMillis(false));


		if(txt_title.getText().toString().trim().isEmpty()) {
			mDiary.setTitle("무제");
		} else {
			mDiary.setTitle(txt_title.getText().toString());			
		}

		if(txt_content.getText().toString().trim().isEmpty()) {
			Util.tst(mContext, "일기 내용을 작성해주세요");
			return;
		} else {
			Util.ll("일기 내용", txt_content.getText().toString());
			mDiary.setContent(txt_content.getText().toString());
		}

		//이모티콘 선택안하고 저장 눌렀을 시
		if(selectedEmoIndex != -1)
			mDiary.setEmotion(Common.getEmomtionNameByIndex(selectedEmoIndex));

		// 이미지
		if (selectedImages != null && selectedImages.size() != 0) {
			mDiary.setImages(selectedImages);
		}

		if (selectedTags != null && selectedTags.size() != 0) {
			mDiary.setTags(selectedTags);
		}
		Util.ll("save호출", Util.covertArrayToString(selectedTags));


		if (selectedFolders == null || selectedFolders.size() == 0) {
			selectedFolders = new ArrayList<String>();
			selectedFolders.add(getResources().getString(R.string.mydiary));
		}

		Util.ll("save호출", Util.covertArrayToString(selectedFolders));
		mDiary.setFolders(selectedFolders);

		if (selectedLocation != null && !selectedLocation.equals("")) {
			mDiary.setLocation(selectedLocation);
		}

		Boolean result = true;

		Util.ll("일기 디비에 넣기전 id", mDiary.getNo());
		if (mDiary.getNo() == -1) {
			result = dataMgr.insertDiary(mDiary);
			Util.tst(mContext, "일기 insert");
		} else { 
			result = dataMgr.updateDiary(mDiary);
			Util.tst(mContext, "일기 update");
		}


		if (result) {
			Fragment fragment = new DiaryListViewFragement();
			MainActivity.changeFragment(fragment);
		} else {
			//예외처리
		}

		Activity activity = getActivity();
		if(activity instanceof MainActivity){
			MainActivity mainActivity = (MainActivity) activity;
			mainActivity.refreshDrawer();
		}

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


}


