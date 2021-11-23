package com.example.courseorganiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDB extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "courses";
    private static final String COL_NAME = "name";
    private static final String COL_ID = "id";

    public MyDB(@Nullable Context context) {
        super(context, "courses.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER,"
                + COL_NAME + " TEXT)";

        db.execSQL(query);
    }

    public boolean addOne(CourseModel course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        cv.put(COL_NAME, course.getName());
        cv.put(COL_ID, -1);

        long in = db.insert(TABLE_NAME, null, cv);

        return in > 0;

    }

    public boolean deleteOne(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_NAME + " = '" + name + "'";
        Cursor c = db.rawQuery(queryString, null);

        boolean deleted = c.moveToFirst();

        c.close();

        return deleted;
    }

    public List<String> getCoursesNames() {
        List<String> names = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + COL_NAME + " FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return names;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean checkForDuplicateCourse(String name) {
        //needsToBeCheckd
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "SELECT " + COL_NAME + " FROM " + TABLE_NAME + " WHERE " + COL_NAME + " = '" + name + "'";
        Cursor c = db.rawQuery(queryString, null);

        boolean duplicate = c.moveToFirst();

        c.close();

        return duplicate;
    }


    public void deleteCoursesTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        db.close();
    }
}
