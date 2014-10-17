package ru.dev_server.client.utils;

/**
 * .
 */
public class Utils {

    public static String concat(String s1,String s2){
        return s1+s2;
    }
    public static  String trunc(String s1, int length){
        if(s1==null){
            return "";
        }
        if(s1.length()>length&&length<=2){
            return s1.substring(0,length);
        }
        if(s1.length()>length){
            return s1.substring(0,length-2)+"..";
        }
        return s1;
    }
}
