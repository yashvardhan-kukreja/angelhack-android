package com.ieeevit.evento.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointOfContact implements Parcelable{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("email")
    @Expose
    private String email;

    private PointOfContact(Parcel in) {
        id = in.readString();
        name = in.readString();
        contact = in.readString();
        email = in.readString();
    }

    public static final Creator<PointOfContact> CREATOR = new Creator<PointOfContact>() {
        @Override
        public PointOfContact createFromParcel(Parcel in) {
            return new PointOfContact(in);
        }

        @Override
        public PointOfContact[] newArray(int size) {
            return new PointOfContact[size];
        }
    };

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(contact);
        dest.writeString(email);
    }
}
