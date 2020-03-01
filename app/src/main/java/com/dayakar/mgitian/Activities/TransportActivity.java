package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Adapters.TransportAdapter;
import com.dayakar.mgitian.Data.Transport_Data;
import com.dayakar.mgitian.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransportActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private TextView networkInfo;
    private ProgressBar mProgressBar;
    private ArrayList<Transport_Data> mTransport_data=new ArrayList<>();
    private TransportAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        setUP();
        if(isConnectedtoInternet()){
            loadTransport_details();
        }
    }

    private void setUP(){
        mToolbar=findViewById(R.id.transport_toolbar);
        mRecyclerView=findViewById(R.id.transport_recyclerVew);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transport");
        mProgressBar=findViewById(R.id.transport_progressBar);
        networkInfo=findViewById(R.id.transport_networkInfo);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));


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

    private void loadTransport_details(){
        //App/Notifications
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/Transport/Bus_routes");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                      try{      Transport_Data data=childSnapshot.getValue(Transport_Data.class);
                          mTransport_data.add(data);

                      }catch (Exception e){
                          System.out.println(e.getMessage());

                      }

                }

                setUpAdapter();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(TransportActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void setUpAdapter(){
        mAdapter=new TransportAdapter(mTransport_data,this);
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.INVISIBLE);


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
