package com.faint.cucina.classes;

import java.util.ArrayList;

public class UserMenu {

    String name;
    ArrayList<OrderDish> dishes;

    public UserMenu(String name, ArrayList<OrderDish> dishes) {
        this.name = name;
        this.dishes = dishes;
    }

    public String getName() {
        return name;
    }

    public ArrayList<OrderDish> getDishes() {
        return dishes;
    }
}
