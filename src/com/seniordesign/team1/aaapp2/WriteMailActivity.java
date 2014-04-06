package com.seniordesign.team1.aaapp2;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WriteMailActivity extends Activity {
	SharedPreferences login_prefs;
	Editor login_editor; 
	private WriteMailActivity _this;
	AlertDialogManager alert = new AlertDialogManager();
	//NetworkAsyncTask newMailTask;
	
	public void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_mail_page);
		
		//newPostTask = new NetworkAsyncTask(this);
		Button writeMailButton = (Button) findViewById(R.id.submit_mail);
		writeMailButton.setOnClickListener(mWriteMailButton);
	}
	
	public void onCancelClick(View v) { 	  
        this.finish();  
    }  
	
	private OnClickListener mWriteMailButton = new OnClickListener() { 	
		
		@Override
        public void onClick(View v) {
			login_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			EditText newMail = (EditText)findViewById(R.id.mail_box);
			String newMailString = newMail.getText().toString();
			String username = login_prefs.getString("USERNAME", "defualt value"); 
			int timeout = 48; //this will change to use the settings preference
			String response = "";
			JSONArray json_array = null;
			JSONObject json_object = null;
			
			
			if (newMailString.length() == 0) {
				alert.showAlertDialog(v.getContext(), "Entry error", "No text to send.", false);
				return;
			} else {
				//get mail duration from preferences:
				//SharedPreferences login_prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
				//int mail_timeout = login_prefs.getInt("pref_mailTimeAmmount", false);
				
				NetworkAsyncTask sendMailTask = new NetworkAsyncTask(_this);
				sendMailTask.execute(NetworkAsyncTask.serverLit + "mail/new?username=" + username + "&message=" + newMailString + "&timeout=" + timeout);
				try{
					response = sendMailTask.get(5, TimeUnit.SECONDS);
					json_array = new JSONArray(response);
					
					if(HelperFunctions.isJSONValid(json_array)){
						json_object = json_array.getJSONObject(0); //this is the message object that we just sent
						
						//TODO when refresh is working, do a refresh here
						Intent i = new Intent(getApplicationContext(), MainActivity.class);
		                //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		                startActivity(i);
		                finish();
						return;
					} else {//invalid JSON object
						
						alert.showAlertDialog(v.getContext(), "Invalid request", "Server sent 'JSON is not valid'", false);
						return;
					}
				}catch (JSONException e){//NOT a JSON object
					alert.showAlertDialog(v.getContext(), "Invalid request", "Response from server: " + e.toString(), false);
					return;
					
				}
				catch (Exception e){
					alert.showAlertDialog(v.getContext(), "Exception", "System message: " + e.toString(), false);
					//setResult(e.toString());
				}
			}
			
        }
		
	};
}

