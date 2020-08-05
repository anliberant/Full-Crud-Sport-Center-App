package com.example.sportclub.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class SportClubContract {
    private SportClubContract(){

    }
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "sportclub";
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.sportclub";
    public static final String PATH_MEMBERS = "members";
    public static final Uri BASE_CONTENT_URI =
            Uri.parse(SCHEME + AUTHORITY);

    public static final class MemberEntry implements BaseColumns {
        public static final String NAME_TABLE = "members";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_SPORT_GROUP = "sportGroup";
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final String CONTENT_MULTIPLY_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;
        public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;

        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);
    }
}
