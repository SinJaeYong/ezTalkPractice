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
        String pin1 = ts[0].getText().toString();
        String pin2 = ts[1].getText().toString();

        return pin1.equals(pin2);
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
