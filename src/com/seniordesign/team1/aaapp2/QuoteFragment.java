package com.seniordesign.team1.aaapp2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class QuoteFragment extends Fragment {
	
	public static final String QUOTE = "Default Quote";
	public static final String TITLE = "Default Title";
	public static final String USER = "Defualt User";
	
	public QuoteFragment(){
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_quote,
				container, false);
		TextView userTextView = (TextView) rootView
				.findViewById(R.id.UserDisplay);
		TextView bodyTextView = (TextView) rootView
				.findViewById(R.id.QuoteBody);
		TextView titleTextView = (TextView) rootView
				.findViewById(R.id.QuoteTitle);
		userTextView.setText(getArguments().getString(
				USER));
		bodyTextView.setText(getArguments().getString(
				QUOTE));
		titleTextView.setText(getArguments().getString(
				TITLE));
		userTextView.setTextSize(24);
		titleTextView.setTextSize(16);
		bodyTextView.setTextSize(12);
		return rootView;
	}
}