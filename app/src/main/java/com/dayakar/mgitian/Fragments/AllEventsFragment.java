package com.dayakar.mgitian.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.dayakar.mgitian.BuildConfig;
import com.dayakar.mgitian.Data.Event;
import com.dayakar.mgitian.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllEventsFragment extends Fragment {
    private DatabaseReference mDatabase;
    public RecyclerView mRecyclerView;
    private List<Event> listEvent = new ArrayList<>();
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private TextView mNetworkInfo;
    private String input_path_value;

    public AllEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_allevents, container, false);
        setUpUI(v);
        if (isConnectedtoInternet()) {
            load_Events();
        }

        return v;

    }

    private void setUpUI(View v) {
        mRecyclerView = v.findViewById(R.id.contentRecyclerView);
        refreshLayout = v.findViewById(R.id.all_events_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        mProgressBar = v.findViewById(R.id.all_event_loading_progressbar);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(true);
        mNetworkInfo = v.findViewById(R.id.allEventNetwork);
        //refresh layout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        load_Events();
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

    private void load_Events() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/events/All_Events");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listEvent.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot eventShopsht : postSnapshot.getChildren()) {
                        try {
                            Event firebaseEvent = eventShopsht.getValue(Event.class);

                            listEvent.add(firebaseEvent);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "failed to load events", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                storeTotalEventCount(listEvent.size());
                setUpAdapter(listEvent);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean isConnectedtoInternet() {

        ConnectivityManager
                cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //checking connection Status if connected or not...
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            mNetworkInfo.setVisibility(View.INVISIBLE);
            return true;
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            return false;


        }
    }

    private void setUpAdapter(List<Event> listEvent) {
        if (BuildConfig.VERSION_CODE > 1) {
            boolean psT = true;
            for (Event e : listEvent) {
                if (e.getPostedTime() == null) {
                    psT = false;
                }
            }
            if (psT) {
                Collections.sort(listEvent, Event.sortByLatest);
            }
        }
        AlleventsRecyclerAdapter adapter = new AlleventsRecyclerAdapter(getContext(), listEvent);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);


        mProgressBar.setVisibility(View.INVISIBLE);

    }

    private void storeTotalEventCount(int count) {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("alleventsCount", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("eventsCount", count);
            editor.apply();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }


    }
}