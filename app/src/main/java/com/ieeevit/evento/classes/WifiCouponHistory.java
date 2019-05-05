package com.ieeevit.evento.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WifiCouponHistory implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("coupon_password")
    @Expose
    private String couponPassword;
    @SerializedName("event_id")
    @Expose
    private String eventId;

    private WifiCouponHistory(Parcel in) {
        id = in.readString();
        couponId = in.readString();
        couponPassword = in.readString();
        eventId = in.readString();
    }

    public static final Creator<WifiCouponHistory> CREATOR = new Creator<WifiCouponHistory>() {
        @Override
        public WifiCouponHistory createFromParcel(Parcel in) {
            return new WifiCouponHistory(in);
        }

        @Override
        public WifiCouponHistory[] newArray(int size) {
            return new WifiCouponHistory[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponPassword() {
        return couponPassword;
    }

    public void setCouponPassword(String couponPassword) {
        this.couponPassword = couponPassword;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(couponId);
        dest.writeString(couponPassword);
        dest.writeString(eventId);
    }
}