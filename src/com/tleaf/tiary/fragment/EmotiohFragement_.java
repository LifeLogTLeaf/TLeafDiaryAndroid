package com.tleaf.tiary.fragment;

import com.tleaf.tiary.MainActivity;
import com.tleaf.tiary.R;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EmotiohFragement_ extends Fragment {

	private boolean isContinued = false;

	private ImageView img_emo[];

	private String[] emotion = { "happy", "sad", "good", "soso", "happy",
			"sad", "good", "soso", "happy", "sad", "good", "soso", "happy",
			"sad" };

	private LinearLayout layout_hided;

	public EmotiohFragement_() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_emotion_, container,
				false);

		// ImageView img_emo1 = (ImageView)
		// rootView.findViewById(R.id.img_emotion1);
		// ImageView img_emo2 = (ImageView)
		// rootView.findViewById(R.id.img_emotion2);
		// ImageView img_emo3 = (ImageView)
		// rootView.findViewById(R.id.img_emotion3);
		// ImageView img_emo4 = (ImageView)
		// rootView.findViewById(R.id.img_emotion4);
		// ImageView img_emo5 = (ImageView)
		// rootView.findViewById(R.id.img_emotion5);
		// ImageView img_emo6 = (ImageView)
		// rootView.findViewById(R.id.img_emotion6);
		// ImageView img_emo7 = (ImageView)
		// rootView.findViewById(R.id.img_emotion7);
		// ImageView img_emo8 = (ImageView)
		// rootView.findViewById(R.id.img_emotion8);
		// ImageView img_emo9 = (ImageView)
		// rootView.findViewById(R.id.img_emotion9);
		// ImageView img_emo10 = (ImageView)
		// rootView.findViewById(R.id.img_emotion10);
		//

		Resources res = getResources();
		int resId;

		final int count = 14;
		img_emo = new ImageView[count];
		String name = "img_emotion";

		for (int i = 0; i < count; i++) {
			resId = res.getIdentifier(name + (i + 1), "id", getActivity()
					.getPackageName()); // R.id.img_emotion1
			img_emo[i] = (ImageView) rootView.findViewById(resId);
			img_emo[i].setOnClickListener(mListener);
			img_emo[i].setTag(i);
		}

		layout_hided = (LinearLayout) rootView
				.findViewById(R.id.layout_emotion_hided);
		return rootView;
	}

	
	
	private OnClickListener mListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getTag() != null && v.getTag() instanceof Integer) {
				int index = (Integer) v.getTag();
				if (index == 5 && !isContinued) {
					img_emo[index].setImageResource(R.drawable.person5);
					layout_hided.setVisibility(View.VISIBLE);
				} else {
					Fragment fragment = new DiaryListViewFragement(emotion[index]);
					MainActivity.changeFragment(fragment);
				}

			}

		}
	};

}
