package com.dayakar.mgitian.Fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Activities.WinnouActivity;
import com.dayakar.mgitian.Adapters.Category_Adapter;
import com.dayakar.mgitian.Adapters.TrendingRecyclerAdapter;
import com.dayakar.mgitian.Data.Event;
import com.dayakar.mgitian.Interfaces.OnImageDownloadListener;
import com.dayakar.mgitian.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnImageDownloadListener {
    private RecyclerView category_RecyclerView,trending_RecyclerView;
    private ArrayList<Integer> icons=new ArrayList<>();
    private  ArrayList<String> titles=new ArrayList<>();
    private ProgressBar mProgressBar;
    private TextView categoryText,networkInfo;
    private ArrayList<String> listEvent = new ArrayList<>();
    private DatabaseReference mDatabase;
    private TextView mlatest,mLatest2;
    private ArrayList<String> circulars=new ArrayList<>();
    private ArrayList<String> circularLinks=new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        setUp_UI(v);
        if(isConnectedtoInternet()){
            setUpTrendingRecyclerView(v);
            load_Circular();
        }
        loading_categories();


        return v;
    }



    private void setUp_UI(View v){

        networkInfo=v.findViewById(R.id.main_networkInfo);
        mlatest=v.findViewById(R.id.latest_textValue);
        mLatest2=v.findViewById(R.id.latest_textValue2);
        categoryText=v. findViewById(R.id.categories);
        category_RecyclerView= v.findViewById(R.id.category_recyclerView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3,RecyclerView.VERTICAL,false);
        category_RecyclerView.setLayoutManager(gridLayoutManager);
        category_RecyclerView.setNestedScrollingEnabled(false);
        category_RecyclerView.setHasFixedSize(true);
        mProgressBar=v.findViewById(R.id.trending_loading_progressBar);
        mlatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectedtoInternet()){
                if(circularLinks.size()>0){
                Intent intent=new Intent(getContext(), WinnouActivity.class);
                intent.putExtra("page","Circular");
                intent.putExtra("pageURL",circularLinks.get(0));
                startActivity(intent);}}
            }
        });
        mLatest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectedtoInternet()){
                if(circularLinks.size()>0){
                Intent intent=new Intent(getContext(), WinnouActivity.class);
                intent.putExtra("page","Circular");
                intent.putExtra("pageURL",circularLinks.get(1));
                startActivity(intent);}}

            }
        });


    }
    private boolean isConnectedtoInternet(){

        ConnectivityManager
                cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private void setUpTrendingRecyclerView(View v){
        trending_RecyclerView= v.findViewById(R.id.trending_RecyeclerView);
        trending_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        trending_RecyclerView.setHasFixedSize(true);
        load_Events();


    }
    private void load_Events(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/events/trending");
        mDatabase.limitToFirst(3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listEvent.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    listEvent.add(postSnapshot.getValue().toString());
                }
                setUpTrendingAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setUpTrendingAdapter(){
        TrendingRecyclerAdapter adapter=new TrendingRecyclerAdapter(getContext(),listEvent,this);
        trending_RecyclerView.setAdapter(adapter);
    }
    private void load_Circular(){
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/circulars/latest");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                circulars.clear();
                for(DataSnapshot circular: dataSnapshot.getChildren()){
                    circulars.add(circular.getKey());
                    circularLinks.add(circular.getValue().toString());
                }

                mlatest.setText(circulars.get(0));
                mlatest.setSelected(true);
                mLatest2.setText(circulars.get(1));
                mLatest2.setSelected(true);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loading_categories(){
       // icons.add(R.drawable.shedule);
        icons.add(R.drawable.syllabus);
        //icons.add(R.drawable.results);
        icons.add(R.drawable.transportbus);
        icons.add(R.drawable.winnou);
        icons.add(R.drawable.bunk_manager_icon);
        icons.add(R.drawable.ebook);
        icons.add(R.drawable.website);
        icons.add(R.drawable.calendar);
        icons.add(R.drawable.announcement);
        icons.add(R.drawable.calculator);


      //  titles.add("Schedule");
        titles.add("Syllabus");
        titles.add("Transport");
        titles.add("Winnou");
        titles.add("Bunk Manager");
        titles.add("E-Books");
        titles.add("MGIT Website");
        titles.add("Exams and\nAcademics");
        titles.add("Announcements");
        titles.add("SGPA Calculator");

        Category_Adapter adapter=new Category_Adapter(getContext(),icons,titles);

        category_RecyclerView.setAdapter(adapter);
    }








    @Override
    public void onImageLoad() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }



}
