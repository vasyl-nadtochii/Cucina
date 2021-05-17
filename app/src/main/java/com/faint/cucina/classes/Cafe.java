package com.faint.cucina.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Cafe implements Parcelable {

    private double latitude, longitude;
    private boolean isOpened;
    private String address;

    public Cafe(double latitude, double longitude, boolean isOpened, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOpened = isOpened;
        this.address = address;
    }

    protected Cafe(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        isOpened = in.readByte() != 0;
        address = in.readString();
    }

    public static final Creator<Cafe> CREATOR = new Creator<Cafe>() {
        @Override
        public Cafe createFromParcel(Parcel in) {
            return new Cafe(in);
        }

        @Override
        public Cafe[] newArray(int size) {
            return new Cafe[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeByte((byte) (isOpened ? 1 : 0));
        parcel.writeString(address);
    }
}
