package com.seniordesign.team1.aaapp2;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniordesign.team1.aaapp2.ContactsContract.ContactEntry;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WriteMailActivity extends Activity {
	SharedPreferences login_prefs;
	Editor login_editor; 
	private WriteMailActivity _this;
	AlertDialogManager alert = new AlertDialogManager();
	//NetworkAsyncTask newMailTask;
	String receiverusername = "happy123"; //hardcoded until receiver can be selected
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_mail_page);
		SQLiteAsyncQuery db = new SQLiteAsyncQuery(getApplicationContext());
		db.execute("SELECT " + ContactEntry.COLUMN_FIRST_NAME + ", " + ContactEntry.COLUMN_USERNAME + 
				" FROM " + ContactEntry.TABLE_NAME);
		//newPostTask = new NetworkAsyncTask(this);
		Button writeMailButton = (Button) findViewById(R.id.submit_mail);
		writeMailButton.setOnClickListener(mWriteMailButton);
		
		LinearLayout contactsMailView = (LinearLayout) findViewById(R.id.contactsMailView);
		
		try {
			List<Cursor> cursors = db.get();
			Cursor cursor = cursors.get(0);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				TextView newMailContact = new TextView(getApplicationContext());
				newMailContact.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)newMailContact.getLayoutParams();
				params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
				newMailContact.setLayoutParams(params);
				if (Build.VERSION.SDK_INT >= 16){
					newMailContact.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
				} 
				else{
					newMailContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
				}
				
				
				newMailContact.setText(Html.fromHtml("<b>" + cursor.getString(cursor.getColumnIndex("firstname")) + "</b> @" + cursor.getString(cursor.getColumnIndex("username")) + "<br/>" ));
				contactsMailView.addView(newMailContact);
				contactsMailView.invalidate();
				cursor.moveToNext();
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
			String password = login_prefs.getString("PASSWORD", null);
			int mail_timeout = login_prefs.getInt("pref_messTimeAmmount", 32);
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
				sendMailTask.execute(NetworkAsyncTask.serverLit + "directmessage/new?username=" + username + "&message=" + newMailString + "&timeout=" + mail_timeout + "&receiversusername=" + receiverusername + "&rusername=" + username + "&rpassword" + password);
				try{
					response = sendMailTask.get(20, TimeUnit.SECONDS);
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
					return;
				}
			}
			
        }
		
	};
}

