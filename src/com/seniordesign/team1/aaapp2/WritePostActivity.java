package com.seniordesign.team1.aaapp2;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WritePostActivity extends Activity {
	SharedPreferences login_prefs;
	Editor login_editor; 
	private WritePostActivity _this;
	AlertDialogManager alert = new AlertDialogManager();
	//NetworkAsyncTask newPostTask;
	
	public void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_post_page);
		
		//newPostTask = new NetworkAsyncTask(this);
		Button writePostButton = (Button) findViewById(R.id.submit_post);
		writePostButton.setOnClickListener(mWritePostButton);
	}
	
	public void onCancelClick(View v) { 	  
        this.finish();  
    }  
	
	private OnClickListener mWritePostButton = new OnClickListener() { 	
		
		@Override
        public void onClick(View v) {
			login_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			EditText newPost = (EditText)findViewById(R.id.post_box);
			String newPostString = newPost.getText().toString();
			String username = login_prefs.getString("USERNAME", "defualt value"); 
			int timeout = 48; //this will change to use the settings preference
			String response = "";
			JSONArray json_array = null;
			JSONObject json_object = null;
			
			
			if (newPostString.length() == 0) {
				alert.showAlertDialog(v.getContext(), "Entry error", "No text to send.", false);
				return;
			} else {
				//get post duration from preferences:
				//SharedPreferences login_prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
				//int post_timeout = login_prefs.getInt("pref_postTimeAmmount", false);
				
				NetworkAsyncTask sendPostTask = new NetworkAsyncTask(_this);
				sendPostTask.execute(NetworkAsyncTask.serverLit + "post/new?username=" + username + "&message=" + newPostString + "&timeout=" + timeout);
				try{
					response = sendPostTask.get(5, TimeUnit.SECONDS);
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
