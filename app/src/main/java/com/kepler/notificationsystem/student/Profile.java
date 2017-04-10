package com.kepler.notificationsystem.student;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.notification.Config;
import com.kepler.notificationsystem.services.Student;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.SimpleNetworkHandler;
import com.kepler.notificationsystem.support.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class Profile extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.cn)
    EditText cn;
    @BindView(R.id.edit)
    Button edit;
    @BindView(R.id.update)
    Button update;
    @BindView(R.id.cancel)
    Button cancel;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_USER, 0).getString(Params.USER, "");
        username.setText(email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edit.setOnClickListener(this);
        update.setOnClickListener(this);
        cancel.setOnClickListener(this);
//        spinner.getSelectedView().setEnabled(false);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_profile;
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

    @Override
    public int getActionBarTitle() {
        return R.string.profile;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit:
                edit.setVisibility(View.GONE);
                update.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                cn.setVisibility(View.VISIBLE);
                break;
            case R.id.update:
                if (cn.getText().toString().trim().length() < 10) {
                    Utils.toast(getApplicationContext(), R.string.password_error_msg);
                    return;
                }
                edit.setVisibility(View.VISIBLE);
                update.setVisibility(View.GONE);
                cn.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                updateContact();
                break;
            case R.id.cancel:
                edit.setVisibility(View.VISIBLE);
                update.setVisibility(View.GONE);
                cn.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                break;
        }
    }

    private void updateContact() {
        Student.updateContactNumber(getApplicationContext(), email, cn.getText().toString(), new SimpleNetworkHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = ProgressDialog.show(Profile.this, "", getResources().getString(R.string.loading));
            }

            @Override
            public void onResult(int statusCode, Header[] headers, Object responseBody) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseBody.toString());
                    if (jsonObject.getBoolean(Params.STATUS)) {
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

    }
}
