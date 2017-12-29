package com.sum.library.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by sdl on 2017/12/27.
 */

public class AppUtils {
    /**
     * 通知到系统上下文中
     */
    public static void displayToGallery(Context context, File photoFile) {
        if (photoFile == null || !photoFile.exists()) {
            return;
        }
        String photoPath = photoFile.getAbsolutePath();
        String photoName = photoFile.getName();
        // 其次把文件插入到系统图库
        try {
            ContentResolver contentResolver = context.getContentResolver();
            MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
    }

    /**
     * 计算当然值占据比例
     *
     * @param value 当前进度
     * @param min   最小进度
     * @param max   最大进度
     * @return 占据比例 0-1
     */
    public static float getProgress(int value, int min, int max) {
        if (min == max) {
            throw new IllegalArgumentException("Max (" + max + ") cannot equal min (" + min + ")");
        }

        return (value - min) / (float) (max - min);
    }

    /**
     * 可以用做比例x目标值获取当前值
     *
     * @param value  当前进度
     * @param target 目标进度
     * @return 0 - 1 进度比例
     */
    public static float progressPercent(int value, int target) {
        float progress = getProgress(value, 0, target);
        //最小为0：0%
        float curProgress = Math.max(progress, 0);
        //最大是1：100%
        return Math.min(curProgress, 1);
    }


    /**
     * @param fraction   进度比例（0-1）
     * @param startValue 开始色值
     * @param endValue   结束色值
     * @return 当前进度的色值
     */
    public static int rgbEvaluate(float fraction, int startValue, int endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

    public static <T> T checkNotNull(@Nullable T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}