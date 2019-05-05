package com.ieeevit.evento.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ScannableSessionList implements Parcelable{

    private List<Scannable> scannables;

    protected ScannableSessionList(Parcel in) {
        scannables = in.createTypedArrayList(Scannable.CREATOR);
    }

    public static final Creator<ScannableSessionList> CREATOR = new Creator<ScannableSessionList>() {
        @Override
        public ScannableSessionList createFromParcel(Parcel in) {
            return new ScannableSessionList(in);
        }

        @Override
        public ScannableSessionList[] newArray(int size) {
            return new ScannableSessionList[size];
        }
    };

    public List<Scannable> getScannables() {
        return scannables;
    }

    public void setScannables(List<Scannable> scannables) {
        this.scannables = scannables;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(scannables);
    }

    public ScannableSessionList(List<Scannable> scannables) {
        this.scannables = scannables;
    }

    public ScannableSessionList() {
    }
}
