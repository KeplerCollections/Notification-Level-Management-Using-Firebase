package com.kepler.notificationsystem.admin;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.admin.adapter.ExpandableListAdapter;
import com.kepler.notificationsystem.admin.adapter.SelectStudentAdapter;
import com.kepler.notificationsystem.admin.adapter.StudentAdapter;
import com.kepler.notificationsystem.dao.StudentParent;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.services.Student;
import com.kepler.notificationsystem.student.Profile;
import com.kepler.notificationsystem.support.EmptyRecyclerView;
import com.kepler.notificationsystem.support.OnViewActionListener;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.Utils;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class SelectStudents extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.recycler_view)
    EmptyRecyclerView recycler_view;
    @BindView(R.id.recycler_empty_view)
    LinearLayout recycler_empty_view;
    @BindView(R.id.search_students)
    EditText search_students;
    private SelectStudentAdapter selectStudentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        // Fetch the empty view from the layout and set it on
        // the new recycler view
        recycler_view.setEmptyView(recycler_empty_view);
        load();
    }

    private void load() {
        Student.select(getApplicationContext(), new SimpleNetworkHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = ProgressDialog.show(SelectStudents.this, "", getResources().getString(R.string.loading));
            }

            @Override
            public void onResult(int statusCode, Header[] headers, Object responseBody) {
                Gson gson = new Gson();
                StudentParent fromJson = gson.fromJson(responseBody.toString(), StudentParent.class);
                if (fromJson.isStatus()) {
                    if (selectStudentAdapter == null) {
                        if (fromJson.getData().size() > 0) {
                            search_students.setEnabled(true);
                            search_students.addTextChangedListener(new MyTextWatcher());
                        }
                        selectStudentAdapter = new SelectStudentAdapter(getApplicationContext());
                        selectStudentAdapter.addAll(fromJson.getData());
                        recycler_view.setAdapter(selectStudentAdapter);
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
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_students;
    }
//when activity in single top
//    @Override
//    protected void onNewIntent(Intent intent) {
//        handleIntent(intent);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.select_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.m_done:
                if (selectStudentAdapter == null)
                    return false;
                Intent intent = new Intent();
                intent.putExtra(Params.DATA, selectStudentAdapter.getSelected());
                setResult(RESULT_OK, intent);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getActionBarTitle() {
        return R.string.select;
    }

    @Override
    public void onClick(View view) {

    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            selectStudentAdapter.getFilter().filter(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }


}
