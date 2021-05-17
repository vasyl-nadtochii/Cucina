package com.faint.cucina.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Dish implements Parcelable {

    String name;
    int img;

    public Dish(String name, int img) {
        this.name = name;
        this.img = img;
    }

    protected Dish(Parcel in) {
        name = in.readString();
        img = in.readInt();
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getImg() {
        return img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(img);
    }
}
