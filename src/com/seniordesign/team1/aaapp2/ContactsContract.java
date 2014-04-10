package com.seniordesign.team1.aaapp2;

import android.provider.BaseColumns;

public final class ContactsContract {
	// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ContactsContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "Contacts";
        public static final String COLUMN_FIRST_NAME = "firstname";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHONE = "phone";
        
       
    }
}
