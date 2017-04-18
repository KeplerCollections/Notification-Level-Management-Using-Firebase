package com.kepler.notificationsystem.student;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.dao.Student;
import com.kepler.notificationsystem.dao.StudentParent;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.support.Logger;
import com.kepler.notificationsystem.support.OnYearSelect;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.Utils;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class Update extends BaseActivity implements View.OnClickListener {
    private static final String TAG = Update.class.getSimpleName();
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
    @BindView(R.id.select_batch)
    TextView select_batch;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getParcelable(Params.DATA) == null) {
            onBackPressed();
            return;
        }
        username.setEnabled(false);
        password.setEnabled(false);
        student = bundle.getParcelable(Params.DATA);
        setView();
        register.setText(R.string.update);
        register.setOnClickListener(this);
//        select_batch.setOnClickListener(this);
    }

    private void setView() {
        name.setText(student.getName());
        username.setText(student.getEmailid());
        cn.setText(student.getCn());
        password.setText(R.string.password_demo);
        rn.setText(student.getRn());
        select_batch.setText(student.getBatch());
    }

    @Override
    public int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    public int getActionBarTitle() {
        return R.string.update;
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
                if (rn.getText().toString().trim().length() < 10) {
                    Utils.toast(getApplicationContext(), R.string.rn_error_msg);
                    return;
                }
                if (select_batch.getText().toString().toLowerCase().length() == 0) {
                    Utils.toast(getApplicationContext(), R.string.batch_error_msg);
                    return;
                }

                update();
                break;
//            case R.id.select_batch:
//                openYearPickerDialog(Update.this, new OnYearSelect() {
//
//                    @Override
//                    public void onYearSet(String year) {
//                        select_batch.setText(year);
//
//                    }
//                });
//                break;
        }
    }


    private void update() {
        student.setEmailid(String.valueOf(username.getText()));
        student.setName(String.valueOf(name.getText()));
        student.setCn(String.valueOf(cn.getText()));
        student.setRn(String.valueOf(rn.getText()));
        student.setBatch(String.valueOf(select_batch.getText()));
        com.kepler.notificationsystem.services.Student.update(getApplicationContext(), student, new SimpleNetworkHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = ProgressDialog.show(Update.this, "", getResources().getString(R.string.loading));
            }

            @Override
            public void onResult(int statusCode, Header[] headers, Object responseBody) {
                Gson gson = new Gson();
                StudentParent fromJson = gson.fromJson(responseBody.toString(), StudentParent.class);
                if (fromJson.isStatus()) {
                    Intent intent = new Intent();
                    intent.putExtra(Params.DATA, student);
                    setResult(RESULT_OK, intent);
                    Utils.toast(getApplicationContext(), fromJson.getMessage());
                    dismiss();
                    onBackPressed();
                } else {
                    Utils.toast(getApplicationContext(), fromJson.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                dismiss();
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
