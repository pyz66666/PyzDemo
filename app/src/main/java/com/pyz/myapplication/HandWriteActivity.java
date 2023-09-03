package com.pyz.myapplication;

import androidx.annotation.BoolRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pyz.myapplication.databinding.ActivityHandWriteBinding;
import com.pyz.myapplication.utils.ARouterConstant;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@Route(path = ARouterConstant.HandWriteActivity)
public class HandWriteActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 1;
    ActivityHandWriteBinding mBinding;
    private static final String[] permissionsGroup = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityHandWriteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.btnReset.setOnClickListener(view -> {
            mBinding.handView.cleanPath();
        });
        mBinding.btnEraser.setOnClickListener(view -> {
            mBinding.handView.changeEraser(true);
        });
        mBinding.btnPaint.setOnClickListener(view -> {
            mBinding.handView.changeEraser(false);
        });
        mBinding.save.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveBitmap();
                        }
                    }).start();
                    return;
                }
                RxPermissions rxPermissions = new RxPermissions(this);
                rxPermissions.request(permissionsGroup).subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    saveBitmap();
                                }
                            }).start();
                        }else {
                            ActivityCompat.requestPermissions(HandWriteActivity.this, permissionsGroup, PERMISSION_REQUEST);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                if(Environment.isExternalStorageManager()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveBitmap();
                        }
                    }).start();
                    return;
                }
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", this.getPackageName(), null));
                startActivity(intent);
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST:

                break;
        }
    }

    private void saveBitmap(){
        //因为xml用的是背景，所以这里也是获得背景
        Bitmap bitmap=mBinding.handView.getMBufferBitmap();
        //创建文件，因为不存在2级目录，所以不用判断exist，要保存png，这里后缀就是png，要保存jpg，后缀就用jpg
        File file=new File(Environment.getExternalStorageDirectory() +"/pyz/"+System.currentTimeMillis()+".png");
        FileUtils.createOrExistsDir(Environment.getExternalStorageDirectory() +"/pyz");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //文件输出流
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            //压缩图片，如果要保存png，就用Bitmap.CompressFormat.PNG，要保存jpg就用Bitmap.CompressFormat.JPEG,质量是100%，表示不压缩
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            //写入，这里会卡顿，因为图片较大
            fileOutputStream.flush();
            //记得要关闭写入流
            fileOutputStream.close();
            //成功的提示，写入成功后，请在对应目录中找保存的图片
            ToastUtils.showShort("图片保存在"+Environment.getExternalStorageDirectory() +"/pyz目录中");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //失败的提示
            ToastUtils.showShort(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            //失败的提示
            ToastUtils.showShort(e.getMessage());
        }
    }
}