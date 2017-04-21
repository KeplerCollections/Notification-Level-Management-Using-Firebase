package com.kepler.notificationsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.kepler.notificationsystem.admin.AdminMain;
import com.kepler.notificationsystem.admin.SelectStudents;
import com.kepler.notificationsystem.admin.adapter.SelectStudentAdapter;
import com.kepler.notificationsystem.dao.Student;
import com.kepler.notificationsystem.dao.StudentParent;
import com.kepler.notificationsystem.notification.Config;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.student.Main;
import com.kepler.notificationsystem.support.Logger;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.Utils;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class Login extends BaseActivity implements View.OnClickListener {
    private static final String TAG = Login.class.getSimpleName();
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.forgot)
    TextView forgot;
    @BindView(R.id.register)
    TextView register;
    //    private SharedPreferences pref;
    private FirebaseAuth mAuth;
    private String mEmail;
//    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgot.setOnClickListener(this);
        Logger.e(TAG, "token");
        System.out.println(FirebaseInstanceId.getInstance().getToken());
        Logger.e(TAG, "token");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    username.setText("developer.kepler@gmail.com");
//                    password.setText("qwerty");
                    username.setEnabled(true);
                    forgot.setEnabled(true);
                    register.setEnabled(true);
                } else {
                    username.setText("admin");
//                    password.setText("qwerty");
                    username.setEnabled(false);
                    forgot.setEnabled(false);
                    register.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // StudentParent is signed in
//                    Logger.e(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                    Logger.e(TAG, "onAuthStateChanged:signed_in:" + user.getEmail());
//                } else {
//                    // StudentParent is signed out
//                    Logger.e(TAG, "onAuthStateChanged:signed_out");
//                }
//                // ...
//            }
//        };
//
//        new CheckFireBaseRegId().execute();

    }


    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public int getActionBarTitle() {
        return R.string.login;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (spinner.getSelectedItemPosition() == 1) {
                    mEmail = Utils.ADMIN_EMAIL_ID;
                } else {
                    mEmail = username.getText().toString().trim();
                    if (mEmail.length() == 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                        Utils.toast(getApplicationContext(), R.string.email_error_msg);
                        return;
                    }
                }
                if (password.getText().toString().trim().length() == 0) {
                    Utils.toast(getApplicationContext(), R.string.password_error_msg);
                    return;
                }
                login();
                break;
            case R.id.register:
                Utils.startActivity(this, Register.class, null, false);
                break;
            case R.id.forgot:
                forgotPassword();
                break;
        }
    }

    private void forgotPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Email Id");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setPadding(10, 10, 10, 10);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().trim().length() == 0) {
                    Utils.toast(getApplicationContext(), R.string.email_error_msg);
                    return;
                }
                mAuth.sendPasswordResetEmail(input.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            final ProgressDialog progressDialog = ProgressDialog.show(Login.this, "", getResources().getString(R.string.loading));

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Utils.toast(getApplicationContext(), R.string.password_link_send);
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

    private void login() {
        if (FirebaseInstanceId.getInstance().getToken() == null) {
            Utils.toast(getApplicationContext(), R.string.please_try_after_a_minute);
            return;
        }
        mAuth.signInWithEmailAndPassword(mEmail, password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    final ProgressDialog progressDialog = ProgressDialog.show(Login.this, "", getResources().getString(R.string.authenticating));

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Logger.e(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        dismiss();
                        if (!task.isSuccessful()) {
                            Logger.e(TAG, "signInWithEmail:failed", task.getException());
//                            Utils.toast(getApplicationContext(), R.string.auth_failed);
                            Utils.toast(getApplicationContext(), task.getException().getMessage());
                        } else {
                            if (!(task.getResult().getUser().getEmail().equalsIgnoreCase(Utils.ADMIN_EMAIL_ID) || task.getResult().getUser().isEmailVerified())) {
                                rensendVerifyEmailLink(task);
                                return;
                            }
                            storeRegIdToServer();
                        }
                    }

                    private void dismiss() {
                        progressDialog.dismiss();
                    }
                });
    }

    private void storeRegIdToServer() {
        if (spinner.getSelectedItemPosition() == 1) {
            storeRegIdInPref();
            Utils.startActivity(Login.this, AdminMain.class, null, true);
        } else {
            com.kepler.notificationsystem.services.Student.login(getApplicationContext(), mEmail, FirebaseInstanceId.getInstance().getToken(), new SimpleNetworkHandler() {

                public ProgressDialog progressDialog;

                @Override
                public void onStart() {
                    progressDialog = ProgressDialog.show(Login.this, "", getResources().getString(R.string.loading));
                }

                @Override
                public void onResult(int statusCode, Header[] headers, Object responseBody) {
                    Gson gson = new Gson();
                    StudentParent fromJson = gson.fromJson(responseBody.toString(), StudentParent.class);
                    if (fromJson.isStatus()) {
                        if (fromJson.getData().size() > 0) {
//                            FirebaseMessaging.getInstance().subscribeToTopic(Utils.getType(fromJson.getData().get(0).getCourse(), fromJson.getData().get(0).getBatch()));
                            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                            storeRegIdInPref(fromJson.getData().get(0));
                            Utils.startActivity(Login.this, Main.class, null, true);
                        } else {
                            Utils.toast(getApplicationContext(), R.string.failed);
                        }
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
    }

    private void storeRegIdInPref(Student student) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_USER, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Params.USER, mEmail);
        editor.putString(Params.BATCH, student.getBatch());
        editor.putString(Params.COURSE, student.getCourse());
        editor.putInt(Params.YEAR, student.getYear());
        editor.commit();
    }

    private void storeRegIdInPref() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_USER, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Params.USER, mEmail);
        editor.commit();
    }

    private void rensendVerifyEmailLink(final Task<AuthResult> task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// Set up the input
        final TextView input = new TextView(this);
        input.setPadding(10, 10, 10, 10);
        input.setGravity(Gravity.CENTER);
        input.setText(R.string.registered_success);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Send Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().trim().length() == 0) {
                    Utils.toast(getApplicationContext(), R.string.email_error_msg);
                    return;
                }
                task.getResult().getUser().sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            final ProgressDialog progressDialog = ProgressDialog.show(Login.this, "", getResources().getString(R.string.resending));

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Utils.toast(getApplicationContext(), R.string.registered_success);
                                }
                                dismiss();
                            }

                            private void dismiss() {
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


//    @Override
//    public void onStart() {
//        super.onStart();
//            mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null)
//            mAuth.removeAuthStateListener(mAuthListener);
//    }
}