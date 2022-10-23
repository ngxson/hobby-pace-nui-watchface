package com.ngxson.pacewatchface.resource;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.util.Log;

import com.huami.watch.watchface.util.Util;
import com.ngxson.pacewatchface.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Resource manager for caching purposes
 */
public class ResourceManager {
    static Calendar calendar;
    public static ArrayList<byte[]> minuteIndicator = new ArrayList<>();
    public static ArrayList<byte[]> minuteIndicator8c = new ArrayList<>();
    public static HashMap<String, Bitmap> bgCached = new HashMap<>();
    public static HashMap<String, byte[]> bgCached8c = new HashMap<>();
    public static Bitmap baseBitmapMinInd;

    public enum Font {
        ROBOTO("fonts/RobotoCondensed-Regular.ttf");

        private final String path;

        Font(String path) {
            this.path = path;
        }
    }

    private static Map<Font, Typeface> TYPE_FACES = new EnumMap<>(Font.class);

    public static Typeface getTypeFace(final Resources resources, final Font font) {
        Typeface typeface = TYPE_FACES.get(font);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(resources.getAssets(), font.path);
            TYPE_FACES.put(font, typeface);
        }
        return typeface;
    }

    /**
     * BACKGROUND
     */

    public static int getBackgroundTodayImgNum() {
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int lunarDay = LunarCoreHelper.convertSolar2Lunar(day, month, year, 10)[0];
        int imgNum = lunarDay > 30 ? 1 : lunarDay;
        return imgNum;
    }

    public static byte[] getBackground8c(Context ctx, int imgNum) {
        String file = "nui_bg_8c/nui_bg_" + imgNum + ".png";
        if (!bgCached8c.containsKey(file)) {
            byte[] data = Util.assetToBytes(ctx, file);
            bgCached8c.put(file, data);
            return data;
        } else {
            return bgCached8c.get(file);
        }
    }

    public static Bitmap getBackground(Context ctx, int imgNum) {
        String file = "nui_bg/nui_bg_" + imgNum + ".png";
        if (!bgCached.containsKey(file)) {
            Bitmap data = Util.decodeImage(ctx.getResources(), file);
            bgCached.put(file, data);
            return data;
        } else {
            return bgCached.get(file);
        }
    }

    /**
     * INDICATOR
     */

    public static Bitmap assetToBitmap(Context ctx, String filePath) {
        AssetManager assetManager = ctx.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            Log.e("assetToBitmap", e.getMessage());
            return null;
        }
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmapOrg, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmapOrg, bitmapOrg.getWidth(), bitmapOrg.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                matrix, true);
        return rotatedBitmap;
    }

    public static void preloadMinuteIndicator(Context ctx) {
        /*if (!minuteIndicator.isEmpty()) return;
        Bitmap baseBitmap = assetToBitmap(ctx, "nui_widget/minute_indicator.png");
        baseBitmapMinInd = baseBitmap;
        for (int i = 0; i < 60; i++) {
            Bitmap tmp = rotateBitmap(baseBitmap, i * 6);
            byte[] bytes = com.huami.watch.watchface.util.Util.Bitmap2Bytes(tmp);
            minuteIndicator.add(bytes);
            Log.d("preloadMinuteIndicator", "i=" + i);
        }
        baseBitmap = assetToBitmap(ctx, "nui_widget_8c/minute_indicator.png");
        for (int i = 0; i < 60; i++) {
            Bitmap tmp = rotateBitmap(baseBitmap, i * 6);
            byte[] bytes = com.huami.watch.watchface.util.Util.Bitmap2Bytes(tmp);
            minuteIndicator8c.add(bytes);
            Log.d("preloadMinuteIndicator", "i=" + i);
        }*/
    }
}
