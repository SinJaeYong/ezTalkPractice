package com.example.bizmekatalk.utils;


import android.widget.EditText;


public class Validation {

    public static <T extends EditText> boolean validateLogin(T... ts) {
        for (T t : ts) {
            String text = t.getText().toString().trim();
            if ("".equals(text)) {
                return false;
            }
        }
        return true;
    }//loginValidation

    public static <T extends EditText> boolean validatePin(T... ts) {
        String text = ts[0].getText().toString();
        String text1 = ts[1].getText().toString();
        if (text.length()<4||!isNumber(text)||!(text1.equals(text))) {
            return false;
        }
        return true;
    }//pinValidation

    public static boolean isNumber(String str) {
        for (Character c : str.toCharArray()) {
            if(!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }//isNumber


}//Validate
