package com.kepler.notificationsystem.notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kepler.notificationsystem.Login;
import com.kepler.notificationsystem.admin.db.DatabaseHelper;
import com.kepler.notificationsystem.dao.Push;
import com.kepler.notificationsystem.student.Main;
import com.kepler.notificationsystem.support.Logger;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.e(TAG, "From: " + remoteMessage.getFrom());
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_USER, 0);
        if (pref.getString(Params.USER, null).equals(Utils.ADMIN_EMAIL_ID) || remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Logger.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json, pref);
            } catch (Exception e) {
                Logger.e(TAG, "Exception: " + e.getMessage());
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Logger.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json, SharedPreferences pref) {
        Logger.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            String file = data.getString("file");
            int msg_type = data.getInt("msg_type");
            JSONObject payload = data.getJSONObject("payload");

            Push push = new Push();
            push.setTitle(title);
            push.setMessage(message);
            push.setIs_background(isBackground);
            push.setImage(imageUrl);
            push.setTimestamp(timestamp);
            push.setFile(file);
            push.setMsg_type(msg_type);
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            db.insertMsg(push);
            db.closeDB();
//            Logger.e(TAG, "title: " + title);
//            Logger.e(TAG, "message: " + message);
//            Logger.e(TAG, "isBackground: " + isBackground);
//            Logger.e(TAG, "payload: " + payload.toString());
//            Logger.e(TAG, "imageUrl: " + imageUrl);
//            Logger.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent;
                if (pref.getString(Params.USER, null) == null) {
                    resultIntent = new Intent(getApplicationContext(), Login.class);
                } else {
                    resultIntent = new Intent(getApplicationContext(), Main.class);
                    resultIntent.putExtra(Params.MESSAGE, "message");
                }

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Logger.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Logger.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}