package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dayakar.mgitian.Fragments.Academicfragment;
import com.dayakar.mgitian.Fragments.ExamFragment;
import com.dayakar.mgitian.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ExamsActivity extends AppCompatActivity {
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private String value;
    private ArrayList<String> titles=new ArrayList<>();
    private final String STOARAGEREAD = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String STORAGEWRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String TAG = "ExamsActivity.class";
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 214;
    private boolean isPermissionsGranted = false;
    private TextView storageinfo;
    private Button mAllowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);

        mPager = findViewById(R.id.examViewPager);
        mTabLayout = findViewById(R.id.examTablayout);
        mToolbar=findViewById(R.id.exam_toolbar);
        storageinfo=findViewById(R.id.storage_info_textView);
        mAllowButton=findViewById(R.id.storage_button);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        if(intent.hasExtra("key")){
          value=intent.getStringExtra("key");
          if(value.equalsIgnoreCase("ebooks")){
              getSupportActionBar().setTitle("E-Books");
              titles=getEbookTitle();

          }else{
              getSupportActionBar().setTitle("Exams and Academics");
              titles=getExamtitles();
          }
      }
        checkPermissions();
        if(isPermissionsGranted){

            setUpViewPager();
        }else{
            storageinfo.setText("Please allow storage permission to view files.");
            mAllowButton.setVisibility(View.VISIBLE);
        }
        mAllowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                if(isPermissionsGranted){
                    setUpViewPager();}
            }
        });

    }
    private void setUpViewPager(){
        mAllowButton.setVisibility(View.INVISIBLE);
        storageinfo.setVisibility(View.INVISIBLE);
       //setting up the pagerAdapter
    PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);

        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mPager);}

   private   ArrayList<String> getExamtitles(){
        ArrayList<String> examtitles=new ArrayList<>();
        examtitles.add("Exams");
        examtitles.add("Academic Calenders");
        return examtitles;
    }
   private   ArrayList<String> getEbookTitle(){
        ArrayList<String> ebook=new ArrayList<>();
          ebook.add("Online files");
          ebook.add("Saved files");
          return ebook;
    }

   private class PagerAdapter extends FragmentStatePagerAdapter{


       public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
           super(fm, behavior);

       }


       @NonNull
       @Override
       public Fragment getItem(int position) {
           if(position==0){
               ExamFragment fragment=new ExamFragment();
               Bundle bundle=new Bundle();
               bundle.putString("fragment_title",titles.get(position));
               fragment.setArguments(bundle);
               return fragment;
           }else{

               Academicfragment fragment=new Academicfragment();
           Bundle bundle=new Bundle();
               bundle.putString("fragment_title",titles.get(position));
           fragment.setArguments(bundle);
           return fragment;}
       }

       @Override
       public int getCount() {
           return titles.size();
       }

       @Nullable
       @Override
       public CharSequence getPageTitle(int position) {
           return titles.get(position);
       }
   }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkPermissions() {

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this, STORAGEWRITE) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, STOARAGEREAD) == PackageManager.PERMISSION_GRANTED) {
                isPermissionsGranted = true;
                //load data here
            } else {
                ActivityCompat.requestPermissions(this, permissions, STORAGE_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, STORAGE_PERMISSION_REQUEST_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++) {
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        isPermissionsGranted = true;
                          setUpViewPager();
                    }else{
                        isPermissionsGranted=false;

                    }
                   }
                }
                return;


        }
    }}
}
