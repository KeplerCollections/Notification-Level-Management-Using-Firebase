package com.kepler.notificationsystem.notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kepler.notificationsystem.support.Logger;
import com.kepler.notificationsystem.support.Params;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
 
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
 
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
 
        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
 
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra(Params.TOKEN, refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
 
    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Logger.e(TAG, "sendRegistrationToServer: " + token);
    }
 
    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Params.REG_ID, token);
        editor.commit();
    }
}