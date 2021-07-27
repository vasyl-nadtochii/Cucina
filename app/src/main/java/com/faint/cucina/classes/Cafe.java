package com.faint.cucina.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Cafe implements Parcelable {

    private final double latitude;
    private final double longitude;
    private final boolean isOpened;
    private final String address;
    private final int cafeID;

    public Cafe(double latitude, double longitude, boolean isOpened, String address, int cafeID) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOpened = isOpened;
        this.address = address;
        this.cafeID = cafeID;
    }

    protected Cafe(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        isOpened = in.readByte() != 0;
        address = in.readString();
        cafeID = in.readInt();
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

    public int getCafeID() {
        return cafeID;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeByte((byte) (isOpened ? 1 : 0));
        parcel.writeString(address);
        parcel.writeInt(cafeID);
    }
}
