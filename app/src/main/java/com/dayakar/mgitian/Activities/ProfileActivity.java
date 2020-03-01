package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Data.Event;
import com.dayakar.mgitian.Fragments.MessageFragment;
import com.dayakar.mgitian.Fragments.Profile_Events_Fragment;
import com.dayakar.mgitian.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView userEmail,mEventInfo,mNetworkInfo;
    private TextView userName;
    private List<Event> listEvent=new ArrayList<>();
    private DatabaseReference mDatabase;
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private NestedScrollView mNestedScrollView;
    private CollapsingToolbarLayout ctl;
    private RelativeLayout mRelativeLayout;
    private Button sentToLoginButton;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpUI();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(isConnectedtoInternet()){
            //load_Events();
            if(user==null){
                sentToLoginButton.setVisibility(View.VISIBLE);
                mNetworkInfo.setVisibility(View.VISIBLE);
                mNetworkInfo.setText("Login to customise posted and favorited events");

                mPager.setVisibility(View.INVISIBLE);
            }else {
                mPager.setVisibility(View.VISIBLE);
                sentToLoginButton.setVisibility(View.INVISIBLE);
                mNetworkInfo.setVisibility(View.INVISIBLE);
                setUpViewPager();
            }

            setDisplayName();
        }
    }
    private void setUpUI(){
        mToolbar=findViewById(R.id.profile_toolbar);
        mNetworkInfo=findViewById(R.id.profile_network_info);
        mPager = findViewById(R.id.profileViewPager);
        mTabLayout = findViewById(R.id.profileTablayout);
        userEmail=findViewById(R.id.profile_email);
        userName=findViewById(R.id.profile_userEditText);
        sentToLoginButton=findViewById(R.id.profile_login_Button);
        mNestedScrollView=findViewById(R.id.nested);
        mRelativeLayout=findViewById(R.id.relativeLaoutProfile);
        mNestedScrollView.setFillViewport(true);
        ctl=findViewById(R.id.profile_collapsingToolarView);
        ctl.setTitleEnabled(false);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account  ");


        sentToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
          }
    private void setDisplayName(){
    FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = auth.getCurrentUser();
            if (firebaseUser != null) {
                String userId = firebaseUser.getDisplayName();
                String userEm = firebaseUser.getEmail();
                userName.setText(userId);
                userEmail.setText(userEm);
                userName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName("Dayakar").build();
                        firebaseUser.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(ProfileActivity.this, "User name updated", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

        }

}
    private boolean isConnectedtoInternet(){

        ConnectivityManager
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //checking connection Status if connected or not...
        if(activeNetwork != null &&  activeNetwork.isConnectedOrConnecting()) {
            mNetworkInfo.setVisibility(View.INVISIBLE);
            return true;
        }else {

            return false;


        }
    }

    private void setUpViewPager(){
        //setting up the pagerAdapter
         mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);

        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mPager);
        mPager.setNestedScrollingEnabled(false);
            mTabLayout.getTabAt(0).setIcon(R.drawable.ic_event_posted);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_favorite_events);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_message_received);
        mTabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                 tab.getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

             }

             @Override
             public void onTabUnselected(TabLayout.Tab tab) {
                 tab.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);


             }

             @Override
             public void onTabReselected(TabLayout.Tab tab) {

             }
         });
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<String> list;


        public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
           list =new ArrayList<>();
            list.add("Events posted");
            list.add("Favourites");
            list.add("Messages");


        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            String userId=null;
            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
               userId= user.getUid();

            }
            if (position == 0) {
                Profile_Events_Fragment fragment = new Profile_Events_Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("fragment", "postedEvents");
                bundle.putString("path","all_users/"+userId+"/postedEvents");
                fragment.setArguments(bundle);
                return fragment;
            } else if (position == 1) {
                Profile_Events_Fragment fragment = new Profile_Events_Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("fragment", "favorites");
                bundle.putString("path","all_users/"+userId+"/favorites");
                fragment.setArguments(bundle);
                return fragment;
            } else
                return new MessageFragment();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            if(mPagerAdapter==null){
                sentToLoginButton.setVisibility(View.INVISIBLE);
                mNetworkInfo.setVisibility(View.INVISIBLE);
                setDisplayName();
                mPager.setVisibility(View.VISIBLE);
             mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);
            mPager.setAdapter(mPagerAdapter);
            mPagerAdapter.notifyDataSetChanged();
            }


        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}