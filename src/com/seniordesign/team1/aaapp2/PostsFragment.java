package com.seniordesign.team1.aaapp2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostsFragment extends Fragment {
	
	public static final String POSTS = "";

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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		return rootView;
	}

}
