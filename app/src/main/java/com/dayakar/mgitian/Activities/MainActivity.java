package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Fragments.AllEventsFragment;
import com.dayakar.mgitian.BuildConfig;
import com.dayakar.mgitian.Data.DataContract;
import com.dayakar.mgitian.Data.DatabaseHelper;
import com.dayakar.mgitian.Data.NotificationData;
import com.dayakar.mgitian.Fragments.EventHomeFragment;
import com.dayakar.mgitian.Fragments.HomeFragment;
import com.dayakar.mgitian.Fragments.MessageFragment;
import com.dayakar.mgitian.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mTextVIew;
    private RecyclerView category_RecyclerView,trending_RecyclerView;
    private List<String> subjects=new ArrayList<String>();
    private List<String> lecturers=new ArrayList<String>();
    private List<String> timings=new ArrayList<String>();
    private List<String> location=new ArrayList<String>();
    private ArrayList<Integer> icons=new ArrayList<>();
    private  ArrayList<String> titles=new ArrayList<>();
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private BottomNavigationView navigation;
    private int databaseItems;
    private ProgressBar mProgressBar;
    private TextView categoryText,networkInfo;
    private DatabaseReference mDatabaseReference,mDatabase_faculty_Reference,mSyllabusRef;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> listEvent;
    private DatabaseReference mDatabase;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 214;
    private   TextView textCartItemCount;
    private int mCartItemCount = 0;
    private String new_version;
    private int new_items,previous_items,events_previous_count;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Fragment bottomFragment =null;
    private String fragmenttag="";
    private Fragment active=null;
    private String current="";
    private ArrayList<String> circulars=new ArrayList<>();
    private TextView mbageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp_UI();
         registeringForFirebaseTopic();
        checkLatestAppVersion();
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            sendDataToFireBaseForAnalytics();
            loadFragment(new HomeFragment(),"HomeFragment");
            try{
                loadNotifications();
                load_EventCount();
            }catch (Exception e){

            }


    }
    private void loadFragment(Fragment fragment, String tag) {
        //switching bottomFragment
        Fragment fm=getSupportFragmentManager().findFragmentByTag(tag);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_contaner, fragment)
                .commit();

    }
    private void remove_fragment_backstack(){

        FragmentManager manager = getSupportFragmentManager();
       // FragmentTransaction trans = manager.beginTransaction();
      //  trans.remove_fragment_backstack(fragment);
      //  trans.commit();
        manager.popBackStack();

    }

    private void sendDataToFireBaseForAnalytics(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MGITIAN");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
    private void registeringForFirebaseTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("updates")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("SetUpActivity","Topic Registation successful");
                    }
                });
    }
    private void setLayoutForWeeked(){
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) categoryText.getLayoutParams();
       // lp.addRule(RelativeLayout.BELOW, mSundayText.getId());
        categoryText.setLayoutParams(lp);
    }
    private void setUp_UI(){
        //drawer and toolbar setup
        mToolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = findViewById(R.id.main_drawer);
        mNavigationView = findViewById(R.id.main_navigation);
        mProgressBar=findViewById(R.id.trending_loading_progressBar);
        mNavigationView.bringToFront();
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setTitle(R.string.app_name);

        mNavigationView.setNavigationItemSelectedListener(this);

         navigation = findViewById(R.id.mainBottomnav);
        navigation.setOnNavigationItemSelectedListener(bottomNavListener);

    }
    private void readingRoomData() {

        DatabaseHelper dr = new DatabaseHelper(this);
        SQLiteDatabase db = dr.getReadableDatabase();

        String[] projection = {
                DataContract.WeekEntry.TIMINGS,
                DataContract.WeekEntry.COLUMN_ID


        };
        String selection = DataContract.WeekEntry.COLUMN_DAY + " = ?";
        String[] selectionArgs = {getToday() };


        Cursor cursor = db.query(
                DataContract.WeekEntry.TABLE_WEEK,
                null,
                selection,
                selectionArgs,
                null,
                null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //reading room numbers
                int index1 = cursor.getColumnIndex(DataContract.WeekEntry.P1ROOM);
                int index2= cursor.getColumnIndex(DataContract.WeekEntry.P2ROOM);
                int index3 = cursor.getColumnIndex(DataContract.WeekEntry.P3ROOM);
                int index4 = cursor.getColumnIndex(DataContract.WeekEntry.P4ROOM);
                int index5 = cursor.getColumnIndex(DataContract.WeekEntry.P5ROOM);
                int index6 = cursor.getColumnIndex(DataContract.WeekEntry.P6ROOM);
                //reading lecturer names
                int index11 = cursor.getColumnIndex(DataContract.WeekEntry.P1Lecturer);
                int index22= cursor.getColumnIndex(DataContract.WeekEntry.P2Lecturer);
                int index33 = cursor.getColumnIndex(DataContract.WeekEntry.P3Lecturer);
                int index44 = cursor.getColumnIndex(DataContract.WeekEntry.P4Lecturer);
                int index55 = cursor.getColumnIndex(DataContract.WeekEntry.P5Lecturer);
                int index66 = cursor.getColumnIndex(DataContract.WeekEntry.P6Lecturer);
                //reading clases
                int index111 = cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_1);
                int index222= cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_2);
                int index333 = cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_3);
                int index444 = cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_4);
                int index555 = cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_5);
                int index666 = cursor.getColumnIndex(DataContract.WeekEntry.PERIOD_6);

                subjects.add(cursor.getString(index111));
                subjects.add(cursor.getString(index222));
                subjects.add(cursor.getString(index333));
                subjects.add(cursor.getString(index444));
                subjects.add(cursor.getString(index555));
                subjects.add(cursor.getString(index666));

                location.add(cursor.getString(index1));
                location.add(cursor.getString(index2));
                location.add(cursor.getString(index3));
                location.add(cursor.getString(index4));
                location.add(cursor.getString(index5));
                location.add(cursor.getString(index6));

                lecturers.add(cursor.getString(index11));
                lecturers.add(cursor.getString(index22));
                lecturers.add(cursor.getString(index33));
                lecturers.add(cursor.getString(index44));
                lecturers.add(cursor.getString(index55));
                lecturers.add(cursor.getString(index66));

                cursor.moveToNext();
            }
        }
        cursor.close();
