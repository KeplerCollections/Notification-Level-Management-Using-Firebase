package com.kepler.notificationsystem.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.dao.Student;
import com.kepler.notificationsystem.dao.StudentParent;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.Utils;

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
    }

    private void setView() {
        name.setText(student.getName());
        username.setText(student.getEmailid());
        cn.setText(student.getCn());
        password.setText(R.string.password_demo);
        rn.setText(student.getRn());
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

                update();
                break;
        }
    }


    private void update() {
        student.setName(String.valueOf(name.getText()));
        student.setCn(String.valueOf(cn.getText()));
        student.setRn(String.valueOf(rn.getText()));
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
