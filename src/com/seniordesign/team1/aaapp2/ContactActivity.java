package com.seniordesign.team1.aaapp2;

import com.seniordesign.team1.aaapp2.ContactsContract.ContactEntry;
import com.seniordesign.team1.aaapp2.ContactsContract.ConversationEntry;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactActivity extends Activity{
	private ContactActivity _this;
	AlertDialogManager alert = new AlertDialogManager();
	SharedPreferences user_prefs;
	String selected_user = ContactsFragment.selected_username;
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_page);
		
		user_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		//
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
					String firstname = contacts.getString(firstname_index);
					String email = contacts.getString(email_index);
					String phone = contacts.getString(phone_index);
					
					TextView firstname_textview = new TextView(getApplicationContext());
					firstname_textview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams fn_params = (LinearLayout.LayoutParams)firstname_textview.getLayoutParams();
					fn_params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
					firstname_textview.setLayoutParams(fn_params);
					firstname_textview.setText(Html.fromHtml("<b>" + firstname + "</b>" +  "<br/>"));
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
					firstname_textview.setText(Html.fromHtml("<b>" + selected_user + "</b>" +  "<br/>"));
					if (Build.VERSION.SDK_INT >= 16){
						username_textview.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
					} 
					else{
						username_textview.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
					}
					user_name_layout.addView(username_textview);
					user_name_layout.invalidate();
					
					//if(not null)
					TextView phone_textview = new TextView(getApplicationContext());
					phone_textview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams p_params = (LinearLayout.LayoutParams)phone_textview.getLayoutParams();
					p_params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
					phone_textview.setLayoutParams(p_params);
					firstname_textview.setText(Html.fromHtml("<b>" + phone + "</b>" +  "<br/>"));
					if (Build.VERSION.SDK_INT >= 16){
						phone_textview.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
					} 
					else{
						phone_textview.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
					}
					phone_layout.addView(phone_textview);
					phone_layout.invalidate();
					
					//if(not null)
					TextView email_textview = new TextView(getApplicationContext());
					email_textview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams e_params = (LinearLayout.LayoutParams)email_textview.getLayoutParams();
					e_params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
					email_textview.setLayoutParams(e_params);
					firstname_textview.setText(Html.fromHtml("<b>" + email + "</b>" +  "<br/>"));
					if (Build.VERSION.SDK_INT >= 16){
						email_textview.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
					} 
					else{
						email_textview.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
					}
					email_layout.addView(email_textview);
					email_layout.invalidate();
					
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
		if(isChecked){
			//receiverusername = (String)contactList.get(buttonView); //grabs value from the hashMap
			return;
		} else {
			//receiverusername = "";
			//alert.showAlertDialog(getApplicationContext(), "No recipient selected.", "Select a contact as a recipient.", false);
			return;
		}
		
	}
}

