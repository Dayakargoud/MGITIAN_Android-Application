package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Adapters.AnouncementAdapter;
import com.dayakar.mgitian.Adapters.NotificationAdapter;
import com.dayakar.mgitian.Data.NotificationData;
import com.dayakar.mgitian.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private NotificationAdapter mNotificationAdapter;
    private ArrayList<String> titles=new ArrayList<>();
    private ArrayList<String> descs=new ArrayList<>();
    private ArrayList<String> timeStamp=new ArrayList<>();
    private ProgressBar mProgressBar;
    private String activitytitle;
    private TextView networkInfo;
    private ArrayList<NotificationData> dataList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Intent intent=getIntent();
        if(intent.hasExtra("activity")){
            activitytitle=intent.getStringExtra("activity");
            setUpUI();
          if(isConnectedtoInternet()){
              if(activitytitle.equalsIgnoreCase("Notifications")){
                  loadNotifications();
              }else{
                  loadAnnouncements();
              }

          }

        }



    }

    private void setUpUI(){
        mToolbar=findViewById(R.id.notification_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(activitytitle);
        mProgressBar=findViewById(R.id.notification_progressbar);
        networkInfo=findViewById(R.id.notification_networkInfo);
        mRecyclerView= findViewById(R.id.notification_recyclerVew);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));


    }
    private void loadNotifications(){
             //App/Notifications
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("App/Notifications");

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataList.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                             NotificationData data=postSnapshot.getValue(NotificationData.class);
                            System.out.println(data);
                                dataList.add(data);


                    }
                    storeNotificationCount(dataList.size());
                    setUpAdapter();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(NotificationsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });




    }
    private boolean isConnectedtoInternet(){

        ConnectivityManager
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        //checking connection Status if connected or not...
        if(activeNetwork != null &&  activeNetwork.isConnectedOrConnecting()) {

            return true;
        }else {
            mProgressBar.setVisibility(View.INVISIBLE);
            networkInfo.setVisibility(View.VISIBLE);
            return false;


        }
    }

    private void loadAnnouncements(){
        //colleges/MGIT/Notifications
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/Announcements");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                titles.clear();
                descs.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    titles.add(postSnapshot.getKey());
                    descs.add(postSnapshot.getValue().toString());
                }
                setUpAnnouncementAdapter();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(NotificationsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void setUpAdapter(){
        mNotificationAdapter=new NotificationAdapter(this,dataList);
        mRecyclerView.setAdapter(mNotificationAdapter);
        mProgressBar.setVisibility(View.INVISIBLE);
        if(mNotificationAdapter.getItemCount()==0){
            networkInfo.setText("No Notifications yet...");
        }


    }

    private void storeNotificationCount(int count){
        SharedPreferences sharedPreferences = getSharedPreferences("notifications", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("notificationKey", count);
        editor.apply();

    }
    private void setUpAnnouncementAdapter(){
        AnouncementAdapter adapter=new AnouncementAdapter(this,titles,descs);
        mRecyclerView.setAdapter(adapter);
        mProgressBar.setVisibility(View.INVISIBLE);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
