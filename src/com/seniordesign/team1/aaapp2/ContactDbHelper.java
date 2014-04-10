package com.seniordesign.team1.aaapp2;

import com.seniordesign.team1.aaapp2.ContactsContract.ContactEntry;
import com.seniordesign.team1.aaapp2.ContactsContract.ConversationEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDbHelper extends SQLiteOpenHelper {
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contract.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CONTACT_ENTRIES =
        "CREATE TABLE " + ContactEntry.TABLE_NAME + "(" +
        ContactEntry.COLUMN_USERNAME + TEXT_TYPE + " PRIMARY KEY," +
        ContactEntry.COLUMN_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
        ContactEntry.COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP +
        ContactEntry.COLUMN_PHONE + TEXT_TYPE +
        " )";
    private static final String SQL_CREATE_CONVERSATION_ENTRIES =    
    	"CREATE TABLE " + ConversationEntry.TABLE_NAME + "(" +
        ConversationEntry.COLUMN_USERNAME + TEXT_TYPE +  COMMA_SEP +
        ConversationEntry.COLUMN_RECEIVERSUSERNAME + TEXT_TYPE +  COMMA_SEP +
        ConversationEntry.COLUMN_MESSAGE + TEXT_TYPE +  COMMA_SEP +
        ConversationEntry.COLUMN_MESSAGEID + TEXT_TYPE + "PRIMARY KEY," +
        " )";

    private static final String SQL_DELETE_CONTACT_ENTRIES =
        "DROP TABLE IF EXISTS '" + ContactEntry.TABLE_NAME + "'" ;
    private static final String SQL_DELETE_CONVERSATION_ENTRIES =
        "DROP TABLE IF EXISTS '" + ConversationEntry.TABLE_NAME + "'";

    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACT_ENTRIES);
        db.execSQL(SQL_CREATE_CONVERSATION_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_CONTACT_ENTRIES);
        db.execSQL(SQL_DELETE_CONVERSATION_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
