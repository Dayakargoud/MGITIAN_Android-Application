package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.dayakar.mgitian.Adapters.Faculty_Adapter;
import com.dayakar.mgitian.Data.DataContract;
import com.dayakar.mgitian.Data.DatabaseHelper;
import com.dayakar.mgitian.R;

import java.util.ArrayList;

public class FacultyActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<String> designations=new ArrayList<>();
    private ArrayList<String> faculty_names=new ArrayList<>();
    private ArrayList<String> qualifications=new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        Toolbar mToolbar= findViewById(R.id.faculty_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Faculty");
        mRecyclerView= findViewById(R.id.faculty_recyclerVew);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        load_faculty_data();
        Faculty_Adapter adapter=new Faculty_Adapter(faculty_names,qualifications,designations,this);
        mRecyclerView.setAdapter(adapter);

    }

    private void load_faculty_data(){

            DatabaseHelper dr = new DatabaseHelper(this);
            SQLiteDatabase db = dr.getReadableDatabase();

            String[] projection={
                    DataContract.FacultyEntry.COLUMN_ID,
                    DataContract.FacultyEntry.NAME,
                    DataContract.FacultyEntry.DESIGNATION,
                    DataContract.FacultyEntry.QUALIFICATION
            };



            Cursor cursor = db.query(
                    DataContract.FacultyEntry.TABLE_Faculty,
                    projection,
                    null,
                    null,
                    null,
                    null, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    //reading room numbers
                    int index1 = cursor.getColumnIndex(DataContract.FacultyEntry.COLUMN_ID);
                    int index2= cursor.getColumnIndex(DataContract.FacultyEntry.NAME);
                    int index3 = cursor.getColumnIndex(DataContract.FacultyEntry.QUALIFICATION);
                    int index4 = cursor.getColumnIndex(DataContract.FacultyEntry.DESIGNATION);


                    faculty_names.add(cursor.getString(index2));
                    qualifications.add(cursor.getString(index3));
                    designations.add(cursor.getString(index4));


                    cursor.moveToNext();
                }
            }
            cursor.close();


            dr.close();
            //reading lecturer names


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
