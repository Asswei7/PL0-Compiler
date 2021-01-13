package com.naming.test;

/**
 * @author Asswei
 */
public class NamingTest {
    private static final char UNDER_LINE = '_';
    private static final char DOLLAR = '$';
    private static final int ASCII_A = 65;
    private static final int ASCII_Z = 90;
    boolean test4BeginEnd(String str){
        int len = str.length();
        char[] chars = str.toCharArray();
        boolean flag = true;
        if(chars[0]==DOLLAR || chars[len-1]==DOLLAR || chars[0]==UNDER_LINE || chars[len-1]==UNDER_LINE){
            flag = false;
        }
        return flag;
    }

    boolean test4PackageName(String str){
        if(!test4BeginEnd(str)) {
            return false;
        }
        //包名必须全小写字母
        int len = str.length();
        for(int i=0;i<len-1;i++){
            if(Character.isUpperCase(str.charAt(i)) ){
                return false;
            }
        }
        return true;
    }

    boolean isLowerFirst(String str){
        int num = str.charAt(0);
        return num < ASCII_A || num > ASCII_Z;
    }
    boolean isUpperFirst(String str){
        int num = str.charAt(0);
        return num >= ASCII_A && num <= ASCII_Z;
    }

}
