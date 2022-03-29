package com.ngxson.pacewatchface.resource;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.huami.watch.watchface.util.Util;

import java.util.ArrayList;

public class IndicatorResource {
    static ArrayList<Bitmap> img = new ArrayList<>();
    static ArrayList<byte[]> img8c = new ArrayList<>();
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
            { 164, 276 },
            { 149, 279 },
            { 134, 280 },
            { 118, 279 },
            { 103, 276 },
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

    public static Bitmap getImage(int minute) {
        return img.get(minute);
    }

    public static byte[] getImage8c(int minute) {
        return img8c.get(minute);
    }

    public static int[] getXY(int minute) {
        return POSITION[minute];
    }

    static boolean readyImg = false;
    static boolean readyImg8c = false;

    public static void preload(Context ctx, boolean isSlpt) {
        try {
            if (isSlpt && !readyImg) {
                readyImg = true;
                Bitmap bmp = ResourceManager.assetToBitmap(ctx, "indicator_sprite_set.png");
                int w = bmp.getHeight();
                for (int i = 0; i < 60; i++) {
                    Bitmap mBmp = Bitmap.createBitmap(bmp, i*w, 0, w, w);
                    img.add(mBmp);
                    Log.d("IndicatorResource", "Non-slpt " + i);
                }
            }

            if (!isSlpt && !readyImg8c) {
                readyImg8c = true;
                Bitmap bmp = ResourceManager.assetToBitmap(ctx, "indicator_sprite_set.png");
                int w = bmp.getHeight();
                for (int i = 0; i < 60; i++) {
                    Bitmap mBmp = Bitmap.createBitmap(bmp, i*w, 0, w, w);
                    img8c.add(Util.Bitmap2Bytes(mBmp));
                    Log.d("IndicatorResource", "Slpt " + i);
                }
            }
        } catch (Exception e) {
            Log.e("IndicatorResource", e.getMessage());
        }
    }
}
