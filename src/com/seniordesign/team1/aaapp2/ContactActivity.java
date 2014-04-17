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
import android.widget.CompoundButton;

public class ContactActivity extends Activity{
	private ContactActivity _this;
	AlertDialogManager alert = new AlertDialogManager();
	SharedPreferences user_prefs;
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_page);
		
		user_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String username = user_prefs.getString("USERNAME", null);
		
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
					username
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
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

