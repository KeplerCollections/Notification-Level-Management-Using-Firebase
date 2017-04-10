package com.kepler.notificationsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        setTitle(getActionBarTitle());
    }

    public abstract int getContentView();

    public abstract int getActionBarTitle();

    public void setTitle(String newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }

    public void setTitle(int newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }
}
