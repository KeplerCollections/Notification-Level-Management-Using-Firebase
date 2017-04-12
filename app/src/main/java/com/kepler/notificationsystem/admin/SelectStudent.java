package com.kepler.notificationsystem.admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.admin.adapter.ExpandableListAdapter;
import com.kepler.notificationsystem.admin.adapter.StudentAdapter;
import com.kepler.notificationsystem.dao.StudentParent;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.services.Student;
import com.kepler.notificationsystem.support.EmptyRecyclerView;
import com.kepler.notificationsystem.support.Utils;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class SelectStudent extends BaseActivity {

    @BindView(R.id.expandable_list)
    ExpandableListView expandable_list;
    private ExpandableListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        expandable_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                if(listAdapter.getChildrenCount(i)==0){
                    Utils.toast(getApplicationContext(),R.string.batch_data_empty);
                }
                return false;
            }
        });
        load();
    }
    private void load() {
        Student.select(getApplicationContext(), new SimpleNetworkHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = ProgressDialog.show(SelectStudent.this, "", getResources().getString(R.string.loading));
            }

            @Override
            public void onResult(int statusCode, Header[] headers, Object responseBody) {
                Gson gson = new Gson();
                StudentParent fromJson = gson.fromJson(responseBody.toString(), StudentParent.class);
                if (fromJson.isStatus()) {
                    if (listAdapter == null) {
                        listAdapter = new ExpandableListAdapter(getApplicationContext());
                        listAdapter.addHeader(fromJson.getData());
                        expandable_list.setAdapter(listAdapter);
                    } else {
                        listAdapter.addHeader(fromJson.getData());
                        listAdapter.notifyDataSetChanged();
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
        return R.layout.activity_select_student;
    }

    @Override
    public int getActionBarTitle() {
        return R.string.select;
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
