package com.ieeevit.evento.activities;

import android.content.Context;
import android.content.Intent;
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
import static maes.tech.intentanim.CustomIntent.customType;
import com.airbnb.lottie.LottieAnimationView;
import com.ieeevit.evento.networkAPIs.RegistrationAPI;
import com.ieeevit.evento.networkmodels.GeneralResponse;
import com.ieeevit.evento.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static final String TAG = "SignupActivity";
    private TextView loadingText;
    private EditText emailId;
    private EditText name;
    private EditText username;
    private EditText password;
    private EditText contactNumber;
    private EditText passwordConfirmation;
    private Button signupButton;
    private View loadingScreen;
    private LottieAnimationView loadingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailId = findViewById(R.id.et_email_signup);
        name = findViewById(R.id.et_name_signup);
        username = findViewById(R.id.et_username_signup);
        contactNumber = findViewById(R.id.et_phone_signup);
        password = findViewById(R.id.et_password_signup);
        passwordConfirmation = findViewById(R.id.et_password_confirmation_signup);
        signupButton = findViewById(R.id.btn_signup);
        loadingScreen = findViewById(R.id.signup_loading_screen);
        loadingAnimation = findViewById(R.id.signup_loading_animation);
        loadingText = findViewById(R.id.signup_loading_text);

        View view = findViewById(R.id.signupScreen);
        final GestureDetector gestureDetector = new GestureDetector(this, this);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {

        if (checkInput()) {
            View currentView = this.getCurrentFocus();
            if (currentView != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
            }
            findViewById(R.id.signupScreen).setVisibility(View.GONE);
            loadingScreen.setVisibility(View.VISIBLE);
            loadingAnimation.playAnimation();
            loadingText.setText("Signing You Up");
            Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create()).build();
            RegistrationAPI registrationAPI = retrofit.create(RegistrationAPI.class);
            Call<GeneralResponse> register = registrationAPI.userRegister(name.getText().toString(),
                    emailId.getText().toString(),
                    password.getText().toString(),
                    username.getText().toString(),
                    contactNumber.getText().toString());
            register.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    findViewById(R.id.signupScreen).setVisibility(View.VISIBLE);
                    String success = response.body().getSuccess().toString();
                    String message = response.body().getMessage();
                    loadingScreen.setVisibility(View.INVISIBLE);
                    loadingAnimation.pauseAnimation();
                    if (success.equals("false")){
                        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignupActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        customType(SignupActivity.this, "fadein-to-fadeout");
                        finishAfterTransition();
                    }
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    findViewById(R.id.signupScreen).setVisibility(View.VISIBLE);
                    loadingScreen.setVisibility(View.INVISIBLE);
                    loadingAnimation.pauseAnimation();
                    t.printStackTrace();
                    Toast.makeText(SignupActivity.this, "An error occured", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean checkInput(){
        if (name.getText().toString().isEmpty()
                || username.getText().toString().isEmpty()
                || contactNumber.getText().toString().isEmpty()
                || emailId.getText().toString().isEmpty()
                || password.getText().toString().isEmpty()
                || passwordConfirmation.getText().toString().isEmpty()){
            Toast.makeText(this, "Hey, You forgot to Enter Some Details", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (passwordConfirmation.getText().toString().equals(password.getText().toString())){
                return true;
            } else {
                Toast.makeText(this, "Your Passwords do not Match", Toast.LENGTH_SHORT).show();
                return false;
            }

        }

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
                Log.v(TAG, "up");
                return true;
            case 2:
                Log.v(TAG, "left");
                return true;
            case 3:
                Log.v(TAG, "down");
                onBackPressed();
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
