package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Data.DataContract;
import com.dayakar.mgitian.Data.DatabaseHelper;
import com.dayakar.mgitian.Data.Facutly_Data_Class;
import com.dayakar.mgitian.Data.Student;
import com.dayakar.mgitian.Data.Syllabus_Data;
import com.dayakar.mgitian.Data.Week_Data_class;
import com.dayakar.mgitian.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class SetUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner mSinner_College,mSpinner_Branch,mSpinner_Sub_Branch,mSpinner_Year;
    private Button addDataButton;
    private ArrayList<String> branchlist=new ArrayList<>();
    private ArrayList<String> colleges_list=new ArrayList<>();
    private ArrayList<String> sub_branches_list=new ArrayList<>();
    private ArrayList<String> years_list=new ArrayList<>();
    private String user_College,user_Branch,user_subBranch,user_Year;
    private DatabaseReference mDatabaseReference,mDatabase_faculty_Reference,mSyllabusRef;
    private ProgressBar mProgressBar;
    private LinearLayout l1,l2,l3,l4;
    private TextView mInfoText,networkinfo;
    private LinearLayout mLinearLayout;
    private Button retryButton;
    private RelativeLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        setUpUI();
        initialising_Spinners();
        if(isConnectedtoInternet()){
            loadBranches();
            registeringForFirebaseTopic();
        }

         retryButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(isConnectedtoInternet()){
                 loadBranches();
                 }else{
                     Toast.makeText(SetUpActivity.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                 }
             }
         });
        addDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data="colleges/"+user_College+"/Branches/"+user_Branch+"/"+user_subBranch+"/year/"+user_Year+"/";
                // "colleges/MGIT/Branches/EEE/EEE_2/year/4-2/timetables";
                saveUserInfo(data);
                String userBranch=user_subBranch+"/"+user_Year;
                Student student=new Student(user_Branch,user_subBranch,user_Year,user_Year);
                saveStudentInfo(student);
                saveAppOpened(true);
                mProgressBar.setVisibility(View.VISIBLE);
                rootLayout.setClickable(false);
                load_data();
                addDataButton.setEnabled(false);
            }
        });

    }
    private void setUpUI(){
        addDataButton = findViewById(R.id.selection_add_data_button);
        mProgressBar= findViewById(R.id.setUp_loading_progressbar);
        mInfoText= findViewById(R.id.info_textView);
        networkinfo=findViewById(R.id.setup_network_text);
        mLinearLayout=findViewById(R.id.linear_network);
        retryButton=findViewById(R.id.setup_retry_button);
        rootLayout=findViewById(R.id.relativeLayout);

        l1= findViewById(R.id.linear1);
        l2= findViewById(R.id.linear2);
        l3= findViewById(R.id.linear3);
        l4= findViewById(R.id.linear4);

        //initializing spinner Views
        mSinner_College = findViewById(R.id.spinner_college);
        mSpinner_Branch= findViewById(R.id.spinner_Branch);
        mSpinner_Sub_Branch= findViewById(R.id.spinner_subBranch);
        mSpinner_Year= findViewById(R.id.spinner_Year);
    }
    private void registeringForFirebaseTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("updates")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("SetUpActivity","Topic Registation successful");
                    }
                });
    }
    private void saveUserInfo(String value){
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.userPreference), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.userinfoKey), value);
        editor.apply();
    }
    private void saveStudentInfo(Student student){
        SharedPreferences sharedPreferences = getSharedPreferences("student", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("branch", student.getBranch());
        editor.putString("subBranch", student.getSub_branch());
        String yr=student.getYear().substring(0,1);
        editor.putString("year", yr);
        String sem=student.getSem().substring(2);
        editor.putString("sem", sem);
        editor.apply();


    }
    private void saveUserBranch(String value){
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.userBranch), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.userBranch), value);
        editor.apply();
    }
    private void saveAppOpened(boolean value){
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("setupkey", value);
        editor.apply();
    }
    private void initialising_Spinners(){


        //spinners default items
        colleges_list.add("MGIT");
        branchlist.add("Select Branch");
        sub_branches_list.add("Select Section");
        years_list.add("Select Year");

        mSinner_College.setOnItemSelectedListener(this);
        mSpinner_Branch.setOnItemSelectedListener(this);
        mSpinner_Sub_Branch.setOnItemSelectedListener(this);
        mSpinner_Year.setOnItemSelectedListener(this);


        // Creating adapter for spinner for branches
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branchlist);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        mSpinner_Branch.setAdapter(dataAdapter);

        //spinner for colleges
        ArrayAdapter<String> colleges_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colleges_list);
        colleges_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSinner_College.setAdapter(colleges_Adapter);

        //spinner for sub branches
        ArrayAdapter<String> subbranch_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sub_branches_list);
        subbranch_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_Sub_Branch.setAdapter(subbranch_Adapter);

        //spinner for Years
        ArrayAdapter<String> years_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years_list);
        years_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_Year.setAdapter(years_Adapter);

    }
