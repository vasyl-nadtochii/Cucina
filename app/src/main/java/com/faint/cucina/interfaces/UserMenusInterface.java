package com.faint.cucina.interfaces;

import com.faint.cucina.classes.UserMenu;

public interface UserMenusInterface {
    void addUserDishToOrder(UserMenu menu);
    void removeUserDishFromOrder(UserMenu menu);
}
