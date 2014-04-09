package com.seniordesign.team1.aaapp2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class SQLiteAsyncQuery extends AsyncTask<String, Void, List<Cursor>> {
	
	private Context context;
	private List<Cursor> ret;
	
	public SQLiteAsyncQuery(Context context){
		super();
		this.context = context;
		ret = new ArrayList<Cursor>();
	}

	@Override
	protected List<Cursor> doInBackground(String... params) {
		for(String sql : params){
			ContactDbHelper dbHelper = new ContactDbHelper(this.context);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			this.ret.add(cursor);
		}
		return this.ret;
	}

}
