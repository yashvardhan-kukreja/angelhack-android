package com.ieeevit.evento.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ieeevit.evento.classes.EventSession;
import com.ieeevit.evento.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventSessionsAdapter extends RecyclerView.Adapter<EventSessionsAdapter.EventSessionsViewHolder>{

    private final List<EventSession> sessionList;

    public EventSessionsAdapter(List<EventSession> sessions) {
        this.sessionList = sessions;
    }

    @NonNull
    @Override
    public EventSessionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_timeline, parent, false);
        return new EventSessionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventSessionsViewHolder holder, int position) {
        EventSession session = sessionList.get(position);
        holder.dateTextView.setText(prettyDate(session.getDate()));
        holder.timeTextView.setText(session.getStartTime());
        holder.sessionNameTextView.setText(session.getName());
        holder.locationTextView.setText(session.getLocation());
        if (session.getSessionDescription().length() == 0){
            holder.descriptionTextView.setVisibility(View.GONE);
        }
        else {
            String eventDescription = session.getSessionDescription();
            eventDescription = eventDescription.replace("\\n", "\n");
            holder.descriptionTextView.setVisibility(View.VISIBLE);
            holder.descriptionTextView.setText(eventDescription);
        }
        String date = session.getDate();
        String startTime = session.getStartTime();
        String endTime = session.getEndTime();
        if (endTime.equals("-")){
            endTime = "11:59 PM";
        }
        String eventStartString = date + " " + startTime;
        String eventEndString = date + " " + endTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        try {
            Date eventStart = dateFormat.parse(eventStartString);
            Date eventEnd = dateFormat.parse(eventEndString);
            Calendar calendar = Calendar.getInstance();
            Date currentTime = calendar.getTime();

            if (eventStart.compareTo(currentTime) > 0) {
                holder.sessionCircle.setImageDrawable(holder.sessionCircle.getContext().getDrawable(R.drawable.ic_timeline_circle));
            }
            else if (eventStart.compareTo(currentTime) < 0 && eventEnd.compareTo(currentTime) > 0){
                holder.sessionCircle.setImageDrawable(holder.sessionCircle.getContext().getDrawable(R.drawable.ic_timeline_circle_current));
            }
            else if (eventEnd.compareTo(currentTime) < 0 ){
                holder.sessionCircle.setImageDrawable(holder.sessionCircle.getContext().getDrawable(R.drawable.ic_timeline_circle_passed));
            } else {
                holder.sessionCircle.setImageDrawable(holder.sessionCircle.getContext().getDrawable(R.drawable.ic_timeline_circle));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }


    private String prettyDate(String uglyDate){
        String prettyDate = uglyDate.substring(0, 2) + " ";
        String uglyMonth = uglyDate.substring(3, 5);
        String prettyMonth = null;
        switch (uglyMonth){
            case "01": prettyMonth = "Jan"; break;
            case "02": prettyMonth = "Feb"; break;
            case "03": prettyMonth = "Mar"; break;
            case "04": prettyMonth = "Apr"; break;
            case "05": prettyMonth = "May"; break;
            case "06": prettyMonth = "Jun"; break;
            case "07": prettyMonth = "Jul"; break;
            case "08": prettyMonth = "Aug"; break;
            case "09": prettyMonth = "Sep"; break;
            case "10": prettyMonth = "Oct"; break;
            case "11": prettyMonth = "Nov"; break;
            case "12": prettyMonth = "Dec"; break;
            default: prettyMonth = "";
        }
        return  prettyDate + prettyMonth + ", " + uglyDate.substring(6, 10);
    }

    public class EventSessionsViewHolder extends RecyclerView.ViewHolder{

        TextView dateTextView;
        TextView timeTextView;
        TextView sessionNameTextView;
        TextView locationTextView;
        TextView descriptionTextView;
        final ImageView sessionCircle;

        EventSessionsViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.tv_date);
            timeTextView = itemView.findViewById(R.id.tv_time);
            sessionNameTextView = itemView.findViewById(R.id.tv_session_name);
            descriptionTextView = itemView.findViewById(R.id.tv_description);
            locationTextView = itemView.findViewById(R.id.tv_session_location);
            sessionCircle = itemView.findViewById(R.id.sessionCircle);
        }
    }

}
