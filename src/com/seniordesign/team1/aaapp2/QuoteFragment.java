package com.seniordesign.team1.aaapp2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class QuoteFragment extends Fragment {
	
	public static final String QUOTE = "Default Quote";
	public static final String TITLE = "Default Title";
	public static final String USER = "Defualt User";
	public static final String QOTD = "Default QotD";
	public static final String TEMPSPONSORNUMBER = "5555555555";
	
	public QuoteFragment(){
		super();
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
		TextView qotdTextView = (TextView) rootView
				.findViewById(R.id.QotD);
		userTextView.setText(getArguments().getString(
				USER));
		bodyTextView.setText(getArguments().getString(
				QUOTE));
		titleTextView.setText(getArguments().getString(
				TITLE));
		qotdTextView.setText(getArguments().getString(
				QOTD));
		userTextView.setTextSize(24);
		qotdTextView.setTextSize(20);
		titleTextView.setTextSize(16);
		bodyTextView.setTextSize(12);
		
		Button callSponsor = (Button) rootView.findViewById(R.id.write_post_button);
		callSponsor.setOnClickListener(mCallSponsorButton);
		
		
		return rootView;
	}
	
	private OnClickListener mCallSponsorButton = new OnClickListener() { 	
			
			@Override
	        public void onClick(View v) {
				
				Intent intent = new Intent(Intent.ACTION_DIAL);
			    intent.setData(Uri.parse("tel:" + TEMPSPONSORNUMBER));
			    startActivity(intent);
	        }
	};
}	
