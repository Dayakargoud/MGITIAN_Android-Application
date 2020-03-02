package com.dayakar.mgitian.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.dayakar.mgitian.Utils.NotificationReceiver;
import com.dayakar.mgitian.Utils.NotificationUtils;
import com.dayakar.mgitian.R;
import com.dayakar.mgitian.Fragments.SettingsPrefFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Toolbar mToolbar;
    private  PendingIntent pendingIntent;
    private  AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mToolbar= findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Preferences");

        getSupportFragmentManager().beginTransaction().replace(R.id.setting_pref_container,new SettingsPrefFragment()).commit();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
       boolean remind= prefs.getBoolean("notify", true);
       if(remind){
           registeringForFirebaseTopic();
       }else {
           unRegisteringForFirebaseTopic();

       }

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  android.R.id.home:{

                onBackPressed();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void dailyNtifications(){
        Calendar calendar=Calendar.getInstance();
        Calendar now=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,30);
        calendar.set(Calendar.SECOND,0);
        if(now.after(calendar)){

            calendar.add(Calendar.DATE,1);
        }

        Intent intent=new Intent(getApplicationContext(), NotificationReceiver.class);

        pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);




    }
    private void registeringForFirebaseTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("dailyNotifications")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("SetUpActivity","Topic Registation successful");

                    }
                });
    }
    private void unRegisteringForFirebaseTopic(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic("dailyNotifications")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("SetUpActivity","Topic UnRegistation successful");

                    }
                });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
       boolean b= sharedPreferences.getBoolean("notify",false);
        if(b){

            registeringForFirebaseTopic();
         //  NotificationUtils.remindUser(this);

        }else{
            unRegisteringForFirebaseTopic();
        }
    }
}
