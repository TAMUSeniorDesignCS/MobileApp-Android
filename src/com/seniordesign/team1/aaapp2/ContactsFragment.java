package com.seniordesign.team1.aaapp2;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniordesign.team1.aaapp2.ContactsContract.ContactEntry;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
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

public class ContactsFragment extends Fragment {
	public static String selected_username = "";
	public static String sponsor_id = "";
	
	public ContactsFragment() {
		super();
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SQLiteAsyncQuery db = new SQLiteAsyncQuery(this.getActivity());
		db.execute("SELECT " + ContactEntry.COLUMN_FIRST_NAME + ", " + ContactEntry.COLUMN_USERNAME + 
				" FROM " + ContactEntry.TABLE_NAME);
		View rootView = inflater.inflate(R.layout.fragment_main_contacts,
				container, false);
		LinearLayout contactsView = (LinearLayout) rootView.findViewById(R.id.contactsView);
		try {
			List<Cursor> cursors = db.get();
			Cursor cursor = cursors.get(0);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				//sponsor_id = cursor.getString(cursor.getColumnIndex("sponsor_id"));//get the sponsor id for the activity
				
				TextView newContact = new TextView(container.getContext());
				newContact.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)newContact.getLayoutParams();
				params.setMargins(6, 4, 6, 4); //substitute parameters for left, top, right, bottom
				newContact.setLayoutParams(params);
				if (Build.VERSION.SDK_INT >= 16){
					newContact.setBackground(getResources().getDrawable(R.drawable.bg_card)); 
				} 
				else{
					newContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_card));
				}
				
				
				newContact.setText(Html.fromHtml("<b>" + cursor.getString(cursor.getColumnIndex("firstname")) + "</b> @" + cursor.getString(cursor.getColumnIndex("username")) + "<br/>" ));
				newContact.setOnClickListener(mViewContact);
				String selected_user_tag = cursor.getString(cursor.getColumnIndex("username"));
				newContact.setTag(selected_user_tag);
				contactsView.addView(newContact);
				cursor.moveToNext();
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return rootView;
	}
	
private OnClickListener mViewContact = new OnClickListener() { 	
		
		@Override
        public void onClick(View v) {
			selected_username = (String)v.getTag();
			Intent view_contact_intent = new Intent(getActivity(), ContactActivity.class);
		    startActivity(view_contact_intent);
        }
	};
}