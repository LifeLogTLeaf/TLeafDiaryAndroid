package com.tleaf.tiary;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.widget.SpinnerAdapter;

public class CustomActionBar {

	//static

	private static ActionBar mAtionBar;


	public static ActionBar getHomeActionBar(ActionBar actionBar, SpinnerAdapter adapter, OnNavigationListener listener) {
		// Set up the action bar to show a dropdown list.
		mAtionBar = actionBar;
		mAtionBar.setDisplayShowTitleEnabled(false);
		mAtionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		// Specify a SpinnerAdapter to populate the dropdown list.
		mAtionBar.setListNavigationCallbacks(adapter, listener);
				
		return mAtionBar;

		//		new ArrayAdapter<String>(
		//				actionBar.getThemedContext(),
		//				android.R.layout.simple_list_item_1,
		//				android.R.id.text1,
		//				new String[] {
		//					getString(R.string.title_section1),
		//					getString(R.string.title_section2),
		//					getString(R.string.title_section3),
		//				}),
		//				this
	}
}
