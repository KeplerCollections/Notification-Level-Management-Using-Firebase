package com.kepler.notificationsystem.admin.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.admin.AdminMain;
import com.kepler.notificationsystem.support.BaseFragment;
import com.kepler.notificationsystem.support.Params;

import butterknife.BindView;

/**
 * Created by Amit on 07-04-2017.
 */

public class Settings extends BaseFragment {
    @BindView(R.id.even_running)
    Switch even_running;
    private AdminMain activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AdminMain) getActivity();
    }

    @Override
    public int getCreateView() {
        return R.layout.fragment_settings;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
