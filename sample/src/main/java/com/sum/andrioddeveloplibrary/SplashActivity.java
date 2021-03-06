package com.sum.andrioddeveloplibrary;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sum.library.app.BaseActivity;
import com.sum.library.utils.AppUtils;
import com.sum.library.utils.Logger;

import java.util.List;

/**
 * Created by sdl on 2017/12/29.
 * 启动页
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO 最简单的屏幕适配
        //老版本 适配假设我们设计稿是宽度是 1080px，资源放在 xxhdpi，那么我们宽度转换为 dp 就是 1080 / 3 = 360dp，
        //新版本 适配假设我们设计稿是宽度是 1080px，资源放在 xxhdpi，宽度为1080dp，这样就可以根据设计图的px直接写dp，
        //最后版本 适配假设我们设计稿是宽度是 1080px，资源放在 xxhdpi，宽度为360dp，自己适配还是推荐360屏幕宽度，
        //TODO 1.22.0 AdaptScreenUtils 适配预览问题
        if (ScreenUtils.isPortrait()) {
//            AdaptScreenUtils.adaptHeight(getResources(), 360);
            ScreenUtils.adaptScreen4VerticalSlide(this, 360);
        } else {
//            AdaptScreenUtils.adaptWidth(getResources(), 360);
            ScreenUtils.adaptScreen4HorizontalSlide(this, 360);
        }

        super.onCreate(savedInstanceState);
        Logger.e("ScreenUtils.isAdaptScreen()->" + ScreenUtils.isPortrait());

        findViewById(R.id.b3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenUtils.toggleFullScreen(SplashActivity.this);
            }
        });
    }
//
//    @Override
//    public Resources getResources() {//设计宽度
//        return AdaptScreenUtils.adaptWidth(super.getResources(), 1080);
//    }

    @Override
    protected void onDestroy() {
//        if (ScreenUtils.isAdaptScreen()) {
//            ScreenUtils.cancelAdaptScreen(this);
//        }
        super.onDestroy();
    }

    @Override
    protected void initParams() {
        findViewById(R.id.b7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                per();
            }
        });


        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.launchAppDetailsSettings();
            }
        });


        findViewById(R.id.b2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLong("是否有通知权限：" + AppUtils.notificationIsOpen());
                if (!AppUtils.notificationIsOpen()) {
                    show();
                }
            }
        });
    }

    private void per() {
        List<String> permissions = PermissionUtils.getPermissions();


        PermissionUtils.permission(PermissionConstants.getPermissions(PermissionConstants.STORAGE)).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {//允许
                ToastUtils.showShort(permissions.toString());
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                show();
            }
        }).request();


    }

    private void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("提示").setMessage("权限拒绝")
                .setPositiveButton("确定", (dialog, which) -> {
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.parse("package:" + SplashActivity.this.getPackageName()));
                    this.startActivity(intent);
                }).setNegativeButton("取消", (dialog, which) -> {
        }).setCancelable(false).show();
    }
}
