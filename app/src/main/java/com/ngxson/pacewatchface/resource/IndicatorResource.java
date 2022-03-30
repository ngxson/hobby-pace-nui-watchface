package com.ngxson.pacewatchface.resource;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.huami.watch.watchface.util.Util;

import java.util.ArrayList;

public class IndicatorResource {
    static ArrayList<Bitmap> imgP0 = new ArrayList<>();
    static ArrayList<byte[]> imgP08c = new ArrayList<>();
    static ArrayList<Bitmap> imgP1 = new ArrayList<>();
    static ArrayList<byte[]> imgP18c = new ArrayList<>();

    static final int[][] POSITION = {
            { 134, -12 },
            { 149, -12 },
            { 164, -9 },
            { 179, -5 },
            { 193, 0 },
            { 207, 7 },
            { 219, 15 },
            { 231, 25 },
            { 242, 36 },
            { 252, 48 },
            { 260, 61 },
            { 267, 74 },
            { 272, 88 },
            { 276, 103 },
            { 279, 118 },
            { 280, 134 },
            { 279, 149 },
            { 276, 164 },
            { 272, 179 },
            { 267, 193 },
            { 260, 207 },
            { 252, 219 },
            { 242, 231 },
            { 231, 242 },
            { 219, 252 },
            { 207, 260 },
            { 193, 267 },
            { 179, 272 },
            { 163, 273 },
            { 148, 273 },
            { 134, 273 },
            { 119, 273 },
            { 104, 273 },
            { 88, 272 },
            { 74, 267 },
            { 61, 260 },
            { 48, 252 },
            { 36, 242 },
            { 25, 231 },
            { 15, 219 },
            { 7, 207 },
            { 0, 193 },
            { -5, 179 },
            { -9, 164 },
            { -12, 149 },
            { -12, 134 },
            { -12, 118 },
            { -9, 103 },
            { -5, 88 },
            { 0, 74 },
            { 7, 60 },
            { 15, 48 },
            { 25, 36 },
            { 36, 25 },
            { 48, 15 },
            { 60, 7 },
            { 74, 0 },
            { 88, -5 },
            { 103, -9 },
            { 118, -12 }
    };

    public static Bitmap getImage(int minute, int priority) {
        return priority == 0 ? imgP0.get(minute) : imgP1.get(minute);
    }

    public static byte[] getImage8c(int minute, int priority) {
        return priority == 0 ? imgP08c.get(minute) : imgP18c.get(minute);
    }

    public static int[] getXY(int minute) {
        return POSITION[minute];
    }

    static boolean loadingImg = false;
    static boolean readyImg = false;
    static boolean readyImg8c = false;

    public static void preload(Context ctx, boolean isSlpt) {
        try {
            if (isSlpt && !loadingImg) {
                loadingImg = true;
                Bitmap bmp = ResourceManager.assetToBitmap(ctx, "indicator_sprite_set.png");
                int w = bmp.getHeight();
                for (int i = 0; i < 60; i++) {
                    Bitmap mBmp = Bitmap.createBitmap(bmp, i*w, 0, w, w);
                    imgP0.add(mBmp);
                    Log.d("IndicatorResource", "Non-slpt P0 " + i);
                }
                bmp = ResourceManager.assetToBitmap(ctx, "indicator_2_sprite_set.png");
                for (int i = 0; i < 60; i++) {
                    Bitmap mBmp = Bitmap.createBitmap(bmp, i*w, 0, w, w);
                    imgP1.add(mBmp);
                    Log.d("IndicatorResource", "Non-slpt P1 " + i);
                }
                readyImg = true;
            }

            if (!isSlpt && !readyImg8c) {
                readyImg8c = true;
                Bitmap bmp = readyImg
                        ? null
                        : ResourceManager.assetToBitmap(ctx, "indicator_sprite_set.png");
                int w = readyImg ? imgP0.get(0).getHeight() : bmp.getHeight();
                for (int i = 0; i < 60; i++) {
                    Bitmap mBmp = readyImg
                            ? imgP0.get(i)
                            : Bitmap.createBitmap(bmp, i*w, 0, w, w);
                    imgP08c.add(Util.Bitmap2Bytes(mBmp));
                    Log.d("IndicatorResource", "Slpt P0 " + i);
                }
                bmp = readyImg
                        ? null
                        : ResourceManager.assetToBitmap(ctx, "indicator_2_sprite_set.png");
                for (int i = 0; i < 60; i++) {
                    Bitmap mBmp = readyImg
                            ? imgP1.get(i)
                            : Bitmap.createBitmap(bmp, i*w, 0, w, w);
                    imgP18c.add(Util.Bitmap2Bytes(mBmp));
                    Log.d("IndicatorResource", "Slpt P1 " + i);
                }
            }
        } catch (Exception e) {
            Log.e("IndicatorResource", e.getMessage());
        }
    }
}
