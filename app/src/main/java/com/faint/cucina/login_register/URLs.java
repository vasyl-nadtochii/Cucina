package com.faint.cucina.login_register;


public class URLs {
    private static final String ROOT_URL = "http://192.168.1.8/cucina/registrationapi.php?apicall=";
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN = ROOT_URL + "login";

    public static final String URL_POST_ORDER = "http://192.168.1.8/cucina/postOrder.php";

    public static final String URL_CHANGE_ROOT = "http://192.168.1.8/cucina/changeUserData.php?apicall=";
    public static final String URL_CHANGE_NAME = URL_CHANGE_ROOT + "username";
    public static final String URL_CHANGE_PASSWORD = URL_CHANGE_ROOT + "password";
}
