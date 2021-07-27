package com.faint.cucina.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Order implements Parcelable {

    public String name, phone, clarifications;
    public ArrayList<OrderDish> orderList;
    public int cafeID;

    public Order(String name, String phone,
                 ArrayList<OrderDish> orderList, String clarifications, int cafeID) {
        this.name = name;
        this.phone = phone;
        this.orderList = orderList;
        this.clarifications = clarifications;
        this.cafeID = cafeID;
    }

    @SuppressWarnings("unchecked")
    protected Order(Parcel in) {
        name = in.readString();
        phone = in.readString();
        orderList = in.readArrayList(OrderDish.class.getClassLoader());
        clarifications = in.readString();
        cafeID = in.readInt();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public ArrayList<OrderDish> getOrderList() {
        return orderList;
    }

    public String getClarifications() {
        return clarifications;
    }

    public void setClarifications(String clarifications) {
        this.clarifications = clarifications;
    }

    public void setCafeID(int cafeID) {
        this.cafeID = cafeID;
    }

    public int getCafeID() {
        return cafeID;
    }

    public void addDishToOrder(Dish dishToAdd) {
        boolean found = false;

        for(OrderDish dish : orderList) {
            if(dish.getName().equals(dishToAdd.getName())) {
                found = true;
                dish.setAmount(dish.getAmount() + 1);

                break;
            }
        }

        if(!found) {
            orderList.add(new OrderDish(dishToAdd.getName(), 1));
        }
    }

    public void removeDish(Dish dishToRemove) {
        for(OrderDish dish : orderList) {
            if(dish.getName().equals(dishToRemove.getName())) {
                dish.setAmount(dish.getAmount() - 1);

                if(dish.getAmount() == 0) {
                    orderList.remove(dish);
                }

                break;
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeList(orderList);
        parcel.writeString(clarifications);
        parcel.writeInt(cafeID);
    }
}
