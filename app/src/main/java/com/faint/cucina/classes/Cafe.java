package com.faint.cucina.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Cafe implements Parcelable {

    private final double latitude;
    private final double longitude;
    private final int state;
    private final String address;
    private final int cafeID;
    private final ArrayList<String> imgUrls;

    public Cafe(double latitude, double longitude, int state, String address, int cafeID, ArrayList<String> imgUrls) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
        this.address = address;
        this.cafeID = cafeID;
        this.imgUrls = imgUrls;
    }

    @SuppressWarnings("unchecked")
    protected Cafe(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        state = in.readInt();
        address = in.readString();
        cafeID = in.readInt();
        imgUrls = in.readArrayList(String.class.getClassLoader());  // ?
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

    public int getState() {
        return state;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getCafeID() {
        return cafeID;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeInt(state);
        parcel.writeString(address);
        parcel.writeInt(cafeID);
        parcel.writeList(imgUrls);
    }
}
