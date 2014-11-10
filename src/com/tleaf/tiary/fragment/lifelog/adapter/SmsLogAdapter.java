package com.tleaf.tiary.fragment.lifelog.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.model.MySms;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class SmsLogAdapter extends MyLogAdapter {
	private int mLayout;
	private ImageLoader imageLoader;

	public SmsLogAdapter(Context context, int layout) {
		super(context);
		mLayout = layout;
	}

	public MySms getItem(int position) {
		MyLog sms = super.getItem(position);

		if (sms != null && sms instanceof MySms)
			return (MySms) arrItem.get(position);
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
		MySms sms = getItem(position);

		LinearLayout ll = (LinearLayout) convertView
				.findViewById(R.id.layout_item_sms);
		TextView txt_nolog = (TextView) convertView
				.findViewById(R.id.item_txt_sms_nolog);
	
		
		TextView txt_name = (TextView) convertView
				.findViewById(R.id.item_txt_name_sms);
		TextView txt_number = (TextView) convertView
				.findViewById(R.id.item_txt_number_sms);
		TextView txt_date = (TextView) convertView
				.findViewById(R.id.item_txt_date_sms);
		TextView txt_msg = (TextView) convertView
				.findViewById(R.id.item_txt_message_sms);
		ImageView img = (ImageView) convertView.findViewById(R.id.item_img_sms);

		if (sms == null) {
			ll.setVisibility(View.GONE);
			txt_nolog.setVisibility(View.VISIBLE);
			return convertView;
		}

		txt_name.setText(sms.getName());
		txt_number.setText(sms.getNumber());
		String dateStr = MyTime
				.getLongToStringWithTime(mContext, sms.getDate());
		txt_date.setText(dateStr);
		String msg = sms.getMessage();
		txt_msg.setText(msg);

		String type = sms.getType();
		Util.ll("sms type", type);
		if (type.equals(Common.INCOMING)) {
			img.setImageResource(R.drawable.incoming_sms);
			img.setColorFilter(mContext.getResources().getColor(
					R.color.point_light));
		} else if (type.equals(Common.OUTGOING)) {
			img.setImageResource(R.drawable.outgoing_sms);
			img.setColorFilter(mContext.getResources().getColor(
					R.color.log_violet_light));
		} else {
			img.setImageResource(R.drawable.question);
			img.setColorFilter(mContext.getResources().getColor(
					R.color.diary_title));
		}
		return convertView;
	}

}
