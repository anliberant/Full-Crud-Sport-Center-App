package com.example.sportclub;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.sportclub.data.SportClubContract;

public class AddMemberActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDIT_MEMBER_LOADER = 111;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText sportGroupEditText;
    private Spinner genderSpinner;
    private int gender = 0;
    private ArrayAdapter spinnerAdapter;
    Uri currentMemberUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent intent = getIntent();
        currentMemberUri = intent.getData();
        if (currentMemberUri == null) {
            setTitle("Add a Member");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit the Member");
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LOADER, null, this);
        }
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        sportGroupEditText = findViewById(R.id.sportGroupEditText);
        genderSpinner = findViewById(R.id.genderSpinner);


        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(spinnerAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selectedGender)) {
                    if (selectedGender.equals("Male")) {
                        gender = SportClubContract.MemberEntry.GENDER_MALE;
                    } else if (selectedGender.equals("Female")) {
                        gender = SportClubContract.MemberEntry.GENDER_FEMALE;
                    } else
                        gender = SportClubContract.MemberEntry.GENDER_UNKNOWN;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = 0;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentMemberUri == null){
            MenuItem menuItem = menu.findItem(R.id.deleteMember);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_member_memu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveMember:
                saveMember();
                return true;
            case R.id.deleteMember:
                showDeleteMemberDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveMember() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String sportGroup = sportGroupEditText.getText().toString().trim();
        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Input a first name", Toast.LENGTH_LONG);
            return;
        } else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Input a last name", Toast.LENGTH_LONG);
            return;
        } else if (TextUtils.isEmpty(sportGroup)) {
            Toast.makeText(this, "Input a sport group", Toast.LENGTH_LONG);
            return;
        } else if (gender == SportClubContract.MemberEntry.GENDER_UNKNOWN) {
            Toast.makeText(this, "Choose the gender", Toast.LENGTH_LONG);
        }


        ContentValues contentValues = new ContentValues();
        contentValues.put(SportClubContract.MemberEntry.COLUMN_FIRST_NAME, firstName);
        contentValues.put(SportClubContract.MemberEntry.COLUMN_LAST_NAME, lastName);
        contentValues.put(SportClubContract.MemberEntry.COLUMN_SPORT_GROUP, sportGroup);
        contentValues.put(SportClubContract.MemberEntry.COLUMN_GENDER, gender);

        if (currentMemberUri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri insert = contentResolver.insert(SportClubContract.MemberEntry.CONTENT_URI, contentValues);
            if (insert == null) {
                Toast.makeText(this, "Can't connect to this URI", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Successful connection", Toast.LENGTH_LONG).show();

            }
        } else {
            int update = getContentResolver().update(currentMemberUri, contentValues, null, null);
            if (update == 0) {
                Toast.makeText(this, "Saving failed", Toast.LENGTH_LONG);
            } else {
                Toast.makeText(this, "Member data updated", Toast.LENGTH_LONG);
            }
        }

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                SportClubContract.MemberEntry._ID,
                SportClubContract.MemberEntry.COLUMN_FIRST_NAME,
                SportClubContract.MemberEntry.COLUMN_LAST_NAME,
                SportClubContract.MemberEntry.COLUMN_GENDER,
                SportClubContract.MemberEntry.COLUMN_SPORT_GROUP
        };
        return new CursorLoader(this,
                currentMemberUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            int firstNameColumnIndex = data.getColumnIndex(SportClubContract.MemberEntry.COLUMN_FIRST_NAME);
            int lastNameColumnIndex = data.getColumnIndex(SportClubContract.MemberEntry.COLUMN_LAST_NAME);
            int genderColumnIndex = data.getColumnIndex(SportClubContract.MemberEntry.COLUMN_GENDER);
            int sportGroupColumnIndex = data.getColumnIndex(SportClubContract.MemberEntry.COLUMN_SPORT_GROUP);
            String firstName = data.getString(firstNameColumnIndex);
            String lastName = data.getString(lastNameColumnIndex);
            int gender = data.getInt(genderColumnIndex);
            String sportGroup = data.getString(sportGroupColumnIndex);

            firstNameEditText.setText(firstName);
            lastNameEditText.setText(lastName);
            sportGroupEditText.setText(sportGroup);


            switch (gender) {
                case SportClubContract.MemberEntry.GENDER_MALE:
                    genderSpinner.setSelection(1);
                    break;
                case SportClubContract.MemberEntry.GENDER_FEMALE:
                    genderSpinner.setSelection(2);
                    break;
                case SportClubContract.MemberEntry.GENDER_UNKNOWN:
                    genderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
    private void showDeleteMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete the member?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMember();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteMember() {
        if (currentMemberUri != null){
            int rowsDeleted = getContentResolver().delete(currentMemberUri, null, null);
            if (rowsDeleted == 0){
                Toast.makeText(this, "Something was wrong", Toast.LENGTH_LONG);
            } else{
                Toast.makeText(this, "Member is deleted", Toast.LENGTH_LONG);
            }
        }
        finish();
    }
}