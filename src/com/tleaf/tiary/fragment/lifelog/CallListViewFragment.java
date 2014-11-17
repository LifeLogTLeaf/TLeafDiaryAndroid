package com.tleaf.tiary.fragment.lifelog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
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
import com.tleaf.tiary.fragment.lifelog.adapter.CallLogAdapter;
import com.tleaf.tiary.model.Call;
import com.tleaf.tiary.model.Diary;

public class CallListViewFragment extends BaseFragment {

	private CallLogAdapter mAdapter;

	public CallListViewFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.fragment_call, container, false);
		ListView lv = (ListView) rootView.findViewById(R.id.list_call);

		
		mAdapter = new CallLogAdapter(mContext, R.layout.item_call);
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(mItemClickListener);

		new AsyncMyLogLoad(mContext, Common.CALL, mAdapter).execute();

		TextView txt_all = (TextView) rootView.findViewById(R.id.txt_all);
		txt_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncMyLogLoad(mContext, Common.CALL, mAdapter).execute();
			}
		});
		
		TextView txt_incoming = (TextView) rootView.findViewById(R.id.txt_incoming);
		txt_incoming.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncMyLogLoad(mContext, Common.CALL, Common.INCOMING, mAdapter).execute();
			}
		});
		TextView txt_outgoing = (TextView) rootView.findViewById(R.id.txt_outgoing);
		txt_outgoing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncMyLogLoad(mContext, Common.CALL, Common.OUTGOING, mAdapter).execute();
			}
		});
		TextView txt_missed = (TextView) rootView.findViewById(R.id.txt_missed);
		txt_missed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncMyLogLoad(mContext, Common.CALL, Common.MISSED, mAdapter).execute();
			}
		});


		return rootView;
	}



	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//			Diary diary = getDiaryListByType().get(position);
			//			Fragment fragment = new DiaryFragment(diary);
			//			MainActivity.changeFragment(fragment);
		}

	};

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}
