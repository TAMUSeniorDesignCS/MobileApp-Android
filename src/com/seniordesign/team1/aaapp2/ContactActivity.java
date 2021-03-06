package com.seniordesign.team1.aaapp2;

import java.util.HashMap;

import com.seniordesign.team1.aaapp2.ContactsContract.ContactEntry;
import com.seniordesign.team1.aaapp2.ContactsContract.ConversationEntry;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactActivity extends Activity implements OnCheckedChangeListener{
	private ContactActivity _this;
	AlertDialogManager alert = new AlertDialogManager();
	SharedPreferences user_prefs;
	Editor user_editor; 
	String selected_user = ContactsFragment.selected_username;
	private String firstname, phone, email;
	private static final int TITLE_FONT_SIZE = 48;
	private static final int CONTENT_FONT_SIZE = 24;
	private static final int SUBTITLE_FONT_SIZE = 24;
	private static final int SUBCONTENT_FONT_SIZE = 24;
	private static final String COLOR = "black";
	private static final String FACE = "verdana";
	CheckBox setAsSponsor, blockUser;
	public static HashMap<String, Boolean> sponsor_map = new HashMap<String, Boolean>(); //map to set the username to sponsor = true
	public static HashMap<String, Boolean> blocked_map = new HashMap<String, Boolean>(); //map to set the username to blocked = true
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_page);
		
		user_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		user_editor = user_prefs.edit();
		setAsSponsor = (CheckBox) findViewById(R.id.set_sponsor);
		if(sponsor_map.get(selected_user) != null){
			setAsSponsor.setChecked(user_prefs.getBoolean("SETASSPONSOR", false));
		}	
		blockUser = (CheckBox) findViewById(R.id.block_user);
		if(blocked_map.get(selected_user)!= null){
			blockUser.setChecked(user_prefs.getBoolean("BLOCKUSER", false));
		}
		setAsSponsor.setOnCheckedChangeListener(this);
		blockUser.setOnCheckedChangeListener(this);
		
		LinearLayout first_name_layout = (LinearLayout) findViewById(R.id.first_name_layout);
		LinearLayout user_name_layout = (LinearLayout) findViewById(R.id.user_name_layout);
		LinearLayout phone_layout = (LinearLayout) findViewById(R.id.phone_layout);
		LinearLayout email_layout = (LinearLayout) findViewById(R.id.email_layout);
		try {
			
			ContactDbHelper cDbHelper = new ContactDbHelper(getApplicationContext());
			SQLiteDatabase db = cDbHelper.getWritableDatabase();
			String[] projection = {
					ContactEntry.COLUMN_FIRST_NAME,
					ContactEntry.COLUMN_USERNAME,
					ContactEntry.COLUMN_EMAIL,
					ContactEntry.COLUMN_PHONE
				    };
			String sortOrder = null;
			String selection = //WHERE (receiver = ? AND user = ?) OR (receiver = ? AND user = ?)
					ContactEntry.COLUMN_USERNAME + " = ?";
					
			String[] selectionArgs = {
					selected_user
				};
					
			Cursor contacts = db.query(
					true,									  // Distinct rows
					ContactEntry.TABLE_NAME,  			 	  // The table to query
				    projection,                               // The columns to return
				    selection,                                // The columns for the WHERE clause (WHERE receivername = intended receiver)
				    selectionArgs,                            // The values for the WHERE clause
				    null,                                     // don't group the rows
				    null,                                     // don't filter by row groups
				    sortOrder,                                // The sort order
				    null 									  // No limit
				    );
			contacts.moveToFirst();
			int firstname_index = contacts.getColumnIndex(ContactEntry.COLUMN_FIRST_NAME);
			int email_index = contacts.getColumnIndex(ContactEntry.COLUMN_EMAIL);
			int phone_index = contacts.getColumnIndex(ContactEntry.COLUMN_PHONE);
			while(!contacts.isAfterLast()){
				try {
					firstname = contacts.getString(firstname_index);
					email = contacts.getString(email_index);
					phone = contacts.getString(phone_index);
					
					TextView firstname_textview = new TextView(getApplicationContext());
					firstname_textview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams fn_params = (LinearLayout.LayoutParams)firstname_textview.getLayoutParams();
					fn_params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
					firstname_textview.setLayoutParams(fn_params);
					firstname_textview.setText(Html.fromHtml("<font size=" + TITLE_FONT_SIZE + " color=" + COLOR + " face =" + FACE + ">" + firstname + "</font>"));
					firstname_textview.setTextSize(TITLE_FONT_SIZE);
					if (Build.VERSION.SDK_INT >= 16){
						firstname_textview.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
					} 
					else{
						firstname_textview.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
					}
					first_name_layout.addView(firstname_textview);
					first_name_layout.invalidate();
					
					TextView username_textview = new TextView(getApplicationContext());
					username_textview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams un_params = (LinearLayout.LayoutParams)username_textview.getLayoutParams();
					un_params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
					username_textview.setLayoutParams(un_params);
					username_textview.setText(Html.fromHtml("<br/>" + "<font size=" + CONTENT_FONT_SIZE + " color=" + COLOR + " face =" + FACE + "> @" + selected_user + "</font>" +  "<br/>"));
					username_textview.setTextSize(CONTENT_FONT_SIZE);
					if (Build.VERSION.SDK_INT >= 16){
						username_textview.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
					} 
					else{
						username_textview.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
					}
					user_name_layout.addView(username_textview);
					user_name_layout.invalidate();
					
					if(!phone.equals(null)){
						TextView phone_textview = new TextView(getApplicationContext());
						phone_textview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
						LinearLayout.LayoutParams p_params = (LinearLayout.LayoutParams)phone_textview.getLayoutParams();
						p_params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
						phone_textview.setLayoutParams(p_params);
						phone_textview.setText(Html.fromHtml("<br/>" + "<font size=" + SUBTITLE_FONT_SIZE + " color=" + COLOR + " face =" + FACE + "> Phone: " + phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-"+ phone.substring(6) + "</font>" + "<br/>"));
						username_textview.setTextSize(SUBTITLE_FONT_SIZE);
						if (Build.VERSION.SDK_INT >= 16){
							phone_textview.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
						} 
						else{
							phone_textview.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
						}
						phone_layout.addView(phone_textview);
						phone_layout.invalidate();
					}
					
					if(!email.equals(null) || !email.equals("")){
						TextView email_textview = new TextView(getApplicationContext());
						email_textview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
						LinearLayout.LayoutParams e_params = (LinearLayout.LayoutParams)email_textview.getLayoutParams();
						e_params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
						email_textview.setLayoutParams(e_params);
						email_textview.setText(Html.fromHtml("<br/>" + "<font size=" + SUBTITLE_FONT_SIZE + " color=" + COLOR + " face =" + FACE + "> Email: " + email + "</font>" + "<br/>"));
						email_textview.setTextSize(SUBCONTENT_FONT_SIZE);
						if (Build.VERSION.SDK_INT >= 16){
							email_textview.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
						} 
						else{
							email_textview.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
						}
						email_layout.addView(email_textview);
						email_layout.invalidate();
					}
					
					
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				contacts.moveToNext();
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView == setAsSponsor){
			if(isChecked){
				user_editor.putString("SPONSOR", selected_user);
				user_editor.putString("SPONSOR_PHONE", phone);
				user_editor.putBoolean("SETASSPONSOR", true);
				user_editor.commit();
				
				//wipe and update the local sponsor info
				sponsor_map.clear();
				sponsor_map.put(selected_user, true);
				
				//TODO: send member update with new sponsor info
				String my_user, my_firstname, my_password, my_email, my_phone;
				Boolean display_phone;
				my_user = user_prefs.getString("USERNAME", null);
				my_password = user_prefs.getString("PASSWORD", null);
				my_firstname = user_prefs.getString("FIRSTNAME", null);
				my_email = user_prefs.getString("EMAIL", null);
				my_phone = user_prefs.getString("PHONE", null);
				display_phone = user_prefs.getBoolean("pref_sharePhone", false);
				
				NetworkAsyncTask update_info_task = new NetworkAsyncTask(this);
				String urlVariables = "member/edit?rusername=" + my_user + "&rpassword=" + my_password + "&oldusername=" + my_user + "&firstname=" + my_firstname + "&username=" + my_user + "&password=" + my_password + "&sponsorid=" + selected_user + "&email=" + my_email + "&phonenumber=" + my_phone + "&displayphonenumber=" + display_phone; 
				update_info_task.execute(NetworkAsyncTask.serverLit + urlVariables);
				
				return;
			} else {
				//this should either be called only if the state changes, in which case we want to update the sponsor to null
				//or it will be called no matter what, in which case we would only want to update the sponsor if this was previously the sponor
				user_editor.putBoolean("SETASSPONSOR", false);
				user_editor.commit();
				sponsor_map.put(selected_user, false);
				
				//TODO:send member update with sponsor set to null
				String my_user, my_firstname, my_password, my_email, my_phone;
				int display_phone;
				my_user = user_prefs.getString("USERNAME", null);
				my_password = user_prefs.getString("PASSWORD", null);
				my_firstname = user_prefs.getString("FIRSTNAME", null);
				my_email = user_prefs.getString("EMAIL", null);
				my_phone = user_prefs.getString("PHONE", null);
				display_phone = HelperFunctions.boolToInt(user_prefs.getBoolean("pref_sharePhone", false));
				
				NetworkAsyncTask update_info_task = new NetworkAsyncTask(this);
				String urlVariables = "member/edit?rusername=" + my_user + "&rpassword=" + my_password + "&oldusername=" + my_user + "&firstname=" + my_firstname + "&username=" + my_user + "&password=" + my_password + "&sponsorid=" + "null" + "&email=" + my_email + "&phonenumber=" + my_phone + "&displayphonenumber=" + display_phone; 
				update_info_task.execute(NetworkAsyncTask.serverLit + urlVariables);
				
				return;
			}
		}
		if (buttonView == blockUser){
			if(isChecked){
				user_editor.putBoolean("BLOCKUSER", true);
				user_editor.commit();
				blocked_map.put(selected_user, true);
				return;
			} else {
				user_editor.putBoolean("BLOCKUSER", false);
				user_editor.commit();
				blocked_map.put(selected_user, false);
				return;
			}
		}
	}
}

