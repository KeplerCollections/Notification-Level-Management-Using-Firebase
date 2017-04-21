package com.kepler.notificationsystem.student;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.Login;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.admin.Students;
import com.kepler.notificationsystem.admin.adapter.StudentAdapter;
import com.kepler.notificationsystem.admin.db.DatabaseHelper;
import com.kepler.notificationsystem.dao.Push;
import com.kepler.notificationsystem.dao.StudentParent;
import com.kepler.notificationsystem.notification.Config;
import com.kepler.notificationsystem.notification.MyFirebaseInstanceIDService;
import com.kepler.notificationsystem.notification.NotificationUtils;
import com.kepler.notificationsystem.services.Student;
import com.kepler.notificationsystem.student.adapter.MessageAdapter;
import com.kepler.notificationsystem.support.EmptyRecyclerView;
import com.kepler.notificationsystem.support.Logger;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.support.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class Main extends BaseActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SharedPreferences pref;
    private static final String TAG = Main.class.getSimpleName();
    private boolean doubleBackToExitPressedOnce;
    private String emaiid;
    @BindView(R.id.recycler_view)
    EmptyRecyclerView recycler_view;
    @BindView(R.id.recycler_empty_view)
    LinearLayout recycler_empty_view;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_USER, 0);
        emaiid = pref.getString(Params.USER, null);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    Utils.toast(getApplicationContext(), "registered");


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                    Push push = databaseHelper.getMessage();
                    databaseHelper.closeDB();
                    if (push != null) {
                        add(push);
                    }
                    Utils.toast(getApplicationContext(), "New Messge Received");
                    recycler_view.getLayoutManager().scrollToPosition(messageAdapter.getItemCount() - 1);
//                    String message = intent.getStringExtra("message");
//                    Logger.e(TAG, "Push notification: " + message);

                }
            }
        };
        setView();
    }

    private void setView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(mLayoutManager);
        // Fetch the empty view from the layout and set it on
        // the new recycler view
        recycler_view.setEmptyView(recycler_empty_view);
        messageAdapter = new MessageAdapter(getApplicationContext());
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        recycler_view.setAdapter(messageAdapter);
        addAll(databaseHelper.getAllMsgs());
        databaseHelper.closeDB();
    }

    private void add(Push message) {
        messageAdapter.add(message);
        notifyMe();
    }

    private void addAll(List<Push> messages) {
        messageAdapter.addAll(messages);
        notifyMe();
    }

    private void notifyMe() {
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public int getActionBarTitle() {
        return R.string.home;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home, menu);
//        menu.getItem(1).getSubMenu().getItem(0).getIcon().setColorFilter(getColorBy(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
//        menu.getItem(1).getSubMenu().getItem(1).getIcon().setColorFilter(getColorBy(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_profile:
                Bundle bundle = new Bundle();
                bundle.putString(Params.EMAILID, emaiid);
                bundle.putBoolean(Params.IS_STUDENT, true);
                Utils.startActivity(this, Profile.class, bundle, false);
                return true;
            case R.id.m_update_password:
                updatePassword();
                return true;
            case R.id.m_logout:
                logout();
                return true;
            case R.id.m_update_email:
                updateEmailRequest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout).setMessage(R.string.are_you_sure);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                com.kepler.notificationsystem.services.Student.logout(getApplicationContext(), emaiid, "", new SimpleNetworkHandler() {

                    public ProgressDialog progressDialog;

                    @Override
                    public void onStart() {
                        progressDialog = ProgressDialog.show(Main.this, "", getResources().getString(R.string.loading));
                    }

                    @Override
                    public void onResult(int statusCode, Header[] headers, Object responseBody) {
                        Gson gson = new Gson();
                        StudentParent fromJson = gson.fromJson(responseBody.toString(), StudentParent.class);
                        if (fromJson.isStatus()) {
//                            MyFirebaseInstanceIDService.removeRegIdInPref(getApplicationContext());
                            pref.edit().remove(Params.USER).commit();
                            FirebaseAuth.getInstance().signOut();
                            doubleBackToExitPressedOnce = true;
                            Utils.startActivity(Main.this, Login.class, null, true, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        } else {
                            Utils.toast(getApplicationContext(), fromJson.getMessage());
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void updatePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter New Password");

// Set up the input
        final EditText password = new EditText(this);
        password.setPadding(10, 10, 10, 10);

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        password.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(password);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (password.getText().toString().trim().length() == 0) {
                    Utils.toast(getApplicationContext(), R.string.password_error_msg);
                    return;
                }
                final ProgressDialog progressDialog = ProgressDialog.show(Main.this, "", getResources().getString(R.string.loading));
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updatePassword(password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Logger.e(TAG, "StudentParent password updated.");
                                    Utils.toast(getApplicationContext(), R.string.password_changed);
                                } else {
                                    Utils.toast(getApplicationContext(), task.getException().getMessage());
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateEmailRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter New EmailId");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setPadding(10, 10, 10, 10);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().trim().length() == 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(input.getText().toString().trim()).matches()) {
                    Utils.toast(getApplicationContext(), R.string.email_error_msg);
                    return;
                }
                final ProgressDialog progressDialog = ProgressDialog.show(Main.this, "", getResources().getString(R.string.updating));
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateEmail(input.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Student.updateEmailId(getApplicationContext(), emaiid, input.getText().toString(), new SimpleNetworkHandler() {

                                        @Override
                                        public void onStart() {
                                            super.onStart();
                                        }

                                        @Override
                                        public void onResult(int statusCode, Header[] headers, Object responseBody) {
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(responseBody.toString());
                                                if (jsonObject.getBoolean(Params.STATUS)) {
                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_USER, 0);
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString(Params.USER, input.getText().toString());
                                                    editor.commit();
                                                    emaiid = input.getText().toString();
                                                    Utils.toast(getApplicationContext(), R.string.update_success);
                                                } else {
                                                    Utils.toast(getApplicationContext(), R.string.failed);
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onFinish() {
                                            super.onFinish();
                                            progressDialog.dismiss();
                                        }
                                    });

                                } else {
                                    progressDialog.dismiss();
                                    Utils.toast(getApplicationContext(), task.getException().getMessage());
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Utils.toast(getApplicationContext(), R.string.back_message);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
