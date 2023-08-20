package com.pyz.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pyz.myapplication.databinding.ActivityHandWriteBinding;

public class HandWriteActivity extends AppCompatActivity {

    ActivityHandWriteBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityHandWriteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }
}