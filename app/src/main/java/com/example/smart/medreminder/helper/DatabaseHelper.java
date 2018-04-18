package com.example.smart.medreminder.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smart.medreminder.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smart on 10/04/2018, 2:58 PM.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "userManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    // User table column names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_FULLNAME = "user_fullname";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // Create SQL table query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_USER_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FULLNAME + " TEXT," +
            COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // Drop SQL table query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS" + TABLE_USER;

    // Database constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // Drop table if exist
        sqLiteDatabase.execSQL(DROP_USER_TABLE);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // This method is used to create user record
    public void addUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, user.getFullname());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    // This method is to fetch all user and return the list of user records

    public List<User> getAllUsers() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_FULLNAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PASSWORD
        };
        // Sorting orders
        String sortOrder = COLUMN_FULLNAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like
         * we use sql query. SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, // Table to query
                columns,    // Columns to return
                null,       // Columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,       // group the rows
                null,       //filter by row groups
                sortOrder);        // The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setFullname(cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));

                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    // This method is for updating user record
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, user.getFullname());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // This method is to delete user record
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // This method is used to check if user exist or not
    public boolean checkUser(String email) {

        // Array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // Selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // Selection argument
        String[] selectionArgs = {email};

        // Query user table with condition

        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER,    //Table to query
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
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // Array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // Selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // Selection arguments
        String[] selectionArgs = {email, password};

        // Query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'clinton@gmail.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER,    //Table to query
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
