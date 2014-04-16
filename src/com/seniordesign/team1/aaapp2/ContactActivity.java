package com.seniordesign.team1.aaapp2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
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

