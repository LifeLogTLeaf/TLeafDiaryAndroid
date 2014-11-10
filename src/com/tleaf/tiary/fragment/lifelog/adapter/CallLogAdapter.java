package com.tleaf.tiary.fragment.lifelog.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class CallLogAdapter extends MyLogAdapter {
	private int mLayout;
	private ImageLoader imageLoader;

	public CallLogAdapter(Context context, int layout) {
		super(context);
		mLayout = layout;
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

		if(call == null) {
			ll.setVisibility(View.GONE);
			txt_nolog.setVisibility(View.VISIBLE);
			return convertView;
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
		
		String type = arrItem.get(position).getType();
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