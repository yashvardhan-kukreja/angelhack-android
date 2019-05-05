package com.ieeevit.evento.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventSession implements Parcelable {

    @SerializedName("sessionType")
    @Expose
    private String sessionType;
    @SerializedName("participantsPresent")
    @Expose
    private List<String> participantsPresent = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("sessionDescription")
    @Expose
    private String sessionDescription;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("sessionId")
    @Expose
    private Integer sessionId;
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("__v")
    @Expose
    private Integer v;

    private EventSession(Parcel in) {
        sessionType = in.readString();
        participantsPresent = in.createStringArrayList();
        id = in.readString();
        sessionDescription = in.readString();
        name = in.readString();
        date = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        location = in.readString();
        if (in.readByte() == 0) {
            sessionId = null;
        } else {
            sessionId = in.readInt();
        }
        eventId = in.readString();
        if (in.readByte() == 0) {
            v = null;
        } else {
            v = in.readInt();
        }
    }

    public static final Creator<EventSession> CREATOR = new Creator<EventSession>() {
        @Override
        public EventSession createFromParcel(Parcel in) {
            return new EventSession(in);
        }

        @Override
        public EventSession[] newArray(int size) {
            return new EventSession[size];
        }
    };

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public List<String> getParticipantsPresent() {
        return participantsPresent;
    }

    public void setParticipantsPresent(List<String> participantsPresent) {
        this.participantsPresent = participantsPresent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionDescription() {
        return sessionDescription;
    }

    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sessionType);
        dest.writeStringList(participantsPresent);
        dest.writeString(id);
        dest.writeString(sessionDescription);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(location);
        if (sessionId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sessionId);
        }
        dest.writeString(eventId);
        if (v == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(v);
        }
    }
}