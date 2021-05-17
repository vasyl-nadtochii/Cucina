package com.faint.cucina.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

// can be also used for ready-made menus
public class DishGroup implements Parcelable {

    String name;
    ArrayList<Dish> dishes;

    public DishGroup(String name, ArrayList<Dish> dishes) {
        this.name = name;
        this.dishes = dishes;
    }

    @SuppressWarnings("unchecked")
    protected DishGroup(Parcel in) {
        name = in.readString();
        dishes = in.readArrayList(Dish.class.getClassLoader());
    }

    public static final Creator<DishGroup> CREATOR = new Creator<DishGroup>() {
        @Override
        public DishGroup createFromParcel(Parcel in) {
            return new DishGroup(in);
        }

        @Override
        public DishGroup[] newArray(int size) {
            return new DishGroup[size];
        }
    };

    public String getName() {
        return name;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeList(dishes);
    }
}
