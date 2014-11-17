package com.tleaf.tiary.fragment.lifelog;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.fragment.BaseFragment;
import com.tleaf.tiary.fragment.lifelog.adapter.CardLogAdapter;

/** 카드 로그 리스트 뷰를 담당하는 프래그먼트 클래스 **/
public class CardListViewFragment extends BaseFragment {

	private CardLogAdapter mAdapter ;

	public CardListViewFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();

		View rootView = inflater.inflate(R.layout.fragment_card, container,
				false);
		ListView lv = (ListView) rootView.findViewById(R.id.list_card);

		mAdapter = new CardLogAdapter(mContext, R.layout.item_card);
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(mItemClickListener);

		new AsyncMyLogLoad(mContext, Common.CARD, mAdapter).execute();

//		TextView txt_all = (TextView) rootView
//				.findViewById(R.id.txt_all);
//		txt_all.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				new AsyncMyLogLoad(mContext, Common.SMS, mAdapter).execute();
//				
//			}
//		});
//		TextView txt_incoming = (TextView) rootView
//				.findViewById(R.id.txt_incoming);
//		txt_incoming.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				new AsyncMyLogLoad(mContext, Common.SMS, Common.INCOMING, mAdapter).execute();
//
//			}
//		});
//		TextView txt_outgoing = (TextView) rootView
//				.findViewById(R.id.txt_outgoing);
//		txt_outgoing.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				new AsyncMyLogLoad(mContext, Common.SMS, Common.OUTGOING, mAdapter).execute();
//
//			}
//		});

		return rootView;
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Diary diary = getDiaryListByType().get(position);
			// Fragment fragment = new DiaryFragment(diary);
			// MainActivity.changeFragment(fragment);
		}

	};

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}



