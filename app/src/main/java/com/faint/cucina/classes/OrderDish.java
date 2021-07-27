package com.faint.cucina.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderDish implements Parcelable {

    String name;
    int amount;

    public OrderDish(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    protected OrderDish(Parcel in) {
        name = in.readString();
        amount = in.readInt();
    }

    public static final Creator<OrderDish> CREATOR = new Creator<OrderDish>() {
        @Override
        public OrderDish createFromParcel(Parcel in) {
            return new OrderDish(in);
        }

        @Override
        public OrderDish[] newArray(int size) {
            return new OrderDish[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(amount);
    }
}
