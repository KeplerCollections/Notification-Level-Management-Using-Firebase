package com.kepler.notificationsystem.admin.frag;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.admin.AdminMain;
import com.kepler.notificationsystem.support.BaseFragment;
import com.kepler.notificationsystem.support.Utils;

import java.util.Random;

import butterknife.BindView;

/**
 * Created by Amit on 07-04-2017.
 */

public class Home extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.home_fragment)
    LinearLayout home_fragment;
    private AdminMain activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AdminMain) getActivity();
    }

    @Override
    public int getCreateView() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Random mRandom = new Random(System.currentTimeMillis());
        for (int i = 1; i < activity.getDrawerItems().size(); i++) {
            Button button = new Button(activity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 0);
            button.setLayoutParams(layoutParams);
            button.setText(activity.getDrawerItems().get(i));
            button.setBackgroundColor(generateRandomColor(mRandom));
            button.setTag(i);
            button.setOnClickListener(this);
//            button.setBackgroundResource(R.drawable.button_drawable);
            home_fragment.addView(button);
        }
    }

    public int generateRandomColor(Random mRandom) {
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;

        return Color.rgb(red, green, blue);
    }

    @Override
    public void onClick(View view) {
        activity.selectItem((int) view.getTag());
    }
}
