package com.kepler.notificationsystem;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.kepler.notificationsystem.admin.AdminMain;
import com.kepler.notificationsystem.notification.Config;
import com.kepler.notificationsystem.student.Main;
import com.kepler.notificationsystem.support.Params;
import com.kepler.notificationsystem.support.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends AppCompatActivity {

    @BindView(R.id.background_splash_logo)
    ImageView background_splash_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        startAnimations();
    }

    private void startAnimations() {
        Animation anim = AnimationUtils.loadAnimation(Splash.this, R.anim.translate);
        anim.reset();
        background_splash_logo.clearAnimation();
        background_splash_logo.startAnimation(anim);
//        background_splash_logo.setVisibility(View.VISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
                                      @Override
                                      public void onAnimationStart(Animation animation) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        pushClientManager.registerIfNeeded(null, getApplicationContext());
//                    }
//                }).start();
                                      }

                                      @Override
                                      public void onAnimationEnd(Animation animation) {
//                if (pushClientManager.isGooglePlayServicesAvailable(SplashScreen.this, false))
//                    if (mSharedPreferences.getBoolean(UtilityMyBestPrice.PREF_KEY_USER_LOGIN, false)) {
//                        startFrpmSplash(MyBestPriceMainActivity);
//                    } else {
//                        startFrpmSplash(LoginScreen);
//                    }
                                          if (isGooglePlayServicesAvailable(Splash.this)) {
                                             final String email=getApplicationContext().getSharedPreferences(Config.SHARED_PREF_USER, 0).getString(Params.USER, null);
                                              if (email == null) {
                                                  Utils.startActivity(Splash.this, Login.class, null, true);
                                              } else if(email.equalsIgnoreCase(Utils.ADMIN_EMAIL_ID)) {
                                                  Utils.startActivity(Splash.this, AdminMain.class, null, true);
                                              }else{
                                                  Utils.startActivity(Splash.this, Main.class, null, true);
                                              }
                                          }
                                      }

                                      @Override
                                      public void onAnimationRepeat(Animation animation) {

                                      }
                                  }

        );
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

}
