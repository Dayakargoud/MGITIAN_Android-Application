package com.dayakar.mgitian.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.dayakar.mgitian.Data.DataContract.WeekEntry;
import com.dayakar.mgitian.Data.DataContract.SyllabusEntry;
import com.dayakar.mgitian.Data.DataContract.FacultyEntry;



public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABSE_VERSION=1;
    public static final String DATABSE_NAME="classroom.db";

    public DatabaseHelper(Context context) {
        super(context,DATABSE_NAME,null,DATABSE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //CREATE TABLE SYLLABUS(id INTEGER PRIMARY KEY,name TEXT);
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + SyllabusEntry.TABLE_SYLLABUS+ " (" +
                        SyllabusEntry.COLUMN_ID + " INTEGER PRIMARY KEY   ," +
                        SyllabusEntry.SUB_CODE + " TEXT ," +
                        SyllabusEntry.SUBJECT + " TEXT ," +
                        SyllabusEntry.UNIT_1 + " TEXT ," +
                        SyllabusEntry.UNIT_2 + " TEXT ," +
                        SyllabusEntry.UNIT_3 + " TEXT ," +
                        SyllabusEntry.UNIT_4 + " TEXT ," +
                        SyllabusEntry.UNIT_5 + " TEXT ," +
                        SyllabusEntry.REFERENCE + " TEXT)";

        Log.d("table info: ","table created success");
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);


        String SQL_DAY_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + DataContract.WeekEntry.TABLE_WEEK + " (" +
                        WeekEntry.COLUMN_ID + " INTEGER PRIMARY KEY,"+
                        WeekEntry.COLUMN_DAY+ " TEXT," +
                        WeekEntry.PERIOD_1 + " TEXT, "+
                        WeekEntry.PERIOD_2 + " TEXT,"+
                        WeekEntry.PERIOD_3 + " TEXT,"+
                        WeekEntry.PERIOD_4 + " TEXT,"+
                        WeekEntry.PERIOD_5 + " TEXT,"+
                        WeekEntry.PERIOD_6+ " TEXT,"+
                        WeekEntry.TIMINGS+ " TEXT,"+
                        WeekEntry.P1ROOM+ " TEXT,"+
                        WeekEntry.P2ROOM+ " TEXT,"+
                        WeekEntry.P3ROOM+ " TEXT,"+
                        WeekEntry.P4ROOM+ " TEXT,"+
                        WeekEntry.P5ROOM+ " TEXT,"+
                        WeekEntry.P6ROOM+ " TEXT,"+
                        WeekEntry.P1Lecturer+ " TEXT,"+
                        WeekEntry.P2Lecturer+ " TEXT,"+
                        WeekEntry.P3Lecturer+ " TEXT,"+
                        WeekEntry.P4Lecturer+ " TEXT,"+
                        WeekEntry.P5Lecturer+ " TEXT,"+
                        WeekEntry.P6Lecturer+ " TEXT)";

                sqLiteDatabase.execSQL(SQL_DAY_ENTRIES);

         //faculty table
        String SQL_FACULTY_ENTRIES=
                "CREATE TABLE IF NOT EXISTS " + DataContract.FacultyEntry.TABLE_Faculty + " (" +
                        FacultyEntry.COLUMN_ID + " INTEGER PRIMARY KEY,"+
                        FacultyEntry.NAME+ " TEXT," +
                        FacultyEntry.DESIGNATION + " TEXT, "+
                        FacultyEntry.QUALIFICATION + " TEXT,"+
                        FacultyEntry.CABIN + " TEXT)";

        sqLiteDatabase.execSQL(SQL_FACULTY_ENTRIES);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
    public void putWeekInformation(String _id,String week,String timings,String mon_sched,String tue_sched,
                                   String wed_sched,String thur_sched,String fri_sched,String sat_sched,

                                   SQLiteDatabase db){




        ContentValues values = new ContentValues();
        values.put(WeekEntry._ID,_id);
        values.put(DataContract.WeekEntry.COLUMN_DAY, week);
        values.put(WeekEntry.PERIOD_1, mon_sched);
        values.put(DataContract.WeekEntry.PERIOD_2, tue_sched);
        values.put(DataContract.WeekEntry.PERIOD_3, wed_sched);
        values.put(DataContract.WeekEntry.PERIOD_4, thur_sched);
        values.put(DataContract.WeekEntry.PERIOD_5, fri_sched);
        values.put(WeekEntry.PERIOD_6, sat_sched);


        db.insert(DataContract.WeekEntry.TABLE_WEEK, null, values);


    }
}
