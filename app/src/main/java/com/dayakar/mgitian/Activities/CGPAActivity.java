package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Adapters.CGPAdapter;
import com.dayakar.mgitian.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CGPAActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private Button calculate;
    private Spinner years,branch;
    private ArrayList<String> subjects=new ArrayList<>();
    private ArrayList<String> grades=new ArrayList<>();
    private ArrayList<Integer> credits=new ArrayList<>();
    private DatabaseReference mDatabaseReference;
    private RelativeLayout mRelativeLayout;
    private TextView mdataText,cgpa,sgpa;
    private CGPAdapter adpter;
    private CardView cgpa_card;
    private String selected_year,selected_branch;
    private NestedScrollView mNestedScrollView;
    boolean isSelectedBranch=false;
    boolean isSelectedYear =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgpa);
        setUpUI();




    }
    private void setUpUI(){
        mRecyclerView=findViewById(R.id.cgpa_RecyclerView);
        mToolbar=findViewById(R.id.cgpa_toolbar);
        calculate=findViewById(R.id.caluclate_CGPA);
        years=findViewById(R.id.spinner_cgpa_year);
        branch=findViewById(R.id.spinner_cgpa_branch);
        mRelativeLayout=findViewById(R.id.cgpa_relativeLayout);
        mdataText=findViewById(R.id.cgpa_data);
        cgpa_card=findViewById(R.id.cgpa_result_card);
        cgpa= findViewById(R.id.cgpa__result);
        sgpa=findViewById(R.id.sgpa_result);
        mNestedScrollView=findViewById(R.id.cgpa_scrollView);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("SGPA Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
          calculate.setText("Confirm");
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(isSelectedBranch && isSelectedYear)){
                    Toast.makeText(CGPAActivity.this, "Please select Branch and Year", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(calculate.getText().toString().equalsIgnoreCase("Confirm")){
                    loadData();

                }else {

                    try {
                        double tC=21;
                        if((selected_year.contains("4")||selected_year.contains("3"))){
                            tC=24;
                        }
                        double sgpa_result = adpter.calculate(selected_year);
                        cgpa_card.setVisibility(View.VISIBLE);
                        cgpa_card.setFocusableInTouchMode(true);
                        double ss=sgpa_result/tC;
                        sgpa.setText("SGPA= "+sgpa_result+"/"+tC);
                        mNestedScrollView.fullScroll(View.FOCUS_DOWN);
                        cgpa.setText("SGPA="+String.format("%.3f",ss));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        Toast.makeText(CGPAActivity.this, "Please select grade", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });


        years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    selected_year=parent.getItemAtPosition(position ).toString();
                    isSelectedYear =true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                selected_branch= parent.getItemAtPosition(position ).toString();
                isSelectedBranch=true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setUpAdapter(){
        adpter=new CGPAdapter(subjects,credits,this);
        mRecyclerView.setAdapter(adpter);
        calculate.setText("Calculate");
    }
    private void loadData(){

        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/JNTUH_CGPA/"+selected_branch+"/"+selected_year);
      //  mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/JNTUH_CGPA").child(selected_branch).child(selected_year);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjects.clear();
                credits.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    String subj= snapshot.getKey();
                    int cred=Integer.parseInt(snapshot.getValue().toString());
                    subjects.add(subj);
                    credits.add(cred);
                   }

                }else {
                    mdataText.setVisibility(View.VISIBLE);
                    mRelativeLayout.setVisibility(View.INVISIBLE);
                    return;
                }
                setUpAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
