package com.dayakar.mgitian.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.dayakar.mgitian.Adapters.Schedule_list_adapter;
import com.dayakar.mgitian.Data.DataContract;
import com.dayakar.mgitian.Data.DatabaseHelper;
import com.dayakar.mgitian.R;

import java.util.ArrayList;

public class Schedule_Activity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> arrayList=new ArrayList<String>();
    private String day_value;
    private Schedule_list_adapter adapter;
    private ArrayList<Integer> foreground;
    private Toolbar mToolbar;
    private String toolbar_Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Intent intent=getIntent();
        if(intent.hasExtra("feature")){
        day_value=intent.getStringExtra("feature");}
        if(intent.hasExtra("activity_title")){
            toolbar_Title=intent.getStringExtra("activity_title");

        }
        setUp_UI();
        setUpHomeRecyclerView();


           readingSubData();

           adapter=new Schedule_list_adapter(this,arrayList,foreground);

           mRecyclerView.setAdapter(adapter);

       }
   private void setUpHomeRecyclerView(){
       mRecyclerView= findViewById(R.id.schd_recyclerVew);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setHasFixedSize(true);


        foreground=new ArrayList<>();

        foreground.add(R.drawable.gradient_color1);
       foreground.add(R.drawable.gradient_color2);
       foreground.add(R.drawable.gradient_color3);
       foreground.add(R.drawable.gradient_color4);
       foreground.add(R.drawable.gradient_color5);
       foreground.add(R.drawable.gradient_color6);
       foreground.add(R.drawable.gradient_color7);
       foreground.add(R.drawable.gradient_back5);






   }


    private void setUp_UI(){

        mToolbar = findViewById(R.id.schedule_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView= findViewById(R.id.schd_recyclerVew);
        linearLayoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        getSupportActionBar().setTitle(toolbar_Title);

    }
    private void readingWeekData() {

        DatabaseHelper dr = new DatabaseHelper(this);
        SQLiteDatabase db = dr.getReadableDatabase();
        String[] projection = {
                DataContract.WeekEntry.COLUMN_DAY


        };
        String selection = DataContract.WeekEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { "1" };


        Cursor cursor = db.query(
                DataContract.WeekEntry.TABLE_WEEK,
                projection,
                null,
                null,
                null,
                null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                int index = cursor.getColumnIndex(DataContract.WeekEntry.COLUMN_DAY);

                arrayList.add(cursor.getString(index));


                cursor.moveToNext();
            }
        }
        cursor.close();

        dr.close();


    }
    private void readingSubData() {

        DatabaseHelper dr = new DatabaseHelper(this);
        SQLiteDatabase db = dr.getReadableDatabase();
        String[] projection = {
                DataContract.SyllabusEntry.SUBJECT


        };
        Cursor cursor = db.query(
                DataContract.SyllabusEntry.TABLE_SYLLABUS,
                projection,
                null,
                null,
                null,
                null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                int index = cursor.getColumnIndex(DataContract.SyllabusEntry.SUBJECT);

                arrayList.add(cursor.getString(index));


                cursor.moveToNext();
            }
        }
        cursor.close();

        dr.close();


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
}

