package com.ngxson.pacewatchface;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.ngxson.pacewatchface.resource.CalendarResource;

import java.util.Calendar;

public class NuiAlarm extends BroadcastReceiver {
    public static final int SHOW_SEC_HAND_FROM_HR = 6; // hrs
    private static final int NUI_ALARM_CAL_EVENT_UPDATE = 1212;
    private static final int NUI_ALARM_SEC_HAND_UPDATE = 1241;
    public static final String REQUEST_CODE = "code";

    @Override
    public void onReceive(Context context, Intent intent) {
        int code = intent.getIntExtra(REQUEST_CODE, 0);
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NuiWatchFace:alarm");
        wl.acquire(5*1000L);

        if (code == NUI_ALARM_CAL_EVENT_UPDATE) {
            // update calendar
            CalendarResource.getIndicators(context); // trigger update
            if (PaceWatchFaceSplt.instance != null) {
                PaceWatchFaceSplt.instance.updateSlptClock();
            }
            long nextAlarm = CalendarResource.getNextAlarm(context);
            setAlarm(context, nextAlarm);

        } else if (code == NUI_ALARM_SEC_HAND_UPDATE) {
            // update second hand visibility
            if (PaceWatchFaceSplt.instance != null) {
                PaceWatchFaceSplt.instance.updateSlptClock();
                registerSecondHandUpdate(context);
            }
        }

        wl.release();
    }

    public static void setAlarm(Context context, long milis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        cancelAlarm(context, alarmManager);
        Intent i = new Intent(context, NuiAlarm.class);
        i.putExtra(REQUEST_CODE, NUI_ALARM_CAL_EVENT_UPDATE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, NUI_ALARM_CAL_EVENT_UPDATE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, milis, alarmIntent);
            Log.d("NuiAlarm", "setAlarm " + milis);
        } else {
            Log.e("NuiAlarm", "setAlarm FAILED: alarmManager is null");
        }
    }

    private static void cancelAlarm(Context context, AlarmManager alarmManager) {
        Intent intent = new Intent(context, NuiAlarm.class);
        try {
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarmManager.cancel(sender);
        } catch (Exception e) {
            Log.e("NuiAlarm", e.getMessage());
        }
    }

    private final static long ONE_HR = 60*60000L;
    public static void registerSecondHandUpdate(Context context) {
        Calendar cal = Calendar.getInstance();
        long now = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 4);
        cal.set(Calendar.MILLISECOND, 0);
        long tBeginOfToday = cal.getTimeInMillis();
        long tShowSecondHand = tBeginOfToday + SHOW_SEC_HAND_FROM_HR * ONE_HR;
        long tBeginOfTomorrow = tBeginOfToday + 24*ONE_HR;
        /**
         * tBeginOfToday       now        tShowSecondHand    tBeginOfTomorrow
         *     |----------------x----------------|------------------|
         *
         * tBeginOfToday  tShowSecondHand       now          tBeginOfTomorrow
         *     |----------------|----------------x------------------|
         */

        if (now < tBeginOfToday) { // this case should never happen
            setAlarmUpdateSecondHand(context, now + 10000L);
        } else if (now > tBeginOfTomorrow) { // this case should never happen
            setAlarmUpdateSecondHand(context, tBeginOfTomorrow + SHOW_SEC_HAND_FROM_HR * ONE_HR);
        } else if (now < tShowSecondHand) {
            setAlarmUpdateSecondHand(context, tShowSecondHand);
        } else {
            setAlarmUpdateSecondHand(context, tBeginOfTomorrow);
        }
    }

    private static void setAlarmUpdateSecondHand(Context context, long milis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, NuiAlarm.class);
        i.putExtra(REQUEST_CODE, NUI_ALARM_SEC_HAND_UPDATE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, NUI_ALARM_SEC_HAND_UPDATE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, milis, alarmIntent);
            Log.d("NuiAlarm", "setAlarmUpdateSecondHand " + milis);
        } else {
            Log.e("NuiAlarm", "setAlarmUpdateSecondHand FAILED: alarmManager is null");
        }
    }
}
