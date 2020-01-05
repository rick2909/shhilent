package com.example.shhilent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static String TAG = "FCM-Test";
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String date = remoteMessage.getData().get("date");
            String time = remoteMessage.getData().get("time");

            //send to loacal database
            DbHandler dbHandler = new DbHandler(this);
            dbHandler.insertMelding(title,body,date,time);

            updateMyActivity(this);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }

//    private void sendRegistrationToServer(String token) {
//        final String SENDER_ID = "286297839302";
//        FirebaseMessaging fm = FirebaseMessaging.getInstance();
//        fm.send(new RemoteMessage.Builder(SENDER_ID + "@fcm.googleapis.com")
//                .setMessageId("K-2019")
//                .addData("my_message", "Hello World")
//                .addData("my_action","SAY_HELLO")
//                .build());
//    }

    static void updateMyActivity(Context context) {

        Intent intent = new Intent("unique_name");

        //send broadcast
        context.sendBroadcast(intent);
    }
}
