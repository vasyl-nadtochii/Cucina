package com.faint.cucina.login_register;

public class URLs {

    private static final String ROOT_URL = "https://cucinacafeapp.000webhostapp.com/registrationapi.php?apicall=";
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN = ROOT_URL + "login";

    public static final String URL_POST_ORDER = "https://cucinacafeapp.000webhostapp.com/postOrder.php";
    public static final String URL_POST_COMPLAINT = "https://cucinacafeapp.000webhostapp.com/postComplaint.php";
    public static final String URL_GET_ORDERS = "https://cucinacafeapp.000webhostapp.com/getUserOrders.php";

    public static final String URL_GET_CAFES = "https://cucinacafeapp.000webhostapp.com/getCafes.php";
    public static final String URL_GET_CAFE_BY_POS = "https://cucinacafeapp.000webhostapp.com/getCafeByLatLng.php";

    public static final String URL_REMOVE_ORDER = "https://cucinacafeapp.000webhostapp.com/removeOrder.php";

    public static final String URL_GET_NEWS = "https://cucinacafeapp.000webhostapp.com/getNews.php";
    public static final String URL_GET_DISHES = "https://cucinacafeapp.000webhostapp.com/getDishes.php";

    public static final String URL_CHANGE_ROOT = "https://cucinacafeapp.000webhostapp.com/changeUserData.php?apicall=";
    public static final String URL_CHANGE_NAME = URL_CHANGE_ROOT + "username";
    public static final String URL_CHANGE_PASSWORD = URL_CHANGE_ROOT + "password";
    public static final String URL_CHANGE_CITY = URL_CHANGE_ROOT + "city";
    public static final String URL_CHANGE_PHONE = URL_CHANGE_ROOT + "phone";

    public static final String URL_CHECK_TOKEN = "https://cucinacafeapp.000webhostapp.com/checkDeviceToken.php";
    public static final String URL_CHECK_REMOVED_DISHES = "https://cucinacafeapp.000webhostapp.com/checkRemovedDishes.php";
}
