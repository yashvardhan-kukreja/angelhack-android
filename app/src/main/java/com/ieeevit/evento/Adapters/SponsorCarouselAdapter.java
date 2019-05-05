package com.ieeevit.evento.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.gtomato.android.ui.widget.CarouselView;
import com.ieeevit.evento.classes.Sponsor;
import com.ieeevit.evento.R;

import java.util.List;

public class SponsorCarouselAdapter extends CarouselView.Adapter<SponsorCarouselAdapter.SponsorCarouselViewHolder>{

    private List<Sponsor> sponsorList;

    public SponsorCarouselAdapter(List<Sponsor> sponsors) {
        this.sponsorList = sponsors;
    }

    @NonNull
    @Override
    public SponsorCarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_sponsor_carousel, parent, false);
        return new SponsorCarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SponsorCarouselViewHolder holder, int position) {
        Sponsor data = sponsorList.get(position);
        holder.sponsorName.setText(data.getName());
        Glide.with(holder.sponsorImage.getContext())
                .load(data.getImgUrl())
                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .into(holder.sponsorImage);
    }

    @Override
    public int getItemCount() {
        return sponsorList.size();
    }

    public class SponsorCarouselViewHolder extends RecyclerView.ViewHolder{

        TextView sponsorName;
        ImageView sponsorImage;

        SponsorCarouselViewHolder(View itemView) {
            super(itemView);
            sponsorImage = itemView.findViewById(R.id.iv_sponsor);
            sponsorName = itemView.findViewById(R.id.tv_sponsor_name);
        }
    }
}
