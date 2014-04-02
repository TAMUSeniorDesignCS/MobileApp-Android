package com.seniordesign.team1.aaapp2;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostsFragment extends Fragment {
	
	public static final String POSTS = "";
	AlertDialogManager alert = new AlertDialogManager();
	
	public PostsFragment() {
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_posts,
				container, false);
		LinearLayout postsView = (LinearLayout) rootView.findViewById(R.id.postsView);
		JSONArray json = null;
		try {
			json = new JSONArray(getArguments().getString(POSTS));
			for(int i=0; i<json.length(); i++){
				try {
					JSONObject jsonPost = json.getJSONObject(i);
					TextView newPost = new TextView(container.getContext());
					newPost.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
					newPost.setText(jsonPost.getString("message"));
					postsView.addView(newPost);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*for(int i=0; i<json.length(); i++){
			try {
				JSONObject jsonPost = json.getJSONObject(i);
				TextView newPost = new TextView(container.getContext());
				newPost.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				newPost.setText(jsonPost.getString("message"));
				postsView.addView(newPost);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		Button writePostButton = (Button) rootView.findViewById(R.id.callSponsorButton);
		writePostButton.setOnClickListener(mWritePostButton);

		return rootView;
	}
	
	private OnClickListener mWritePostButton = new OnClickListener() { 	
		
		@Override
        public void onClick(View v) {
			EditText newPost = (EditText)v.findViewById(R.id.write_post);
			String newPostString = newPost.getText().toString();
			String username = "9"; //this will change to use the username once server is updated
			int timeout = 48; //this will change to use the settings preference
			String response = "";
			JSONObject json_return = null;
			
			if (newPostString.length() == 0) {
				alert.showAlertDialog(v.getContext(), "Entry error", "No text to send.", false);
				return;
			} else {
				//get post duration from preferences:
				//SharedPreferences login_prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
				//int post_timeout = login_prefs.getInt("pref_postTimeAmmount", false);
				
				NetworkAsyncTask sendPostTask = new NetworkAsyncTask(v.getContext());
				sendPostTask.execute(NetworkAsyncTask.serverLit + "post/new?posterid=" + username + "&message=" + newPostString + "&timeout=" + timeout);
				try{
					response = sendPostTask.get(5, TimeUnit.SECONDS);
					json_return = new JSONArray(response).getJSONObject(0);

				}catch (JSONException e){//NOT a JSON object

					if(response.equals("Your request is invalid.")){
						alert.showAlertDialog(v.getContext(), "Invalid request", "Response from server: " + response, false);
						return;
					}else if(response.equals("Request Handled successfully.")){
						//refresh
						Intent i = new Intent(v.getContext(), MainActivity.class);
		                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		                startActivity(i);
					}
				}
				catch (Exception e){
					alert.showAlertDialog(v.getContext(), "Exception", "System message: " + e.toString(), false);
					//setResult(e.toString());
				}
			}
			
        }
};

}
