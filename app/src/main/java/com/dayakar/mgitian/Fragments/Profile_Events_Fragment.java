package com.dayakar.mgitian.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Adapters.AlleventsRecyclerAdapter;
import com.dayakar.mgitian.Adapters.ProfileEventsRecyclerAdapter;
import com.dayakar.mgitian.Data.Event;
import com.dayakar.mgitian.Interfaces.OnEventDeleteListener;
import com.dayakar.mgitian.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Events_Fragment extends Fragment implements OnEventDeleteListener {

    private DatabaseReference mDatabase;
    public RecyclerView mRecyclerView;
    private List<Event> listEvent=new ArrayList<>();
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private TextView mNetworkInfo;
    private String input_path_value;
    private String fraggment_name;

    public Profile_Events_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile_events, container, false);
        input_path_value=this.getArguments().getString("path");
        fraggment_name=this.getArguments().getString("fragment");
        setUpUI(v);
        if(isConnectedtoInternet()){
            if(input_path_value!=null)
            load_Events(input_path_value);

        }

        return v;
    }

    private void setUpUI(View v){
        mRecyclerView = v.findViewById(R.id.contentRecyclerView);
        refreshLayout= v.findViewById(R.id.all_events_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        mProgressBar = v.findViewById(R.id.all_event_loading_progressbar);
        linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(true);
        mNetworkInfo=v.findViewById(R.id.allEventNetwork);
        //refresh layout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override public void run() {
                        load_Events(input_path_value);
                        refreshLayout.setRefreshing(false);

                    }

                }, 2000); // Delay in millis

            }

        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                refreshLayout.setEnabled(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0); // 0 is for first item position
            }
        });

    }
    private void load_Events(String path){
        mDatabase = FirebaseDatabase.getInstance().getReference().child(path);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listEvent.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eventShopsht : dataSnapshot.getChildren()) {
                        Event firebaseEvent = eventShopsht.getValue(Event.class);
                        listEvent.add(firebaseEvent);

                    }
                    setUPAdapter();
                }else{
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mNetworkInfo.setText("No events added ..!");
                    mNetworkInfo.setVisibility(View.VISIBLE);


                }
            }

// Sorting the list taking into account the comparator

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setUPAdapter(){
        if(listEvent.size()>1){
            Collections.sort(listEvent, Event.sortByLatest);
        }
        ProfileEventsRecyclerAdapter adapter = new ProfileEventsRecyclerAdapter(getContext(), listEvent,this,fraggment_name);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mProgressBar.setVisibility(View.INVISIBLE);

                //  Toast.makeText(getContext(), "No events added", Toast.LENGTH_SHORT).show();
            }
    private boolean isConnectedtoInternet(){

        ConnectivityManager
                cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //checking connection Status if connected or not...
        if(activeNetwork != null &&  activeNetwork.isConnectedOrConnecting()) {
            mNetworkInfo.setVisibility(View.INVISIBLE);
            return true;
        }else {
            mProgressBar.setVisibility(View.INVISIBLE);
            return false;


        }
    }

    @Override
    public void onEventDelete(String key) {
        showAlertOnDelete(key);
    }
    private void showAlertOnDelete(String key){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setCancelable(false);
        if(fraggment_name.equalsIgnoreCase("postedEvents")){
            dialog.setIcon(R.drawable.ic_delete_black_24dp);
            dialog.setTitle("Are you sure you want to Delete this Event?");
        }else{
            dialog.setIcon(R.drawable.ic_favorite_black_24dp);
            dialog.setTitle("Are you sure you want to remove this favourite?");

        }
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(fraggment_name.equalsIgnoreCase("postedEvents")){
                    delete_Event(key);
                }else{
                    remove_Event(key);
                }

                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void remove_Event(String postId){

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String uid=user.getUid();
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("all_users").child(uid).child(fraggment_name).child(postId);

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }
    private void delete_Event(String postId){

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String uid=user.getUid();
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("all_users").child(uid).child(fraggment_name).child(postId);

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               // Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            DatabaseReference all_events_ref=FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/events/All_Events");
            all_events_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        DataSnapshot snapshot=postSnapshot.child(postId);
                        if(snapshot.exists()){
                            snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }


}