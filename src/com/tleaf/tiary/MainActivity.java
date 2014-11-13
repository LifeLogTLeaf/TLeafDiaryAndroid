package com.tleaf.tiary;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.tleaf.tiary.core.AppContext;
import com.tleaf.tiary.db.DataManager;
import com.tleaf.tiary.fragment.DiaryEditFragment;
import com.tleaf.tiary.fragment.DiaryListViewFragement;
import com.tleaf.tiary.fragment.DiaryMonthFragment;
import com.tleaf.tiary.fragment.EmotionFragement;
import com.tleaf.tiary.fragment.ExpandableListFragment;
import com.tleaf.tiary.fragment.HomeFragement;
import com.tleaf.tiary.fragment.ShackFragment;
import com.tleaf.tiary.fragment.TagFragement;
import com.tleaf.tiary.fragment.lifelog.MyLifeLogFragement;
import com.tleaf.tiary.util.MyPreference;
import com.tleaf.tiary.util.MyTime;
import com.tleaf.tiary.util.Util;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	// private String bundelKey;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private DataManager dataMgr;

	private static FragmentManager fragmentManager;
	private boolean exitApplication = false;
	private Fragment mFragment;

	public static Handler mHandler = new Handler();
	
	private MyPreference pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dataMgr = new DataManager(getApplicationContext());
		pref = new MyPreference(getApplicationContext());

		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.rgb(0, 153, 237)));

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		// if (savedInstanceState != null) {
		// mFragment = getFragmentManager().getFragment(savedInstanceState,
		// "mFragment");
		// }
		//
		// if (mFragment != null) {
		// fragmentManager = getFragmentManager();
		// fragmentManager.beginTransaction().replace(R.id.container, mFragment)
		// .commit();
		// }
		
		initializingLog();
	}
	private void initializingLog() {
		setInstallTime();
	}
	
	private void setInstallTime() {
		if(!pref.getBooleanPref(Common.KEY_INSTALLATION)) { 
			long currentTime = MyTime.getCurrentTime();
			Util.ll(Common.KEY_INSTALL_TIME, MyTime.getLongToString(currentTime));
			
			pref.setLongPref(Common.KEY_INSTALL_TIME, currentTime);
			pref.setBooleanPref(Common.KEY_INSTALLATION, true);
			
			Util.ll("installation true", pref.getBooleanPref(Common.KEY_INSTALLATION));
		}
	}

	@Override
	public boolean onNavigationDrawerItemSelected(int position,
			int childPosition) {
		// update the main content by replacing fragments
		fragmentManager = getFragmentManager();
		Fragment fragment = null;

		if (childPosition == -1) {
			switch (position) {
			case Common.HOME:
				fragment = new DiaryMonthFragment(this);// HomeFragement();DiaryListViewFragement DiaryMonthFragment(this)
				break;
			case Common.MYLIFTLOG:
				fragment = new MyLifeLogFragement();// MyPageFragement();
				// //ExpandableListFragment
				break;
			case Common.WRITE:
				fragment = new DiaryEditFragment();// WriteFragement();
				break;
			case Common.FOLDER:
				return true;
			case Common.TAG:
				fragment = new TagFragement();
				break;
			case Common.EMOTION:
				fragment = new EmotionFragement();
				break;
			case Common.SHACK:
				fragment = new ShackFragment();
				break;
			case Common.SETIING:
				fragment = new ExpandableListFragment(); // SettingFragement();
				break;
			}
		} else {
			Util.tst(this, "childPosition " + childPosition);
			fragment = new DiaryListViewFragement(dataMgr
					.getDistinctFolderList().get(childPosition));
		}

		// fragmentManager.beginTransaction()
		// .replace(R.id.container,
		// fragment)//PlaceholderFragment.newInstance(position + 1))
		// .commit();

//		changeFragment(fragment, false);
		
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.container, fragment);// PlaceholderFragment.newInstance(position
		// + 1))
		clearBackStack();
		transaction.commit();

		return false;
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.home);
			break;
		case 2:
			mTitle = getString(R.string.mylifelog);
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
			Util.tst(this, "mNavigationDrawerFragment.isDrawerOpen()"
					+ mNavigationDrawerFragment.isDrawerOpen());
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			// setActionBar();
			restoreActionBar();
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
		// getFragmentManager().beginTransaction()
		// .replace(R.id.container, PlaceholderFragment.newInstance(position +
		// 1))
		// .commit();
		// return true;
		return false;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			Util.ll("getActionBar().getNavigationItemCount()", getActionBar()
					.getNavigationItemCount());
			if (getActionBar().getNavigationItemCount() != 0) {
				getActionBar().setSelectedNavigationItem(
						savedInstanceState
						.getInt(STATE_SELECTED_NAVIGATION_ITEM));
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	// private void setBundleKey(String key) {
	//
	// }

	private void setActionBar() {
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
				// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] { "search", "home",
					"month", "listview", "[폴 더]", "폴더명1", "폴더명2",
					"폴더명3", "tag" }), this);

		// new String[] {
		// getString(R.string.title_section1),
		// getString(R.string.title_section2),
		// getString(R.string.title_section3),
		// }),
	}

	// 디비의 폴더리스트가 변경시 drawer를 refresh
	public void refreshDrawer() {
		mNavigationDrawerFragment.refreshDrawer();
	}

	static public void changeFragment(Fragment fragment) {

		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.container, fragment);// PlaceholderFragment.newInstance(position
		// + 1))
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void clearBackStack() {
		int cnt = fragmentManager.getBackStackEntryCount();
		for (int i = 0; i < cnt; i++) {
			fragmentManager.popBackStack();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(!AppContext.getPreference().getBooleanPref("isAutoLogin")) AppContext.getPreference().setBooleanPref("isLogin", false);
	}
	
	

	// public void switchContent(Fragment fragment, boolean addBackStack) {
	// exitApplication = false;
	// // mFragment = fragment;
	//
	// fragmentManager = getFragmentManager();
	//
	// if (!addBackStack) {
	// fragmentManager.beginTransaction()
	// .replace(R.id.container, fragment).commit();
	// } else {
	// fragmentManager.beginTransaction()
	// .replace(R.id.container, fragment).addToBackStack(null)
	// .commit();
	// }
	//
	// }

	// @Override
	// public void onBackPressed() {
	// // If the fragment exists and has some back-stack entry
	// if (MainActivity.this != null
	// && MainActivity.this.getFragmentManager()
	// .getBackStackEntryCount() > 0) {
	// // Get the fragment fragment manager - and pop the backstack
	// MainActivity.this.getFragmentManager().popBackStack();
	// }
	// // Else, nothing in the direct fragment back stack
	// else {
	// if (exitApplication == false) {
	// Util.tst(getApplicationContext(), "한번 더 누르면 종료");
	// exitApplication = true;
	// mHandler.postDelayed(new Runnable() {
	// @Override
	// public void run() {
	// exitApplication = false;
	// }
	// }, 3000);
	// } else {
	// super.onBackPressed();
	// }
	//
	// }
	// }

}
