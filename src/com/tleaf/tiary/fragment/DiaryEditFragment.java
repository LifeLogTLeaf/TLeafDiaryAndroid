package com.tleaf.tiary.fragment;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tleaf.tiary.MapActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.dialog.FolderDialogFragment;
import com.tleaf.tiary.dialog.TagDialogFragment;
import com.tleaf.tiary.util.Util;

public class DiaryEditFragment extends Fragment {

	private Context mContext;

	private DialogFragment dFragment;

	private FragmentManager fm;

	public DiaryEditFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_diary_edit, container, false);

		mContext = getActivity();
		fm = getFragmentManager();

		TextView txt_date = (TextView) rootView.findViewById(R.id.txt_edit_date);
		txt_date.setOnClickListener(cl);

		ImageView img_photo = (ImageView) rootView.findViewById(R.id.img_edit_gallery);
		img_photo.setOnClickListener(cl);

		ImageView img_tag = (ImageView) rootView.findViewById(R.id.img_edit_tag);
		img_tag.setOnClickListener(cl);

		ImageView img_folder = (ImageView) rootView.findViewById(R.id.img_edit_folder);
		img_folder.setOnClickListener(cl);

		ImageView img_location = (ImageView) rootView.findViewById(R.id.img_edit_location);
		img_location.setOnClickListener(cl);

		ImageView img_add = (ImageView) rootView.findViewById(R.id.img_edit_add);
		img_add.setOnClickListener(cl);

		ImageView img_template = (ImageView) rootView.findViewById(R.id.img_edit_template);
		img_template.setOnClickListener(cl);

		ImageView img_setting = (ImageView) rootView.findViewById(R.id.img_edit_setting);
		img_setting.setOnClickListener(cl);


		return rootView;
	}


	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_edit_gallery:
				Util.tst(mContext, "갤러리 호출 ");

				break;

			case R.id.img_edit_tag:
				Util.tst(mContext, "태그 호출 ");
				dFragment = TagDialogFragment.newInstace(tagDialogOkClickListener, dialogCancelClickListener);
				dFragment.show(fm, "dialog");
				break;


			case R.id.img_edit_folder:
				Util.tst(mContext, "폴더 호출 ");
				dFragment = FolderDialogFragment.newInstace(folderDialogOkClickListener, dialogCancelClickListener);
				dFragment.show(fm, "dialog");
				break;

			case R.id.img_edit_location:
				Util.tst(mContext, "지도 호출 ");
				Intent intent = new Intent(mContext, MapActivity.class);
				//				Log.e("arItem.get(pos).isbn", ""+arItem.get(pos).getDealLocation());
				//				intent.putExtra("location", arItem.get(pos).getDealLocation());
				startActivity(intent);
				break;

			case R.id.img_edit_add:
				Util.tst(mContext, "갤러리 호출 ");

				break;


			case R.id.img_edit_template:
				Util.tst(mContext, "갤러리 호출 ");

				break;


			case R.id.img_edit_setting:
				Util.tst(mContext, "갤러리 호출 ");

				break;



			case R.id.txt_edit_date:
//				final DateTimeSetListener startListener = new DateTimeSetListener(DateTimeSetListener.START_TIME);
//
//				DialogFragment newFragment = DatePickerDialog.newInstance(startListener, time.year, time.month,
//						time.monthDay);

			default:
				break;
			}

		}
	};


	private View.OnClickListener tagDialogOkClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dFragment.dismiss();
		}
	};

	private View.OnClickListener folderDialogOkClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dFragment.dismiss();
		}
	};

	private View.OnClickListener dialogCancelClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dFragment.dismiss();
		}
	};

	//	public void mOnClick(View v) {
	//		final LinearLayout linear = (LinearLayout)
	//				View.inflate(this, R.layout.order, null);
	//
	//		new AlertDialog.Builder(this)
	//		.setTitle("주문 정보를 입력하시오.")
	//		.setIcon(R.drawable.androboy)
	//		.setView(linear)
	//		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
	//			public void onClick(DialogInterface dialog, int whichButton) {
	//				EditText product = (EditText)linear.findViewById(R.id.product);
	//				EditText number = (EditText)linear.findViewById(R.id.number);
	//				CheckBox paymethod = (CheckBox)linear.findViewById(R.id.paymethod);
	//				TextView text = (TextView)findViewById(R.id.text);
	//				text.setText("주문 정보 " + product.getText() + " 상품 " + 
	//						number.getText() + "개." +
	//						(paymethod.isChecked() ? "착불결제":""));
	//			}
	//		})
	//		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
	//			public void onClick(DialogInterface dialog, int whichButton) {
	//				TextView text = (TextView)findViewById(R.id.text);
	//				text.setText("주문을 취소했습니다.");
	//			}
	//		})
	//		.show();
	//	}
}
