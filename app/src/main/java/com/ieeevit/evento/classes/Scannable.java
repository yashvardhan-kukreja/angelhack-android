package com.ieeevit.evento.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Scannable implements Parcelable{

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("scannableType")
    @Expose
    private String scannableType;
    @SerializedName("participantsPresent")
    @Expose
    private List<String> participantsPresent = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("__v")
    @Expose
    private Integer v;

    private Scannable(Parcel in) {
        description = in.readString();
        scannableType = in.readString();
        participantsPresent = in.createStringArrayList();
        id = in.readString();
        name = in.readString();
        eventId = in.readString();
        if (in.readByte() == 0) {
            v = null;
        } else {
            v = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(scannableType);
        dest.writeStringList(participantsPresent);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(eventId);
        if (v == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(v);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Scannable> CREATOR = new Creator<Scannable>() {
        @Override
        public Scannable createFromParcel(Parcel in) {
            return new Scannable(in);
        }

        @Override
        public Scannable[] newArray(int size) {
            return new Scannable[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScannableType() {
        return scannableType;
    }

    public void setScannableType(String scannableType) {
        this.scannableType = scannableType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
