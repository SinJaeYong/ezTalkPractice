package com.example.bizmekatalk.fragment.sublayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.bizmekatalk.R;

public class SubNaviLayout extends LinearLayout {
    public SubNaviLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public SubNaviLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sub_navi_layout,this,true);
    }

}
