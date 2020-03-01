package com.dayakar.mgitian.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.dayakar.mgitian.Activities.MainActivity;
import com.dayakar.mgitian.R;

import java.util.Random;


public class NotificationUtils {
    private static final int NOTIFICATION_ID=1122;
    private static final int PENDING_INTENT_ID=1122;
    private static final String NOTIFICATION_CHANEL_ID="reminder";
    private static PendingIntent sPendingIntent(Context context){

        Intent startActivityIntent=new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context,PENDING_INTENT_ID,startActivityIntent,PendingIntent.FLAG_ONE_SHOT);

       // return PendingIntent.getActivity(context,PENDING_INTENT_ID,startActivityIntent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

   private static Bitmap largeIcon(Context context){
       Resources res=context.getResources();
       Bitmap largeIcon= BitmapFactory.decodeResource(res, R.drawable.applogo);
       return largeIcon;
   }
    private static Bitmap smallIcon(Context context){
        Resources res=context.getResources();
        Bitmap largeIcon= BitmapFactory.decodeResource(res,R.drawable.applogo);
        return largeIcon;
    }
    private static int getRandomNumber(int min, int max) {
        // min (inclusive) and max (exclusive)
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }


    public static void remindUser(Context context){
       NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

       //Creating notification channel for android Oreo+ devices
       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
           NotificationChannel mChannel=new NotificationChannel(NOTIFICATION_CHANEL_ID,
                   context.getString(R.string.notificcation_Channel_Name),NotificationManager.IMPORTANCE_HIGH);
           notificationManager.createNotificationChannel(mChannel);

       }

       //creating notifications for all devices
       NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(context,NOTIFICATION_CHANEL_ID)
               .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
               .setSmallIcon(R.drawable.ic_feedback_black_24dp)
               .setLargeIcon(largeIcon(context))
               .setContentTitle("Hey check our timetable")
               .setContentText("Here is your today timetable hope you like it.")
               .setStyle(new NotificationCompat.BigTextStyle().bigText("Although you must set the notification " +
                       "importance/priority as shown here, the system does not guarantee the alert behavior you'll get. In some cases the system might change" +
                       " the importance level based other factors, and the user can always redefine what the importance level is for a given channel."))
               .setDefaults(Notification.DEFAULT_VIBRATE)
               .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
               .setContentIntent(sPendingIntent(context))
               .setAutoCancel(true);


       if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
           notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
       }

       //Trigger the notification by calling notify method
       //notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build());
        notificationManager.notify(getRandomNumber(1,100),notificationBuilder.build());

    }
}
