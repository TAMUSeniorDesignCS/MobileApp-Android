package com.seniordesign.team1.aaapp2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniordesign.team1.aaapp2.ContactsContract.ConversationEntry;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MailFragment extends Fragment {
	
	public static final String MAIL = "";
	AlertDialogManager alert = new AlertDialogManager();
	public static String selected_receiver;
	
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
			ContactDbHelper cDbHelper = new ContactDbHelper(getActivity());
			SQLiteDatabase db = cDbHelper.getWritableDatabase();
			for(int i=0; i<json.length()-1; i++){
				try {
					JSONObject jsonMail = json.getJSONObject(i);
					TextView newMail = new TextView(container.getContext());
					newMail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)newMail.getLayoutParams();
					params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
					newMail.setLayoutParams(params);
					if (Build.VERSION.SDK_INT >= 16){
						newMail.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
					} 
					else{
						newMail.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
					}
					
					//store values
					String my_username = jsonMail.getString("username");
					String receiver_username = jsonMail.getString("receiversusername");
					String message = jsonMail.getString("message");
					String messageid = Integer.toString(jsonMail.getInt("directmessageid"));
					ContentValues values = new ContentValues();
					values.put(ConversationEntry.COLUMN_MESSAGEID, messageid);
					values.put(ConversationEntry.COLUMN_MESSAGE, message);
					values.put(ConversationEntry.COLUMN_RECEIVERSUSERNAME, receiver_username);
					values.put(ConversationEntry.COLUMN_USERNAME, my_username);
					long newRowId;
					newRowId = db.insertWithOnConflict(
							ConversationEntry.TABLE_NAME,
							null,
					        values,
					        SQLiteDatabase.CONFLICT_IGNORE);
					
					newMail.setText(Html.fromHtml("<b>" + receiver_username + "</b>"));
					newMail.setTag(receiver_username);
					newMail.setOnClickListener(mViewConversation);
					mailView.addView(newMail);
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
	
	private OnClickListener mViewConversation = new OnClickListener() { 	
		
		@Override
        public void onClick(View v) {
			selected_receiver = (String)v.getTag();
			Intent view_conversation_intent = new Intent(getActivity(), ConversationActivity.class);
		    startActivity(view_conversation_intent);
        }
};
}