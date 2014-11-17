package com.tleaf.tiary.template;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.model.Message;

/** 채팅 리스트뷰를 대화로 채워주는 리스트뷰 어답터 **/
public class ChatAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Message> msgArr = new ArrayList<Message>();

	public ChatAdapter(Context context, ArrayList<Message> messages) {
		this.mContext = context;
		this.msgArr = messages;
	}

	@Override
	public int getCount() {
		return msgArr.size();
	}

	@Override
	public Object getItem(int position) {		
		return msgArr.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = (Message) this.getItem(position);

		ViewHolder holder; 
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat, parent, false);
			holder.message = (TextView) convertView.findViewById(R.id.txt_chat);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.message.setText(message.getMessage());

		LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
		if(message.isStatusMessage()) {
			holder.message.setBackgroundDrawable(null);
			lp.gravity = Gravity.LEFT;
			holder.message.setTextColor(mContext.getResources().getColor(R.color.point));
		} else {		
			if(message.isMine()) {
				holder.message.setBackgroundResource(R.drawable.bubble_white);
				lp.gravity = Gravity.RIGHT;
			} else {
				holder.message.setBackgroundResource(R.drawable.bubble_skyblue_grey_left);
				lp.gravity = Gravity.LEFT; }
			holder.message.setLayoutParams(lp);
			holder.message.setTextColor(mContext.getResources().getColor(R.color.diary_content));	
		}
		return convertView;
	}


	private class ViewHolder
	{
		TextView message;
	}


}
