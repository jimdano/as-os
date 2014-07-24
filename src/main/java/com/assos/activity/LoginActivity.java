package com.assos.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.FrameLayout;

import com.assos.fragment.ConnexionFragment;
import com.parse.Parse;

/**
 * Created by jimmy on 03/06/2014.
 */
public class LoginActivity extends FragmentActivity {

    private FragmentManager fm;
    private FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Parse.initialize(this, "raooEIp6ziCuzXjZ4T8Vt83WpG23G2fDZhZiNZWf", "tct119JCo8u1edN48znQhAJ1jCMtailcCzG17xgl");
        } catch (Exception e){}
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.login_activity);

        FrameLayout frameHome = (FrameLayout) findViewById(R.id.container);
        Fragment connectionFragment = new ConnexionFragment();

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        if (frameHome != null) {
            ft.replace(R.id.container, connectionFragment);
        }
        ft.commit();
    }

    public void onCreate() {
        Parse.initialize(this, "raooEIp6ziCuzXjZ4T8Vt83WpG23G2fDZhZiNZWf", "tct119JCo8u1edN48znQhAJ1jCMtailcCzG17xgl");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
