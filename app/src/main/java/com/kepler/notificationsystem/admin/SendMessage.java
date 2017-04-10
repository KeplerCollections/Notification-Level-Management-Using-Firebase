package com.kepler.notificationsystem.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;

public class SendMessage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_send_message;
    }

    @Override
    public int getActionBarTitle() {
        return R.string.send_message;
    }
}
