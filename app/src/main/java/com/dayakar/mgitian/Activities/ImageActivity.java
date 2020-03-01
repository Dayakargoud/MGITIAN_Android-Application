package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private PhotoView mImageView;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private TextView infoText;
    private String link;
    private ImageView netwoelImage;
    private Button confirm;
    private Spinner years,branch;
    private String selected_year,selected_branch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        setUpUI();
        if(checkingConnectionStatus()){
            load_data();
        }

    }

    private void setUpUI(){
        mImageView= findViewById(R.id.timetable_image_view);
        mToolbar=findViewById(R.id.timetable_toolbar);
        mProgressBar=findViewById(R.id.timetable_progressbar);
        infoText=findViewById(R.id.timetable_info_text);
        netwoelImage=findViewById(R.id.networkImage);
        confirm=findViewById(R.id.schedule_confirm_button);
        years=findViewById(R.id.spinner_schedule_year);
        branch=findViewById(R.id.spinner_schedule_branch);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Timetable");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    selected_year=parent.getItemAtPosition(position ).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_branch= parent.getItemAtPosition(position ).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void load_data(){
      //   classValue.setText(getUserBranch());
      //  getStudentInfo();
        System.out.println("branch "+getUserBranch());
        System.out.println("timetable= "+getUserInfo());
        link=getUserInfo()+"timetables";
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child(link);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String imageUrl= dataSnapshot.getValue().toString();
                    loadImage(imageUrl);
                }else{
                    infoText.setText("No timetable found at this moment.");
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ImageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private boolean checkingConnectionStatus(){

        ConnectivityManager
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        //checking connection Status if connected or not...
        if(activeNetwork != null &&  activeNetwork.isConnectedOrConnecting()) {

            return true;
        }else {
            mProgressBar.setVisibility(View.INVISIBLE);
            netwoelImage.setVisibility(View.VISIBLE);
            infoText.setText("No internet Connection...!");
            return false;


        }
    }
    private void loadImage(String url){
        Picasso.get().load(url).into(mImageView, new Callback() {
            @Override
            public void onSuccess() {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    private String getUserInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.userPreference), MODE_PRIVATE);
        String info = sharedPreferences.getString(getString(R.string.userinfoKey), "");
        return info;
    }
    private String getUserBranch(){
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.userBranch), MODE_PRIVATE);
        String info = sharedPreferences.getString("userBranch", "");
        return info;
    }
    private void getStudentInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("student", MODE_PRIVATE);
        String branch = sharedPreferences.getString("branch", "");
        String subBranch = sharedPreferences.getString("subBranch", "");
        String year = sharedPreferences.getString("year", "");
        String sem = sharedPreferences.getString("sem", "");

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
