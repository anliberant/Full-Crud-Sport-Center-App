package com.example.sportclub;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sportclub.R;
import com.example.sportclub.data.SportClubContract;

import org.w3c.dom.Text;

public class MemberCursorAdapter extends CursorAdapter {
    public MemberCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.member_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView firstNameTextView = view.findViewById(R.id.firstNameTextView);
        TextView lastNameTextView = view.findViewById(R.id.lastNameTextView);
        TextView sportGroupTextView = view.findViewById(R.id.sportGroupTextView);

        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(SportClubContract.MemberEntry.COLUMN_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(SportClubContract.MemberEntry.COLUMN_LAST_NAME));
        String sportGroup = cursor.getString(cursor.getColumnIndexOrThrow(SportClubContract.MemberEntry.COLUMN_SPORT_GROUP));

        firstNameTextView.setText(firstName);
        lastNameTextView.setText(lastName);
        sportGroupTextView.setText(sportGroup);
    }
}
