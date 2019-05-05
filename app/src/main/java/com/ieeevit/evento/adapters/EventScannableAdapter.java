package com.ieeevit.evento.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ieeevit.evento.R;
import com.ieeevit.evento.classes.Scannable;
import com.ieeevit.evento.classes.User;

import java.util.List;

public class EventScannableAdapter extends RecyclerView.Adapter<EventScannableAdapter.EventScannableViewHolder>{

    private List<Scannable> scannableList;

    public EventScannableAdapter(List<Scannable> scannableList) {
        this.scannableList = scannableList;
    }

    @NonNull
    @Override
    public EventScannableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_scannables, parent, false);
        return new EventScannableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventScannableViewHolder holder, int position) {
        Scannable data = scannableList.get(position);
        SharedPreferences sharedPreferences = holder.scannableName.getContext().getSharedPreferences("Details", Context.MODE_PRIVATE);
        String userString = sharedPreferences.getString("UserDetails", "userNotFound");
        Gson gson = new Gson();
        User user = gson.fromJson(userString, User.class);
        holder.scannableName.setText(data.getName());
        holder.scannableType.setText(data.getScannableType());
        holder.scannableDescription.setText(data.getDescription());
        for (int i = 0; i < data.getParticipantsPresent().size(); i++){
            if (data.getParticipantsPresent().get(i).equals(user.getId())) {
                holder.scannableStatus.setText("Scanned");
                holder.scannableStatus.setTextColor(Color.GREEN);
            }
        }
    }

    @Override
    public int getItemCount() {
        return scannableList.size();
    }

    class EventScannableViewHolder extends RecyclerView.ViewHolder{

        TextView scannableName, scannableType, scannableDescription, scannableStatus;

        EventScannableViewHolder(View itemView) {
            super(itemView);
            scannableName = itemView.findViewById(R.id.scannableName);
            scannableType = itemView.findViewById(R.id.scannableType);
            scannableDescription = itemView.findViewById(R.id.scannableDescription);
            scannableStatus = itemView.findViewById(R.id.scannableStatus);
        }
    }
}
