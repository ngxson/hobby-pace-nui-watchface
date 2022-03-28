package com.ngxson.pacewatchface.resource;

import android.content.res.Resources;
import android.graphics.Typeface;

import java.util.Calendar;
import java.util.EnumMap;
import java.util.Map;

/**
 * Resource manager for caching purposes
 */
public class ResourceManager {
    static Calendar calendar;

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

    public static String getBackgroundToday(boolean slpt) {
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int lunarDay = LunarCoreHelper.convertSolar2Lunar(day, month, year, 2)[0];
        int imgNum = lunarDay > 30 ? 1 : lunarDay;
        return (slpt ? "nui_bg_8c/nui_bg_" : "nui_bg/nui_bg_") + imgNum + ".png";
    }

}
