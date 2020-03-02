package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Data.Event;
import com.dayakar.mgitian.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Event_Details_Activity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageView image;
    private TextView title,description,contact_Info,reg_fee,venue,branch,timimg;
    private DatabaseReference  mDatabaseReference;
    private  String mPost_key=null;
    private String imagevalue,descValue,titleValue,contactValue,branchValue,reg_fee_Value,venueValue,postId,time_value,postedBy;
    private Long postedTime;
    private boolean is_favorite=false;
    private int adapter_postion;
    private FirebaseUser user;
    private Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__details_);

        user= FirebaseAuth.getInstance().getCurrentUser();
        setUpUIviews();
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        Intent intent = getIntent();
        if(intent!=null) {
            getValuesFromIntent(intent);
            search_favorites(postId);

        }
    }

    private void setUpUIviews(){
        mToolbar= findViewById(R.id.singlepostToolbar);
        image= findViewById(R.id.singlepostImage);
        title= findViewById(R.id.singleposttitle);
        description= findViewById(R.id.singlepostDescription);
        contact_Info= findViewById(R.id.singlepostcontact);
        reg_fee= findViewById(R.id.single_reg_fee_value);
        venue= findViewById(R.id.single_venue_value);
        branch= findViewById(R.id.single_branch_value);
        timimg=findViewById(R.id.eventDetails_timings);
        setSupportActionBar(mToolbar);
        CollapsingToolbarLayout collapsingToolbarLayout= findViewById(R.id.collapsingToolarView);
        getSupportActionBar().setTitle("Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getValuesFromIntent(Intent intent){
        imagevalue = intent.getStringExtra("image");
        descValue=intent.getStringExtra("desc");
        titleValue=intent.getStringExtra("title");
        contactValue=intent.getStringExtra("contact");
        branchValue=intent.getStringExtra("branch");
        reg_fee_Value=intent.getStringExtra("reg_fee");
        venueValue=intent.getStringExtra("venue");
        postId=intent.getStringExtra("postId");
        time_value=intent.getStringExtra("time");
        adapter_postion= intent.getIntExtra("position",0);
        postedTime=intent.getLongExtra("postedTime",0);
        postedBy=intent.getStringExtra("postedBy");

        Picasso.get().load(imagevalue).into(image);

        title.setText(titleValue);
        description.setText(descValue);

        contact_Info.setText(contactValue);
        reg_fee.setText(reg_fee_Value);
        branch.setText(branchValue);
        venue.setText(venueValue);
        if(time_value!=null){
        timimg.setText(time_value);
        }else{
            timimg.setText("Not available");}
        String t=titleValue+"\n"+descValue+"\n"+venueValue;
        if(postId==null){
            postId= UUID.randomUUID().toString();
         //   Toast.makeText(this, "generated id "+postId, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_details_toolbar_menu,menu);
        if(is_favorite){
            MenuItem item=menu.findItem(R.id.favourite_Event);
            item.setIcon(R.drawable.ic_favorite_black_24dp);}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.share_single_Event:{
                try{
                sharePost(titleValue);}catch (Exception e){
                    Toast.makeText(this, "Unable share, please try again later", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.favourite_Event:{
                if(user!=null){
                    if(is_favorite) {
                        try {
                            remove_favourite(postId);
                            item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                        }catch (Exception e){
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else {

                        add_to_favourites(postId);
                        item.setIcon(R.drawable.ic_favorite_black_24dp);
                    }

                }

                else{
                    Toast.makeText(this, "Please Login to add as favourite", Toast.LENGTH_SHORT).show();
                }
            } break;
            case R.id.chat:{
                Toast.makeText(this, "Feature will added soon..", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);


    }

    private void search_favorites(String postId){
        if(user!=null){
            String uid=user.getUid();
            DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("all_users").child(uid).child("favorites").child(postId);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        is_favorite=true;

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }
    private void add_to_favourites(String postid) {

        if (user != null) {
            String uid = user.getUid();
//TO-DO
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("all_users").child(uid).child("favorites");
            Event favourite = new Event(titleValue, descValue, imagevalue, contactValue, reg_fee_Value, venueValue, branchValue, postId,time_value,postedBy,postedTime);
            mDatabaseReference.child(postid).setValue(favourite);
            Toast.makeText(Event_Details_Activity.this, "Favourited", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean remove_favourite(String postId){
        if(user!=null){
            String uid=user.getUid();
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("all_users").child(uid).child("favorites").child(postId);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                is_favorite=false;
                                Toast.makeText(Event_Details_Activity.this, "Removed from favourites", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        return false;

    }
    private void sharePost(String posttitle){

        String body="Hey check out this "+posttitle+" an interesting Event from our college for more details about the event download  MGITIAN app \n"+
                "https://play.google.com/store/apps/details?id="+getPackageName();

        String mimeType="text/plain";
        Bitmap bm = ((BitmapDrawable)image.getDrawable()).getBitmap();

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share Event with:")
                .setText(body)
                .setStream(getLocalBitmapUri(bm,this))
                .startChooser();

    }

    private CharSequence timeStamp(){

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String time ="2019-10-28T18:17:51";
        sdf.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh:mm:ss");
        String format = simpleDateFormat.format(new Date());
        System.out.println(format);
        try{
            long receivedtime=sdf.parse(time).getTime();
            long now=System.currentTimeMillis();
            CharSequence ago= DateUtils.getRelativeTimeSpanString(receivedtime,now,DateUtils.MINUTE_IN_MILLIS);
            System.out.println(ago);
            Toast.makeText(this, ago, Toast.LENGTH_SHORT).show();
            return ago;
        }catch (Exception e){
            e.getMessage();
            return time;

        }
    }
    private static Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

}
