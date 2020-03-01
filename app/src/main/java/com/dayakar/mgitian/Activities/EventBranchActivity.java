package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Adapters.AlleventsRecyclerAdapter;
import com.dayakar.mgitian.Data.Event;
import com.dayakar.mgitian.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventBranchActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private List<Event> listEvent;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private String toolbarTitle;
    private TextView adapterItems;
    private SwipeRefreshLayout refreshLayout;
    private String branchValue;
    private LinearLayoutManager linearLayoutManager;
    private TextView branch_intro,event_Name;
    private CardView mCardView;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_branch);

        Intent intent=getIntent();
        setUpUIviews();

        toolbarTitle =getToolbarTitle(intent);
        getSupportActionBar().setTitle(toolbarTitle);

        if(intent.hasExtra("branch")){
            branchValue=getBranchValue(intent);
            boolean isClub=intent.getBooleanExtra("club",false);
            if(!isClub){
             branch_intro.setText(getResources().getIdentifier(branchValue,"string",getPackageName()));
             event_Name.setText(toolbarTitle);
            }else{
                mCardView.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mRelativeLayout.getLayoutParams();
                lp.addRule(RelativeLayout.BELOW, mToolbar.getId());
                mRelativeLayout.setLayoutParams(lp);
            }
        }
        if(isConnectedtoInternet())
        load_branch_events();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }
    private void setUpUIviews(){
        mToolbar = findViewById(R.id.branchtoolbar);
        adapterItems= findViewById(R.id.itemcount);
        branch_intro= findViewById(R.id.event_intro);
        event_Name= findViewById(R.id.event_name);
        mCardView=findViewById(R.id.event_branch_cardview);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.branchRecyclerView);
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRelativeLayout=findViewById(R.id.relative_LayoutBranch);

        mProgressBar= findViewById(R.id.branch_event_loading_progressbar);
        //refresh layout


    }
    private String getToolbarTitle(Intent intent){

        return intent.getStringExtra("activity_title");
    }
    private String getBranchValue(Intent intent){
        String s=intent.getStringExtra("branch");
        if(s.isEmpty()){
            return " ";
        }else return s;

    }
    private void load_branch_events() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/events/All_Events").child(branchValue);
        System.out.println("colleges/MGIT/events/All_Events");
        System.out.println(branchValue);
        listEvent=new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listEvent.clear();
              if(dataSnapshot.exists()){
                  for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                      Event firebaseEvent = postSnapshot.getValue(Event.class);
                      listEvent.add(firebaseEvent);

                  }
                  setUpAdapter();
              }else {
                  mProgressBar.setVisibility(View.INVISIBLE);
                  adapterItems.setText("No Events Found...");
                  adapterItems.setVisibility(View.VISIBLE);
              }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(EventBranchActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpAdapter(){
        AlleventsRecyclerAdapter adapter = new AlleventsRecyclerAdapter(EventBranchActivity.this, listEvent);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mProgressBar.setVisibility(View.INVISIBLE);
        if (adapter.getItemCount() == 0) {
            adapterItems.setText("No Events Found...");
            adapterItems.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);

        }

    }
    private boolean isConnectedtoInternet(){

        ConnectivityManager
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //checking connection Status if connected or not...
        if(activeNetwork != null &&  activeNetwork.isConnectedOrConnecting()) {

            return true;
        }else {
            adapterItems.setText("No internet connection..!");
            adapterItems.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            return false;


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }

        }
        return super.onOptionsItemSelected(item);


    }
}
