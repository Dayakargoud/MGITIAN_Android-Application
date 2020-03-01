package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dayakar.mgitian.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class SuggestionsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText suggestionText;
    private Button msend;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestions);
        mToolbar=findViewById(R.id.suggest_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Suggestions");
        suggestionText=findViewById(R.id.suggest_Edittext);
        msend=findViewById(R.id.send_suggestions_button);

        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSuggestions();

            }
        });

    }

    private void sendSuggestions(){
        final String userfeedback = suggestionText.getText().toString().trim();
        if (!TextUtils.isEmpty(userfeedback)) {
            setprogressdialog();
            DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("App/Suggestions");
            mDatabaseReference.child(getTimestamp()).setValue(userfeedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mProgress.dismiss();


                    AlertDialog.Builder dialog = new AlertDialog.Builder(SuggestionsActivity.this);
                    dialog.setCancelable(false);
                    dialog.setIcon(R.drawable.completed);
                    dialog.setTitle("Thank you");
                    dialog.setMessage("Your feedback is successfully submitted");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            suggestionText.setText("");
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    suggestionText.setText("");

                    // Toast.makeText(FeedbackActivity.this, "Feedback sent...", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }
    private void setprogressdialog(){

        mProgress = new ProgressDialog(SuggestionsActivity.this);
        mProgress.setTitle("sending your suggestions");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();


    }
    private String getTimestamp(){

        Date c = Calendar.getInstance().getTime();
        return c.toString();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);


    }
}

