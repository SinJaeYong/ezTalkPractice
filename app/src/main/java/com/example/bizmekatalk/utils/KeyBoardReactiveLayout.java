package com.example.bizmekatalk.utils;

import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Vector;

public class KeyBoardReactiveLayout implements SoftKeyboard.SoftKeyboardChanged {

    private LinearLayout mainLayout;
    private List<LinearLayout> layoutList = new Vector<LinearLayout>();

    public KeyBoardReactiveLayout(LinearLayout mainLayout, List<LinearLayout> layoutList) {
        this.mainLayout = mainLayout;
        this.layoutList = layoutList;
    }

    @Override
    public void onSoftKeyboardHide() {

    }

    @Override
    public void onSoftKeyboardShow() {

    }

    public void buildReactiveLayout(LinearLayout.LayoutParams params){

    }
}
