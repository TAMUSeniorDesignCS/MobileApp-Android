package com.seniordesign.team1.aaapp2;

import java.util.concurrent.TimeUnit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
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
import android.widget.TextView;

public class LoginActivity extends Activity{
	private AccountManager _accountMgr = null;
    private LoginActivity _this;
    AlertDialogManager alert = new AlertDialogManager();
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        _this = this;
         
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
         
        try {
            //_accountMgr = AccountManager.get(this);
            //Account [] accounts = _accountMgr.getAccounts();
            /*String accountsList = "Accounts: " + accounts.length + "\n";
            for (Account account : accounts) {
                accountsList += account.toString() + "\n";
            }
            setMessage(accountsList);*/
             
        } catch (Exception e) {
            setMessage(e.toString());
        }
         
        Button createActBtn = (Button) findViewById(R.id.create_account);
        createActBtn.setOnClickListener(mCreateAcctListener);
        Button loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(mLoginListener);
    }
    
	private OnClickListener mCreateAcctListener = new OnClickListener() { 	//should launch "create account" activity
        
        public void onClick(View v) {
            try {
            	Intent intent = new Intent(_this, CreateAcctActivity.class);
    			startActivity(intent);
                 
            } catch (Exception e) {
                setResult(e.toString());
            }
             
             
        }
    };
    
    private OnClickListener mLoginListener = new OnClickListener() {		//should launch "login" activity
         
        public void onClick(View v) {
        	EditText etUsername = (EditText)findViewById(R.id.username_entry);
        	EditText etPassword = (EditText)findViewById(R.id.password_entry);
        	String username, password, firstname, groupid;
        	String response = "";
        	SharedPreferences login_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    		Editor login_editor = login_prefs.edit();
    		
            try {
            	username = etUsername.getText().toString();  
            	password = etPassword.getText().toString();
            	
                if (username.length() == 0) {
                	alert.showAlertDialog(LoginActivity.this, "Entry error", "You must enter a username.", false);
                    return;
                }else if (password.length() == 0) {
                	alert.showAlertDialog(LoginActivity.this, "Entry error", "You must enter a password.", false);
                    return;
                }else { //attempt login communication with server
                	NetworkAsyncTask loginTask = new NetworkAsyncTask();
                	loginTask.execute(NetworkAsyncTask.serverLit + "member/auth?username=" + username + "&password=" + password);
                	
                	try{
                		String[] r1 = loginTask.get(5, TimeUnit.SECONDS).split("===");
                		response = r1[0];
                	}catch (Exception e){
                		setResult(e.toString());
                	}
                	if(response.equals("Your request is invalid.")){
                		alert.showAlertDialog(LoginActivity.this, "Invalid request", "Response from server: " + response, false);
                        return;
                	}else if(!(response.equals("OK"))){
                		alert.showAlertDialog(LoginActivity.this, "Login Error", "Account not found. Try again, or create a new account.", false);
                        return;
                	}else if(response.equals("OK")){ //server recognizes the username and password entered; now ask for other user info
                
                		String userInfoRequest = NetworkAsyncTask.serverLit + "member/getinfo.json?username=" + username;
                		loginTask.execute(userInfoRequest);
                		try{
                    		String[] r1 = loginTask.get(5, TimeUnit.SECONDS).split("===");
                    		response = r1[0]; //split this for info

                    	}catch (Exception e){
                    		setResult(e.toString());
                    	}
	                	//values to store locally
	                	//login_editor.putString("FIRSTNAME", firstname);	// value to store
		                login_editor.putString("USERNAME", username); 
		                login_editor.putString("PASSWORD", password);
		                //login_editor.putString("GROUPID", groupid);
		                login_editor.putBoolean("loggedIn", true);		//logs the user in for future opening of app
		                login_editor.commit();
                	}
                }
                 
            } catch (Exception e) {
                setResult(e.toString());
            }
             
             
        }
    };
     
    private class GetAuthTokenCallback implements AccountManagerCallback<Bundle> {
        public void run(AccountManagerFuture<Bundle> result) {
                Bundle bundle;
                try {
                        bundle = result.getResult();
                        Intent intent = (Intent)bundle.get(AccountManager.KEY_INTENT);
                        if(intent != null) {
                            // User input required
                            startActivity(intent);
                        } else {
                            _this.setResult("Token: " + bundle.getString(AccountManager.KEY_AUTHTOKEN));
                        }
                } catch (Exception e) {
                    _this.setResult(e.toString());
                }
        }
    };
     
    public void setResult(String msg) {
        TextView tv = (TextView) this.findViewById(R.id.result);
        tv.setText(msg);
    }
     
    public void setMessage(String msg) {
        TextView tv = (TextView) this.findViewById(R.id.welcome_message);
        tv.setText(msg);
    }
}

