package com.seniordesign.team1.aaapp2;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
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
	
	private class MyOnClickListener implements OnClickListener{
		
		private Context context;
		public MyOnClickListener(Context context){
			this.context = context;
		}
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			EditText etUsername = (EditText)findViewById(R.id.username_entry);
			EditText etPassword = (EditText)findViewById(R.id.password_entry);
			String username, password, firstname = "", email = "";
			int groupid;
			String sponsorid;
			String response = "";
			SharedPreferences login_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor login_editor = login_prefs.edit();
			JSONArray json_array = null;
			JSONObject json_object = null;

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
					NetworkAsyncTask loginTask = new NetworkAsyncTask(this.context);
					loginTask.execute(NetworkAsyncTask.serverLit + "member/auth?username=" + username + "&password=" + password);

					try{
						response = loginTask.get(5, TimeUnit.SECONDS);
						json_array = new JSONArray(response);
						if(HelperFunctions.isJSONValid(json_array)){
							json_object = json_array.getJSONObject(0);
							
							if(json_object.getString("username").equals(username) && json_object.getString("password").equals(password)){ //server recognizes the username and password entered; now ask for other user info
	
								firstname = json_object.getString("firstname");
								groupid = json_object.getInt("groupid");
								email = json_object.getString("email");
								sponsorid = json_object.getString("sponsorid");
								
								//values to store locally
								login_editor.putString("FIRSTNAME", firstname);	// value to store
								login_editor.putString("USERNAME", username); 
								login_editor.putString("PASSWORD", password);
								login_editor.putInt("GROUPID", groupid);
								login_editor.putString("EMAIL", email);
								login_editor.putString("SPONSORID", sponsorid);
								login_editor.putBoolean("loggedIn", true);		//logs the user in for future opening of app
								login_editor.commit();
								
								Intent i = new Intent(getApplicationContext(), MainActivity.class);
				                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				                startActivity(i);
				                finish();
				                //return;
							}
						} else {
							alert.showAlertDialog(LoginActivity.this, "Login Error", "Account not found. Try again, or create a new account.", false);
						}
					}catch (JSONException e){//NOT a JSON object

						if(response.equals("Your request is invalid.")){
							alert.showAlertDialog(LoginActivity.this, "Invalid request 1", "Response from server: " + response, false);
							return;
						}else if(!(response.equals("OK"))){
							alert.showAlertDialog(LoginActivity.this, "Invalid request 2", "Response from server:" + response, false);
							return;
						}
					}catch (Exception e){

						setResult("try_catch 1: " + e.toString());
					}
					
				}
			} catch (Exception e) {
				setResult("try_catch 2: " + e.toString());
			}
		}
		
	}

	private OnClickListener mLoginListener = new MyOnClickListener(this);

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

