package com.kepler.notificationsystem.admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.admin.adapter.StudentAdapter;
import com.kepler.notificationsystem.dao.Student;
import com.kepler.notificationsystem.dao.StudentParent;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.Utils;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class Profile extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.edit_pic)
    TextView edit_pic;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.back_pic)
    ImageView back_pic;
    @BindView(R.id.profile_pic)
    ImageView profile_pic;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        edit_pic.setOnClickListener(this);
        student = new Student(null, getIntent().getExtras().getString(Params.EMAILID, null), null, null, null, null);
        load();
        load();
    }

    private void load() {
        com.kepler.notificationsystem.services.Student.select(getApplicationContext(), student, new SimpleNetworkHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = ProgressDialog.show(Profile.this, "", getResources().getString(R.string.loading));
            }

            @Override
            public void onResult(int statusCode, Header[] headers, Object responseBody) {
                Gson gson = new Gson();
                StudentParent fromJson = gson.fromJson(responseBody.toString(), StudentParent.class);
                if (fromJson.isStatus()) {
                    if (fromJson.getData().size() > 0) {
                        student = fromJson.getData().get(0);
                        setView();
                    } else {
                        onBackPressed();
                    }
                } else {
                    Utils.toast(getApplicationContext(), fromJson.getMessage());
                }
            }

            private void setView() {
                content.setText(Html.fromHtml(
                        "<font color='#FF4081'><big>Name</font><br>" + student.getName()
                                + "<br><font color='#FF4081'><big>Email Id</font><br>" + student.getEmailid()
                                + "<br><font color='#FF4081'><big>Roll Number</font><br>" + student.getRn()
                                + "<br><font color='#FF4081'><big>Contact Number</font><br>" + student.getCn()
                                + "<br><font color='#FF4081'><big>Batch</font><br>" + student.getBatch()
                ));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        }, 0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_profile2;
    }

    @Override
    public int getActionBarTitle() {
        return R.string.profile;
    }

    @Override
    public void onClick(View view) {

    }
}
