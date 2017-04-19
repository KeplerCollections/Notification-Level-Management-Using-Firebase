package com.kepler.notificationsystem.admin;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.admin.adapter.StudentAdapter;
import com.kepler.notificationsystem.dao.StudentParent;
import com.kepler.notificationsystem.services.Student;
import com.kepler.notificationsystem.student.Profile;
import com.kepler.notificationsystem.support.EmptyRecyclerView;
import com.kepler.notificationsystem.support.OnViewActionListener;
import com.kepler.notificationsystem.services.SimpleNetworkHandler;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.Utils;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class SearchResultsActivity extends BaseActivity implements OnViewActionListener {

    @BindView(R.id.search_for)
    TextView search_for;
    @BindView(R.id.recycler_view)
    EmptyRecyclerView recycler_view;
    @BindView(R.id.recycler_empty_view)
    LinearLayout recycler_empty_view;
    private int page = 1;
    private StudentAdapter studentAdapter;
    private com.kepler.notificationsystem.dao.Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        // Fetch the empty view from the layout and set it on
        // the new recycler view
        recycler_view.setEmptyView(recycler_empty_view);
        handleIntent(getIntent());
    }

    @Override
    public int getContentView() {
        return R.layout.activity_search_results;
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

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            search_for.setText("You are searching for \"" + query + "\"");
            student = new com.kepler.notificationsystem.dao.Student(query,null,null,null);
            load();
        } else {
            onBackPressed();
        }
    }

    private void load() {
        Student.select(getApplicationContext(), student, new SimpleNetworkHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = ProgressDialog.show(SearchResultsActivity.this, "", getResources().getString(R.string.loading));
            }

            @Override
            public void onResult(int statusCode, Header[] headers, Object responseBody) {
                Gson gson = new Gson();
                StudentParent fromJson = gson.fromJson(responseBody.toString(), StudentParent.class);
                if (fromJson.isStatus()) {
                    if(studentAdapter==null){
                        studentAdapter=new StudentAdapter(getApplicationContext(), SearchResultsActivity.this);
                        studentAdapter.addAll(fromJson.getData());
                        recycler_view.setAdapter(studentAdapter);
                    }else{
                        studentAdapter.addAll(fromJson.getData());
                        studentAdapter.notifyDataSetChanged();
                    }
                    page++;
                }else{
                    Utils.toast(getApplicationContext(),fromJson.getMessage());
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
    public int getActionBarTitle() {
        return R.string.search_result;
    }

    @Override
    public void refresh() {
        load();
    }

    @Override
    public void onProfileBtnClicked(com.kepler.notificationsystem.dao.Student student) {
        Bundle bundle=new Bundle();
        bundle.putParcelable(Params.DATA,student);
        Utils.startActivity(this,Profile.class,bundle,false);
    }

    @Override
    public void onSendMessageBtnClicked(com.kepler.notificationsystem.dao.Student student) {
//        Bundle bundle=new Bundle();
//        bundle.putParcelable(Params.DATA,student);
        Utils.startActivity(this,SendMessage.class,null,false);
    }
}
