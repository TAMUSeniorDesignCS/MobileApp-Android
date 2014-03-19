package com.seniordesign.team1.aaapp2;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	/**
	 * 
	 */
	private MainActivity mainActivity;

	public SectionsPagerAdapter(MainActivity mainActivity, FragmentManager fm) {
		super(fm);
		this.mainActivity = mainActivity;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		Fragment fragment;
		if(position == 0){ //Home page fragment
			NetworkAsyncTask quoteTask = new NetworkAsyncTask();
			quoteTask.execute(NetworkAsyncTask.quoteLit);
			fragment = new QuoteFragment();
			Bundle args = new Bundle();
			String quote = "";
			String title = "";
			try {
				String[] r1 = quoteTask.get(5, TimeUnit.SECONDS).split("\\+\\+\\+");
				quote = r1[0];
				title = r1[1];
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			args.putString(QuoteFragment.TITLE, title);
			args.putString(QuoteFragment.QUOTE, quote);
			fragment.setArguments(args);
			return fragment;
		}//else if (position == 1){ //Posts page fragment
			
		//}
		else{
			fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}
	}

	@Override
	public int getCount() {
		// Show 4 total pages.
		return 4;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return this.mainActivity.getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return this.mainActivity.getString(R.string.title_section2).toUpperCase(l);
		case 2:
			return this.mainActivity.getString(R.string.title_section3).toUpperCase(l);
		case 3:
			return this.mainActivity.getString(R.string.title_section4).toUpperCase(l);
		}
		return null;
	}
}