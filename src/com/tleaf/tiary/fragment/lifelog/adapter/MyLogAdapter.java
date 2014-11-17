package com.tleaf.tiary.fragment.lifelog.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.model.MySms;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

abstract public class MyLogAdapter extends BaseAdapter {

	protected Context mContext;
	protected LayoutInflater mInflater;

	protected ArrayList<MyLog> arrItem;

	public MyLogAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void updateItem(ArrayList<MyLog> arr) {
		arrItem = new ArrayList<MyLog>();
		arrItem.clear();
		arrItem.addAll(arr);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (arrItem == null) {
			// 아직 조회안한시점 (초기값)
			return 0;
		} else if (arrItem.size() == 0) {
			// 조회했는데 0인 경우 no log뷰를 띄우기 위해 값을 1로 설정
			return 1;
		}

		return arrItem.size();
	}
	
	@Override
	public MyLog getItem(int position) {
		// 조회했는데 0인 경우 array는 0이고 getCount는 1
		if (getCount() == 1 && arrItem.size() == 0) {
			return null;
		}
		return arrItem.get(position);
	}
}