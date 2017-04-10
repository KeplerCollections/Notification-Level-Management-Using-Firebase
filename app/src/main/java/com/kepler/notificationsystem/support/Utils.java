package com.kepler.notificationsystem.support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;

/**
 * Created by Amit on 03-04-2017.
 */

public class Utils {
    public static final String ADMIN_EMAIL_ID = "developer.amitjaiswal@gmail.com";

    public static void startActivity(Context context, Class<? extends BaseActivity> activityClass, Bundle bundle, boolean finish) {
        Intent intent = new Intent(context, activityClass);
        if (bundle != null)
            intent.putExtras(bundle);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivity(intent);
            if (finish)
                activity.onBackPressed();
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

    public static void startActivity(Context context, Class<? extends BaseActivity> activityClass, Bundle bundle, boolean finish, String actions) {
        Intent intent = new Intent(context, activityClass);
        intent.setAction(actions);
        if (bundle != null)
            intent.putExtras(bundle);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivity(intent);
            if (finish)
                activity.onBackPressed();
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void startActivity(Context context, Class<? extends BaseActivity> activityClass, Bundle bundle, boolean finish, int flags) {
        Intent intent = new Intent(context, activityClass);
        if (bundle != null)
            intent.putExtras(bundle);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivity(intent);
            if (finish)
                activity.onBackPressed();
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void toast(Context applicationContext, int _msg) {
        Toast.makeText(applicationContext, _msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context applicationContext, String _msg) {
        Toast.makeText(applicationContext, _msg, Toast.LENGTH_SHORT).show();
    }

}