// cursor for reading timings
        Cursor cursor2 = db.query(
                DataContract.WeekEntry.TABLE_WEEK, projection,
                null,
                null,
                null,
                null, null);


        if (cursor2.moveToFirst()) {
            while (!cursor2.isAfterLast()) {

                int time=cursor2.getColumnIndex(DataContract.WeekEntry.TIMINGS);

                timings.add(cursor2.getString(time));


                cursor2.moveToNext();
            }
        }
        databaseItems= cursor2.getCount();
        cursor2.close();
        dr.close();

    }
    private boolean isSetUPCompleted(){
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        boolean value=sharedPreferences.getBoolean("setupkey", false);
        return value;
    }

    private String getToday(){
        String today="";

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        DateFormat format2 = new SimpleDateFormat("EEEE");
        try {
             today = format2.format(df.parse(formattedDate));
            System.out.println("today is "+today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("MainActivity",today);
        if(today.equals("Sunday")){

          //do something if day is sunday

        }
        return today;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu,menu);

        final MenuItem menuItem = menu.findItem(R.id.notification);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }
    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }

    }
    private void load_EventCount(){

        mDatabase = FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/events/All_Events");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long sum=0;
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    sum=sum+ postSnapshot.getChildrenCount();
                }
                  checkEventsCount((int)sum);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        setupBadge();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.notification){
            Intent intent=new Intent(MainActivity.this, NotificationsActivity.class);
            intent.putExtra("activity","Notifications");
            mCartItemCount=0;
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_settings: {
                item.setChecked(true);
                startActivity(new Intent(this, SettingsActivity.class));
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
            case R.id.nav_feedback:{
                item.setChecked(true);
                sendFeedback();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            case R.id.suggestions: {
                item.setChecked(true);
                startActivity(new Intent(MainActivity.this, SuggestionsActivity.class));
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
            case R.id.nav_share: {
                item.setChecked(true);
                shareApp();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
            case R.id.nav_about: {
                item.setChecked(true);
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                mDrawerLayout.closeDrawer(GravityCompat.START);

                return true;

            }
            case R.id.optout:{
                item.setChecked(true);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    showAlertOnOptOut();
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            case R.id.add_Event:{
                item.setChecked(true);
                startActivity(new Intent(MainActivity.this, AddEventActivity.class));
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            case R.id.account:{
                item.setChecked(true);
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }


        }

        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.Home: {
                            bottomFragment= new HomeFragment();
                            fragmenttag = "HomeFragment";
                            if(!current.equals(fragmenttag)){
                                remove_fragment_backstack();
                                loadFragment(bottomFragment,fragmenttag);}
                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                            current=fragmenttag;
                            break;
                        }
                        case R.id.eventsHome: {
                            bottomFragment= new EventHomeFragment();
                            fragmenttag = "EventHomeFragment";
                            if(!current.equals(fragmenttag)){
                                   remove_fragment_backstack();
                                loadFragment(bottomFragment,fragmenttag);
                            }
                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                            current=fragmenttag;
                            break;
                        }
                        case R.id.messages: {
                            bottomFragment= new MessageFragment();
                            fragmenttag = "MessageFragment";
                            if(!current.equals(fragmenttag)){
                                remove_fragment_backstack();
                                loadFragment(bottomFragment,fragmenttag);}
                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                            current=fragmenttag;
                            break;
                        }
                        case R.id.allevents: {
                            bottomFragment = new AllEventsFragment();
                            fragmenttag = "AllEventsFragment";
                            if(!current.equals(fragmenttag)){
                                remove_fragment_backstack();
                                loadFragment(bottomFragment,fragmenttag);}
                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                            active = bottomFragment;
                            current=fragmenttag;
                            navigation.removeBadge(R.id.allevents);

                        }
                    }
                    return true;
                }
            };

    private void load_Circular(){
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("Colleges/MGIT/Circulars/latest");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                circulars.clear();
                for(DataSnapshot circular: dataSnapshot.getChildren()){
                    circulars.add(circular.getValue().toString());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void shareApp(){

        String body="Check out MGITIAN App,I use it to check attendance,syllabus,events and many more happening in our college.Get it for free " +
                "at \n"+
                "https://play.google.com/store/apps/details?id="+getPackageName();

        String mimeType="text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share App with: ")
                .setText(body)
                .startChooser();
    }

    private void sendFeedback() {

        String mailto = "mailto:timetableapp214@gmail.com" +
                "&subject=" + Uri.encode("Feedback about Mgitian Application") +
                "&body=" + Uri.encode("write your valuable feedback ");

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            e.getMessage();
            Toast.makeText(this, "No apps found to send email", Toast.LENGTH_SHORT).show();
        }

    }

    private void optOut(){
       //clearing all data
        this.deleteDatabase("classroom.db");
        SharedPreferences sharedPreferences = getSharedPreferences("student", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        //clearing setup data
        SharedPreferences userSetUp=getSharedPreferences("app",MODE_PRIVATE);
        SharedPreferences.Editor userEditor=userSetUp.edit();
        userEditor.clear();
        userEditor.apply();

        FirebaseAuth.getInstance().signOut();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            }
        },200);

    }

    private void showAlertOnOptOut(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.logout);
        dialog.setTitle("Are you sure you want to Logout?");
       // dialog.setMessage("This will erases your preferences and log out from app.");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                optOut();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void checkLatestAppVersion(){

        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("App/version");
             mRef.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     new_version=dataSnapshot.child("latestVersion").getValue().toString().trim();
                     compare_versions(new_version);

                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
    }
    private void compare_versions(String new_version){
        float latest=Float.parseFloat(new_version);
        String current= BuildConfig.VERSION_NAME;
        float curnt=Float.parseFloat(current);
       try{
        if(latest>curnt){
            showDialogForAppUpdate();
        }}catch (Exception e){
           Log.i("Main Activity",e.getMessage());
       }


    }
    private void showDialogForAppUpdate(){
      AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
      dialog.setCancelable(false);
      dialog.setIcon(R.drawable.ic_system_update_black_24dp);
      dialog.setTitle("Update Available");
      dialog.setMessage("Please update the app to newer version to get latest features");
      dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
              openPlayStore();
          }
      });
      dialog.setNegativeButton("Later", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
          }
      });
      dialog.show();
  }
    private  void openPlayStore() {
        try {
            Intent goToMarket = new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
            startActivity(goToMarket);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private void open_file(String filePath) {

        File file = new File(filePath);

        Uri pdfuri = FileProvider.getUriForFile(this, "com.dayakar.mgitian.provider", file);
        Intent target = new Intent(Intent.ACTION_VIEW);

        target.setDataAndType(pdfuri, "application/vnd.android.package-archive");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = Intent.createChooser(target, "Open File");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application found to open this file", Toast.LENGTH_SHORT).show();
        }


    }


    private void loadNotifications(){
        //App/Notifications
        ArrayList<NotificationData> count=new ArrayList<>();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("App/Notifications");
           count.clear();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    NotificationData data=postSnapshot.getValue(NotificationData.class);
                    System.out.println(data);
                    count.add(data);


                }

                checkNotifications(count.size());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("MainActivity",databaseError.getMessage());
            }
        });



    }

    private void checkNotifications(int totalItems){

        SharedPreferences sharedPreferences = getSharedPreferences("notifications", MODE_PRIVATE);
        previous_items= sharedPreferences.getInt("notificationKey", 0);


            if(totalItems>previous_items){
                mCartItemCount=(totalItems-previous_items);
                setupBadge();
            }


    }

    private void checkEventsCount(int totalItems){

        SharedPreferences sharedPreferences = getSharedPreferences("alleventsCount", MODE_PRIVATE);
        events_previous_count= sharedPreferences.getInt("eventsCount", 0);


        if(totalItems>events_previous_count){
            int new_count=(totalItems-events_previous_count);
            navigation.getOrCreateBadge(R.id.allevents).setNumber(new_count);
        }


    }


}
