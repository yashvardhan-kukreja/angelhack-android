package com.ieeevit.evento.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ieeevit.evento.R;
import com.ieeevit.evento.classes.Faq;

import java.util.List;
import java.util.zip.Inflater;

public class EventFaqAdapter extends RecyclerView.Adapter<EventFaqAdapter.EventFaqViewHolder>{

    List<Faq> faqList;

    public EventFaqAdapter(List<Faq> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public EventFaqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_faq, parent, false);
        return new EventFaqViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventFaqViewHolder holder, int position) {
        Faq data = faqList.get(position);
        holder.faqQuestion.setText(data.getQuestion());
        holder.faqAnswer.setText(data.getAnswer());
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    class EventFaqViewHolder extends RecyclerView.ViewHolder{

        TextView faqQuestion, faqAnswer;

        EventFaqViewHolder(View itemView) {
            super(itemView);
            faqQuestion = itemView.findViewById(R.id.faqQuestion);
            faqAnswer = itemView.findViewById(R.id.faqAnswer);
        }
    }

}
