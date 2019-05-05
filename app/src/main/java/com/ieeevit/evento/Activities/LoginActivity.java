package com.ieeevit.evento.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.ieeevit.evento.classes.Event;
import com.ieeevit.evento.classes.ScannableSessionList;
import com.ieeevit.evento.classes.User;
import com.ieeevit.evento.networkAPIs.AuthAPI;
import com.ieeevit.evento.networkAPIs.FetchAPI;
import com.ieeevit.evento.networkmodels.ScannableModel;
import com.ieeevit.evento.networkmodels.UserEventModel;
import com.ieeevit.evento.networkmodels.UserLoginModel;
import com.ieeevit.evento.networkmodels.UserModel;
import com.ieeevit.evento.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class LoginActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private EditText email, password;
    private Button loginBtn;
    private TextView loadingText;
    private View loadingScreen;
    private LottieAnimationView loadingAnimation;
    private SharedPreferences sharedPreferences;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("Details", Context.MODE_PRIVATE);

        View view = findViewById(R.id.loginScreen);
        final GestureDetector gestureDetector = new GestureDetector(this, this);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                customType(LoginActivity.this, "bottom-to-up");
                finishAfterTransition();
            }
        });

        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_pwd);
        loginBtn = findViewById(R.id.btn_user_login);
        loadingScreen = findViewById(R.id.signup_loading_screen);
        loadingAnimation = findViewById(R.id.signup_loading_animation);
        loadingText = findViewById(R.id.signup_loading_text);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

    }

    private void userLogin() {
        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter all the details", Toast.LENGTH_LONG).show();
        } else {
            View currentView = this.getCurrentFocus();
            if (currentView != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
            }
            loadingScreen.setVisibility(View.VISIBLE);
            loadingAnimation.playAnimation();
            loadingText.setText("Authenticating");
            findViewById(R.id.loginScreen).setVisibility(View.GONE);
            Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
            AuthAPI authAPI = retrofit.create(AuthAPI.class);
            Call<UserLoginModel> login = authAPI.userLogin(email.getText().toString(), password.getText().toString());
            login.enqueue(new Callback<UserLoginModel>() {
                @Override
                public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                    String success = response.body().getSuccess().toString();
                    String message = response.body().getMessage();
                    findViewById(R.id.loginScreen).setVisibility(View.VISIBLE);
                    if (success.equals("false")) {
                        loadingScreen.setVisibility(View.INVISIBLE);
                        loadingAnimation.pauseAnimation();
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        String token = response.body().getToken();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.putBoolean("IsCoordinator", false);
                        editor.apply();
                        getUser();
                    }
                }

                @Override
                public void onFailure(Call<UserLoginModel> call, Throwable t) {
                    loadingScreen.setVisibility(View.INVISIBLE);
                    loadingAnimation.pauseAnimation();
                    findViewById(R.id.loginScreen).setVisibility(View.VISIBLE);
                    t.printStackTrace();
                    Toast.makeText(LoginActivity.this, "An error occured", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private void getUser() {
        String token = sharedPreferences.getString("token", "");
        loadingText.setText("Fetching User Details");
        findViewById(R.id.loginScreen).setVisibility(View.GONE);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        FetchAPI fetchAPI = retrofit.create(FetchAPI.class);
        Call<UserModel> userInfo = fetchAPI.userInfo(token);
        userInfo.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                String success = response.body().getSuccess().toString();
                String message = response.body().getMessage();
                findViewById(R.id.loginScreen).setVisibility(View.VISIBLE);
                if (success.equals("false")) {
                    loadingScreen.setVisibility(View.INVISIBLE);
                    loadingAnimation.pauseAnimation();
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    saveUserDetails(response);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                loadingScreen.setVisibility(View.INVISIBLE);
                loadingAnimation.pauseAnimation();
                findViewById(R.id.loginScreen).setVisibility(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "An error occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveUserDetails(Response<UserModel> response) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        User currUser = response.body().getUser();
        Gson gson = new Gson();
        String json = gson.toJson(currUser);
        editor.putString("UserDetails", json);
        editor.apply();
        loginEvent();
    }

    private void loginEvent() {
        View currentView = this.getCurrentFocus();
        if (currentView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        }
        loadingText.setText("Finding Your Event");
        findViewById(R.id.loginScreen).setVisibility(View.GONE);
        String eventString = sharedPreferences.getString("EventDetails", "eventNotFound");
        Gson gson = new Gson();
        final Event currentEvent = gson.fromJson(eventString, Event.class);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        AuthAPI authAPI = retrofit.create(AuthAPI.class);
        Call<UserEventModel> event = authAPI.eventLogin(sharedPreferences.getString("token", ""), currentEvent.getEventId());
        event.enqueue(new Callback<UserEventModel>() {
            @Override
            public void onResponse(Call<UserEventModel> call, Response<UserEventModel> response) {
                String success = response.body().getSuccess().toString();
                if (success.equals("false")) {
                    String encryptedId = response.body().getEncryptedId();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("IsRegistered", false);
                    editor.putBoolean("IsCoordinator", false);
                    editor.putString("encryptedId", encryptedId);
                    editor.apply();
                } else {
                    String encryptedId = response.body().getEncryptedId();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("encryptedId", encryptedId);
                    editor.putBoolean("IsRegistered", true);
                    editor.putBoolean("IsCoordinator", response.body().getIsCoordinator());
                    editor.apply();
                    getEventScannables();
                }
            }

            @Override
            public void onFailure(Call<UserEventModel> call, Throwable t) {
                loadingScreen.setVisibility(View.INVISIBLE);
                loadingAnimation.pauseAnimation();
                findViewById(R.id.loginScreen).setVisibility(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "An error occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getEventScannables() {
        final View currentView = this.getCurrentFocus();
        if (currentView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        }

        loadingText.setText("Fetching Event Scannables");

        // Creating the retrofit instance
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        FetchAPI fetchAPI = retrofit.create(FetchAPI.class);

        final SharedPreferences sharedPreferences = getSharedPreferences("Details", Context.MODE_PRIVATE);
        String eventString = sharedPreferences.getString("EventDetails", "eventNotFound");
        Gson gson = new Gson();
        final Event event = gson.fromJson(eventString, Event.class);

        Call<ScannableModel> eventScannables = fetchAPI.eventScannables(event.getEventId());
        eventScannables.enqueue(new Callback<ScannableModel>() {
            @Override
            public void onResponse(Call<ScannableModel> call, Response<ScannableModel> response) {
                findViewById(R.id.loginScreen).setVisibility(View.VISIBLE);
                String success = response.body().getSuccess().toString();
                String message = response.body().getMessage();
                loadingScreen.setVisibility(View.INVISIBLE);
                loadingAnimation.pauseAnimation();
                if (success.equals("false")) {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    ScannableSessionList scannableList = new ScannableSessionList(response.body().getScannables());
                    String json = gson.toJson(scannableList);
                    editor.putString("EventScannables", json);
                    editor.apply();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                    customType(LoginActivity.this, "bottom-to-up");
                    finishAfterTransition();
                }
            }

            @Override
            public void onFailure(Call<ScannableModel> call, Throwable t) {
                loadingScreen.setVisibility(View.INVISIBLE);
                loadingAnimation.pauseAnimation();
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "An error occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
            case 1:
                Bundle bundle = ActivityOptions
                        .makeSceneTransitionAnimation(LoginActivity.this,
                                findViewById(R.id.signupContainer), "SwipeUp")
                        .toBundle();
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent, bundle);
                return true;
            case 2:
                Log.v(TAG, "left");
                return true;
            case 3:
                Log.v(TAG, "down");
                return true;
            case 4:
                Log.v(TAG, "right");
                return true;
        }
        return false;
    }

    private int getSlope(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
        if (angle > 45 && angle <= 135)
            // top
            return 1;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            // left
            return 2;
        if (angle < -45 && angle >= -135)
            // down
            return 3;
        if (angle > -45 && angle <= 45)
            // right
            return 4;
        return 0;
    }
}
