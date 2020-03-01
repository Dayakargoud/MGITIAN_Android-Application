package com.dayakar.mgitian.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.dayakar.mgitian.Adapters.MainRecycerView_Adapter;
import com.dayakar.mgitian.Adapters.Syllabus_Adapter;
import com.dayakar.mgitian.Data.DataContract;
import com.dayakar.mgitian.Data.DatabaseHelper;
import com.dayakar.mgitian.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayDetailsActivity extends AppCompatActivity {
  private   List<String> subjects=new ArrayList<>();
    private List<String> lecturers=new ArrayList<>();
    private List<String> timings=new ArrayList<>();
    private List<String> location=new ArrayList<>();
    private ArrayList<String> units=new ArrayList<>();
    private RecyclerView mRecyclerView;
    private  String data_value;
    private String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);
        Toolbar mToolbar= findViewById(R.id.day_detailtoolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        data_value =intent.getStringExtra("activity_title");
        setUP_UI();

        value=intent.getStringExtra("key");

        getSupportActionBar().setTitle(data_value);


         if(value.equals("daily")){
             readingRoomData();
             MainRecycerView_Adapter adapter=new MainRecycerView_Adapter(subjects,lecturers,timings,location,this);
             mRecyclerView.setAdapter(adapter);


         }else{
                 reading_syllabus();
             units.removeAll(Arrays.asList(null,""));
                 Syllabus_Adapter ab=new Syllabus_Adapter(units,this);
                 mRecyclerView.setAdapter(ab);

         }



    }
    private void setUP_UI(){
        Toolbar mToolbar= findViewById(R.id.day_detailtoolbar);
        setSupportActionBar(mToolbar);
        mRecyclerView= findViewById(R.id.day_recyclerVew);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  android.R.id.home:{
                onBackPressed();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void readingRoomData() {

        DatabaseHelper dr = new DatabaseHelper(this);
        SQLiteDatabase db = dr.getReadableDatabase();
        String[] projection = {
                DataContract.WeekEntry.TIMINGS,
                DataContract.WeekEntry.COLUMN_ID


        };
        String selection = DataContract.WeekEntry.COLUMN_DAY + " = ?";
        String[] selectionArgs = {data_value};


        Cursor cursor = db.query(
                DataContract.WeekEntry.TABLE_WEEK,
                null,
                selection,
                selectionArgs,
                null,
                null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //reading room numbers
                int index1 = cursor.getColumnIndex(DataContract.WeekEntry.P1ROOM);
                int index2= cursor.getColumnIndex(DataContract.WeekEntry.P2ROOM);
                int index3 = cursor.getColumnIndex(DataContract.WeekEntry.P3ROOM);
                int index4 = cursor.getColumnIndex(DataContract.WeekEntry.P4ROOM);
                int index5 = cursor.getColumnIndex(DataContract.WeekEntry.P5ROOM);
                int index6 = cursor.getColumnIndex(DataContract.WeekEntry.P6ROOM);
                //reading lecturer names
                int index11 = cursor.getColumnIndex(DataContract.WeekEntry.P1Lecturer);
                int index22= cursor.getColumnIndex(DataContract.WeekEntry.P2Lecturer);
                int index33 = cursor.getColumnIndex(DataContract.WeekEntry.P3Lecturer);
                int index44 = cursor.getColumnIndex(DataContract.WeekEntry.P4Lecturer);
                int index55 = cursor.getColumnIndex(DataContract.WeekEntry.P5Lecturer);
                int index66 = cursor.getColumnIndex(DataContract.WeekEntry.P6Lecturer);
                //reading clases
                int index111 = cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_1);
                int index222= cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_2);
                int index333 = cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_3);
                int index444 = cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_4);
                int index555 = cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_5);
                int index666 = cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_6);

                subjects.add(cursor.getString(index111));
                subjects.add(cursor.getString(index222));
                subjects.add(cursor.getString(index333));
                subjects.add(cursor.getString(index444));
                subjects.add(cursor.getString(index555));
                subjects.add(cursor.getString(index666));

                location.add(cursor.getString(index1));
                location.add(cursor.getString(index2));
                location.add(cursor.getString(index3));
                location.add(cursor.getString(index4));
                location.add(cursor.getString(index5));
                location.add(cursor.getString(index6));

                lecturers.add(cursor.getString(index11));
                lecturers.add(cursor.getString(index22));
                lecturers.add(cursor.getString(index33));
                lecturers.add(cursor.getString(index44));
                lecturers.add(cursor.getString(index55));
                lecturers.add(cursor.getString(index66));

                cursor.moveToNext();
            }
        }
        cursor.close();
// cursor for reading timings
        Cursor cursor2 = db.query(
                DataContract.WeekEntry.TABLE_WEEK, projection,
                null,
                null,
                null,
                null, null);


        if (cursor2.moveToFirst()) {
            while (!cursor2.isAfterLast()) {

                int time=cursor2.getColumnIndex(DataContract.WeekEntry.TIMINGS);

                timings.add(cursor2.getString(time));


                cursor2.moveToNext();
            }
        }
        cursor2.close();


        dr.close();


    }
    private void reading_syllabus(){


        DatabaseHelper dr = new DatabaseHelper(this);
        SQLiteDatabase db = dr.getReadableDatabase();

        String selection = DataContract.SyllabusEntry.SUBJECT + " = ?";
        String[] selectionArgs = {data_value};


        Cursor cursor = db.query(
                DataContract.SyllabusEntry.TABLE_SYLLABUS,
                null,
                selection,
                selectionArgs,
                null,
                null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //reading room numbers
                int index1 = cursor.getColumnIndex(DataContract.SyllabusEntry.UNIT_1);
                int index2= cursor.getColumnIndex(DataContract.SyllabusEntry.UNIT_2);
                int index3 = cursor.getColumnIndex(DataContract.SyllabusEntry.UNIT_3);
                int index4 = cursor.getColumnIndex(DataContract.SyllabusEntry.UNIT_4);
                int index5 = cursor.getColumnIndex(DataContract.SyllabusEntry.UNIT_5);
                int index6 = cursor.getColumnIndex(DataContract.SyllabusEntry.REFERENCE);
                   units.add(cursor.getString(index1));
                units.add(cursor.getString(index2));
                units.add(cursor.getString(index3));
                if(cursor.getString(index4)!=null){
                    units.add(cursor.getString(index4));

                }if(cursor.getString(index5)!=null){
                    units.add(cursor.getString(index5));

                }if(cursor.getString(index6)!=null){
                    units.add(cursor.getString(index6));

                }



                cursor.moveToNext();
            }
        }
        cursor.close();


        dr.close();
        //reading lecturer names
    }


}
