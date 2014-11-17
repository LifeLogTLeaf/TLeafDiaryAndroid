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

import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.model.Card;
import com.tleaf.tiary.model.MyLog;
import com.tleaf.tiary.model.MySms;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class CardLogAdapter extends MyLogAdapter {
	private int mLayout;
	private HashSet<Integer> mFirstDayPositionSet;


	public CardLogAdapter(Context context, int layout) {
		super(context);
		mLayout = layout;
		mFirstDayPositionSet = new HashSet<Integer>();
	}

	public Card getItem(int position) {
		MyLog card = super.getItem(position);

		if (card != null && card instanceof Card)
			return (Card) arrItem.get(position);
		else {
			return null;
		}
	}

	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public void updateItem(ArrayList<MyLog> arr) {
		Util.ll("updateItem CardLogAdapter arr", arr.size());
		if (arr.size() == 0) {
			super.updateItem(arr);
			return;
		}

		/* card log array에서 요일이 바뀌는 position을 확인한다 */
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
		Card card = getItem(position);

		LinearLayout ll = (LinearLayout) convertView
				.findViewById(R.id.layout_item_card);
		TextView txt_nolog = (TextView) convertView
				.findViewById(R.id.item_txt_card_nolog);
	
		TextView txt_name = (TextView) convertView
				.findViewById(R.id.item_txt_name_card);
		TextView txt_number = (TextView) convertView
				.findViewById(R.id.item_txt_number_card);
		TextView txt_date = (TextView) convertView
				.findViewById(R.id.item_txt_date_card);
		
		
		TextView txt_cardType = (TextView) convertView
				.findViewById(R.id.item_txt_cardType_card);
		
		TextView txt_cardDate = (TextView) convertView
				.findViewById(R.id.item_txt_cardDate_card);
		
		TextView txt_spendedMoney = (TextView) convertView
				.findViewById(R.id.item_txt_spendedMoney_card);
		
		TextView txt_spendedPlace = (TextView) convertView
				.findViewById(R.id.item_txt_spendedPlace_card);
		
		TextView txt_leftMoney = (TextView) convertView
				.findViewById(R.id.item_txt_leftMoney_card);
		
		
		RelativeLayout ll_date = (RelativeLayout) convertView
				.findViewById(R.id.layout_item_card_day);
		TextView txt_date_title = (TextView) convertView
				.findViewById(R.id.item_txt_card_date);
		
		/* card log가 없을 경우 없음을 알리는 뷰를 visible한다, card log를 보여주는 listview와 날짜 타이틀 뷰를 gone한다 */
		if (card == null) {
			ll.setVisibility(View.GONE);
			ll_date.setVisibility(View.GONE);
			txt_nolog.setVisibility(View.VISIBLE);
			return convertView;
		}
		
		/* card log array에서 요일이 바뀌는 position일 경우 날짜 타이틀 뷰를 visible한다 */
		if (mFirstDayPositionSet.contains(position)) {
			ll_date.setVisibility(View.VISIBLE);
			txt_date_title.setText(MyTime.getLongToString(mContext,
					card.getDate()));
		} else {
			ll_date.setVisibility(View.GONE);
		}
		
		ll.setVisibility(View.VISIBLE);
		txt_nolog.setVisibility(View.GONE);

		txt_name.setText(card.getName());
		txt_number.setText(card.getNumber());
		String dateStr = MyTime
				.getLongToStringWithTime(mContext, card.getDate());
		
		txt_date.setText(dateStr);
		
		txt_cardType.setText("결제카드 : "+card.getCardType());
		txt_cardDate.setText("결제시간 : "+MyTime
				.getLongToStringWithTime(mContext, card.getCardDate()));
		txt_spendedMoney.setText("결제금액 : " + card.getSpendedMoney()+"원");
		txt_spendedPlace.setText("결제장소 : " + card.getSpendedPlace());
		txt_leftMoney.setText("잔액 : "+card.getLeftMoney()+"원");
	
		return convertView;
	}

}