//    private void loadingcolleges(){                                                      //Colleges/MGIT/Branches/EEE
//
//        final DatabaseReference mCollges_Databasereference= FirebaseDatabase.getInstance().getReference().child("colleges/");
//
//        mCollges_Databasereference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot classdata:dataSnapshot.getChildren()){
//                    colleges_list.add(classdata.getKey());
//                }
//                loadingCollegesCompleted();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }
    private void load_data(){


        String user_choice="colleges/"+user_College+"/Branches/"+user_Branch+"/"+user_subBranch+"/year/"+user_Year;
        //"colleges/MGIT/Branches/EEE/EEE_2/year/4-2/syllabus"

        DatabaseHelper dr = new DatabaseHelper(SetUpActivity.this);
        final SQLiteDatabase data = dr.getWritableDatabase();

        mSyllabusRef = FirebaseDatabase.getInstance().getReference().child(user_choice+"/syllabus");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(user_choice+"/timetable");
        mDatabase_faculty_Reference = FirebaseDatabase.getInstance().getReference().child(user_choice+"/faculty");


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){

                    Week_Data_class dm=postSnapshot.getValue(Week_Data_class.class);
                    //  Log.d("MainActivty", "onDataChange: "+eventShopsht.getValue());



                    ContentValues values = new ContentValues();
                    values.put(DataContract.WeekEntry.COLUMN_ID,dm.get_id());
                    values.put(DataContract.WeekEntry.COLUMN_DAY, dm.getDay());
                    values.put(DataContract.WeekEntry.PERIOD_1, dm.getPeriod1());
                    values.put(DataContract.WeekEntry.PERIOD_2,dm.getPeriod2());
                    values.put(DataContract.WeekEntry.PERIOD_3, dm.getPeriod3());
                    values.put(DataContract.WeekEntry.PERIOD_4, dm.getPeriod4());
                    values.put(DataContract.WeekEntry.PERIOD_5, dm.getPeriod5());
                    values.put(DataContract.WeekEntry.PERIOD_6, dm.getPeriod6());
                    values.put(DataContract.WeekEntry.TIMINGS,dm.getTimings());
                    //loading room numbers
                    values.put(DataContract.WeekEntry.P1ROOM,dm.getP1r());
                    values.put(DataContract.WeekEntry.P2ROOM,dm.getP2r());
                    values.put(DataContract.WeekEntry.P3ROOM,dm.getP3r());
                    values.put(DataContract.WeekEntry.P4ROOM,dm.getP4r());
                    values.put(DataContract.WeekEntry.P5ROOM,dm.getP5r());
                    values.put(DataContract.WeekEntry.P6ROOM,dm.getP6r());
                    //loading lecturer names
                    values.put(DataContract.WeekEntry.P1Lecturer,dm.getP1l());
                    values.put(DataContract.WeekEntry.P2Lecturer,dm.getP2l());
                    values.put(DataContract.WeekEntry.P3Lecturer,dm.getP3l());
                    values.put(DataContract.WeekEntry.P4Lecturer,dm.getP4l());
                    values.put(DataContract.WeekEntry.P5Lecturer,dm.getP5l());
                    values.put(DataContract.WeekEntry.P6Lecturer,dm.getP6l());



                    try{

                        data.insertOrThrow(DataContract.WeekEntry.TABLE_WEEK, null, values);

                    }
                    catch (SQLiteConstraintException e){

                        Log.e("MAIN_ACTIVITY",e.getMessage());



                    }
                }
                dr.close();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String error=  databaseError.getMessage();
                Log.e("SetUp_Activity",error);


            }
        });

        mSyllabusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()) {
                    Syllabus_Data dm=postSnapshot.getValue(Syllabus_Data.class);
                    DatabaseHelper db = new DatabaseHelper(
                            SetUpActivity.this);
                    final SQLiteDatabase data = db.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(DataContract.SyllabusEntry.COLUMN_ID,dm.getId());
                    values.put(DataContract.SyllabusEntry.SUB_CODE, dm.getSub_code());
                    values.put(DataContract.SyllabusEntry.SUBJECT, dm.getSubject());
                    values.put(DataContract.SyllabusEntry.UNIT_1,dm.getUnit1());
                    values.put(DataContract.SyllabusEntry.UNIT_2, dm.getUnit2());
                    values.put(DataContract.SyllabusEntry.UNIT_3, dm.getUnit3());
                    values.put(DataContract.SyllabusEntry.UNIT_4, dm.getUnit4());
                    values.put(DataContract.SyllabusEntry.UNIT_5, dm.getUnit5());
                    values.put(DataContract.SyllabusEntry.REFERENCE, dm.getReference());
                    try{

                        data.insertOrThrow(DataContract.SyllabusEntry.TABLE_SYLLABUS, null, values);

                    }
                    catch (SQLiteConstraintException e){

                        Log.e("Setup_ACTIVITY",e.getMessage());


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase_faculty_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot facultySnopshot:dataSnapshot.getChildren()){
                    Facutly_Data_Class fd=facultySnopshot.getValue(Facutly_Data_Class.class);
                    DatabaseHelper dbf = new DatabaseHelper(
                            SetUpActivity.this);
                    final SQLiteDatabase data = dbf.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(DataContract.FacultyEntry.COLUMN_ID,fd.getId());
                    values.put(DataContract.FacultyEntry.NAME, fd.getName());
                    values.put(DataContract.FacultyEntry.DESIGNATION, fd.getDesignation());
                    values.put(DataContract.FacultyEntry.QUALIFICATION,fd.getQualification());
                    try{

                        data.insertOrThrow(DataContract.FacultyEntry.TABLE_Faculty,null,values);

                    }
                    catch (SQLiteConstraintException e){

                        Log.e("Setup_ACTIVITY",e.getMessage());


                    }

                }
                try{

                     startActivty(); }
                catch (Exception e){
                    Log.e("SetupActivity",e.getMessage());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void loadingBranchesCompleted(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mInfoText.setVisibility(View.VISIBLE);
        l1.setVisibility(View.VISIBLE);
        l2.setVisibility(View.VISIBLE);
        l3.setVisibility(View.VISIBLE);
        l4.setVisibility(View.VISIBLE);
        addDataButton.setVisibility(View.VISIBLE);
    }
    private boolean isConnectedtoInternet(){

        ConnectivityManager
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //checking connection Status if connected or not...
        if(activeNetwork != null &&  activeNetwork.isConnectedOrConnecting()) {
            mLinearLayout.setVisibility(View.INVISIBLE);
            return true;
        }else {
                   mLinearLayout.setVisibility(View.VISIBLE);
                   mProgressBar.setVisibility(View.INVISIBLE);
            return false;


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if(parent.getId() == R.id.spinner_college)
        {
            user_College = parent.getItemAtPosition(position).toString();
            // Showing selected spinner item

            loadBranches();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branchlist);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            mSpinner_Branch.setAdapter(dataAdapter);
            if(position>0){

               // Toast.makeText(parent.getContext(), "Selected: " + user_College, Toast.LENGTH_LONG).show();
            }
        }
        else if(parent.getId() == R.id.spinner_Branch)
        {

            user_Branch = parent.getItemAtPosition(position).toString();
            loadSubBranches();

            //spinner for sub branches
            ArrayAdapter<String> subbranch_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sub_branches_list);
            subbranch_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner_Sub_Branch.setAdapter(subbranch_Adapter);



            // Showing selected spinner item

            if(position>0){

              //  Toast.makeText(parent.getContext(), "Selected: " + user_Branch, Toast.LENGTH_LONG).show();
                  }
        }
        else if(parent.getId() == R.id.spinner_subBranch)
        {
            user_subBranch = parent.getItemAtPosition(position).toString();
                 load_years();
                 ArrayAdapter<String> year_Adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,years_list);
                 year_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                 mSpinner_Year.setAdapter(year_Adapter);


            // Showing selected spinner item
            if(position>0){
               // Toast.makeText(parent.getContext(), "Selected: " + user_subBranch, Toast.LENGTH_LONG).show();
            }}
        else if(parent.getId() == R.id.spinner_Year)
        {
            user_Year = parent.getItemAtPosition(position).toString();

            // Showing selected spinner item
            if(position>0){
            addDataButton.setEnabled(true);

            }else {
                addDataButton.setEnabled(false);
            }
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void loadBranches(){
        branchlist.clear();
        branchlist.add("Select Branch");
        final DatabaseReference mBranches_Databasereference= FirebaseDatabase.getInstance().getReference().child("colleges/"+user_College+"/Branches/");
        mBranches_Databasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot classdata:dataSnapshot.getChildren()){
                    branchlist.add(classdata.getKey());
                }
                loadingBranchesCompleted();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
    private void loadSubBranches(){

        sub_branches_list.clear();
        sub_branches_list.add("Select section");
        final DatabaseReference mSubBranch_Databasereference= FirebaseDatabase.getInstance().getReference().child(
                "colleges/"+user_College+"/Branches/"+user_Branch
        ); //.child("Colleges/"+user_College+"/"+"Branches"+user_Branch+"/");
        mSubBranch_Databasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot classdata:dataSnapshot.getChildren()){
                    sub_branches_list.add(classdata.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void load_years(){
        years_list.clear();
        years_list.add("Select Year");
        final DatabaseReference mBranches_Databasereference= FirebaseDatabase.getInstance().getReference()
                .child( "colleges/"+user_College+"/Branches/"+user_Branch+"/"+user_subBranch+"/year");
      //colleges/MGIT/Branches/EEE/EEE_2/year
        mBranches_Databasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot classdata:dataSnapshot.getChildren()){
                    years_list.add(classdata.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SetUpActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void startActivty(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.INVISIBLE);
                startActivity(new Intent(SetUpActivity.this,MainActivity.class));
                finish();

            }
        },5000);
    }


}
