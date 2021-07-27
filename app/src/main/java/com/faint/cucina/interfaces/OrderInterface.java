package com.faint.cucina.interfaces;

import com.faint.cucina.classes.Dish;

public interface OrderInterface {
    void addDishToOrder(Dish dish);
    void removeDishFromOrder(Dish dish);
    void showHideFAB(boolean show);
}
