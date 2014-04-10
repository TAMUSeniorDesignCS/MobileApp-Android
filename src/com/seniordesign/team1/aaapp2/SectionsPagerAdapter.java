package com.seniordesign.team1.aaapp2;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniordesign.team1.aaapp2.ContactsContract.ContactEntry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
	private final String emptyNetwork = "---EMPTY---";
	private String response = emptyNetwork;
	private NetworkAsyncTask networkTask;

	public SectionsPagerAdapter(MainActivity mainActivity, FragmentManager fm) {
		super(fm);
		this.mainActivity = mainActivity;
		this.networkTask = new NetworkAsyncTask(this.mainActivity);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mainActivity.getApplicationContext());
		int groupNo = prefs.getInt("GROUPID", -1);
		String username = prefs.getString("USERNAME", null);
		String password = prefs.getString("PASSWORD", null);
		//replace www.google.com with mail stuff
		this.networkTask.execute(NetworkAsyncTask.quoteLit,NetworkAsyncTask.serverLit + "post/refresh?groupid=" + groupNo + "&rusername=" + username + "&rpassword=" + password,"www.google.com",NetworkAsyncTask.serverLit + "member/getinfo?groupid=" + groupNo + "&rusername=" + username + "&rpassword=" + password);
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		if(this.response.equals(this.emptyNetwork)){
			//get the network stuff
			try {
				this.response = this.networkTask.get(10, TimeUnit.SECONDS);
				String[] reps = this.response.split("===");
				if(reps.length == 4){
					try{
						ArrayList<String> queries = new ArrayList<String>();
						JSONArray json = new JSONArray(reps[3]);
						for(int i=0; i<json.length()-1; i++){
							JSONObject obj = json.getJSONObject(i);
							String userName = obj.getString("username");
							String firstName = obj.getString("firstname");
							String phone = obj.getString("phonenumber");
							String email = obj.getString("email");
							String query = "INSERT OR IGNORE INTO " + ContactEntry.TABLE_NAME + 
									" (" + ContactEntry.COLUMN_USERNAME + ", " +
									ContactEntry.COLUMN_FIRST_NAME + ", " +
									ContactEntry.COLUMN_PHONE + ", " +
									ContactEntry.COLUMN_EMAIL + ") VALUES (" +
									"'" + userName + "', " + "'" + firstName + "', ";
							if(phone.equalsIgnoreCase("null")){
								query += "NULL, ";
							}
							else{
								query += "'" + phone + "', ";
							}
							if(email.equalsIgnoreCase("null")){
								query += "NULL)";
							}
							else{
								query += "'" + email + "')";
							}
							queries.add(query);
									
						}
						SQLiteAsyncSQL db = new SQLiteAsyncSQL(this.mainActivity);
						db.execute(queries.toArray(new String[queries.size()]));
					}
					catch(JSONException e){
						e.printStackTrace();
					}
				}
			} catch (InterruptedException e) { //DAMN YOU JAVA 6!
				this.response = NetworkAsyncTask.errorLit;
				e.printStackTrace();
			} catch (ExecutionException e) {
				this.response = NetworkAsyncTask.errorLit;
				e.printStackTrace();
			} catch (TimeoutException e) {
				this.response = NetworkAsyncTask.errorLit;
				e.printStackTrace();
			}
		}
		Fragment fragment;
		if(position == 0){ //Home page fragment
			fragment = new QuoteFragment();
			Bundle args = new Bundle();
			SharedPreferences user_prefs = PreferenceManager.getDefaultSharedPreferences(this.mainActivity);
			String quote = "";
			String title = "";
			String userWelcome = "Hi, " + user_prefs.getString("FIRSTNAME", "[name not found]") + "!";
			String qotd = "Quote of the Day";
			
			String resp = this.response.split("===")[0];
			if(!resp.equals(NetworkAsyncTask.errorLit)){
				String[] r1 = resp.split("\\+\\+\\+");
				quote = r1[0];
				title = r1[1];
			}
			else{
				quote = "";
				title = "Sorry, no network connection."; //TODO: don't have strings out of xml
			}

			args.putString(QuoteFragment.TITLE, title);
			args.putString(QuoteFragment.QUOTE, quote);
			args.putString(QuoteFragment.USER, userWelcome);
			args.putString(QuoteFragment.QOTD, qotd);
			fragment.setArguments(args);
			return fragment;
		}
		else if (position == 1){ //Posts page fragment
			String resps[] = this.response.split("===");
			String resp = "";
			if(resps.length >= 2){
				resp = resps[1];
			}
			Bundle args = new Bundle();
			fragment = new PostsFragment();
			args.putString(PostsFragment.POSTS, resp);
			fragment.setArguments(args);
			return fragment;
		}
		else if (position == 3){
			//TODO 
			fragment = new ContactsFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		} else if(position == 2){ 	//Mail fragment
			NetworkAsyncTask mailTask = new NetworkAsyncTask(this.mainActivity);
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mainActivity.getApplicationContext());
			String username = prefs.getString("USERNAME", null);
			String password = prefs.getString("PASSWORD", null);
			String response = "";
			Bundle args = new Bundle();
			fragment = new MailFragment();
			if(username != null){
				mailTask.execute(NetworkAsyncTask.serverLit + "directmessage/refresh?username=" + username + "&directmessageidlimit=" + "-" + "&rusername=" + username + "&rpassword=" + password);
				try{
					response = mailTask.get(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
			}
			args.putString(MailFragment.MAIL, response);
			fragment.setArguments(args);
			return fragment;
		}
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