package com.tleaf.tiary.fragment.lifelog.adapter;

import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;


/** 장소 로그 리스트 뷰를 장소 로그로 채워주는 리스트뷰 어답터 **/
public class LocationLogAdapter extends MyLogAdapter {
	private int mLayout;
	private HashSet<Integer> mFirstDayPositionSet;

	public LocationLogAdapter(Context context, int layout) {
		super(context);
		mLayout = layout;
		mFirstDayPositionSet = new HashSet<Integer>();
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public void updateItem(ArrayList<MyLog> arr) {
		Util.ll("updateItem LocationLogAdapter arr", arr.size());
		if (arr.size() == 0) {
			super.updateItem(arr);
			return;
		}

		/* location log array에서 요일이 바뀌는 position을 확인한다 */
		mFirstDayPositionSet.clear();
		mFirstDayPositionSet.add(0);

		Time time = new Time(Time.getCurrentTimezone());
		time.setToNow();
		int befoJulian, julian;

		befoJulian = Time.getJulianDay(arr.get(0).getDate(), time.gmtoff);

		for (int i = 1; i < arr.size(); i++) {
			julian = Time.getJulianDay(arr.get(i).getDate(), time.gmtoff);
			if (julian != befoJulian) {
				mFirstDayPositionSet.add(i);
			}
			befoJulian = julian;
		}

		super.updateItem(arr);

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(mLayout, parent, false);
		}
		MyLog location = getItem(position);

		LinearLayout ll = (LinearLayout) convertView
				.findViewById(R.id.layout_item_location);
		TextView txt_nolog = (TextView) convertView
				.findViewById(R.id.item_txt_location_nolog);

		TextView txt_name = (TextView) convertView
				.findViewById(R.id.item_txt_name_location);
		TextView txt_address = (TextView) convertView
				.findViewById(R.id.item_txt_address_location);
		TextView txt_date = (TextView) convertView
				.findViewById(R.id.item_txt_date_location);


		RelativeLayout ll_date = (RelativeLayout) convertView
				.findViewById(R.id.layout_item_location_day);
		TextView txt_date_title = (TextView) convertView
				.findViewById(R.id.item_txt_location_date);

		/* location log가 없을 경우 없음을 알리는 뷰를 visible한다, location log를 보여주는 listview와 날짜 타이틀 뷰를 gone한다 */
		if (location == null) {
			ll.setVisibility(View.GONE);
			ll_date.setVisibility(View.GONE);
			txt_nolog.setVisibility(View.VISIBLE);
			return convertView;
		}

		/* location log array에서 요일이 바뀌는 position일 경우 날짜 타이틀 뷰를 visible한다 */
		if (mFirstDayPositionSet.contains(position)) {
			ll_date.setVisibility(View.VISIBLE);
			txt_date_title.setText(MyTime.getLongToString(mContext,
					location.getDate()));
		} else {
			ll_date.setVisibility(View.GONE);
		}

		ll.setVisibility(View.VISIBLE);
		txt_nolog.setVisibility(View.GONE);

		txt_name.setText(location.getMyAddressName());
		txt_address.setText(location.getMyAddress());

		String dateStr = MyTime
				.getLongToStringWithTime(mContext, location.getDate());

		txt_date.setText(dateStr);

		return convertView;
	}

}
