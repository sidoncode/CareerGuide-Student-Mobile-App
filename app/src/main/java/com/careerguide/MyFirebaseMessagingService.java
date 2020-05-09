package com.careerguide;
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
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import android.text.TextUtils;
import android.util.Log;

import com.careerguide.youtubeVideo.youtubeFeedDetail;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options



            Log.d(TAG, "From: " + remoteMessage.getFrom());
            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                String message = remoteMessage.getData().get("message");
                String title = remoteMessage.getData().get("title");
                //imageUri will contain URL of the image to be displayed with Notification
                String imageUri = remoteMessage.getData().get("image");
                if(TextUtils.isEmpty(imageUri)){
                    imageUri = "https://www.careerguide.com/images-mcg/counselling1.jpg";
                }
                String activity = remoteMessage.getData().get("Activity");
                String data_id = remoteMessage.getData().get("id");
                Log.e("#id" , "ff" +data_id);
                Log.e("#message" , "ff" +message);
                Log.e("#title" , "ff" +title);
                Log.e("#imageurl" , "ff" +imageUri);
                Log.e("#activity" , "ff" +activity);
                bitmap = getBitmapfromUrl(imageUri);
                if(TextUtils.isEmpty(title)||title==null) {
                    Log.i("Notification has", "null values");
                    return;
                }
                if (remoteMessage.getNotification() != null) {
                    Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                }
                else
                    sendNotification(title , message, bitmap, activity , data_id);



        }else{
            Log.d(TAG,"notification body is null");
        }

    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String title, String messageBody,  Bitmap image, String activity , String data_id) {
        PendingIntent pendingIntent;


        if (activity!=null && activity.contains("youtubeFeedDetail")){
            Intent intent = new Intent(this, youtubeFeedDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Log.e("dataIdnoti" , "--> " +data_id);
            intent.putExtra("data_id", data_id);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        else{
            Intent intent = new Intent(this, exoplayerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("data_id", data_id);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }


        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, 500, 200 , true);
        Bitmap bitmap_image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String id = "my_channel_01";
        CharSequence name = "hii2";
        String description = "hiii";
        int importance = NotificationManager.IMPORTANCE_LOW;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name,importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setLargeIcon(bitmap_image)/*Notification icon image*/
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(scaledBitmap))/*Notification with Image*/
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setChannelId(id)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(10 /* ID of notification */, notificationBuilder.build());

            //     notification.setSmallIcon(R.drawable.icon_transperent);
            //    notification.setColor(getResources().getColor(R.color.notification_color));
        } else {
            //    notification.setSmallIcon(R.drawable.icon);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setLargeIcon(bitmap_image)/*Notification icon image*/
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(scaledBitmap))/*Notification with Image*/
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setChannelId(id)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(10 /* ID of notification */, notificationBuilder.build());
        }

    }

    /*
     *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}


//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.TaskStackBuilder;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Build;
//import android.util.Log;
//
//import androidx.annotation.RequiresApi;
//import androidx.core.app.NotificationCompat;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class Mymessageservices extends FirebaseMessagingService {
//    final int IMAGE_MAX_SIZE = 1200000;
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//      //  super.onMessageReceived(remoteMessage);
//        Log.d("#wwww1234", "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d("wwww123", "Message data payload: " + remoteMessage.getData());
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d("#wwww", "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//        Log.e("#title" , "url" +remoteMessage);
//        String image = remoteMessage.getData().get("img_url");
//        Log.e("#img" , "url" +image);
//        Bitmap imgurl = getBitmapfromUrl(image);
//       // showNotification(remoteMessage.getNotification().getTitle() , remoteMessage.getNotification().getBody(), bitmap);
//        String title =  remoteMessage.getNotification().getTitle();
//        String message = remoteMessage.getNotification().getBody();
//        Log.e("#msg" , "url" +message);
//        Log.e("#title" , "url" +title);
//
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String id = "my_channel_01";
//        CharSequence name = "hii2";
//        String description = "hiii";
//        int importance = NotificationManager.IMPORTANCE_LOW;
//        NotificationChannel mChannel = new NotificationChannel(id, name,importance);
//        mChannel.setDescription(description);
//        mChannel.enableLights(true);
//        mChannel.setLightColor(Color.RED);
//        mChannel.enableVibration(true);
//        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//        mNotificationManager.createNotificationChannel(mChannel);
//        Bitmap big_bitmap_image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//
//
//        int height = imgurl.getHeight();
//        int width = imgurl.getWidth();
//        Log.d("#wid", "1th scale operation dimenions - width: " + width + "height: " + height);
//
//
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imgurl, 500, 200 , true);
//        Bitmap bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.cc_launcher);
//        Log.e("#bitmap" , "zz:---" +bitmap_image);
//
//        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
//                .bigPicture(getBitmapfromUrl(remoteMessage.getData().get("img_url")))
//                .setSummaryText(message);
//
//
//        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
//        nb.setContentTitle(title)
//                .setLargeIcon( BitmapFactory.decodeResource(getResources(), R.drawable.cc_launcher))
//                .setContentText(message)
//                .setSmallIcon(R.drawable.noti_text)
////              .setLargeIcon(bitmap_image)
//                .setTicker(message)
//                .setAutoCancel(true)
//                .setChannelId(id)
//                //API Level min 16 is required
//                .setStyle(style)
//                .build();
//
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//        TaskStackBuilder TSB = TaskStackBuilder.create(this);
//        TSB.addParentStack(MainActivity.class);
//        TSB.addNextIntent(notificationIntent);
//        PendingIntent resultPendingIntent = TSB.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        nb.setContentIntent(resultPendingIntent);
//        nb.setAutoCancel(true);
//        mNotificationManager.notify(10, nb.build());
//
//
//
//    }
//
//    public Bitmap getBitmapfromUrl(String imageUrl) {
//        try {
//            URL url = new URL(imageUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap bitmap = BitmapFactory.decodeStream(input);
//            return bitmap;
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//
//        }
//    }
//
////    @RequiresApi(api = Build.VERSION_CODES.O)
////    public void showNotification(String title , String message , Bitmap imgurl){
////        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////        String id = "my_channel_01";
////        CharSequence name = "hii2";
////        String description = "hiii";
////        int importance = NotificationManager.IMPORTANCE_LOW;
////        NotificationChannel mChannel = new NotificationChannel(id, name,importance);
////        mChannel.setDescription(description);
////        mChannel.enableLights(true);
////        mChannel.setLightColor(Color.RED);
////        mChannel.enableVibration(true);
////        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
////
////        mNotificationManager.createNotificationChannel(mChannel);
////
////
////
////        Bitmap big_bitmap_image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
////
////
////        int height = imgurl.getHeight();
////        int width = imgurl.getWidth();
////        Log.d("#wid", "1th scale operation dimenions - width: " + width + "height: " + height);
////
////
////        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imgurl, 500, 200 , true);
////
////        Bitmap bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.cc_launcher);
////        Log.e("#bitmap" , "zz:---" +bitmap_image);
////
////        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
////                .bigPicture(scaledBitmap)
////                .setSummaryText(message);
////
////        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
////
////                nb.setContentTitle(title)
////                .setContentText(message)
////                .setSmallIcon(R.drawable.noti_text)
////                .setLargeIcon(bitmap_image)
////                .setTicker(message)
////                .setAutoCancel(true)
////                .setChannelId(id)
////                //API Level min 16 is required
////                .setStyle(style)
////                .build();
////
////
////        Intent notificationIntent = new Intent(this, MainActivity.class);
////        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
////
////        TaskStackBuilder TSB = TaskStackBuilder.create(this);
////        TSB.addParentStack(MainActivity.class);
////        TSB.addNextIntent(notificationIntent);
////        PendingIntent resultPendingIntent = TSB.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
////        nb.setContentIntent(resultPendingIntent);
////        nb.setAutoCancel(true);
////        mNotificationManager.notify(10, nb.build());
//
//  //  }
//
//
//}