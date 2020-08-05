package com.example.sportclub.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


public class SportContentProvider extends ContentProvider {
    SportDBOpenHelper dbOpenHelper;
    // Creates a UriMatcher object.
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int CODE1 = 111;
    public static final int CODE2 = 222;


    static {
        uriMatcher.addURI(SportClubContract.AUTHORITY, SportClubContract.PATH_MEMBERS, CODE1);
        uriMatcher.addURI(SportClubContract.AUTHORITY, SportClubContract.PATH_MEMBERS + "/#", CODE2);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new SportDBOpenHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case CODE1:
                cursor = db.query(SportClubContract.MemberEntry.NAME_TABLE,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE2:
                selection = (SportClubContract.MemberEntry._ID + "=?");
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(SportClubContract.MemberEntry.NAME_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_LONG);
                throw new IllegalArgumentException("Can't connect to this URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String firstName = values.getAsString(SportClubContract.MemberEntry.COLUMN_FIRST_NAME);
        if (firstName == null) {
            throw new IllegalArgumentException("You have to input first name");
        }
        String lastName = values.getAsString(SportClubContract.MemberEntry.COLUMN_LAST_NAME);
        if (lastName == null) {
            throw new IllegalArgumentException("You have to input last name");
        }

        String sportGroup = values.getAsString(SportClubContract.MemberEntry.COLUMN_SPORT_GROUP);
        if (sportGroup == null) {
            throw new IllegalArgumentException("You have to input sport group");
        }
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match) {
            case CODE1:
                long id = db.insert(SportClubContract.MemberEntry.NAME_TABLE, null, values);
                if (id == -1) {
                    Log.e("InsertMethod", "Insertion of data in the table failed for " + uri);
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Can't connect to this URI " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rawsDeleted;
        switch (match) {
            case CODE1:
                rawsDeleted = db.delete(SportClubContract.MemberEntry.NAME_TABLE, selection, selectionArgs);
                break;
            case CODE2:
                selection = (SportClubContract.MemberEntry._ID + "=?");
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rawsDeleted = db.delete(SportClubContract.MemberEntry.NAME_TABLE, selection, selectionArgs);
                break;
            default:
                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_LONG);
                throw new IllegalArgumentException("Can't delete this URI " + uri);
        }
        if (rawsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rawsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(SportClubContract.MemberEntry.COLUMN_FIRST_NAME)) {
            String firstName = values.getAsString(SportClubContract.MemberEntry.COLUMN_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException("You have to input first name");
            }
        }

        if (values.containsKey(SportClubContract.MemberEntry.COLUMN_LAST_NAME)) {
            String lastName = values.getAsString(SportClubContract.MemberEntry.COLUMN_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("You have to input last name");
            }
        }
        
        if (values.containsKey(SportClubContract.MemberEntry.COLUMN_SPORT_GROUP)) {
            String sportGroup = values.getAsString(SportClubContract.MemberEntry.COLUMN_SPORT_GROUP);
            if (sportGroup == null) {
                throw new IllegalArgumentException("You have to input sport group");
            }
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rawsUpdated;
        switch (match) {
            case CODE1:
                rawsUpdated = db.update(SportClubContract.MemberEntry.NAME_TABLE, values, selection, selectionArgs);
                break;
            case CODE2:
                selection = (SportClubContract.MemberEntry._ID + "=?");
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rawsUpdated =  db.update(SportClubContract.MemberEntry.NAME_TABLE, values, selection, selectionArgs);
                break;
            default:
                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_LONG);
                throw new IllegalArgumentException("Can't update this URI " + uri);
        }
        if (rawsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rawsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case CODE1:
                return SportClubContract.MemberEntry.CONTENT_MULTIPLY_ITEMS;
            case CODE2:
                return SportClubContract.MemberEntry.CONTENT_SINGLE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

    }
}
