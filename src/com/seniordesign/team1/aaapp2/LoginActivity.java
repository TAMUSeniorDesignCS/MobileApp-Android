package com.seniordesign.team1.aaapp2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends Activity{
	private AccountManager _accountMgr = null;
    private LoginActivity _this;
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
            try {
                Account [] accounts = _accountMgr.getAccounts();
                if (accounts.length == 0) {
                    setResult("No Accounts"); 
                    //launch "create account" activity here?
                    return;
                }else{
                Account account = accounts[0];
                _accountMgr.getAuthToken(account, "mail", false, new GetAuthTokenCallback(), null);
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

