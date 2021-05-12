package com.nure.ua.util;

public class Attribute {
    public static String composeAttribute(String key, String value) {
        return key + ':' + value + ';';
    }

    public static String getAttribute(String str, String key) {
        int index = str.indexOf(key);
        if(index != -1){
            int beginIndex = index + key.length() + 1;
            return str.substring(beginIndex, str.indexOf(';', beginIndex));
        }
        return null;
    }
}
