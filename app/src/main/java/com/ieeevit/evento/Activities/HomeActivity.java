package com.ieeevit.evento.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ieeevit.evento.classes.Event;
import com.ieeevit.evento.fragments.EventFragment;
import com.ieeevit.evento.fragments.FaqFragment;
import com.ieeevit.evento.fragments.ProfileFragment;
import com.ieeevit.evento.fragments.ScannableFragment;
import com.ieeevit.evento.fragments.TimelineFragment;
import com.ieeevit.evento.R;

import java.util.Objects;

import static maes.tech.intentanim.CustomIntent.customType;

public class HomeActivity extends AppCompatActivity {

    private Fragment profileFragment;
    private Fragment homeFragment;
    private Fragment timelineFragment;
    private Fragment scannableFragment;
    private Fragment faqFragment;

    public HomeActivity() {
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(homeFragment);
                    homeFragment.setEnterTransition(new Explode());
                    homeFragment.setExitTransition(new Fade());
                    return true;
                case R.id.navigation_schedule:
                    loadFragment(timelineFragment);
                    timelineFragment.setEnterTransition(new Explode());
                    timelineFragment.setExitTransition(new Fade());
                    return true;
                case R.id.navigation_profile:
                    loadFragment(profileFragment);
                    profileFragment.setEnterTransition(new Explode());
                    profileFragment.setExitTransition(new Fade());
                    return true;

                case R.id.navigation_scannables:
                    loadFragment(scannableFragment);
                    scannableFragment.setEnterTransition(new Explode());
                    scannableFragment.setExitTransition(new Fade());
                    return true;

                case R.id.navigation_faq:
                    loadFragment(faqFragment);
                    faqFragment.setEnterTransition(new Explode());
                    faqFragment.setExitTransition(new Fade());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        profileFragment = new ProfileFragment();
        homeFragment = new EventFragment();
        timelineFragment = new TimelineFragment();
        scannableFragment = new ScannableFragment();
        faqFragment = new FaqFragment();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("Details", Context.MODE_PRIVATE);
        String eventString = sharedPreferences.getString("EventDetails", "eventNotFound");
        Gson gson = new Gson();
        final Event event = gson.fromJson(eventString, Event.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.tv_event_name)).setText(event.getEventName());

    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
        }
    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutItem:
                logout();
                return true;

            case R.id.moreInfoItem:
                Intent intent = new Intent(HomeActivity.this, MoreInfoActivity.class);
                startActivity(intent);
                customType(this, "left-to-right");

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("Details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("InEvent");
        editor.remove("UserDetails");
        editor.remove("IsRegistered");
        editor.remove("IsCoordinator");
        editor.remove("EventScannables");
        editor.apply();
        Intent intent = new Intent(HomeActivity.this, EventIDActivity.class);
        startActivity(intent);
        customType(HomeActivity.this, "up-to-bottom");
        finishAfterTransition();
    }

}
