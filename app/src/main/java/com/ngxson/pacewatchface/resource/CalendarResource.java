package com.ngxson.pacewatchface.resource;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarResource {
    private final static int CALENDAR_DATA_INDEX_START = 2;
    private final static int MAX_NB_EVENTS = 2;

    static ArrayList<Long> getNextEvents(Context ctx) {
        Calendar calendar = java.util.Calendar.getInstance();
        ArrayList<Long> result = new ArrayList<>();
        long current_time = calendar.getTimeInMillis();
        String calendarEvents = Settings.System.getString(ctx.getContentResolver(), "CustomCalendarData");
        try {
            JSONObject json_data = new JSONObject(calendarEvents);
            if (json_data.has("events")) {
                int event_number = json_data.getJSONArray("events").length();
                for (int i = 0; i < event_number; i++) {
                    JSONArray data = json_data.getJSONArray("events").getJSONArray(i);
                    String start_item = data.getString(CALENDAR_DATA_INDEX_START);
                    //Log.d("CalendarResource", "Got event " + start_item);
                    long start_time_millis = Long.parseLong(start_item);
                    if (start_time_millis < current_time) {
                        continue;
                    }

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

    public static ArrayList<EventIndicator> getIndicators(Context ctx) {
        ArrayList<Long> events = getNextEvents(ctx);
        ArrayList<EventIndicator> indicators = new ArrayList<>();
        int priority = 0;
        for (long time : events) {
            EventIndicator e = new EventIndicator();
            e.minute = Long.valueOf(time / 60000).intValue() % 60;
            e.priority = 2;//priority;
            priority = priority + 1;
            indicators.add(e);
        }
        return indicators;
    }

    public static class EventIndicator {
        public int minute;
        public int priority;
    }
}
