package com.kepler.notificationsystem.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.Register;
import com.kepler.notificationsystem.support.OnYearSelect;
import com.kepler.notificationsystem.support.Utils;

import butterknife.BindView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send_message.setOnClickListener(this);
        remove_file.setOnClickListener(this);
        send_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    Utils.startActivityForResult(SendMessage.this, SelectStudent.class, null, REQUEST_CODE);
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

                }
                break;
        }
    }
}
