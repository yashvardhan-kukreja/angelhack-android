package com.ieeevit.evento.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.gtomato.android.ui.transformer.CoverFlowViewTransformer;
import com.gtomato.android.ui.widget.CarouselView;
import com.ieeevit.evento.adapters.SpeakerCarouselAdapter;
import com.ieeevit.evento.adapters.SponsorCarouselAdapter;
import com.ieeevit.evento.classes.Event;
import com.ieeevit.evento.R;
import com.ieeevit.evento.networkAPIs.FetchAPI;
import com.ieeevit.evento.networkmodels.EventModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class EventFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.fragment_event, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Details", MODE_PRIVATE);
        String eventString = sharedPreferences.getString("EventDetails", "eventNotFound");
        Gson gson = new Gson();
        final Event event = gson.fromJson(eventString, Event.class);

        TextView eventDescription = view.findViewById(R.id.tv_event_description);
        eventDescription.setText(event.getAbout());

        ImageView orgImage = view.findViewById(R.id.orgImage);

        Glide.with(getContext()).load(event.getEventLogoUrl())
                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .into(orgImage);

        SpeakerCarouselAdapter carouselAdapter = new SpeakerCarouselAdapter(event.getSpeakers());
        CarouselView speakerCarouselView = view.findViewById(R.id.speaker_carousel);
        speakerCarouselView.setTransformer(new CoverFlowViewTransformer());
        speakerCarouselView.setScrollingAlignToViews(true);
        speakerCarouselView.setAdapter(carouselAdapter);
        speakerCarouselView.setInfinite(true);

        SponsorCarouselAdapter sponsorCarouselAdapter = new SponsorCarouselAdapter(event.getSponsors());
        CarouselView sponsorCarouselView = view.findViewById(R.id.sponsor_carousel);
        sponsorCarouselView.setTransformer(new CoverFlowViewTransformer());
        sponsorCarouselView.setScrollingAlignToViews(true);
        sponsorCarouselView.setAdapter(sponsorCarouselAdapter);
        sponsorCarouselView.setInfinite(true);

        ((SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().findViewById(R.id.navigation).setVisibility(View.GONE);
                getEventInfo(event, view);
            }
        });

        return view;
    }

    private void getEventInfo(Event event, final View view) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        FetchAPI fetchAPI = retrofit.create(FetchAPI.class);

        Call<EventModel> eventInfo = fetchAPI.eventInfo(event.getEventId());
        eventInfo.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                ((SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh)).setRefreshing(false);
                getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);
                String success = response.body().getSuccess().toString();
                String message = response.body().getMessage();
                if (success.equals("false")) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    Event currevent = response.body().getEvent();
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("Details", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(currevent);
                    editor.putString("EventDetails", json);
                    editor.putBoolean("InEvent", true);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                ((SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh)).setRefreshing(false);
                getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(getContext(), "An error occured", Toast.LENGTH_LONG).show();
            }
        });
    }

}
