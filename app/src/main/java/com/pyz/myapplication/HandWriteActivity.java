package com.pyz.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.pyz.myapplication.databinding.ActivityHandWriteBinding;
import com.pyz.myapplication.utils.ARouterConstant;

@Route(path = ARouterConstant.HandWriteActivity)
public class HandWriteActivity extends AppCompatActivity {

    ActivityHandWriteBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityHandWriteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.btnReset.setOnClickListener(view -> {
            mBinding.handView.cleanPath();
        });
    }
}