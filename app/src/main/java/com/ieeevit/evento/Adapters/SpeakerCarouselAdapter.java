package com.ieeevit.evento.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.gtomato.android.ui.widget.CarouselView;
import com.ieeevit.evento.classes.Speaker;
import com.ieeevit.evento.R;


import java.util.List;

public class SpeakerCarouselAdapter extends CarouselView.Adapter<SpeakerCarouselAdapter.SpeakerCarouselViewHolder>{

    private final List<Speaker> speakerList;

    public SpeakerCarouselAdapter(List<Speaker> speakers) {
        this.speakerList = speakers;
    }

    @NonNull
    @Override
    public SpeakerCarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_speaker_carousel, parent, false);
        return new SpeakerCarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeakerCarouselViewHolder holder, int position) {

        Speaker speaker = speakerList.get(position);
        holder.speakerName.setText(speaker.getName());
        holder.speakerDescription.setText(speaker.getDescription());
        Glide.with(holder.circularAvatar.getContext())
                .load(speaker.getImageUrl())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .into(holder.circularAvatar);

    }

    @Override
    public int getItemCount() {
        return speakerList.size();
    }


    public class SpeakerCarouselViewHolder extends CarouselView.ViewHolder{

        final ImageView circularAvatar;
        final TextView speakerName;
        final TextView speakerDescription;

        SpeakerCarouselViewHolder(View itemView) {
            super(itemView);
            circularAvatar = itemView.findViewById(R.id.circle_avatar_image);
            speakerName = itemView.findViewById(R.id.tv_speaker_name);
            speakerDescription = itemView.findViewById(R.id.tv_speaker_description);
        }
    }

}