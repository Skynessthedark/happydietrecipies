package com.happydieting.dev.constant;

public class ControllerConstant {
    public static final String REDIRECT = "redirect:";
    public static final String ERROR = "/error";
    public static final String HOME = "/";
    public static final String LOGIN = "/login";
    public static final String SIGNUP = "/signup";
    public static final String RECIPES = "/recipes";
    public static final String REDIRECT_HOME = REDIRECT + HOME;
    public static final String REDIRECT_RECIPES = REDIRECT + RECIPES;

    public class MyAccount{
        public static final String MY_ACCOUNT = "/me";
    }
}
