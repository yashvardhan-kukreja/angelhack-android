package com.ieeevit.evento.networkmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ieeevit.evento.classes.Event;

public class EventModel {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("event")
    @Expose
    private Event event;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
