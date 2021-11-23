package com.example.courseorganiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHandlerCourses extends SQLiteOpenHelper {

    private static final String TABLE_NAME ="courses";
    private static final String COL_NAME ="name";
    private static final String Col_ID ="id";

    public DBHandlerCourses(@Nullable Context context) {
        super(context,"courses.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + Col_ID+"INTEGER,"
                + COL_NAME + " TEXT)";

        db.execSQL(query);
    }
    public boolean addOne(CourseModel course){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Cursor c= db.rawQuery("SELECT SUM(*) FROM "+TABLE_NAME,null);
        c.moveToFirst();
        int id=c.getInt(0);

        cv.put(COL_NAME,course.getName());
        cv.put(Col_ID,id);

        long in =db.insert(TABLE_NAME,null,cv);

        c.close();
        return in > 0;

    }

    public List<String> getCoursesNames(){
        List<String> names = new ArrayList<String>();
        SQLiteDatabase db =this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT "+COL_NAME+" FROM "+TABLE_NAME,null);

        if(cursor.moveToFirst()){
            do {
                names.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return names;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
