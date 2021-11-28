package com.example.courseorganiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyDB extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "courses";
    private static final String COL_NAME = "name";
    private static final String COL_ID = "id";
    private static final String COL_PAYED = "payed";
    private static final String PARTICIPANTS_ENDING = "_participants '";
    private static final String BEFORE_PARTICIPANTS_TABLES="'";

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

        String query = "CREATE TABLE " + BEFORE_PARTICIPANTS_TABLES+course.getName() + PARTICIPANTS_ENDING + " ("
                + COL_NAME + " TEXT,"
                + COL_PAYED + " INTEGER)";

        db.execSQL(query);

        db.close();
        return in > 0;

    }

    public boolean addOneParticipant(ParticipantModel participant, String courseName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME, participant.getName());
        cv.put(COL_PAYED, participant.isPayed());

        long in = db.insert(BEFORE_PARTICIPANTS_TABLES+courseName + PARTICIPANTS_ENDING, null, cv);

        db.close();
        return in > 0;
    }

    public boolean deleteOneParticipant(String participantName, String courseName) {
        //if two participants have two names its a problem
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "DELETE FROM "+BEFORE_PARTICIPANTS_TABLES + courseName + PARTICIPANTS_ENDING + " WHERE " + COL_NAME + " = '" + participantName + "'";
        Cursor c = db.rawQuery(queryString, null);

        boolean deleted = c.moveToFirst();
        return deleted;
    }

    public boolean deleteOne(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_NAME + " = '" + name + "'";
        Cursor c = db.rawQuery(queryString, null);


        boolean deleted = c.moveToFirst();

        c.close();

        db.execSQL("DROP TABLE IF EXISTS "+BEFORE_PARTICIPANTS_TABLES + name + PARTICIPANTS_ENDING);

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
    public List<Boolean> getParticipantsIsPayed(String courseName){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Boolean> payed = new ArrayList<Boolean>();

        Cursor cursor = db.rawQuery("SELECT " + COL_PAYED + " FROM "+BEFORE_PARTICIPANTS_TABLES + courseName + PARTICIPANTS_ENDING, null);
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(0)==1)
                    payed.add(true);
                else
                    payed.add(false);
            } while (cursor.moveToNext());
        }


        db.close();
        return payed;
    }

    public List<String> getParticipantNames(String courseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> names = new ArrayList<String>();


        Cursor cursor = db.rawQuery("SELECT " + COL_NAME + " FROM "+BEFORE_PARTICIPANTS_TABLES + courseName + PARTICIPANTS_ENDING, null);

        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return names;
    }
    public List<ParticipantModel> getParticipantModelListOfCourse(String courseName){
        List<String> names= getParticipantNames(courseName);
        List<Boolean> payed= getParticipantsIsPayed(courseName);
        List<ParticipantModel> participants=new ArrayList<ParticipantModel>();
        for(int i =0;i<names.size();i++){
            ParticipantModel participant=new ParticipantModel(names.get(i),payed.get(i));
            participants.add(participant);
        }
        return participants;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String queryString = "SELECT " + COL_NAME + " FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(queryString, null);
        if (c.moveToFirst()) {
            do {
                db.execSQL("DROP TABLE IF EXISTS " +BEFORE_PARTICIPANTS_TABLES+ c.getString(0) + PARTICIPANTS_ENDING);
            } while (c.moveToNext());
        }

        c.close();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void updateParticipant(ParticipantModel participant, String oldName, String courseName) {
        deleteOneParticipant(oldName, courseName);
        addOneParticipant(participant, courseName);
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

    public boolean checkForDuplicateParticipant(String participantName, String courseName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "SELECT " + COL_NAME + " FROM " + BEFORE_PARTICIPANTS_TABLES+courseName + PARTICIPANTS_ENDING + " WHERE " + COL_NAME + " = '" + participantName + "'";
        Cursor c = db.rawQuery(queryString, null);

        boolean duplicate = c.moveToFirst();

        c.close();

        return duplicate;
    }

    public boolean getParticipantIsPayed(String name, String courseName) {

        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT " + COL_PAYED + " FROM " +BEFORE_PARTICIPANTS_TABLES+ courseName + PARTICIPANTS_ENDING + " WHERE " + COL_NAME + " = '" + name + "'";
        Cursor c = db.rawQuery(queryString, null);

        c.moveToFirst();

        boolean payed = (1 == c.getInt(0));

        c.close();
        db.close();

        return payed;
    }
    public int getHowMuchParticipantsPayed(String courseName){
        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT COUNT(*) FROM " +BEFORE_PARTICIPANTS_TABLES+ courseName+ PARTICIPANTS_ENDING+" WHERE " + COL_PAYED + " = '" + 1 + "'";
        Cursor c = db.rawQuery(queryString, null);

        c.moveToFirst();

        int numOfParticipantsPayed=c.getInt(0);

        c.close();
        db.close();

        return numOfParticipantsPayed;
    }
    public int getHowMuchParticipantsDidntPayed(String courseName){
        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT COUNT(*) FROM " +BEFORE_PARTICIPANTS_TABLES+ courseName+ PARTICIPANTS_ENDING+" WHERE " + COL_PAYED + " = '" + 0 + "'";
        Cursor c = db.rawQuery(queryString, null);

        c.moveToFirst();

        int numOfParticipantsDidntPayed=c.getInt(0);

        c.close();
        db.close();

        return numOfParticipantsDidntPayed;
    }

    public int getHowMuchParticipantsInCourse(String courseName){
        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT COUNT(*) FROM " +BEFORE_PARTICIPANTS_TABLES+ courseName+ PARTICIPANTS_ENDING;
        Cursor c = db.rawQuery(queryString, null);

        c.moveToFirst();

        int howMuchParticipantsInCourse=c.getInt(0);

        c.close();
        db.close();

        return howMuchParticipantsInCourse;
    }

    public boolean downloadCourseDetails(String courseName){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return false;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT * FROM " +BEFORE_PARTICIPANTS_TABLES+ courseName+ PARTICIPANTS_ENDING;
        Cursor c = db.rawQuery(queryString, null);

        String fileName=courseName+".txt";

        File file = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            String data=courseName+"\r\n";
            fileOutputStream.write(data.getBytes());
            if (c.moveToFirst()){
             do {
                 String isPayedTXT="payed";
                 if(c.getInt(1)==0)
                     isPayedTXT="didn't pay";
                 data=c.getString(0)+" "+isPayedTXT+"\r\n";
                 fileOutputStream.write(data.getBytes());
             }while (c.moveToNext());
             fileOutputStream.close();
            }

        }
        catch (Exception e){
            return false;
        }


        c.close();
        db.close();
        return true;
    }




    public void deleteAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "SELECT " + COL_NAME + " FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(queryString, null);
        if (c.moveToFirst()) {
            do {
                db.execSQL("DROP TABLE IF EXISTS " +BEFORE_PARTICIPANTS_TABLES+ c.getString(0) + PARTICIPANTS_ENDING);
            } while (c.moveToNext());
        }

        c.close();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
        db.close();
    }
}
