package com.kepler.notificationsystem.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.admin.adapter.StudentAdapter;
import com.kepler.notificationsystem.dao.StudentParent;
import com.kepler.notificationsystem.services.Student;
import com.kepler.notificationsystem.student.Profile;
import com.kepler.notificationsystem.support.EmptyRecyclerView;
import com.kepler.notificationsystem.support.OnViewActionListener;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.support.Utils;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class Students extends BaseActivity implements OnViewActionListener {

    @BindView(R.id.recycler_view)
    EmptyRecyclerView recycler_view;
    @BindView(R.id.recycler_empty_view)
    LinearLayout recycler_empty_view;
    @BindView(R.id.search_students)
    EditText search_students;
    private int page = 1;
    private com.kepler.notificationsystem.dao.Student student;
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        // Fetch the empty view from the layout and set it on
        // the new recycler view
        recycler_view.setEmptyView(recycler_empty_view);

        Bundle bundle = getIntent().getExtras();
        student = new com.kepler.notificationsystem.dao.Student(null, null, bundle.getString(Params.COURSE, null), bundle.getString(Params.BATCH, null));
        load();
    }

    private void load() {
        Student.select(getApplicationContext(), student, new SimpleNetworkHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = ProgressDialog.show(Students.this, "", getResources().getString(R.string.loading));
            }

            @Override
            public void onResult(int statusCode, Header[] headers, Object responseBody) {
                Gson gson = new Gson();
                StudentParent fromJson = gson.fromJson(responseBody.toString(), StudentParent.class);
                if (fromJson.isStatus()) {
                    if (studentAdapter == null) {
                        if (fromJson.getData().size() > 0) {
                            search_students.setEnabled(true);
                            search_students.addTextChangedListener(new MyTextWatcher());
                        }
                        studentAdapter = new StudentAdapter(getApplicationContext(), Students.this);
                        studentAdapter.addAll(fromJson.getData());
                        recycler_view.setAdapter(studentAdapter);
                    } else {
                        studentAdapter.addAll(fromJson.getData());
                        studentAdapter.notifyDataSetChanged();
                    }
                    page++;
                } else {
                    Utils.toast(getApplicationContext(), fromJson.getMessage());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        }, page);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getActionBarTitle() {
        return R.string.students;
    }

    @Override
    public void refresh() {
        load();
    }

    @Override
    public void onProfileBtnClicked(com.kepler.notificationsystem.dao.Student student) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Params.DATA, student);
        Utils.startActivity(this, Profile.class, bundle, false);
    }

    @Override
    public void onSendMessageBtnClicked(com.kepler.notificationsystem.dao.Student student) {
        Bundle bundle=new Bundle();
        bundle.putString(Params.REG_ID,student.getReg_id());
        Utils.startActivity(this,SendMessage.class,bundle,false);
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            studentAdapter.getFilter().filter(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }


}
