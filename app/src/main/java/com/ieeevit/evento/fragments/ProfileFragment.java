package com.ieeevit.evento.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.ieeevit.evento.activities.ZoomedImageActivity;
import com.ieeevit.evento.activities.HomeActivity;
import com.ieeevit.evento.activities.LoginActivity;
import com.ieeevit.evento.classes.Event;
import com.ieeevit.evento.classes.EventSession;
import com.ieeevit.evento.classes.User;
import com.ieeevit.evento.R;
import com.ieeevit.evento.classes.WifiCouponHistory;
import com.ieeevit.evento.networkAPIs.AuthAPI;
import com.ieeevit.evento.networkAPIs.FetchAPI;
import com.ieeevit.evento.networkmodels.UserEventModel;
import com.ieeevit.evento.networkmodels.UserModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    private final String QR_BASE_URL = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=";
    private SharedPreferences sharedPreferences;
    private TextView nameTextView, dayTextView, hourTextView, minuteTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("Details", Context.MODE_PRIVATE);
        String userString = sharedPreferences.getString("UserDetails", "userNotFound");

        boolean isRegistered = sharedPreferences.getBoolean("IsRegistered", false);
        boolean isCoordinator = sharedPreferences.getBoolean("IsCoordinator", false);

        if (userString.equals("userNotFound")) {
            return getUserDetails(inflater, container);
        } else if (isRegistered && !isCoordinator) {
            Gson gson = new Gson();
            String encryptedId = sharedPreferences.getString("encryptedId", null);
            User user = gson.fromJson(userString, User.class);
            return setUserDetails(inflater, container, user, encryptedId);
        } else {
            Gson gson = new Gson();
            User user = gson.fromJson(userString, User.class);
            String encryptedId = sharedPreferences.getString("encryptedId", null);
            return setRegistrationDetails(inflater, container, user, isCoordinator, encryptedId);
        }

    }

    private View setUserDetails(final LayoutInflater inflater, final ViewGroup container, final User user, final String qrString) {

        final View view = inflater.inflate(R.layout.fragment_profile_logged_in, container, false);

        Glide.with(this)
                .load(QR_BASE_URL + qrString)
                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .into((ImageView) view.findViewById(R.id.qr_image_view));
        nameTextView = view.findViewById(R.id.tv_name);
        nameTextView.setText(user.getName());
        dayTextView = view.findViewById(R.id.daysRemaining);
        hourTextView = view.findViewById(R.id.hoursRemaining);
        minuteTextView = view.findViewById(R.id.minutesRemaining);

        String eventString = sharedPreferences.getString("EventDetails", "eventNotFound");
        Gson gson = new Gson();
        final Event event = gson.fromJson(eventString, Event.class);

        List<EventSession> mealSessions = new ArrayList<EventSession>();
        List<EventSession> allSessions = event.getEventSessions();
        for (int i = 0; i < allSessions.size(); i++) {
            if (allSessions.get(i).getSessionType().equals("Meal"))
                mealSessions.add(allSessions.get(i));
        }

        List<WifiCouponHistory> wifiCouponHistories = user.getWifiCouponHistory();
        WifiCouponHistory currentCoupon = null;
        for (int i = 0; i < wifiCouponHistories.size(); i++) {
            WifiCouponHistory coupon = wifiCouponHistories.get(i);
            if (coupon.getEventId().equals(event.getEventId())) {
                currentCoupon = coupon;
            }
        }

        if (currentCoupon != null) {
            ((TextView) view.findViewById(R.id.wifiId)).setText(currentCoupon.getCouponId());
            ((TextView) view.findViewById(R.id.wifiPassword)).setText(currentCoupon.getCouponPassword());
        }

        ((SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().findViewById(R.id.navigation).setVisibility(View.GONE);
                getUser(view);
            }
        });

        view.findViewById(R.id.qr_image_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZoomedImageActivity.class);
                intent.putExtra("QrData", qrString);
                startActivity(intent);
                customType(getContext(), "bottom-to-up");
                getActivity().finishAfterTransition();
            }
        });



        setTimer(view);
        return view;
    }

    private View getUserDetails(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_profile_logged_out, container, false);

        nameTextView = view.findViewById(R.id.tv_name);
        nameTextView.setText("Hit Login to get your cool QR Code");
        view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                customType(v.getContext(), "up-to-bottom");
                getActivity().finishAfterTransition();
            }
        });
        return view;
    }

    private View setRegistrationDetails(LayoutInflater inflater, ViewGroup container, User user, boolean isCoordinator, String qrString) {
        View view = inflater.inflate(R.layout.fragment_profile_not_registered, container, false);

        String eventString = sharedPreferences.getString("EventDetails", "eventNotFound");
        Gson eventGson = new Gson();
        Event event = eventGson.fromJson(eventString, Event.class);

        nameTextView = view.findViewById(R.id.tv_name);
        nameTextView.setText(user.getName());
        if (isCoordinator) {
            view.findViewById(R.id.qrCard).setVisibility(View.GONE);
            view.findViewById(R.id.sadFace).setVisibility(View.VISIBLE);
            TextView message = view.findViewById(R.id.messagePrompt);
            message.setText("Seems like you are Coordinator for this Event");
            TextView suggestion = view.findViewById(R.id.suggestionPrompt);
            suggestion.setText("Please Login from the\nAdmin App");
        } else {
            view.findViewById(R.id.qrCard).setVisibility(View.VISIBLE);
            view.findViewById(R.id.sadFace).setVisibility(View.GONE);

            Glide.with(this)
                    .load(QR_BASE_URL + qrString)
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into((ImageView) view.findViewById(R.id.qr_image_view));

            TextView message = view.findViewById(R.id.messagePrompt);
            message.setText("You haven't registered for this event");
            TextView suggestion = view.findViewById(R.id.suggestionPrompt);
            suggestion.setText("Logout and Login again after\nRegistering for Event");
        }

        return view;
    }


    private void setTimer(final View view) {
        sharedPreferences = getContext().getSharedPreferences("Details", Context.MODE_PRIVATE);
        String eventString = sharedPreferences.getString("EventDetails", "eventNotFound");
        Event event;
        long diff = 0;
        if (eventString.equals("eventNotFound")) {
            Toast.makeText(getContext(), "Event Not Found", Toast.LENGTH_SHORT).show();
            event = null;
        } else {
            Gson gson = new Gson();
            event = gson.fromJson(eventString, Event.class);
        }

        if (event != null) {
            EventSession session = event.getEventSessions().get(0);
            String date = session.getDate();
            String time = session.getStartTime();
            String eventStartString = date + " " + time;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            try {
                Date eventStart = dateFormat.parse(eventStartString);
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                diff = eventStart.getTime() - currentDate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (diff > 0) {
                view.findViewById(R.id.timerView).setVisibility(View.VISIBLE);
                view.findViewById(R.id.additionalDetails).setVisibility(View.GONE);
                CountDownTimer countDownTimer = new CountDownTimer(diff, 60000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String diffMinutes = String.valueOf(millisUntilFinished / (60 * 1000) % 60);
                        String diffHours = String.valueOf(millisUntilFinished / (60 * 60 * 1000) % 24);
                        String diffInDays = String.valueOf((int) millisUntilFinished / (1000 * 60 * 60 * 24));

                        if (String.valueOf(diffMinutes).length() == 1) {
                            diffMinutes = "0" + diffMinutes;
                        }
                        if (String.valueOf(diffHours).length() == 1) {
                            diffHours = "0" + diffHours;
                        }
                        if (String.valueOf(diffInDays).length() == 1) {
                            diffInDays = "0" + diffInDays;
                        }

                        minuteTextView.setText(String.valueOf(diffMinutes));
                        hourTextView.setText(String.valueOf(diffHours));
                        dayTextView.setText(String.valueOf(diffInDays));
                    }

                    @Override
                    public void onFinish() {
                        view.findViewById(R.id.timerView).setVisibility(View.GONE);
                        view.findViewById(R.id.additionalDetails).setVisibility(View.VISIBLE);
                    }
                };
                countDownTimer.start();
            } else {
                view.findViewById(R.id.timerView).setVisibility(View.GONE);
                view.findViewById(R.id.additionalDetails).setVisibility(View.VISIBLE);
                }
        }
    }

    private void getUser(final View view){
        String token = sharedPreferences.getString("token", "");
        // Creating the retrofit instance
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        FetchAPI fetchAPI = retrofit.create(FetchAPI.class);
        // Network call for logging in
        Call<UserModel> userInfo = fetchAPI.userInfo(token);
        userInfo.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                String success = response.body().getSuccess().toString();
                String message = response.body().getMessage();
                if (success.equals("false")){
                    ((SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh)).setRefreshing(false);
                    getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    saveUserDetails(response, view);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);
                ((SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh)).setRefreshing(false);
                t.printStackTrace();
                Toast.makeText(getContext(), "An error occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveUserDetails(Response<UserModel> response, final View view){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        User currUser = response.body().getUser();
        Gson gson = new Gson();
        String json = gson.toJson(currUser);
        editor.putString("UserDetails", json);
        editor.apply();
        loginEvent(view);
    }

    private void loginEvent(final View view){
        String eventString = sharedPreferences.getString("EventDetails", "eventNotFound");
        Gson gson = new Gson();
        final Event currentEvent = gson.fromJson(eventString, Event.class);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        AuthAPI authAPI = retrofit.create(AuthAPI.class);
        // Network call for logging in
        Call<UserEventModel> event = authAPI.eventLogin(sharedPreferences.getString("token", ""), currentEvent.getEventId());
        event.enqueue(new Callback<UserEventModel>() {
            @Override
            public void onResponse(Call<UserEventModel> call, Response<UserEventModel> response) {
                ((SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh)).setRefreshing(false);
                getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);
                String success = response.body().getSuccess().toString();
                if (success.equals("false")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("IsRegistered", false);
                    editor.putBoolean("IsCoordinator", false);
                    editor.apply();
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    customType(getContext(), "fadein-to-fadeout");
                    getActivity().finishAfterTransition();
                } else {
                    String encryptedId = response.body().getEncryptedId();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("encryptedId", encryptedId);
                    editor.putBoolean("IsRegistered", true);
                    editor.putBoolean("IsCoordinator", response.body().getIsCoordinator());
                    editor.apply();
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    customType(getContext(), "fadein-to-fadeout");
                    getActivity().finishAfterTransition();
                }
            }

            @Override
            public void onFailure(Call<UserEventModel> call, Throwable t) {
                ((SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh)).setRefreshing(false);
                getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(getContext(), "An error occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String prettyDate(String uglyDate) {
        String prettyDate = uglyDate.substring(0, 2) + " ";
        String uglyMonth = uglyDate.substring(3, 5);
        String prettyMonth;
        switch (uglyMonth) {
            case "01":
                prettyMonth = "Jan";
                break;
            case "02":
                prettyMonth = "Feb";
                break;
            case "03":
                prettyMonth = "Mar";
                break;
            case "04":
                prettyMonth = "Apr";
                break;
            case "05":
                prettyMonth = "May";
                break;
            case "06":
                prettyMonth = "Jun";
                break;
            case "07":
                prettyMonth = "Jul";
                break;
            case "08":
                prettyMonth = "Aug";
                break;
            case "09":
                prettyMonth = "Sep";
                break;
            case "10":
                prettyMonth = "Oct";
                break;
            case "11":
                prettyMonth = "Nov";
                break;
            case "12":
                prettyMonth = "Dec";
                break;
            default:
                prettyMonth = "";
        }
        return prettyDate + prettyMonth + ", " + uglyDate.substring(6, 10);
    }
}
