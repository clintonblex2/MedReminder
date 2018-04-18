package com.example.smart.medreminder.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smart.medreminder.model.MedicationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smart on 11/04/2018, 3:31 PM.
 */

public class MedicationHelper extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 2;

    // Database name
    private static final String DATABASE_NAME = "medication.db";

    // User table name
    private static final String TABLE_REMINDER = "drugTracker";

    // User table column names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_DRUGNAME = "user_fullName";
    private static final String COLUMN_DESCRIPTION = "user_description";
    private static final String COLUMN_INTERVAL = "user_interval";
    private static final String COLUMN_STARTDATE = "user_startDate";
    private static final String COLUMN_ENDDATE = "user_endDate";

    // Create SQL table query
    private String CREATE_TABLE_REMINDER = "CREATE TABLE " + TABLE_REMINDER + "(" + COLUMN_USER_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DRUGNAME + " TEXT," +
            COLUMN_DESCRIPTION + " TEXT," + COLUMN_INTERVAL + " TEXT," +
            COLUMN_STARTDATE + " TEXT," + COLUMN_ENDDATE + " TEXT)";

    // Drop SQL table query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS" + CREATE_TABLE_REMINDER;

    // Database constructor
    public MedicationHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_REMINDER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // Drop table if exist
        sqLiteDatabase.execSQL(DROP_USER_TABLE);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // This method is used to create user record
    public void addReminder(MedicationModel drug) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DRUGNAME, drug.getDrugName());
        values.put(COLUMN_DESCRIPTION, drug.getDescription());
        values.put(COLUMN_INTERVAL, drug.getInterval());
//        values.put(COLUMN_STARTDATE, drug.getStartDate());
//        values.put(COLUMN_ENDDATE, drug.getStartDate());

        // Inserting row
        db.insert(TABLE_REMINDER, null, values);
        db.close();
    }

    // This method is to fetch all user and return the list of user records

    public List<MedicationModel> getAllReminders() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_DRUGNAME,
                COLUMN_DESCRIPTION,
                COLUMN_INTERVAL,
//                COLUMN_STARTDATE,
//                COLUMN_ENDDATE
        };
        // Sorting orders
        String sortOrder = COLUMN_DRUGNAME + " ASC";
        List<MedicationModel> reminderList = new ArrayList<MedicationModel>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like
         * we use sql query. SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_REMINDER, // Table to query
                columns,    // Columns to return
                null,       // Columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,       // group the rows
                null,       //filter by row groups
                sortOrder);        // The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MedicationModel drug = new MedicationModel();
                drug.setMedicineId(Long.parseLong(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                drug.setDrugName(cursor.getString(cursor.getColumnIndex(COLUMN_DRUGNAME)));
                drug.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                drug.setInterval(cursor.getString(cursor.getColumnIndex(COLUMN_INTERVAL)));
//                drug.setStartDate(cursor.getString(cursor.getColumnIndex(COLUMN_STARTDATE)));
//                drug.setEndDate(cursor.getString(cursor.getColumnIndex(COLUMN_ENDDATE)));

                // Adding user record to list
                reminderList.add(drug);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return reminderList;
    }

    // This method is for updating user record
    public void updateReminder(MedicationModel drug) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DRUGNAME, drug.getDrugName());
        values.put(COLUMN_DESCRIPTION, drug.getDescription());
        values.put(COLUMN_INTERVAL, drug.getInterval());
//        values.put(COLUMN_STARTDATE, drug.getStartDate());
//        values.put(COLUMN_ENDDATE, drug.getEndDate());

        // Updating row
        db.update(TABLE_REMINDER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(drug.getMedicineId())});
        db.close();
    }

    // This method is to delete user record
    public void deleteReminder(MedicationModel drug) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_REMINDER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(drug.getMedicineId())});
        db.close();
    }
    // This method is used to check if user exist or not
    public boolean checkReminder(String drugName) {

        // Array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // Selection criteria
        String selection = COLUMN_DRUGNAME + " = ?";

        // Selection argument
        String[] selectionArgs = {drugName};

        // Query user table with condition

        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_REMINDER,    //Table to query
                columns,                        //Columns to return
                selection,                      //Columns for the WHERE clause
                selectionArgs,                  //The values for the WHERE
                null,                   //Group the rows
                null,                   //Filter by row groups
                null);                  // The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param drugName
     * @param description
     * @return true/false
     */
    public boolean checkReminder(String drugName, String description) {

        // Array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // Selection criteria
        String selection = COLUMN_DRUGNAME + " = ?" + " AND " + COLUMN_DESCRIPTION + " = ?";

        // Selection arguments
        String[] selectionArgs = {drugName, description};

        // Query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'clinton@gmail.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_REMINDER,    //Table to query
                columns,                        //Columns to return
                selection,                      //Columns for the WHERE clause
                selectionArgs,                  //The values for the WHERE clause
                null,                   //group the rows
                null,                   //filter by row groups
                null);                  //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true; // User exist
        }

        return false;
    }
}
