package com.dayakar.mgitian.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.dayakar.mgitian.Activities.MainActivity;
import com.dayakar.mgitian.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EventNotifications extends FirebaseMessagingService {
    NotificationManager notificationManager;
    Bitmap bigPicture;
    String NOTIFICATION_CHANNEL_ID = "FirebaseNotification";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FirebaseToken", token);
        System.out.println(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getNotification()!=null){

            System.out.println(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage);

        }

    }

    public void sendNotification(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String content = data.get("content");
        String imageUrl = data.get("url");


        notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "app_Notification", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("Notification");
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);

            notificationManager.createNotificationChannel(notificationChannel);
        }

//        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.nirvana);
        GeneratePictureStyleNotification notify=new GeneratePictureStyleNotification(title,content);
        notify.execute(imageUrl);


    }

    public class GeneratePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {
        String titleText,contentText;

        public GeneratePictureStyleNotification(String titleText, String contentText) {
            this.titleText = titleText;
            this.contentText = contentText;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            bigPicture=bitmap;

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(EventNotifications.this, NOTIFICATION_CHANNEL_ID);

            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
            notificationBuilder.setAutoCancel(true)
                    //.setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setSmallIcon(R.drawable.ic_feedback_black_24dp)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(titleText)
                    .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setLights(Color.BLUE, 500, 500)
                    .setVibrate(pattern)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentText(contentText)
                    .setContentIntent(contentIntent(getApplicationContext()))
                    .setLargeIcon(bigPicture)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bigPicture)
                            .bigLargeIcon(null)
                    )
                    .setContentInfo("Info")
                    .setSound(notificationSound);

            notificationManager.notify(getRandomNumber(1,1000), notificationBuilder.build());

        }
    }
    private static PendingIntent contentIntent(Context context){
        Intent startActivityIntent=new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context,112,startActivityIntent,PendingIntent.FLAG_ONE_SHOT);

    }
    private static int getRandomNumber(int min, int max) {
        // min (inclusive) and max (exclusive)
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }
}
