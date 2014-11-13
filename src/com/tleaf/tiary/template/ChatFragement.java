package com.tleaf.tiary.template;

import java.util.ArrayList;
import java.util.Random;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.fragment.BaseFragment;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class ChatFragement extends BaseFragment {

	private ArrayList<Message> messages;
	private ChatAdapter mAdapter;
	private EditText edit_chat;
	private ListView lv_chat;
	private ImageView img_change;
	private LinearLayout ll_change;

	private static Random rand = new Random();	
	private static String templateName;

	private int currentIcon = Common.LOG;

	private boolean bot = false;
	private boolean mine = true;
	private boolean helloMsg = true;


	public ChatFragement() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
		lv_chat = (ListView) rootView.findViewById(R.id.list_chat);

		edit_chat = (EditText) rootView.findViewById(R.id.edit_chat);
		img_change = (ImageView) rootView.findViewById(R.id.img_change_keyboard_log);
		img_change.setImageResource(R.drawable.log);
		img_change.setOnClickListener(cl);

		ll_change = (LinearLayout) rootView.findViewById(R.id.ll_input_log);

		messages = new ArrayList<Message>();

		mAdapter = new ChatAdapter(mContext, messages);
		lv_chat.setAdapter(mAdapter);
		String chatIntro = "키워드로 입력해주세요!";
		addNewMessage(new Message(helloMsg, chatIntro));

		//		안녕하세요 :)\n오늘은 " + MyTime.getLongToString(mContext, MyTime.getCurrentTime()) + "입니다.\n

		Button btn_send = (Button) rootView.findViewById(R.id.btn_chat_send);
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendMessage();
			}
		});
		return rootView;
	}

	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (currentIcon == Common.LOG) {//로그입력 시작
				//				Util.hideKeyboard(mContext, edit_chat.getApplicationWindowToken());
				ll_change.setVisibility(View.VISIBLE);
				edit_chat.setHint("");
				edit_chat.setEnabled(false);
				edit_chat.setFocusable(false);
				img_change.setImageResource(R.drawable.keyboard);
				currentIcon = Common.KEYBOARD;
			} else if (currentIcon == Common.KEYBOARD){//키보드 입력 시작
				ll_change.setVisibility(View.GONE);
				Util.showKeyboard(mContext, edit_chat); //쇼해야..
				edit_chat.setFocusableInTouchMode(true);
				edit_chat.setEnabled(true);
				edit_chat.setFocusable(true);
				img_change.setImageResource(R.drawable.log);
				currentIcon = Common.LOG;
			}
		}
	};

	private void sendMessage() {
		String newMessage = edit_chat.getText().toString().trim(); 
		if(newMessage != null && newMessage.length() > 0) {
			edit_chat.setText("");
			addNewMessage(new Message(newMessage, true));
			new SendingMessage().execute();
		}
	}


	private void addNewMessage(Message msg) {
		messages.add(msg);
		mAdapter.notifyDataSetChanged();
		lv_chat.setSelection(messages.size()-1);
		//		getView().setSelection(messages.size()-1);
	}


	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}


	private class SendingMessage extends AsyncTask<Void, String, String>
	{
		@Override
		protected String doInBackground(Void... params) {

			try {
				Thread.sleep(2000);//simulate a network call3000
			}catch (InterruptedException e) {
				e.printStackTrace();
			}

			return "";
		}

		@Override
		public void onProgressUpdate(String... v) {

			if(messages.get(messages.size()-1).isStatusMessage) {
				messages.get(messages.size()-1).setMessage(v[0]); 
				mAdapter.notifyDataSetChanged(); 
				lv_chat.setSelection(messages.size()-1);
			} else{
				addNewMessage(new Message(true,v[0]));
			}
		}

		@Override
		protected void onPostExecute(String text) {
			if(messages.get(messages.size()-1).isStatusMessage) {
				messages.remove(messages.size()-1);
			}
			addNewMessage(new Message(text, false)); 
		}

	}

}






