package com.faint.cucina.classes;

import java.util.ArrayList;

public class UserMenu {

    private final String id;
    private String name;
    private ArrayList<OrderDish> dishes;

    public UserMenu(String id, String name, ArrayList<OrderDish> dishes) {
        this.id = id;
        this.name = name;
        this.dishes = dishes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<OrderDish> getDishes() {
        return dishes;
    }

    public void addDish(Dish dishToAdd) {
        boolean found = false;

        for(OrderDish dish : dishes) {
            if(dish.getName().equals(dishToAdd.getName())) {
                found = true;
                dish.setAmount(dish.getAmount() + 1);

                break;
            }
        }

        if(!found) {
            dishes.add(new OrderDish(1, dishToAdd.getName(), dishToAdd.getPrice()));
        }
    }

    public String getID() {
        return id;
    }

    public void setDishes(ArrayList<OrderDish> dishes) {
        this.dishes = dishes;
    }

    public void removeDish(Dish dishToRemove) {
        for(OrderDish dish : dishes) {
            if(dish.getName().equals(dishToRemove.getName())) {
                dish.setAmount(dish.getAmount() - 1);

                if(dish.getAmount() == 0) {
                    dishes.remove(dish);
                }

                break;
            }
        }
    }
}
