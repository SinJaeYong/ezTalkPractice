package com.example.bizmekatalk.utils;


import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Validation {

    public static <T extends EditText> boolean validateLogin(T... ts) {
        return !Arrays.asList(ts).stream().map(t -> t.getText().toString().trim()).equals("");
    }//loginValidation

    public static <T extends EditText> boolean validatePin(T... ts) {
        String pin1 = ts[0].getText().toString();
        String pin2 = ts[1].getText().toString();

        return pin1.equals(pin2);
    }//pinValidation

    public static boolean isNumber(String str) {
        for (Character c : str.toCharArray()) {
            if(Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }//isNumber


}//Validate
