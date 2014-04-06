package com.seniordesign.team1.aaapp2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MailFragment extends Fragment {
	
	public static final String MAIL = "";
	AlertDialogManager alert = new AlertDialogManager();
	
	public MailFragment() {
		super();
	}
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_mail,
				container, false);
		LinearLayout mailView = (LinearLayout) rootView.findViewById(R.id.mailView);
		JSONArray json = null;
		try {
			json = new JSONArray(getArguments().getString(MAIL));
			for(int i=0; i<json.length(); i++){
				try {
					JSONObject jsonMail = json.getJSONObject(i);
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
					
					
					newPost.setText(Html.fromHtml("<b>" + jsonMail.getString("firstname") + "</b> @" + jsonMail.getString("username") + "<br/>" + jsonMail.getString("message")));
					mailView.addView(newPost);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rootView;
	}
}