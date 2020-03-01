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
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dayakar.mgitian.Adapters.File_Adapter;
import com.dayakar.mgitian.Fragments.Academicfragment;
import com.dayakar.mgitian.Interfaces.OnDownloadListener;
import com.dayakar.mgitian.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Syllabus_Activity extends AppCompatActivity implements OnDownloadListener {
    private ArrayList<String> units=new ArrayList<>();
    private Toolbar mToolbar;
    private ArrayList<String> file_names = new ArrayList<>();
    private ArrayList<String> file_links = new ArrayList<>();
    private ArrayList<String> pageTitles=new ArrayList<>();
    private String download_link;
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private DatabaseReference mDatabase;
    private File_Adapter adapter;
    private TextView networkInfo;
    private final String STOARAGEREAD = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String STORAGEWRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 214;
    private static final String TAG = "Syllabus.class";
    private boolean isPermissionsGranted=false;
    private Button mAllowButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus_);
        pageTitles=getTitles();
        setUpUI();
        if(isConnectedtoInternet()){
            checkPermissions();
            if (isPermissionsGranted){
                setUpViewPager();
            }else{
                networkInfo.setText("Please allow storage permission to view files.");
                mAllowButton.setVisibility(View.VISIBLE);
            }

        }

    }
    private void setUpUI(){
        mToolbar= findViewById(R.id.syllabus_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Syllabus");
        networkInfo=findViewById(R.id.syllabus_networkInfo);
        mPager = findViewById(R.id.syllabusViewPager);
        mTabLayout = findViewById(R.id.syllabusTablayout);
        mAllowButton=findViewById(R.id.storage_button_syllabus);
        mAllowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                if(isPermissionsGranted){
                    setUpViewPager();}
            }
        });


    }
    private ArrayList<String> getTitles(){
        ArrayList<String> titles=new ArrayList<>();
        titles.add("1st Year");
        titles.add("2nd Year");
        titles.add("3rd Year");
        titles.add("4th Year");
        return titles;
    }

    private boolean isConnectedtoInternet(){

        ConnectivityManager
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        //checking connection Status if connected or not...
        if(activeNetwork != null &&  activeNetwork.isConnectedOrConnecting()) {

            return true;
        }else {
            networkInfo.setVisibility(View.VISIBLE);
            return false;


        }
    }
    private void setUpViewPager(){
        mAllowButton.setVisibility(View.INVISIBLE);
        networkInfo.setVisibility(View.INVISIBLE);
        //setting up the pagerAdapter
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);

        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mPager);

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    private class PagerAdapter extends FragmentStatePagerAdapter {


        public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);

        }


        @NonNull
        @Override
        public Fragment getItem(int position){
                Academicfragment fragment=new Academicfragment();
                Bundle bundle=new Bundle();
                bundle.putString("fragment_title",pageTitles.get(position));
                int pos=position+1;
                bundle.putString("positionkey","colleges/MGIT/All_Syllabus/"+pos+"year");
                fragment.setArguments(bundle);
                return fragment;}

        @Override
        public int getCount() {
            return pageTitles.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitles.get(position);
        }
    }

    @Override
    public void onDownload(String filepath, String name) {

    }

    @Override
    public void onFileOpen(String path, String name) {

    }

    @Override
    public void onItemSelected(String filePath, String name) {

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
                            networkInfo.setText("Please allow storage permission to download files");
                            networkInfo.setVisibility(View.VISIBLE);

                        }
                    }
                }
                return;


            }
        }}
}