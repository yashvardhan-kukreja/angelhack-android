package com.ieeevit.evento.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.ieeevit.evento.classes.Event;
import com.ieeevit.evento.networkAPIs.FetchAPI;
import com.ieeevit.evento.networkmodels.EventModel;
import com.ieeevit.evento.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class EventIDActivity extends AppCompatActivity {

    private SurfaceView cameraPreview;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private EditText eventID;
    private TextView loadingText;
    private Button enterEventBtn;
    private View loadingScreen;
    private LottieAnimationView loadingAnimation;
    private final int CAMERA_REQUEST_CODE = 1;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_id);
        sharedPreferences = getSharedPreferences("Details", Context.MODE_PRIVATE);

        eventID = findViewById(R.id.et_event_id);
        enterEventBtn = findViewById(R.id.btn_enter_event);
        loadingScreen = findViewById(R.id.event_loading_screen);
        loadingAnimation = findViewById(R.id.event_loading_animation);
        loadingText = findViewById(R.id.event_loading_text);
        cameraPreview = findViewById(R.id.camera_preview);

        barcodeDetector = new BarcodeDetector.Builder(this).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EventIDActivity.this, new String[]{
                            Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0) {
                    eventID.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
                            vibrator.vibrate(300);
                            eventID.setText(qrcodes.valueAt(0).displayValue);
                            cameraSource.stop();
                            getEventInfo();
                        }
                    });
                }
            }
        });

        enterEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEventInfo();
            }
        });
    }

    private void getEventInfo() {

        final View currentView = this.getCurrentFocus();
        if (currentView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        }
        loadingScreen.setVisibility(View.VISIBLE);
        loadingAnimation.playAnimation();
        loadingText.setText("Fetching Event Details");
        enterEventBtn.setVisibility(View.GONE);

        // Creating the retrofit instance
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        FetchAPI fetchAPI = retrofit.create(FetchAPI.class);

        // Network call for logging in
        Call<EventModel> eventInfo = fetchAPI.eventInfo(eventID.getText().toString());
        eventInfo.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                String success = response.body().getSuccess().toString();
                String message = response.body().getMessage();
                loadingScreen.setVisibility(View.INVISIBLE);
                loadingAnimation.pauseAnimation();
                enterEventBtn.setVisibility(View.VISIBLE);
                if (success.equals("false")) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EventIDActivity.this, new String[]{
                                Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(EventIDActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    Event currevent = response.body().getEvent();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(currevent);
                    editor.putString("EventDetails", json);
                    editor.putBoolean("InEvent", true);
                    editor.apply();
                    startActivity(new Intent(EventIDActivity.this, HomeActivity.class));
                    customType(EventIDActivity.this, "bottom-to-up");
                    finishAfterTransition();
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                enterEventBtn.setVisibility(View.VISIBLE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EventIDActivity.this, new String[]{
                            Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadingScreen.setVisibility(View.INVISIBLE);
                loadingAnimation.pauseAnimation();
                enterEventBtn.setVisibility(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(EventIDActivity.this, "An error occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
}
