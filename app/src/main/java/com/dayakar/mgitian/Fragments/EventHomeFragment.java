package com.dayakar.mgitian.Fragments;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Adapters.EventClubAdapter;
import com.dayakar.mgitian.Adapters.EventHomeAdapter;
import com.dayakar.mgitian.Adapters.PagerAdapter;
import com.dayakar.mgitian.Adapters.TrendingEventsRecyclerAdapter;
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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventHomeFragment extends Fragment {
    private RecyclerView mHorizontalRecyclerView,clubRecyclerView,trending_RecyclerView;
    private ImageView flipperImage;
    private ViewPager mViewPager;
    private DatabaseReference mDatabase;
    private List<Event> listEvent;
    private ProgressBar mProgressBar;
    private TextView moreText,networkInfo,trending_new_text;
    private ArrayList<String> items=new ArrayList<>();
    private PagerAdapter bannerAdapter;
    private Handler handler;
    private int delay = 2000; //milliseconds
    private int page = 0;
    private RelativeLayout mtrendingLayout;


    public EventHomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_event_home, container, false);

        setUpUI(view);
        handler=new Handler();
        if(isConnectedtoInternet()){
            loadBannerItems();
            setUpTrendingRecyclerView(view);
        }


        // setUpFlipper();
        setUpHomeRecyclerView(view);
        moreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getContext().startActivity(new Intent(getContext(), TrendingActivity.class));
                  FragmentTransaction ft = getFragmentManager().beginTransaction();
                  ft.setCustomAnimations(R.anim.from_bottom,0,R.anim.from_bottom,0
                  );
                  ft.addToBackStack(null);
                  ft.replace(R.id.fragment_contaner, new AllEventsFragment())
                          .commit();

            }
        });
        return view;
    }

    private void setUpUI(View view){
        moreText=view.findViewById(R.id.trending_more_TextView);
        mtrendingLayout=view.findViewById(R.id.trending_RelativeLayout);
       networkInfo=view.findViewById(R.id.HomeEventnetworkinfo);
       trending_new_text=view.findViewById(R.id.trending_TextView);
       trending_new_text.setVisibility(View.INVISIBLE);
       moreText.setVisibility(View.INVISIBLE);
//        mFlipper = (FlipperLayout)view.findViewById(R.id.flipper);

        mViewPager= view.findViewById(R.id.viewPager);
        mViewPager.setClipToPadding(false);
        mViewPager.setPageMargin(12);
        mViewPager.setPadding(80,0,80,0);
        // mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true,new customAnimation());


    }


    private void loadBannerItems(){
        //colleges/MGIT/events/bannerItems
        DatabaseReference bannerReference=FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/events/bannerItems");
        bannerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                for(DataSnapshot banerSnapshot:dataSnapshot.getChildren()){
                    items.add(banerSnapshot.getValue().toString());
                }
                setUpViewPagerforBanner();
            }

               else {
                   System.out.println("path not exist");
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("EventHomeFragment",databaseError.getMessage());
            }
        });

    }
    private void setUpViewPagerforBanner(){
        bannerAdapter=new PagerAdapter(items,getContext());
        mViewPager.setAdapter(bannerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page=position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private Runnable runnable = new Runnable() {
        public void run() {
            if(bannerAdapter!=null){
            if (bannerAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            mViewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
        }
    };
    private boolean isConnectedtoInternet(){

        ConnectivityManager
                cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //checking connection Status if connected or not...
        if(activeNetwork != null &&  activeNetwork.isConnectedOrConnecting()) {

            return true;
        }else {
            mtrendingLayout.setVisibility(View.INVISIBLE);
            mViewPager.setVisibility(View.INVISIBLE);
            networkInfo.setText("No Internet connection..!");
            return false;


        }
    }


    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);

    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
    private void setUpTrendingRecyclerView(View v){
        trending_RecyclerView= v.findViewById(R.id.trending_RecyeclerView);
        trending_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        trending_RecyclerView.setHasFixedSize(true);

        load_Events();


    }
    private void load_Events(){

        listEvent = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/events/All_Events");


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    for(DataSnapshot eventShopsht:postSnapshot.getChildren()){
                        Event firebaseEvent=eventShopsht.getValue(Event.class);
                        listEvent.add(firebaseEvent);

                    }
                }
                setUpAdapter(listEvent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setUpAdapter(List<Event> listEvent){
            if(BuildConfig.VERSION_CODE>1){
                boolean psT=true;
                for(Event e:listEvent){
                    if(e.getPostedTime()==null){
                        psT=false;
                    }
                }

                if(psT){
                    Collections.sort(listEvent,Event.sortByLatest);
               }
            }

                TrendingEventsRecyclerAdapter adapter=new TrendingEventsRecyclerAdapter(getContext(),listEvent);
                trending_RecyclerView.setAdapter(adapter);
                trending_new_text.setVisibility(View.VISIBLE);
                moreText.setVisibility(View.VISIBLE);

    }
    private void setUpHomeRecyclerView(View view){
        mHorizontalRecyclerView= view.findViewById(R.id.horizontal_RecyeclerView);
        mHorizontalRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mHorizontalRecyclerView.setHasFixedSize(true);

        //club recycler view
        clubRecyclerView= view.findViewById(R.id.club_RecyeclerView);
        clubRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        clubRecyclerView.setHasFixedSize(true);

        ArrayList<String> list=new ArrayList<>();
        list.add("Potenzia\nEEE");
        list.add("Microcosm\nECE");
        list.add(" Technovation\nMCT");
        list.add("Yukti\nIT");
        list.add("Cinfra\nCIVIL");
        list.add("Metallon\nMME");
        list.add("Qubit\nCSE");
        list.add("Ignito\nMECH");
        list.add("PhysicoChemia\nP&C");
        list.add("AimHigh\nM&H");
        list.add("Magistech\nISTE student chapter");

        ArrayList<Integer> background=new ArrayList<>();
        background.add(R.drawable.gradient_color1);
        background.add(R.drawable.gradient_color2);
        background.add(R.drawable.gradient_color3);
        background.add(R.drawable.gradient_color4);
        background.add(R.drawable.gradient_color5);
        background.add(R.drawable.gradient_color6);
        background.add(R.drawable.gradient_color7);
        background.add(R.drawable.gradient_color8);
        background.add(R.drawable.gradient_color9);
        background.add(R.drawable.gradient_back2);
        background.add(R.drawable.gradient_back4);

        background.add(R.drawable.gradient2);
        background.add(R.drawable.gradient3);
        background.add(R.drawable.gradient_color1);
        background.add(R.drawable.gradient_back3);
        background.add(R.drawable.gradient_back1);
        background.add(R.drawable.gradient_color1);
        background.add(R.drawable.gradient_color2);
        background.add(R.drawable.gradient_color1);



        EventHomeAdapter mAdapter=new EventHomeAdapter(list,background,getContext());
        mHorizontalRecyclerView.setAdapter(mAdapter);

        ArrayList<String> clubs=new ArrayList<>();
        clubs.add("Literacy club");
        clubs.add("Music club");
        clubs.add("Arts club");
        clubs.add("Sports club");
        clubs.add("ISTE Student's chapter");
        clubs.add("CSI Student's chapter");
        clubs.add("NSS Unit");
        clubs.add("Street Cause");
        clubs.add("Photography");

        ArrayList<Integer> foreground=new ArrayList<>();
         foreground.add(R.color.material1);
        foreground.add(R.color.material2);
        foreground.add(R.color.material3);
        foreground.add(R.color.material4);
        foreground.add(R.color.material5);
        foreground.add(R.color.material6);
        foreground.add(R.color.material7);
        foreground.add(R.color.material8);
        foreground.add(R.color.material9);

        EventClubAdapter clubAdapter=new EventClubAdapter(clubs,foreground,getContext());
        clubRecyclerView.setAdapter(clubAdapter);

    }
    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.65f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
    public class customAnimation implements ViewPager.PageTransformer{
        private static final float MAX_SCALE = 1.2f;
        private static final float MIN_SCALE = 0.85f;


        @Override
        public void transformPage(@NonNull View page, float position) {

            position=position<-1?-1:position;
            position=position>1?1:position;

            float tempScale=position<0?1+position:1-position;

            float slope=(MAX_SCALE-MIN_SCALE)/1;

            float scaleValue=MIN_SCALE+tempScale*slope;
            page.setScaleX(scaleValue);
            page.setScaleY(scaleValue);
            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
                page.getParent().requestLayout();
            }


        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);



    }
}
