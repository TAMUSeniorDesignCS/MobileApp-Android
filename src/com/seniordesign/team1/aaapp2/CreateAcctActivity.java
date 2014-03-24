package com.seniordesign.team1.aaapp2;

import java.util.concurrent.TimeUnit;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
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

public class CreateAcctActivity extends Activity  {
	//private CreateAcctActivity _this;
	SharedPreferences login_prefs;
	Editor login_editor; 
	AlertDialogManager alert = new AlertDialogManager();
	private static final String PREF_NAME = "Login_preferences";
	EditText etFirstName;
	EditText etUsername; //= (EditText)findViewById(R.id.username_entry);
	EditText etPassword; //= (EditText)findViewById(R.id.password_entry);
	EditText etConf_password;// = (EditText)findViewById(R.id.confirm_password_entry);
	EditText etGroupID;
	String username = "";
	String password = "";
	String conf_password = "";
	String firstname = "";
	String groupid = "";
	String sponsorid = null;
	String email = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		//_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_acct_page);
		
		//login_prefs = getApplicationContext().getSharedPreferences(PREF_NAME, 0);
		login_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		login_editor = login_prefs.edit();
		etFirstName = (EditText)findViewById(R.id.firstname_entry);
		etUsername = (EditText)findViewById(R.id.username_entry);
		etPassword = (EditText)findViewById(R.id.password_entry);
		etConf_password = (EditText)findViewById(R.id.confirm_password_entry);
		etGroupID = (EditText)findViewById(R.id.groupid_entry);
		
		Button createAcct = (Button) findViewById(R.id.create_account);
		createAcct.setOnClickListener(mCreateAcctListener);
	}
	
	public void onCancelClick(View v) { 	  
        this.finish();  
    }  
	
	private OnClickListener mCreateAcctListener = new OnClickListener() { 	
		
		@Override
        public void onClick(View v) {
			firstname = etFirstName.getText().toString();
        	username = etUsername.getText().toString();  
        	password = etPassword.getText().toString();  
        	conf_password = etConf_password.getText().toString();
        	groupid = etGroupID.getText().toString();
        	String response = "";
    		
           // try {
            	if(firstname.length()==0){
            		alert.showAlertDialog(CreateAcctActivity.this, "Entry error", "You must enter your first name.", false);
            	} else if(username.length()==0){
            		alert.showAlertDialog(CreateAcctActivity.this, "Entry error", "You must create a username.", false);
            	}else if(password.length()==0){
            		alert.showAlertDialog(CreateAcctActivity.this, "Entry error", "You must create a password.", false);
            	}else if(groupid.length()==0){
            		alert.showAlertDialog(CreateAcctActivity.this, "Entry error", "You must enter the group ID.", false);
            	}else if(password.equals(conf_password)){//
            		//commit settings locally
            		login_editor.putString("FIRSTNAME", firstname);// value to store
	                login_editor.putString("USERNAME", username); 
	                login_editor.putString("PASSWORD", password);
	                login_editor.putString("GROUPID", groupid);
	                login_editor.putBoolean("loggedIn", true);		//logs the user in for future opening of app
	                login_editor.commit();
	                
	                //commit settings to server
	                String urlVariables = "member/new?groupid = " + groupid + "&firstname=" + firstname + "&username=" + username + "&sponsorid=" + sponsorid + "&password=" + password + "&email=" + email; 
	                NetworkAsyncTask createAcctTask = new NetworkAsyncTask();
	                createAcctTask.execute(NetworkAsyncTask.serverLit + urlVariables);
	                
	                try{
                		String[] r1 = createAcctTask.get(5, TimeUnit.SECONDS).split("===");
                		response = r1[0];
                	}catch (Exception e){
                		alert.showAlertDialog(CreateAcctActivity.this, "Exception", "Response from app: " + e, false);
                	}
                	if(response.equals("Your request is invalid.")){
                		alert.showAlertDialog(CreateAcctActivity.this, "Invalid request", "Response from server: " + response, false);
                        return;
                	}//else if(){ //SQL error checking
                		
                	//}
                	
                	else{ //retrieve data from JSON member object
                	
		                //return to MainActivity
		                Intent i = new Intent(getApplicationContext(), MainActivity.class);
		                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		                startActivity(i);
		                finish();
                	}
            	} else {
            		//display "passwords don't match" error
            		alert.showAlertDialog(CreateAcctActivity.this, "Entry error", "Passwords don't match, please try again.", false);
            	}
            //} catch (Exception e) {
                //setResult(e.toString());
            //}
             
             
        }
    };
}
