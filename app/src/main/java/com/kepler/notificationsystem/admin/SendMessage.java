package com.kepler.notificationsystem.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;

public class SendMessage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

}
