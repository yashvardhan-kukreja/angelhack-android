package com.ieeevit.evento.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ieeevit.evento.R;
import com.ieeevit.evento.activities.LoginActivity;
import com.ieeevit.evento.adapters.EventScannableAdapter;
import com.ieeevit.evento.classes.Event;
import com.ieeevit.evento.classes.ScannableSessionList;
import com.ieeevit.evento.networkAPIs.FetchAPI;
import com.ieeevit.evento.networkmodels.ScannableModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class ScannableFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Details", Context.MODE_PRIVATE);
        String scannableString = sharedPreferences.getString("EventScannables", "scannableNotFound");

        if (!scannableString.equals("scannableNotFound")) {
            final View view = inflater.inflate(R.layout.fragment_scannable, container, false);
            RecyclerView recyclerView = view.findViewById(R.id.scannablesRecyclerView);
            Gson gson = new Gson();
            ScannableSessionList scannableSessionList = gson.fromJson(scannableString, ScannableSessionList.class);
            recyclerView.setHasFixedSize(true);
            EventScannableAdapter adapter = new EventScannableAdapter(scannableSessionList.getScannables());
            recyclerView.setAdapter(adapter);

            ((SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getActivity().findViewById(R.id.navigation).setVisibility(View.GONE);
                    getEventScannables(view);
                }
            });
            return view;
        }
        else {
            View view = inflater.inflate(R.layout.fragment_profile_logged_out, container, false);
            TextView nameTextView = view.findViewById(R.id.tv_name);
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
    }

    private void getEventScannables(final View view) {

        // Creating the retrofit instance
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        FetchAPI fetchAPI = retrofit.create(FetchAPI.class);

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("Details", Context.MODE_PRIVATE);
        String eventString = sharedPreferences.getString("EventDetails", "eventNotFound");
        Gson gson = new Gson();
        final Event event = gson.fromJson(eventString, Event.class);

        Call<ScannableModel> eventScannables = fetchAPI.eventScannables(event.getEventId());
        eventScannables.enqueue(new Callback<ScannableModel>() {
            @Override
            public void onResponse(Call<ScannableModel> call, Response<ScannableModel> response) {
                ((SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh)).setRefreshing(false);
                getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);
                String success = response.body().getSuccess().toString();
                String message = response.body().getMessage();
                if (success.equals("false")) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    ScannableSessionList scannableList = new ScannableSessionList(response.body().getScannables());
                    String json = gson.toJson(scannableList);
                    editor.putString("EventScannables", json);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<ScannableModel> call, Throwable t) {
                ((SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh)).setRefreshing(false);
                getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(getContext(), "An error occured", Toast.LENGTH_LONG).show();
            }
        });
    }
}
