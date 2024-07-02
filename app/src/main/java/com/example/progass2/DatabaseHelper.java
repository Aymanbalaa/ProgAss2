package com.example.progass2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ProfileManager.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance;

    private static final String TABLE_PROFILES = "profiles";
    private static final String TABLE_ACCESS = "access";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_GPA = "gpa";
    private static final String COLUMN_ACCESS_ID = "accessId";
    private static final String COLUMN_PROFILE_ID = "profileId";
    private static final String COLUMN_ACCESS_TYPE = "accessType";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    // Singleton pattern to ensure only one instance of the database helper
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PROFILES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_SURNAME + " TEXT,"
                + COLUMN_GPA + " REAL)");

        db.execSQL("CREATE TABLE " + TABLE_ACCESS + "("
                + COLUMN_ACCESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PROFILE_ID + " INTEGER,"
                + COLUMN_ACCESS_TYPE + " TEXT,"
                + COLUMN_TIMESTAMP + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCESS);
        onCreate(db);
    }

    public long addProfile(long id, String name, String surname, float gpa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SURNAME, surname);
        values.put(COLUMN_GPA, gpa);
        long newId = db.insert(TABLE_PROFILES, null, values);

        // Log creation in access table
        addAccess(newId, "created");
        return newId;
    }

    public List<Profile> getAllProfiles(boolean sortBySurname) {
        List<Profile> profiles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String orderBy = sortBySurname ? COLUMN_SURNAME : COLUMN_ID;
        Cursor cursor = db.query(TABLE_PROFILES, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_SURNAME, COLUMN_GPA},
                null, null, null, null, orderBy);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Profile profile = new Profile(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME)),
                        cursor.getFloat(cursor.getColumnIndex(COLUMN_GPA))
                );
                profiles.add(profile);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return profiles;
    }

    public Profile getProfile(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROFILES, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_SURNAME, COLUMN_GPA},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") Profile profile = new Profile(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME)),
                    cursor.getFloat(cursor.getColumnIndex(COLUMN_GPA))
            );
            cursor.close();
            addAccess(id, "opened");
            return profile;
        }
        return null;
    }

    public void deleteProfile(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        addAccess(id, "deleted");
    }

    private void addAccess(long profileId, String accessType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE_ID, profileId);
        values.put(COLUMN_ACCESS_TYPE, accessType);
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis());  // Use current time in milliseconds
        db.insert(TABLE_ACCESS, null, values);
    }

    public List<Access> getAccessHistory(long profileId) {
        List<Access> accessList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCESS, new String[]{COLUMN_ACCESS_ID, COLUMN_PROFILE_ID, COLUMN_ACCESS_TYPE, COLUMN_TIMESTAMP},
                COLUMN_PROFILE_ID + "=?", new String[]{String.valueOf(profileId)}, null, null, COLUMN_TIMESTAMP + " DESC");

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Access access = new Access(
                        cursor.getLong(cursor.getColumnIndex(COLUMN_ACCESS_ID)),
                        cursor.getLong(cursor.getColumnIndex(COLUMN_PROFILE_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ACCESS_TYPE)),
                        new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP)))
                );
                accessList.add(access);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return accessList;
    }
}
