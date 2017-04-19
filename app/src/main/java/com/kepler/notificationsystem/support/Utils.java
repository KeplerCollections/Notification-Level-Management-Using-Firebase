package com.kepler.notificationsystem.support;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.admin.AdminMain;
import com.kepler.notificationsystem.notification.Config;
import com.kepler.notificationsystem.services.Student;

import java.util.Calendar;

/**
 * Created by Amit on 03-04-2017.
 */

public class Utils {
    public static final String ADMIN_EMAIL_ID = "developer.amitjaiswal@gmail.com";
    public static final int BEFORE_CRT_YEAR = 2;
//    public static final int AFTER_CRT_YEAR = 4;

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

    public static void startActivityForResult(Activity activity, Class<? extends BaseActivity> activityClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, activityClass);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
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

    public static void getBatchDialog(final BaseActivity activity, final OnBatchSelect onBatchSelect) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.select);
        final View view = activity.getLayoutInflater().inflate(R.layout.select_, null);
        view.setPadding(10, 10, 10, 10);
        final Spinner select_course, select_year;
        select_course = (Spinner) view.findViewById(R.id.select_course);
        select_year = (Spinner) view.findViewById(R.id.select_year);
        select_course.setVisibility(View.VISIBLE);
        select_year.setVisibility(View.VISIBLE);
        select_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> dataAdapter;
                if (i == 0) {
                    dataAdapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_spinner_item, activity.getResources().getStringArray(R.array.bca_years));
                } else {
                    dataAdapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_spinner_item, activity.getResources().getStringArray(R.array.mca_years));
                }
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_year.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        builder.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBatchSelect.onBatchSelect(String.valueOf(select_course.getSelectedItem()).toLowerCase(), getBatch(select_year.getSelectedItemPosition() + 1));
            }
        });
        builder.setView(view);
        AlertDialog d = builder.create();
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        d.show();
    }

    public static String getBatch(int year) {
        int current_year = Calendar.getInstance().get(Calendar.YEAR);
        return String.valueOf(current_year - (year - 1));
    }


    public static String getType(String course, String batch) {
        if (course == null || batch == null) {
            return Config.TOPIC_GLOBAL;
        } else {
            return course + "_" + batch;
        }
    }

    public static int getYear(int year) {
        switch (year){
            case 1:
                return R.string.first;
            case 2:
                return R.string.second;
            default:
                return R.string.third;
        }
    }
}