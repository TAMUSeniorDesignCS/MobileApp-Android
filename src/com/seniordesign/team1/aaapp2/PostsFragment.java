package com.seniordesign.team1.aaapp2;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.os.Build;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_posts,
				container, false);
		LinearLayout postsView = (LinearLayout) rootView.findViewById(R.id.postsView);
		JSONArray json = null;
		try {
			json = new JSONArray(getArguments().getString(POSTS));
			for(int i=0; i<json.length()-1; i++){
				try {
					JSONObject jsonPost = json.getJSONObject(i);
					TextView newPost = new TextView(container.getContext());
					newPost.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)newPost.getLayoutParams();
					params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
					newPost.setLayoutParams(params);
					if (Build.VERSION.SDK_INT >= 16){
						newPost.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
					} 
					else{
					    newPost.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
					}
					
					
					newPost.setText(Html.fromHtml("<b>" + jsonPost.getString("firstname") + "</b> @" + jsonPost.getString("username") + "<br/>" + jsonPost.getString("message")));
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
					final String username = prefs.getString("USERNAME", null);
					final String password = prefs.getString("PASSWORD", null);
					final int postId = jsonPost.getInt("postid");
					//if the user owns this post
					if(jsonPost.getString("username").equals(username)){
						newPost.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								/*
								 * create popup with edit and delete options
								 */
								EditPostDialogFragment popup = new EditPostDialogFragment();
								Bundle args = new Bundle();
								args.putInt("postId", postId);
								args.putString("username", username);
								args.putString("pw", password);
								popup.setArguments(args);
								popup.show(getFragmentManager(), "edit_post_popup");
							}
						});
					}
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
		
		/*
		Button writePostButton = (Button) rootView.findViewById(R.id.write_post_button);
		writePostButton.setOnClickListener(mWritePostButton);
		*/
		return rootView;
	}
	
	
	private class MyOnClickListener implements OnClickListener{ 
		
		private Context context;
		public MyOnClickListener(Context context){
			this.context = context;
		}
		
		@Override
        public void onClick(View v) {
			SharedPreferences login_prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			/******************Deprecated---newpost now in actionbar, WritePostActivity*********************/
			/*
			EditText newPost = (EditText)getActivity().findViewById(R.id.write_post);
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
				
				NetworkAsyncTask sendPostTask = new NetworkAsyncTask(v.getContext());
				sendPostTask.execute(NetworkAsyncTask.serverLit + "post/new?username=" + username + "&message=" + newPostString + "&timeout=" + timeout);
				try{
					response = sendPostTask.get(5, TimeUnit.SECONDS);
					json_array = new JSONArray(response);
					
					if(HelperFunctions.isJSONValid(json_array)){
						json_object = json_array.getJSONObject(0); //this is the message object that we just sent
						
						//TODO when refresh is working, do a refresh here
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
			}*/
			
        }
		
};
private OnClickListener mWritePostButton = new MyOnClickListener(this.getActivity());
}
