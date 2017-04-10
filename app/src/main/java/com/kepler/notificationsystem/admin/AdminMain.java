package com.kepler.notificationsystem.admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kepler.notificationsystem.BaseActivity;
import com.kepler.notificationsystem.Login;
import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.Register;
import com.kepler.notificationsystem.admin.adapter.NavigationAdapter;
import com.kepler.notificationsystem.admin.frag.Home;
import com.kepler.notificationsystem.admin.frag.Settings;
import com.kepler.notificationsystem.notification.Config;
import com.kepler.notificationsystem.support.Logger;
import com.kepler.notificationsystem.support.OnYearSelect;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class AdminMain extends BaseActivity implements SearchView.OnQueryTextListener {

    private SharedPreferences pref;
    private static final String TAG = AdminMain.class.getSimpleName();
    private boolean doubleBackToExitPressedOnce;
    private ActionBarDrawerToggle mDrawerToggle;
//    private CharSequence mDrawerTitle;
//    private CharSequence mTitle;

    @BindView(R.id.left_drawer)
    ListView mDrawerList;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private List<String> mItems = new ArrayList<>();
    private String mTitle;
//    private SharedPreferences prefSett;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_USER, 0);
//        prefSett = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_SETTINGS, 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mItems = Arrays.asList(getResources().getStringArray(R.array.drawer_items));
        // Set the adapter for the list view
        mDrawerList.setAdapter(new NavigationAdapter(getApplicationContext(), mItems));
//                android.R.layout.drawer_list_item, mSems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setTitle(mTitle);
                mDrawerToggle.syncState();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(mTitle);
                mDrawerToggle.syncState();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.syncState();
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        selectItem(0);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_admin_main;
    }

    @Override
    public int getActionBarTitle() {
        return R.string.home;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.admin_home, menu);
        menu.getItem(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        menu.getItem(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.m_update_password:
                updatePassword();
                return true;
            case R.id.m_logout:
                logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout).setMessage(R.string.are_you_sure);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pref.edit().remove(Params.USER).commit();
                FirebaseAuth.getInstance().signOut();
                doubleBackToExitPressedOnce = true;
                Utils.startActivity(AdminMain.this, Login.class, null, true, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updatePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter New Password");

// Set up the input
        final EditText password = new EditText(this);
        password.setPadding(10, 10, 10, 10);

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        password.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(password);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (password.getText().toString().trim().length() == 0) {
                    Utils.toast(getApplicationContext(), R.string.password_error_msg);
                    return;
                }
                final ProgressDialog progressDialog = ProgressDialog.show(AdminMain.this, "", getResources().getString(R.string.loading));
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updatePassword(password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Logger.e(TAG, "StudentParent password updated.");
                                    Utils.toast(getApplicationContext(), R.string.password_changed);
                                } else {
                                    Utils.toast(getApplicationContext(), task.getException().getMessage());
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Utils.toast(getApplicationContext(), R.string.back_message);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.trim().length() == 0) {
            Utils.toast(getApplicationContext(), R.string.field_can_not_be_empty);
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString(SearchManager.QUERY, query);
        Utils.startActivity(this, SearchResultsActivity.class, bundle, false, Intent.ACTION_SEARCH);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public List<String> getDrawerItems() {
        return mItems;
    }

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            selectItem(position);
        }

        /**
         * Swaps fragments in the main content view
         */

    }

    public void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment;
        final Bundle args = new Bundle();
//            args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        switch (position) {
            case 0:
                fragment = new Home();
                replaceFragment(fragment, args);
                break;
            case 1:
                Utils.startActivity(this, SendMessage.class, args, false);
                break;
            case 2:
                Register.openYearPickerDialog(AdminMain.this,new OnYearSelect(){
                    @Override
                    public void onYearSet(String year) {
                        args.putString(Params.BATCH,year);
                        Utils.startActivity(AdminMain.this, Students.class, args, false);
                    }
                });
                break;
            case 3:
                fragment = new Settings();
                replaceFragment(fragment, args);
                break;
            default:
                fragment = new Home();
                replaceFragment(fragment, args);
        }
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(String.valueOf(getDrawerItems().get(position)));
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void replaceFragment(Fragment fragment, Bundle args) {
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void setTitle(String newTitle) {
        mTitle = newTitle;
        super.setTitle(newTitle);
    }

}
