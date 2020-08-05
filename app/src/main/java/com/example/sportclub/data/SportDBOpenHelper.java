package com.example.sportclub.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SportDBOpenHelper extends SQLiteOpenHelper {
    public SportDBOpenHelper(Context context) {
        super(context, SportClubContract.DB_NAME, null, SportClubContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEMBERS_TABLE = "CREATE TABLE " + SportClubContract.MemberEntry.NAME_TABLE +
                "("
                + SportClubContract.MemberEntry._ID + " INTEGER PRIMARY KEY,"
                + SportClubContract.MemberEntry.COLUMN_FIRST_NAME + " TEXT,"
                + SportClubContract.MemberEntry.COLUMN_LAST_NAME + " TEXT,"
                + SportClubContract.MemberEntry.COLUMN_GENDER + " INTEGER NOT NULL,"
                + SportClubContract.MemberEntry.COLUMN_SPORT_GROUP + " TEXT" +
                ")";
        db.execSQL(CREATE_MEMBERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SportClubContract.DB_NAME);
        onCreate(db);
    }
}
