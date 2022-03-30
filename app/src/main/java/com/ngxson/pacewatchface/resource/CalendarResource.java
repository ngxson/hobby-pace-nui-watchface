package com.ngxson.pacewatchface.resource;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.ngxson.pacewatchface.NuiAlarm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarResource {
    private final static String SETTING_NAME = "CustomCalendarData";
    private final static int CALENDAR_DATA_INDEX_START = 2;
    private final static int MAX_NB_EVENTS = 2;
    private final static long ONE_HR = 60*60000L;
    private final static long TWO_HRS = 2*60*60000L;

    static ArrayList<Long> getNextEvents(Context ctx) {
        Calendar calendar = java.util.Calendar.getInstance();
        ArrayList<Long> result = new ArrayList<>();
        long currentTime = calendar.getTimeInMillis();
        String calendarEvents = Settings.System.getString(ctx.getContentResolver(), SETTING_NAME);
        try {
            JSONObject json_data = new JSONObject(calendarEvents);
            if (json_data.has("events")) {
                int event_number = json_data.getJSONArray("events").length();
                for (int i = 0; i < event_number; i++) {
                    JSONArray data = json_data.getJSONArray("events").getJSONArray(i);
                    String start_item = data.getString(CALENDAR_DATA_INDEX_START);
                    //Log.d("CalendarResource", "Got event " + start_item);
                    long start_time_millis = Long.parseLong(start_item);

                    // ignore passed events
                    if (start_time_millis < currentTime) continue;

                    //Log.d("CalendarResource", "Take " + start_time_millis);
                    result.add(start_time_millis);
                    if (result.size() >= MAX_NB_EVENTS) {
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("CalendarResource", e.getMessage());
        }
        return result;
    }

    static ArrayList<EventIndicator> indicators = new ArrayList<>();
    static long nextUpdateTime = -1;
    public static ArrayList<EventIndicator> getIndicators(Context ctx) {
        boolean isFirstTime = (nextUpdateTime == -1);
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (isFirstTime || currentTime >= nextUpdateTime)
            updateIndicators(ctx);
        if (isFirstTime) {
            long nextAlarm = getNextAlarm(ctx);
            NuiAlarm.setAlarm(ctx, nextAlarm);
        }
        return indicators;
    }

    private static void updateIndicators(Context ctx) {
        ArrayList<Long> events = getNextEvents(ctx);
        int i = 0;
        indicators.clear();
        for (long time : events) {
            EventIndicator e = new EventIndicator();
            e.milisec = time;
            e.minute = Long.valueOf(time / 60000).intValue() % 60;
            e.priority = i;
            if (i == 0) nextUpdateTime = time;
            i = i + 1;
            indicators.add(e);
        }
        // in case no event is found
        if (indicators.size() == 0) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            nextUpdateTime = currentTime + 24*TWO_HRS;
        }
    }

    public static long getNextAlarm(Context ctx) {
        ArrayList<EventIndicator> indicators = getIndicators(ctx);
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long alarmTime = Long.MAX_VALUE;

        for (EventIndicator indicator : indicators) {
            long time;
            time = indicator.milisec;
            if (currentTime < time && time < alarmTime)
                alarmTime = time;
            time = indicator.milisec - ONE_HR;
            if (currentTime < time && time < alarmTime)
                alarmTime = time;
            time = indicator.milisec - TWO_HRS;
            if (currentTime < time && time < alarmTime)
                alarmTime = time;
        }

        return (alarmTime != Long.MAX_VALUE)
                ? alarmTime
                : (currentTime + 24*TWO_HRS);
    }

    public static void setupDataListender(final Context ctx) {
        ContentResolver contentResolver = ctx.getContentResolver();
        Uri setting = Settings.System.getUriFor(SETTING_NAME);

        ContentObserver observer = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                nextUpdateTime = -1;
                getIndicators(ctx);
            }
        };

        contentResolver.registerContentObserver(setting, false, observer);
    }

    public static class EventIndicator {
        public long milisec;
        public int minute;
        public int priority;
    }
}
