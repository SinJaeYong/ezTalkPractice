package com.example.bizmekatalk.utils;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SoftKeyboard implements View.OnFocusChangeListener {
    private static final int CLEAR_FOCUS = 0;

    private ViewGroup layout;
    private int layoutBottom;
    private InputMethodManager im;
    private int[] coords;
    private boolean isKeyboardShow;
    private SoftKeyboardChangesThread softKeyboardThread;
    private List<EditText> editTextList;

    private View tempView;

    private SoftKeyboard(ViewGroup layout, InputMethodManager im) {
        this.layout = layout;
        keyboardHideByDefault();
        initEditTexts(layout);
        this.im = im;
        this.coords = new int[2];
        this.isKeyboardShow = false;
        this.softKeyboardThread = new SoftKeyboardChangesThread();
        this.softKeyboardThread.start();
    }


    public void openSoftKeyboard() {
        if (!isKeyboardShow) {
            layoutBottom = getLayoutCoordinates();
            im.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
            softKeyboardThread.keyboardOpened();
            isKeyboardShow = true;
        }
    }

    public void closeSoftKeyboard() {
        if (isKeyboardShow) {
            im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            isKeyboardShow = false;
        }
    }

    public void setSoftKeyboardCallback(SoftKeyboardChanged mCallback) {
        softKeyboardThread.setCallback(mCallback);
    }

    public void unRegisterSoftKeyboardCallback() {
        softKeyboardThread.stopThread();
    }

    public interface SoftKeyboardChanged {
        void onSoftKeyboardHide();

        void onSoftKeyboardShow();
    }

    private int getLayoutCoordinates() {
        layout.getLocationOnScreen(coords);
        return coords[1] + layout.getHeight();
    }

    private void keyboardHideByDefault() {
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
    }


    private void initEditTexts(ViewGroup viewgroup) {
        if (editTextList == null)
            editTextList = new ArrayList<>();


        int childCount = viewgroup.getChildCount();
        for (int i = 0; i <= childCount - 1; i++) {
            View v = viewgroup.getChildAt(i);

            if (v instanceof ViewGroup) {
                initEditTexts((ViewGroup) v);
            }

            if (v instanceof EditText) {
                EditText editText = (EditText) v;
                editText.setOnFocusChangeListener(this);
                editText.setCursorVisible(true);
                editTextList.add(editText);
            }
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            tempView = v;
            if (!isKeyboardShow) {
                layoutBottom = getLayoutCoordinates();
                softKeyboardThread.keyboardOpened();
                isKeyboardShow = true;
            }
        }
    }


    private final Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message m) {
            switch (m.what) {
                case CLEAR_FOCUS:
                    if (tempView != null) {
                        tempView.clearFocus();
                        tempView = null;
                    }
                    break;
            }
        }
    };

    private class SoftKeyboardChangesThread extends Thread {
        private AtomicBoolean started;
        private SoftKeyboardChanged mCallback;

        SoftKeyboardChangesThread() {
            started = new AtomicBoolean(true);
        }

        public void setCallback(SoftKeyboardChanged mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        public void run() {
            while (started.get()) {

                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int currentBottomLocation = getLayoutCoordinates();


                while (currentBottomLocation == layoutBottom && started.get()) {
                    currentBottomLocation = getLayoutCoordinates();
                }

                if (started.get())
                    mCallback.onSoftKeyboardShow();


                while (currentBottomLocation >= layoutBottom && started.get()) {
                    currentBottomLocation = getLayoutCoordinates();
                }

                while (currentBottomLocation != layoutBottom && started.get()) {
                    synchronized (this) {
                        try {
                            wait(500);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    currentBottomLocation = getLayoutCoordinates();
                }

                if (started.get())
                    mCallback.onSoftKeyboardHide();


                if (isKeyboardShow && started.get())
                    isKeyboardShow = false;


                if (started.get())
                    mHandler.obtainMessage(CLEAR_FOCUS).sendToTarget();
            }
        }

        void keyboardOpened() {
            synchronized (this) {
                notify();
            }
        }

        void stopThread() {
            synchronized (this) {
                started.set(false);
                notify();
            }
        }
    }


    //필요한 param
    //바탕 LinearLayout,내부 LinearLayout 1, 내부 LinearLayoutd 2,Activity

    public static SoftKeyboard setReactiveKeyboard(Context context, LinearLayout... layouts) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);

        SoftKeyboard softKeyboard = new SoftKeyboard(layouts[0], im);
        SoftKeyboard.SoftKeyboardChanged softKeyboardChanged = new SoftKeyboard.SoftKeyboardChanged() {
            ViewGroup.LayoutParams param1;
            ViewGroup.LayoutParams param2;

            @Override
            public void onSoftKeyboardShow() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, 0);
                    param1 = layouts[1].getLayoutParams();
                    param2 = layouts[2].getLayoutParams();
                    layouts[1].setLayoutParams(params);
                    layouts[2].setLayoutParams(params);
                });
            }

            @Override
            public void onSoftKeyboardHide() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    layouts[1].setLayoutParams(param1);
                    layouts[2].setLayoutParams(param2);
                });
            }
        };

        softKeyboard.setSoftKeyboardCallback(softKeyboardChanged);//setSoftKeyboardCallback
        return softKeyboard;
    }
}
