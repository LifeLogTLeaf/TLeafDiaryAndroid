package com.tleaf.tiary.fragment.lifelog.adapter;

import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;


/** 전화 로그 리스트 뷰를 전화 로그로 채워주는 리스트뷰 어답터 **/
public class CallLogAdapter extends MyLogAdapter {
	private int mLayout;
	private HashSet<Integer> mFirstDayPositionSet;

	public CallLogAdapter(Context context, int layout) {
		super(context);
		mLayout = layout;
		mFirstDayPositionSet = new HashSet<Integer>();
	}

	@Override
	public void updateItem(ArrayList<MyLog> arr) {

		if (arr.size() == 0) {
			super.updateItem(arr);
			return;
		}

		/* call log array에서 요일이 바뀌는 position을 확인한다 */
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

	public Call getItem(int position) {
		MyLog call = super.getItem(position);

		if (call != null && call instanceof Call)
			return (Call) arrItem.get(position);
		else {
			return null;
		}
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		int count = super.getCount();
		Util.ll("getCount", count);
		return count;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(mLayout, parent, false);
		}
		Call call = getItem(position);

		LinearLayout ll = (LinearLayout) convertView
				.findViewById(R.id.layout_item_call);
		TextView txt_nolog = (TextView) convertView
				.findViewById(R.id.item_txt_call_nolog);
		TextView txt_name = (TextView) convertView
				.findViewById(R.id.item_txt_name_call);
		TextView txt_number = (TextView) convertView
				.findViewById(R.id.item_txt_number_call);
		TextView txt_date = (TextView) convertView
				.findViewById(R.id.item_txt_date_call);

		TextView txt_duration = (TextView) convertView
				.findViewById(R.id.item_txt_duration_call);
		ImageView img = (ImageView) convertView
				.findViewById(R.id.item_img_call);

		RelativeLayout ll_date = (RelativeLayout) convertView
				.findViewById(R.id.layout_item_call_day);
		TextView txt_date_title = (TextView) convertView
				.findViewById(R.id.item_txt_call_date);

		/* call log가 없을 경우 없음을 알리는 뷰를 visible한다, call log를 보여주는 listview와 날짜 타이틀 뷰를 gone한다 */
		if (call == null) {
			ll.setVisibility(View.GONE);
			ll_date.setVisibility(View.GONE);
			txt_nolog.setVisibility(View.VISIBLE);
			return convertView;
		}
		
		/* call log array에서 요일이 바뀌는 position일 경우 날짜 타이틀 뷰를 visible한다 */
		if (mFirstDayPositionSet.contains(position)) {
			ll_date.setVisibility(View.VISIBLE);
			txt_date_title.setText(MyTime.getLongToString(mContext,
					call.getDate()));
		} else {
			ll_date.setVisibility(View.GONE);
		}
		
		ll.setVisibility(View.VISIBLE);
		txt_nolog.setVisibility(View.GONE);
		
		
		txt_name.setText(call.getName());
		txt_number.setText(call.getNumber());
		String dateStr = MyTime.getLongToStringWithTime(mContext,
				call.getDate());
		txt_date.setText(dateStr);
		String dur = MyTime.convertSecondToString(call.getDuration());
		txt_duration.setText(dur);

		/* call log의 수신, 발신, 부재중 여부를 확인해 아이콘을 다르게 보여준다 */
		String type = call.getType();
		Util.ll("call type", type);
		if (type.equals(Common.INCOMING)) {
			img.setImageResource(R.drawable.incoming);
			img.setColorFilter(mContext.getResources().getColor(
					R.color.point_light));
		} else if (type.equals(Common.OUTGOING)) {
			img.setImageResource(R.drawable.outgoing);
			img.setColorFilter(mContext.getResources().getColor(
					R.color.log_red_light));
		} else if (type.equals(Common.MISSED)) {
			img.setImageResource(R.drawable.missing);
			img.setColorFilter(mContext.getResources().getColor(
					R.color.drawblemenu));
		} else {
			img.setImageResource(R.drawable.question);
			img.setColorFilter(mContext.getResources().getColor(
					R.color.diary_title));
		}
		return convertView;
	}
}