package com.seniordesign.team1.aaapp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class SQLiteAsyncSQL extends AsyncTask<String, Void, Void> {
	
	private Context context;
	
	public SQLiteAsyncSQL(Context context){
		super();
		this.context = context;
	}

	@Override
	protected Void doInBackground(String... params) {
		ContactDbHelper dbHelper = new ContactDbHelper(this.context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		for(String sql : params){
			db.execSQL(sql);
		}
		return null;
	}

}
