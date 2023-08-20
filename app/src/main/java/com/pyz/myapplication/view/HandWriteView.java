package com.pyz.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class HandWriteView extends View {



    public HandWriteView(Context context) {
        this(context,null);
    }

    public HandWriteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HandWriteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }
}
