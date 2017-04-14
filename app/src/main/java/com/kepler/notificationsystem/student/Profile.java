package com.kepler.notificationsystem.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.dao.Student;
import com.kepler.notificationsystem.dao.StudentParent;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.support.CircleTransform;
import com.kepler.notificationsystem.support.FileChooser;
import com.kepler.notificationsystem.support.Logger;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.Utils;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class Profile extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 999;
    private static final String TAG = Profile.class.getSimpleName();
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.back_pic)
    ImageView back_pic;
    @BindView(R.id.profile_pic)
    ImageView profile_pic;
    @BindView(R.id.edit_pic)
    TextView edit_pic;
    @BindView(R.id.update_profile)
    Button update_profile;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        student = bundle.getParcelable(Params.DATA);
        if (bundle.getBoolean(Params.IS_STUDENT, false)) {
            edit_pic.setVisibility(View.VISIBLE);
            update_profile.setVisibility(View.VISIBLE);
            edit_pic.setOnClickListener(this);
            update_profile.setOnClickListener(this);
        }
        if (student != null) {
            setView();
        } else if (bundle.getString(Params.EMAILID, null) != null) {
            student = new Student(null, getIntent().getExtras().getString(Params.EMAILID, null), null, null, null, null, null);
            load();
        } else {
            onBackPressed();
        }
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

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        }, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    this.student = data.getParcelableExtra(Params.DATA);
                    setView();
                }
                break;
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_profile;
    }

    @Override
    public int getActionBarTitle() {
        return R.string.profile;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_pic:
                new FileChooser(this).setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(final File file) {
                        // do something with the file
                        if (file.exists() && file.canRead())
                            upload(file);
                    }
                }).showDialog();

                break;
            case R.id.update_profile:
                Bundle bundle = new Bundle();
                bundle.putParcelable(Params.DATA, student);
                Utils.startActivityForResult(this, Update.class, bundle, REQUEST_CODE);
                break;
        }
    }

    // url = file path or whatever suitable URL you want.
//    public static String getMimeType(String url) {
//        String type = null;
//        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
//        if (extension != null) {
//            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//        }
//        return type;
//    }

    private void upload(File file) {
        com.kepler.notificationsystem.services.Student.uploadFile(this, file, com.kepler.notificationsystem.services.Student.UPDATE_PIC, student.getEmailid(), new SimpleNetworkHandler() {
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
                    Picasso.with(Profile.this).load(fromJson.getMessage()).resize(150, 150).transform(new CircleTransform()).placeholder(R.drawable.acc)
                            .into(profile_pic);
                } else {
                    Utils.toast(getApplicationContext(), fromJson.getMessage());
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });
    }

    private void setView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content.setText(Html.fromHtml(
                    "<font color='#FF4081'><big><big>NAME</font></big></big><br>" + student.getName()
                            + "<br><br><font color='#FF4081'><big><big>EMAIL ID</font></big></big><br>" + student.getEmailid()
                            + "<br><br><font color='#FF4081'><big><big>ROLL NUMBER</font></big></big><br>" + student.getRn()
                            + "<br><br><font color='#FF4081'><big><big>PASSWORD</font></big></big><br>********"
                            + "<br><br><font color='#FF4081'><big><big>CONTACT NUMBER</font></big></big><br>" + student.getCn()
                            + "<br><br><font color='#FF4081'><big><big>BATCH YEAR</font></big></big><br>" + student.getBatch()
                    , Html.FROM_HTML_MODE_LEGACY));
        } else {
            content.setText(Html.fromHtml(
                    "<font color='#FF4081'><big><big>NAME</font></big></big><br>" + student.getName()
                            + "<br><br><font color='#FF4081'><big><big>EMAIL ID</font></big></big><br>" + student.getEmailid()
                            + "<br><br><font color='#FF4081'><big><big>ROLL NUMBER</font></big></big><br>" + student.getRn()
                            + "<br><br><font color='#FF4081'><big><big>PASSWORD</font></big></big><br>********"
                            + "<br><br><font color='#FF4081'><big><big>CONTACT NUMBER</font></big></big><br>" + student.getCn()
                            + "<br><br><font color='#FF4081'><big><big>BATCH YEAR</font></big></big><br>" + student.getBatch()));
        }
        if (!update_profile.isEnabled()) {
            update_profile.setEnabled(true);
        }
        Picasso.with(Profile.this).load(student.getImg()).resize(150, 150).transform(new CircleTransform()).placeholder(R.drawable.acc)
                .into(profile_pic);
        Picasso.with(Profile.this).load(student.getImg()).centerCrop().placeholder(R.drawable.ic_logo).into(back_pic);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
