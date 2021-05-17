package com.nure.ua;

import java.util.regex.Pattern;

public class Utility {
    public final static String FXML_PATH_AUTH = "/static/auth.fxml";
    public final static String FXML_PATH_REG = "/static/register.fxml";
    public final static String FXML_PATH_MAIN = "/static/main-page.fxml";

    private final static Pattern REGEX_LOGIN = Pattern.compile("^[A-Za-z0-9_-]{2,20}$");
    private final static Pattern REGEX_PASSWORD = Pattern.compile("(?=.*[A-Za-z_-])(?=.*\\d)[A-Za-z0-9_-]{8,30}$");

    private final static Pattern REGEX_NAME = Pattern.compile("^(?=.{5,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");

    static public boolean matchesLogin(String str) {
        return str.matches(REGEX_LOGIN.pattern());
    }

    static public boolean matchesPassword(String str) {
        return str.matches(REGEX_PASSWORD.pattern());
    }

    static public boolean matchesName(String str) {
        return str.matches(REGEX_NAME.pattern());
    }
}
