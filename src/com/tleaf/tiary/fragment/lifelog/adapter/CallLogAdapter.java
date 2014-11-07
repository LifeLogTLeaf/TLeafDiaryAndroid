package com.tleaf.tiary.fragment.lifelog.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class CallLogAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Call> arrItem;
	private int mLayout;
	private ImageLoader imageLoader;

	public CallLogAdapter(Context context, int layout, ArrayList<Call> item) {
		mContext = context;
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		arrItem = item;
		mLayout = layout;

		if (item == null)
			arrItem = new ArrayList<Call>();
	}

	public int getCount() {
		return arrItem.size();
	}

	public Call getItem(int position) {
		return arrItem.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void updateItem(ArrayList<Call> diaryArr) {
		arrItem.clear();
		arrItem.addAll(diaryArr);
		notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(mLayout, parent, false);
		}

		TextView txt_name = (TextView)convertView.findViewById(R.id.item_txt_name_call);
		txt_name.setText(arrItem.get(position).getName());

		TextView txt_number = (TextView)convertView.findViewById(R.id.item_txt_number_call);
		txt_number.setText(arrItem.get(position).getNumber());

		TextView txt_date = (TextView)convertView.findViewById(R.id.item_txt_date_call);
		String dateStr = MyTime.getLongToStringWithTime(mContext, arrItem.get(position).getDate());
		txt_date.setText(dateStr);

		TextView txt_duration = (TextView)convertView.findViewById(R.id.item_txt_duration_call);
		String dur = MyTime.convertSecondToString(arrItem.get(position).getDuration());
		txt_duration.setText(dur);


		ImageView img = (ImageView)convertView.findViewById(R.id.item_img_call);
		String type = arrItem.get(position).getType();
		Util.ll("call type", type);
		if(type.equals(Common.INCOMING)) {
			img.setImageResource(R.drawable.incoming);
			img.setColorFilter(mContext.getResources().getColor(R.color.point_light));
		}
		else if(type.equals(Common.OUTGOING)) {
			img.setImageResource(R.drawable.outgoing);
			img.setColorFilter(mContext.getResources().getColor(R.color.log_red_light));
		} 
		else if(type.equals(Common.MISSED)) {
			img.setImageResource(R.drawable.missing);
			img.setColorFilter(mContext.getResources().getColor(R.color.drawblemenu));
		} 
		else {	
			img.setImageResource(R.drawable.question);
			img.setColorFilter(mContext.getResources().getColor(R.color.diary_title));
		}
		return convertView;
	}

}
