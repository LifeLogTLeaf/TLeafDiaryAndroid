package com.tleaf.tiary.template;

import java.util.ArrayList;
import java.util.Random;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tleaf.tiary.Common;
import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;
import com.tleaf.tiary.fragment.BaseFragment;
import com.tleaf.tiary.fragment.DiaryEditFragment;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.model.Card;
import com.tleaf.tiary.model.Diary;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.model.MySms;
import com.tleaf.tiary.model.TemplateContent;
import com.tleaf.tiary.template.LogPagerAdapter.OnItemClickLogPagerListener;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class ChatFragement extends BaseFragment {

	private ArrayList<Message> messages;
	private ChatAdapter mAdapter;
	private static EditText edit_chat;
	private ListView lv_chat;
	private ImageView img_change;
	private LinearLayout ll_change;

	private static Random rand = new Random();
	private static String templateName;

	private int currentIcon = Common.LOG;

	private boolean bot = false;
	private boolean mine = true;
	private boolean helloMsg = true;
	private int contentNo = 1;
	private Handler handler = new Handler();

	private ArrayList<TemplateContent> contentArr = new ArrayList<TemplateContent>();

	private ViewPager vp_log;
	private LogPagerAdapter logPagerAdapter;

	public ChatFragement(ArrayList<TemplateContent> contentArr) {
		this.contentArr = contentArr;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (contentArr == null && contentArr.size() == 0)
			return null;
		View rootView = inflater.inflate(R.layout.fragment_chat, container,
				false);
		lv_chat = (ListView) rootView.findViewById(R.id.list_chat);

		edit_chat = (EditText) rootView.findViewById(R.id.edit_chat);
		edit_chat.setOnFocusChangeListener(editTextFocusCl);

		img_change = (ImageView) rootView
				.findViewById(R.id.img_change_keyboard_log);
		img_change.setImageResource(R.drawable.log);
		img_change.setOnClickListener(cl);

		ll_change = (LinearLayout) rootView.findViewById(R.id.ll_input_log);

		vp_log = (ViewPager) rootView.findViewById(R.id.viewPager_log);
		logPagerAdapter = new LogPagerAdapter(mActivity, mContext,
				new OnItemClickLogPagerListener() {
			@Override
			public void onClick(MyLog myLog) {
				edit_chat.setText("");
				String selectedLog = "";
				if (myLog instanceof Call) {
					selectedLog = "";
					Call call = (Call) myLog;
					selectedLog = call.getName();
				} else if (myLog instanceof Card) { //먼저 검사해주어야 한다 //내용수정 
					selectedLog = "";
					Card card = (Card) myLog;
//					selectedLog = "결제카드:" + card.getCardType() + "/n" +
//							"결제시간: " + MyTime.getLongToString(mContext, card.getCardDate()) +
					selectedLog =  "" + card.getSpendedMoney();//"결제금액: " +  + "원/n" +
//							"결제장소: " + card.getSpendedPlace() + "/n" +
//							"잔액: " + card.getLeftMoney() + "원";
				} else if (myLog instanceof MySms) {
					selectedLog = "";
					MySms sms = (MySms) myLog;
					selectedLog = sms.getNumber();
				}
				Util.ll("selectedLog", selectedLog);
				edit_chat.setFocusableInTouchMode(true);
				edit_chat.setFocusable(true);
				edit_chat.setText(selectedLog);
			}
		});

		vp_log.setAdapter(logPagerAdapter);

		messages = new ArrayList<Message>();
		mAdapter = new ChatAdapter(mContext, messages);
		lv_chat.setAdapter(mAdapter);

		String chatIntro = "키워드로 입력해주세요!";
		addNewMessage(new Message(helloMsg, chatIntro));
		addNewMessage(new Message(contentArr.get(0).getQuestion(), bot));

		// 안녕하세요 :)\n오늘은
		// " + MyTime.getLongToString(mContext, MyTime.getCurrentTime()) + "입니다.\n

		Button btn_send = (Button) rootView.findViewById(R.id.btn_chat_send);
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendMessage();
			}
		});
		return rootView;
	}

	private OnFocusChangeListener editTextFocusCl = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			Util.ll("user click edittext", hasFocus);
			if (hasFocus) {
				Util.ll("user click edittext ll_change", "ll_change");
				ll_change.setVisibility(View.GONE); // 로그뷰는 gone한다
				img_change.setImageResource(R.drawable.log); // 로그 선택할 수 있는
				// icon으로 변경해준다
				currentIcon = Common.LOG; // 현재 상태가 로그아이콘임을 저장한다
			} else {
				img_change.setImageResource(R.drawable.keyboard); // 키보드 선택할 수
				// 있는 icon으로
				// 변경해준다
				currentIcon = Common.KEYBOARD; // 현재 상태가 키보드아이콘임을 저장한다
			}

		}
	};

	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (currentIcon == Common.LOG) {// 로그입력 시작
				setLogInputMode();
			} else if (currentIcon == Common.KEYBOARD) {// 키보드 입력 시작
				setKeyboardInputMode();
			}
		}
	};

	private void setLogInputMode() {
		Util.hideKeyboard(mContext, edit_chat.getApplicationWindowToken());
		ll_change.setVisibility(View.VISIBLE);
		edit_chat.setHint("");
		edit_chat.setFocusable(false);

		img_change.setImageResource(R.drawable.keyboard);
		currentIcon = Common.KEYBOARD;
	}

	private void setKeyboardInputMode() {
		ll_change.setVisibility(View.GONE);
		edit_chat.setFocusableInTouchMode(true);
		edit_chat.setFocusable(true);
		edit_chat.requestFocus();
		// edit_chat.setSelection(edit_chat.length()); //포커스 맨 마지막으로 이동
		// -> 키보드가 안올라옴
		// Util.showKeyboard(mContext, edit_chat); //editText에 포커스는 가능하게
		// 해주었지만 사용자는 키보드 아이콘을 선택한 상태이므로 실제 포커스가 들어온건 아니다. 직접쇼해줘야한다

		img_change.setImageResource(R.drawable.log);
		currentIcon = Common.LOG;
	}

	private void sendMessage() {
		String newMessage = edit_chat.getText().toString().trim();
		contentArr.get(contentNo++).setContent(newMessage);
		if (newMessage != null && newMessage.length() > 0) {
			edit_chat.setText("");
			addNewMessage(new Message(newMessage, true));
			new SendingMessage().execute();
		}
	}


	private void addNewMessage(Message msg) {
		messages.add(msg);
		mAdapter.notifyDataSetChanged();
		setLogPagerLcoation(msg.getResponseType());
		if (msg.getMessage().equals("일기를 종료합니다"))
			exitChat();

		handler.postDelayed(new Runnable() { //키보드가 사라진 후에 리스트뷰 마지막 아이템을 셀렉션을 해준다
			@Override
			public void run() {
				lv_chat.setSelection(messages.size() - 1);
			}
		}, DateUtils.SECOND_IN_MILLIS);
	}



	private void setLogPagerLcoation(String responseType) {
		if (responseType != null && !responseType.equals("null")) {
			Util.ll("responseType", responseType);
			setLogInputMode();
			if (responseType.equals(Common.STRING_CALL)) {
				vp_log.setCurrentItem(0);
			} else if (responseType.equals(Common.STRING_SMS)) {
				vp_log.setCurrentItem(1);
			} else if (responseType.equals(Common.STRING_CARD)) {
				vp_log.setCurrentItem(2);
			} else if (responseType.equals(Common.STRING_GALLERY)) {
				vp_log.setCurrentItem(3);
			} else if (responseType.equals(Common.STRING_BOOKMARK)) {
				vp_log.setCurrentItem(4);
			} else if (responseType.equals(Common.STRING_LOCATION)) {
				vp_log.setCurrentItem(5);
			}
		}
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	private class SendingMessage extends AsyncTask<Void, String, Message> {
		@Override
		protected Message doInBackground(Void... params) {

			try {
				Thread.sleep(1500);// simulate a network call3000
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (contentNo == contentArr.size()) {
				return new Message("일기를 종료합니다", bot);
			} else {
				TemplateContent content = contentArr.get(contentNo);
				Message msg = new Message();
				msg.setMessage(content.getQuestion());
				msg.setMine(bot);
				msg.setResponseType(content.getType());
				return msg;
			}
		}

		@Override
		public void onProgressUpdate(String... v) {

			if (messages.get(messages.size() - 1).isStatusMessage) {
				messages.get(messages.size() - 1).setMessage(v[0]);
				mAdapter.notifyDataSetChanged();
				lv_chat.setSelection(messages.size() - 1);
			} else {
				addNewMessage(new Message(true, v[0]));
			}
		}

		@Override
		protected void onPostExecute(Message msg) {
			if (messages.get(messages.size() - 1).isStatusMessage) {
				messages.remove(messages.size() - 1);
			}
			addNewMessage(msg);
		}

	}

	private void exitChat() {
		Diary diary = new Diary();
		String fullSetence = "";

		for (int i = 0; i < contentArr.size(); i++) {
			String setence = "";
			if (contentArr.get(i).getFront() == null
					|| contentArr.get(i).getFront().equals(null))
				continue;
			else
				setence += contentArr.get(i).getFront();

			if (contentArr.get(i).getFront() == null
					|| contentArr.get(i).getContent().equals(null))
				continue;
			else
				setence += contentArr.get(i).getContent();

			if (contentArr.get(i).getFront() == null
					|| contentArr.get(i).getEnd().equals(null))
				continue;
			else
				setence += contentArr.get(i).getEnd();

			setence += "\n";
			fullSetence += setence;
		}
		diary.setContent(fullSetence);

		Fragment fragment = new DiaryEditFragment(diary);
		MainActivity.changeFragment(fragment);

	}

}
