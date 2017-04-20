package com.kepler.notificationsystem.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.Register;
import com.kepler.notificationsystem.dao.Push;
import com.kepler.notificationsystem.notification.Config;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.services.Student;
import com.kepler.notificationsystem.support.OnBatchSelect;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.Utils;

import org.json.JSONObject;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class SendMessage extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 999;
    @BindView(R.id.send_to)
    Spinner send_to;
    @BindView(R.id._type)
    Spinner _type;
    @BindView(R.id._title)
    EditText _title;
    @BindView(R.id._msg)
    EditText _msg;
    @BindView(R.id._file)
    TextView _file;
    @BindView(R.id.send_message)
    Button send_message;
    @BindView(R.id.remove_file)
    ImageButton remove_file;
    final Push push = new Push();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send_message.setOnClickListener(this);
        remove_file.setOnClickListener(this);
        if (getIntent().getExtras() != null && getIntent().getExtras().getString(Params.REG_ID, null) != null) {
            send_to.setVisibility(View.GONE);
            push.setReg_id(getIntent().getExtras().getString(Params.REG_ID, null));
            push.setPush_type(Config.PUSH_TYPE_INDIVIDUAL);
        } else {
            push.setPush_type(Config.PUSH_TYPE_TOPIC);
            push.setTopic_name(Config.TOPIC_GLOBAL);
        }
        send_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        push.setTopic_name(Utils.getType(null, null));
                        break;
                    case 1:
                        Utils.getBatchDialog(SendMessage.this, new OnBatchSelect() {
                            @Override
                            public void onBatchSelect(String course, String batch) {
                                if (course == null || batch == null) {
                                    send_to.setSelection(0);
                                } else {
                                    push.setTopic_name(Utils.getType(course, batch));
                                }
                            }
                        });
                        break;
                    case 2:
                        Utils.startActivityForResult(SendMessage.this, SelectStudents.class, null, REQUEST_CODE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_message:
                if (_title.getText().toString().trim().length() == 0) {
                    Utils.toast(getApplicationContext(), R.string.title_er_msg);
                    return;
                }
                if (_msg.getText().toString().trim().length() == 0) {
                    Utils.toast(getApplicationContext(), R.string._msg_er_msg);
                    return;
                }
                sendMessage();
                break;
            case R.id.remove_file:

                break;
        }
    }

    private void sendMessage() {
        push.setTitle(_title.getText().toString());
        push.setMessage(_msg.getText().toString());
        push.setMsg_type(getMessageTpe());
        com.kepler.notificationsystem.services.Student.sendPush(getApplicationContext(), push, new SimpleNetworkHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = ProgressDialog.show(SendMessage.this, "", getResources().getString(R.string.sending));
            }

            @Override
            public void onResult(int statusCode, Header[] headers, Object responseBody) {
                try {
                    final JSONObject jsonObject = new JSONObject(responseBody.toString());
                    if (jsonObject.getBoolean(Params.STATUS)) {
                        JSONObject dataJsonObject = new JSONObject(jsonObject.getString(Params.DATA));
                        if (dataJsonObject.getInt("success") == 0) {
                            Utils.toast(getApplicationContext(), R.string.failed);
                        } else {
                            Utils.toast(getApplicationContext(), R.string.message_sent);
                        }
                    } else {
                        Utils.toast(getApplicationContext(), R.string.failed);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });
    }

    private int getMessageTpe() {
        switch (_type.getSelectedItemPosition()) {
            case 1:
                return Student.IMAPORTANT_TYPE;
            case 2:
                return Student.WARNING_TYPE;
            default:
                return Student.NORMAL_TYPE;
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_send_message;
    }

    @Override
    public int getActionBarTitle() {
        return R.string.send_message;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (requestCode == RESULT_OK) {

                } else {
                    send_to.setSelection(0);
                }
                break;
        }
    }
}
