package com.faint.cucina.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderDish implements Parcelable {

    int amount;
    String name;

    public OrderDish(int amount, String name) {
        this.amount = amount;
        this.name = name;
    }

    protected OrderDish(Parcel in) {
        amount = in.readInt();
        name = in.readString();
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
        parcel.writeInt(amount);
        parcel.writeString(name);
    }
}
