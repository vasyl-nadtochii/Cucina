package com.faint.cucina.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Dish implements Parcelable {

    String name, desc, imgUrl;
    int price;

    public Dish(String name, String desc, String imgUrl, int price) {
        this.name = name;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.price = price;
    }

    protected Dish(Parcel in) {
        name = in.readString();
        desc = in.readString();
        imgUrl = in.readString();
        price = in.readInt();
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(desc);
        parcel.writeString(imgUrl);
        parcel.writeInt(price);
    }
}
