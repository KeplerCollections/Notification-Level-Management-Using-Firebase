package com.kepler.notificationsystem;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kepler.notificationsystem.dao.Student;
import com.kepler.notificationsystem.support.Logger;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.support.Utils;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class Register extends BaseActivity implements View.OnClickListener {
    private static final String TAG = Register.class.getSimpleName();
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.cn)
    EditText cn;
    @BindView(R.id.rn)
    EditText rn;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.select_year)
    Spinner select_year;
    @BindView(R.id.select_course)
    Spinner select_coursee;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = mAuth.getInstance();
        select_year.setVisibility(View.VISIBLE);
        select_coursee.setVisibility(View.VISIBLE);
        register.setOnClickListener(this);
        select_coursee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> dataAdapter;
                if (i == 0) {
                    dataAdapter = new ArrayAdapter<String>(Register.this,
                            android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.bca_years));
                } else {
                    dataAdapter = new ArrayAdapter<String>(Register.this,
                            android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.mca_years));
                }
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_year.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    public int getActionBarTitle() {
        return R.string.register;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                if (name.getText().toString().trim().length() == 0) {
                    Utils.toast(getApplicationContext(), R.string.user_error_msg);
                    return;
                }
                if (username.getText().toString().trim().length() == 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(username.getText().toString().trim()).matches()) {
                    Utils.toast(getApplicationContext(), R.string.email_error_msg);
                    return;
                }
                if (cn.getText().toString().trim().length() < 10) {
                    Utils.toast(getApplicationContext(), R.string.cn_error_msg);
                    return;
                }
                if (password.getText().toString().trim().length() == 0) {
                    Utils.toast(getApplicationContext(), R.string.password_error_msg);
                    return;
                }
                if (rn.getText().toString().trim().length() < 10) {
                    Utils.toast(getApplicationContext(), R.string.rn_error_msg);
                    return;
                }
                register();
                break;
        }
    }

//    public static void openYearPickerDialog(BaseActivity activity, final OnBatchSelect onYearSelect) {
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle(R.string.select_batch);
//        final NumberPicker np = new NumberPicker(activity);
//        final int year = Calendar.getInstance().get(Calendar.YEAR);
//        np.setMinValue(year - Utils.BEFORE_CRT_YEAR);
//        np.setMaxValue(year + Utils.AFTER_CRT_YEAR);
//        np.setValue(year);
//        np.setWrapSelectorWheel(false);
//        builder.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                onYearSelect.onYearSet(String.valueOf(np.getValue()));
//            }
//        });
//        builder.setView(np);
//        AlertDialog d = builder.create();
//        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        d.show();
//    }

    private void register() {
        mAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    final ProgressDialog progressDialog = ProgressDialog.show(Register.this, "", getResources().getString(R.string.loading));

                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        Logger.e(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Logger.e(TAG, "signUp", task.getException());
                            dismiss();
                            Utils.toast(getApplicationContext(), task.getException().getMessage());
                        } else {
                            final Student user = new Student(String.valueOf(name.getText()), String.valueOf(username.getText()),
                                    String.valueOf(password.getText()), String.valueOf(rn.getText()), String.valueOf(cn.getText()), String.valueOf(select_coursee.getSelectedItem()), select_year.getSelectedItemPosition() + 1,
                                    null, Utils.getBatch(select_year.getSelectedItemPosition() + 1));
                            com.kepler.notificationsystem.services.Student.register(getApplicationContext(), user, new SimpleNetworkHandler() {

                                @Override
                                public void onResult(int statusCode, Header[] headers, Object responseBody) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(responseBody.toString());
                                        if (jsonObject.getBoolean(Params.STATUS)) {
                                            task.getResult().getUser().sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Utils.toast(getApplicationContext(), R.string.registered_success);
                                                                onBackPressed();
                                                            }
                                                            dismiss();
                                                        }
                                                    });
                                        } else {
                                            mAuth.getCurrentUser().delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Utils.toast(getApplicationContext(), R.string.failed);
                                                            }
                                                            dismiss();
                                                        }
                                                    });

                                        }
                                    } catch (Exception e) {
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    super.onFailure(statusCode, headers, responseBody, error);
                                    dismiss();
                                }
                            });
                        }
                    }

                    private void dismiss() {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }


                });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
