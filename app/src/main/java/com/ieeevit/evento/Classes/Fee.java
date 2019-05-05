package com.ieeevit.evento.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fee implements Parcelable{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("description")
    @Expose
    private String description;

    private Fee(Parcel in) {
        id = in.readString();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readInt();
        }
        description = in.readString();
    }

    public static final Creator<Fee> CREATOR = new Creator<Fee>() {
        @Override
        public Fee createFromParcel(Parcel in) {
            return new Fee(in);
        }

        @Override
        public Fee[] newArray(int size) {
            return new Fee[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        if (amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(amount);
        }
        dest.writeString(description);
    }
}
