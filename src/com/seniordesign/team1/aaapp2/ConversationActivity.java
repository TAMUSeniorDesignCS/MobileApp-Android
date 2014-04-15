package com.seniordesign.team1.aaapp2;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniordesign.team1.aaapp2.ContactsContract.ConversationEntry;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConversationActivity extends Activity{
	private ConversationActivity _this;
	AlertDialogManager alert = new AlertDialogManager();
	SharedPreferences user_prefs;
	
	//NetworkAsyncTask newPostTask;
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversations_page);
		
		user_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String username = user_prefs.getString("USERNAME", null);
		
		//View rootView = inflater.inflate(R.layout.conversations_page,
				//container, false);
		//ViewGroup vg = findViewById(R.layout.conversations_page);
		LinearLayout conversationView = (LinearLayout) findViewById(R.id.conversationView);
		Button writeMailButton = (Button) findViewById(R.id.send_message_button);
		writeMailButton.setOnClickListener(mSendMessageButton);
		
		try {
			
			ContactDbHelper cDbHelper = new ContactDbHelper(getApplicationContext());
			SQLiteDatabase db = cDbHelper.getWritableDatabase();
			String[] projection = {
					ConversationEntry.COLUMN_USERNAME,
					ConversationEntry.COLUMN_RECEIVERSUSERNAME,
					ConversationEntry.COLUMN_MESSAGE,
					ConversationEntry.COLUMN_MESSAGEID
				    };
			String sortOrder =
					ConversationEntry.COLUMN_MESSAGEID + " DESC";
			String selection = //WHERE (receiver = ? AND user = ?) OR (receiver = ? AND user = ?)
					"(" + ConversationEntry.COLUMN_RECEIVERSUSERNAME + " = ? AND " + ConversationEntry.COLUMN_USERNAME + 
					" = ?) OR (" + ConversationEntry.COLUMN_RECEIVERSUSERNAME + " = ? AND " + ConversationEntry.COLUMN_USERNAME + " = ?)";
			String[] selectionArgs = {
					MailFragment.selected_receiver,
					username,
					username,
					MailFragment.selected_receiver
				};
					
			Cursor our_convo = db.query(
					true,									  // Distinct rows
					ConversationEntry.TABLE_NAME,  			  // The table to query
				    projection,                               // The columns to return
				    selection,                                // The columns for the WHERE clause (WHERE receivername = intended receiver)
				    selectionArgs,                            // The values for the WHERE clause
				    null,                                     // don't group the rows
				    null,                                     // don't filter by row groups
				    sortOrder,                                // The sort order
				    null 									  // No limit
				    );
			our_convo.moveToFirst();
			int sender_index = our_convo.getColumnIndex(ConversationEntry.COLUMN_USERNAME);
			int receiver_index = our_convo.getColumnIndex(ConversationEntry.COLUMN_RECEIVERSUSERNAME);
			int message_index = our_convo.getColumnIndex(ConversationEntry.COLUMN_MESSAGE);
			while(!our_convo.isAfterLast()){
				try {
					
					String sender = our_convo.getString(sender_index);
					String receiver = our_convo.getString(receiver_index);
					String message = our_convo.getString(message_index);
					TextView newMail = new TextView(getApplicationContext());
					newMail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)newMail.getLayoutParams();
					params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
					newMail.setLayoutParams(params);
					if(sender.equals(username)){//set alignment right if mine, left if reciever's
						newMail.setText(Html.fromHtml("<b>" + sender + "</b>" +  "<br/>" + message));
						newMail.setGravity(Gravity.RIGHT); //= RelativeLayout.ALIGN_PARENT_RIGHT;
					}else if(!(sender.equals(username))){
						newMail.setText(Html.fromHtml("<b>" + sender + "</b>"+  "<br/>" + message));
						//params.gravity = RelativeLayout.ALIGN_PARENT_LEFT;
						newMail.setGravity(Gravity.LEFT);
					}
						
					
					if (Build.VERSION.SDK_INT >= 16){
						newMail.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
					} 
					else{
						newMail.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
					}
					
					 
						
					conversationView.addView(newMail);
					conversationView.invalidate();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				our_convo.moveToNext();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}
	
	public void onCancelClick(View v) { 	  
        this.finish();  
    }  
	
	private OnClickListener mSendMessageButton = new OnClickListener() { 	
		
		@Override
        public void onClick(View v) {
			user_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			EditText newMail = (EditText)findViewById(R.id.message_box);
			String newMailString = newMail.getText().toString();
			String username = user_prefs.getString("USERNAME", "defualt value");
			String password = user_prefs.getString("PASSWORD", null);
			int mail_timeout = user_prefs.getInt("pref_messTimeAmmount", 32);
			String response = "";
			String receiverusername = MailFragment.selected_receiver;
			JSONArray json_array = null;
			JSONObject json_object = null;
			
			
			if (newMailString.length() == 0) {
				alert.showAlertDialog(v.getContext(), "Entry error", "No text to send.", false);
				return;
			} else if (receiverusername.equals("")){
				alert.showAlertDialog(v.getContext(), "No recipient selected.", "Select a contact as a recipient.", false);
				return;
			} else {
				NetworkAsyncTask sendMailTask = new NetworkAsyncTask(_this);
				sendMailTask.execute(NetworkAsyncTask.serverLit + "directmessage/new?username=" + username + "&message=" + newMailString + "&timeout=" + mail_timeout + "&receiversusername=" + receiverusername + "&rusername=" + username + "&rpassword=" + password);
					
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
						//commented out this alert because it was causing a WindowLeaked error due to calling the alertDialog twice
						//alert.showAlertDialog(v.getContext(), "Invalid request", "Server sent 'JSON is not valid'", false);
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
