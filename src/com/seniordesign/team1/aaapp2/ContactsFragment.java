package com.seniordesign.team1.aaapp2;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniordesign.team1.aaapp2.ContactsContract.ContactEntry;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactsFragment extends Fragment {
	

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
		LinearLayout postsView = (LinearLayout) rootView.findViewById(R.id.contactsView);
		try {
			List<Cursor> cursors = db.get();
			Cursor cursor = cursors.get(0);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
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
				
				
				newPost.setText(Html.fromHtml("<b>" + cursor.getString(cursor.getColumnIndex("firstname")) + "</b> @" + cursor.getString(cursor.getColumnIndex("username")) + "<br/>" ));
				postsView.addView(newPost);
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
}