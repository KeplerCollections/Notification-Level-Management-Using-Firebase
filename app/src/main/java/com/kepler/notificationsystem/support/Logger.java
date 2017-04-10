package com.kepler.notificationsystem.support;

import android.content.Context;
import android.util.Log;

/**
 * Created by 12 on 1/11/2016.
 */
public class Logger {
    private static final boolean isLoggable = true;

    public static void d(String TAG, String msg) {
        try {
            if (isLoggable)
                Log.d(TAG, msg);
        } catch (Exception e) {
            printTrash(e);
        }
    }

    public static void w(String TAG, String msg) {
        try {
            if (isLoggable)
                Log.w(TAG, msg);
        } catch (Exception e) {
            printTrash(e);
        }
    }

    public static void i(String TAG, String msg) {
        try {
            if (isLoggable)
                Log.i(TAG, msg);
        } catch (Exception e) {
            printTrash(e);
        }
    }

    public static void e(String TAG, String msg) {
        try {
            if (isLoggable)
                Log.e(TAG, msg);
        } catch (Exception e) {
            printTrash(e);
        }
    }

    public static void e(String TAG, String msg, Exception ex) {
        try {
            if (isLoggable)
                Log.e(TAG, msg, ex);
        } catch (Exception e) {
            printTrash(e);
        }
    }

    public static void printTrash(Exception e) {
        if (isLoggable)
            e.printStackTrace();
    }

}

