package com.ngxson.pacewatchface;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.ngxson.pacewatchface.resource.CalendarResource;

public class NuiAlarm extends BroadcastReceiver {
    private static final int NUI_ALARM_CODE = 1234;
    public static final String REQUEST_CODE = "code";

    @Override
    public void onReceive(Context context, Intent intent) {
        int code = intent.getIntExtra(REQUEST_CODE, 0);
        if (code != NUI_ALARM_CODE) return;

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NuiWatchFace:alarm");
        wl.acquire(5*1000L);

        // do stuff
        CalendarResource.getIndicators(context); // trigger update
        if (PaceWatchFaceSplt.instance != null) {
            PaceWatchFaceSplt.instance.updateSlptClock();
        }
        long nextAlarm = CalendarResource.getNextAlarm(context);
        setAlarm(context, nextAlarm);
        // end

        wl.release();
    }

    public static void setAlarm(Context context, long milis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        cancelAlarm(context, alarmManager);
        Intent i = new Intent(context, NuiAlarm.class);
        i.putExtra(REQUEST_CODE, NUI_ALARM_CODE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, milis, pi);
        Log.d("NuiAlarm", "setAlarm " + milis);
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
}
