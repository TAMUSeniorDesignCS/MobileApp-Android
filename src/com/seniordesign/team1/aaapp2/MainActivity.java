package com.seniordesign.team1.aaapp2;

import com.seniordesign.team1.aaapp2.EditPostDialogFragment.EditPostDialogListener;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener,
		EditPostDialogListener{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences login_settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		boolean logged_in = login_settings.getBoolean("loggedIn", false); 						//Is going to need to be a persistent data object
		
		if(!logged_in){
			Intent intent = new Intent(this, LoginActivity.class);
			//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}else if(logged_in){
			
			if(login_settings.getBoolean("firstLaunch", true)){
				Editor login_editor = login_settings.edit();
				login_editor.putBoolean("firstLaunch", false);
				login_editor.commit();
				Intent intent = new Intent(this, DemoActivity.class);
				startActivity(intent);
			}
			 
			
			
			setContentView(R.layout.activity_main);
	
			// Set up the action bar.
			final ActionBar actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
			// Create the adapter that will return a fragment for each of the three
			// primary sections of the app.
			mSectionsPagerAdapter = new SectionsPagerAdapter(
					this, getSupportFragmentManager());
	
			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
	
			// When swiping between different sections, select the corresponding
			// tab. We can also use ActionBar.Tab#select() to do this if we have
			// a reference to the Tab.
			mViewPager
					.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							actionBar.setSelectedNavigationItem(position);
						}
					});
	
			// For each of the sections in the app, add a tab to the action bar.
			for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
				// Create a tab with text corresponding to the page title defined by
				// the adapter. Also specify this Activity object, which implements
				// the TabListener interface, as the callback (listener) for when
				// this tab is selected.
				actionBar.addTab(actionBar.newTab()
						.setText(mSectionsPagerAdapter.getPageTitle(i))
						.setTabListener(this));
			}
		}
	}
	
	public void serverRefresh(){
		this.mSectionsPagerAdapter.serverRefresh();
		mViewPager = (ViewPager) findViewById(R.id.pager);
	    /*Fragment newFragment = this.mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
	    android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    //transaction.replace(this.mSectionsPagerAdapter.getItemId(mViewPager.getCurrentItem()), newFragment);
	    Fragment frag = this.mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
	    transaction.remove(frag);
	    //transaction.attach(frag);
	    transaction.add(R.id.pager, newFragment);
	    //transaction.addToBackStack(null);
	    transaction.commit();*/
		//this.mSectionsPagerAdapter.startUpdate(findViewById(R.id.pager));
		
		//this.mSectionsPagerAdapter.notifyDataSetChanged();
		this.mSectionsPagerAdapter.updatePage(mViewPager.getCurrentItem());
		//for(int i=0; i<this.mSectionsPagerAdapter.getCount(); i++){
		//	this.mSectionsPagerAdapter.updatePage(i);
		//}
		this.mSectionsPagerAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onFinishEditPostDialogFragment(int selection, int postId){
		if(selection == 0){
			//launch activity with text from postId.
			//get the string
			String value = this.mSectionsPagerAdapter.getPostTextById(postId);
			Intent intent_new_post = new Intent(this, WritePostActivity.class);
			Bundle b = new Bundle();
			b.putInt("postIdToEdit", postId);
			b.putString("toEdit", value);
			intent_new_post.putExtras(b);
			startActivity(intent_new_post);
		}
		if(selection == 1){
			this.mSectionsPagerAdapter.quickRemovePost(postId);
			this.mSectionsPagerAdapter.updatePage(1);
			this.mSectionsPagerAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			case R.id.action_settings:
				//show settings menu
				Intent intent_settings = new Intent(this, SettingsActivity.class);
				startActivity(intent_settings);
				return true;
			case R.id.action_logout:
				//log out
				SharedPreferences login_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				Editor login_editor = login_prefs.edit();
				
				//login_editor.clear();
				login_editor.remove("FIRSTNAME");// value to clear
                login_editor.remove("USERNAME"); 
                login_editor.remove("PASSWORD");
                login_editor.remove("GROUPID");
                login_editor.remove("EMAIL");
                login_editor.remove("SPONSORID");
                login_editor.putBoolean("loggedIn", false);
				login_editor.commit();
				
				//delete the database
				getApplicationContext().deleteDatabase(ContactDbHelper.DATABASE_NAME);
				
				Intent intent_logout = new Intent(this, LoginActivity.class);
				intent_logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent_logout);
				return true;
			case R.id.action_new_post:
				//launch new_post_activity
				Intent intent_new_post = new Intent(this, WritePostActivity.class);
				startActivity(intent_new_post);
				return true;
			case R.id.action_new_mail:
				//launch new_mail_activity
				Intent intent_new_mail = new Intent(this, WriteMailActivity.class);
				startActivity(intent_new_mail);
				return true;
			case R.id.action_refresh:
				this.serverRefresh();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
