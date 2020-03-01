package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Data.Event;
import com.dayakar.mgitian.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    private ImageView selectImage;
    private EditText title;
    private EditText description,contactInfo,venue,reg_fee,time,postedBy;
    private Uri imageUri =null;
    private Button postEventButton,sentToLoginButton;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog mProgress;
    private Spinner mSpinner_Branch;
    private String branch_val=null;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private RelativeLayout mContentLayout;
    private TextView mLoginInfoText;
    private boolean isBranchSelected=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mAuth = FirebaseAuth.getInstance();
        mStorage=FirebaseStorage.getInstance().getReference();
        setUpUI();

        sentToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddEventActivity.this, LoginActivity.class);
                startActivity(intent);


            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
        postEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkcredentialandValidate();
            }
        });

        mSpinner_Branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                String selectedItem = parent.getItemAtPosition(position).toString();
                branch_val=selectedItem;
                isBranchSelected=true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void setUpUI(){

        mToolbar = findViewById(R.id.addEventtoolbar);
        mContentLayout= findViewById(R.id.addEvent_content_layout);
        sentToLoginButton= findViewById(R.id.event_login_Button);
        mLoginInfoText= findViewById(R.id.addevent_LoginTextView);
        selectImage= findViewById(R.id.addimage);
        title= findViewById(R.id.addtitle);
        time=findViewById(R.id.eventDate);
        description= findViewById(R.id.addDescription);
        contactInfo= findViewById(R.id.addContactinfo);
        postEventButton= findViewById(R.id.postButton);
        mSpinner_Branch= findViewById(R.id.spinner_branch);
        reg_fee= findViewById(R.id.addRegistationFeeinfo);
        venue= findViewById(R.id.addVenueinfo);
        postedBy=findViewById(R.id.eventPostedby);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress=new ProgressDialog(this);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imageUri = data.getData();

            selectImage.setImageURI(imageUri);
        }
    }
    private void checkcredentialandValidate(){

        mProgress.setTitle("Posting...");
        mProgress.setMessage("Uploading your Event please wait...");

        final String title_val=title.getText().toString().trim();
        final String desc_val=description.getText().toString().trim();
        final  String cotact_val=contactInfo.getText().toString().trim();
        final String reg_fee_val=reg_fee.getText().toString().trim();
        final String venue_val=venue.getText().toString().trim();
        final  String time_val=time.getText().toString().trim();
        final  String event_postedBy=postedBy.getText().toString().trim();

        if(TextUtils.isEmpty(title_val)) {
            title.setError("Title is required");
            return;
        }
        else if(imageUri==null){
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(desc_val)){
            description.setError("Description is required");
            return;
        }
        else if(TextUtils.isEmpty(cotact_val)){
            contactInfo.setError("Contact info is required");
            return;
        }else if(!isBranchSelected){
            Toast.makeText(this, "Please select branch/club", Toast.LENGTH_SHORT).show();
            return;
        }

        else if(TextUtils.isEmpty(reg_fee_val)){
            reg_fee.setError("Registration fee is required");
            return;
        }else if(TextUtils.isEmpty(venue_val)){
            venue.setError("Venue is required");
            return;
        }else if(TextUtils.isEmpty(time_val)){
            venue.setError("Date is required");
            return;
        }

        else {

                String postId = UUID.randomUUID().toString();
                Event upload = new Event(title_val, desc_val, null, cotact_val, reg_fee_val, venue_val, branch_val, postId,time_val,event_postedBy,System.currentTimeMillis());
                post_event(upload);

//                filepath.putFile(imageUri)
//
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                mProgress.dismiss();
//                                     filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                         @Override
//                                         public void onSuccess(Uri uri) {
//                                             String postId = UUID.randomUUID().toString();
//                                             Event upload = new Event(title_val, desc_val, uri.toString(), cotact_val, reg_fee_val, venue_val, branch_val, postId,time_val);
//                                             mDatabaseReference.child(postId).setValue(upload);
//
//                                         }
//                                     });
//
//                                Toast.makeText(AddEventActivity.this, "Event succesfully posted", Toast.LENGTH_SHORT).show();
//
//                                startActivity(new Intent(AddEventActivity.this, MainActivity.class));
//                                finish();
//
//                            }
//
//                        }
//                        );

            }


    }
    private void post_event(Event data){
        mProgress.show();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/events/All_Events").child(branch_val);
        String userUID=mAuth.getCurrentUser().getUid();

     DatabaseReference profile_ref=FirebaseDatabase.getInstance().getReference().child("all_users/"+userUID+"/postedEvents");
        StorageReference filepath =mStorage.child("colleges/MGIT/all_events").child(imageUri.getLastPathSegment());


        filepath.putFile(imageUri)

                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                          @Override
                                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                              mProgress.dismiss();
                                              filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                  @Override
                                                  public void onSuccess(Uri uri) {
                                                      Event upload = new Event(data.getTitle(), data.getDesc(), uri.toString(),
                                                              data.getContact_info(), data.getReg_fee(), data.getVenue(),
                                                              data.getBranch(), data.getPostId(),data.getTime(),data.getPostedBy(),data.getPostedTime());
                                                      mDatabaseReference.child(data.getPostId()).setValue(upload);
                                                      profile_ref.child(data.getPostId()).setValue(upload);
                                                  }
                                              });

                                              Toast.makeText(AddEventActivity.this, "Event succesfully posted", Toast.LENGTH_SHORT).show();

                                              startActivity(new Intent(AddEventActivity.this, MainActivity.class));
                                              finish();

                                          }

                                      }
                );


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            mContentLayout.setVisibility(View.INVISIBLE);
            mLoginInfoText.setVisibility(View.VISIBLE);
            sentToLoginButton.setVisibility(View.VISIBLE);

        }else{
            mContentLayout.setVisibility(View.VISIBLE);
            mLoginInfoText.setVisibility(View.INVISIBLE);
            sentToLoginButton.setVisibility(View.INVISIBLE);
        }

    }
}