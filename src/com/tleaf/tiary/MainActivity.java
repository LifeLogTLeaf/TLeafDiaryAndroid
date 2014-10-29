package com.tleaf.tiary;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.fragment.DiaryEditFragment;
import com.tleaf.tiary.fragment.DiaryListViewFragement;
import com.tleaf.tiary.fragment.EmotionFragement;
import com.tleaf.tiary.fragment.ExpandableListFragment;
import com.tleaf.tiary.fragment.FolderFragement;
import com.tleaf.tiary.fragment.MyPageFragement;
import com.tleaf.tiary.fragment.PlaceholderFragment;
import com.tleaf.tiary.fragment.SettingFragement;
import com.tleaf.tiary.fragment.TagFragement;
import com.tleaf.tiary.util.Util;


public class MainActivity extends Activity
implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.OnNavigationListener {


	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	//	private String bundelKey;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private DataManager dataMgr;

	static public FragmentManager fragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dataMgr = new DataManager(getApplicationContext());

		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 153, 237)));

		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public boolean onNavigationDrawerItemSelected(int position, int childPosition) {
		// update the main content by replacing fragments
		fragmentManager = getFragmentManager();
		Fragment fragment = null;

		if(childPosition == -1) {
			switch(position) {
			case 0:
				fragment = new DiaryListViewFragement();//HomeFragement();
				break;
			case 1:
				fragment = new ExpandableListFragment();//MyPageFragement();
				break;
			case 2:
				fragment = new DiaryEditFragment();//WriteFragement();
				break;
			case 3:
				return true;
			case 4:
				fragment = new TagFragement();
				break;
			case 5:
				fragment = new EmotionFragement();
				break;
			case 6:
				fragment = new SettingFragement();
				break;
			case 7:
				fragment = new ExpandableListFragment(); //SettingFragement();
				break;
			} 
		} else {
			fragment = new DiaryListViewFragement(dataMgr.getDistinctFolderList().get(childPosition));
		}

//		fragmentManager.beginTransaction()
//		.replace(R.id.container, fragment)//PlaceholderFragment.newInstance(position + 1))
//		.commit();
		
		changeFragment(fragment);

		return false;
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.home);
			break;
		case 2:
			mTitle = getString(R.string.mypage);
			break;
		case 3:
			mTitle = getString(R.string.write);
			break;
		case 4:
			mTitle = getString(R.string.folder);
			break;
		case 5:
			mTitle = getString(R.string.tag);
			break;
		case 6:
			mTitle = getString(R.string.emotion);
			break;
		case 7:
			mTitle = getString(R.string.setting);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			Util.tst(this, "mNavigationDrawerFragment.isDrawerOpen()"+mNavigationDrawerFragment.isDrawerOpen());
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			setActionBar();
//			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
//		getFragmentManager().beginTransaction()
//		.replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//		.commit();
//		return true;
		return false;
	}


	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
				getActionBar().getSelectedNavigationIndex());
	}

	//	private void setBundleKey(String key) {
	//
	//	}

	static public void changeFragment(Fragment fragment) {
		fragmentManager.beginTransaction()
		.replace(R.id.container, fragment)//PlaceholderFragment.newInstance(position + 1))
		.commit();
	}

	
	private void setActionBar() {
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		
		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
				// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(
						actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1,
						new String[] {"search", "home", "month", "listview", "[폴 더]", "폴더명1", "폴더명2", "폴더명3", "tag"
						}),
						this);

		
//		new String[] {
//				getString(R.string.title_section1),
//				getString(R.string.title_section2),
//				getString(R.string.title_section3),
//			}),
	}
}

