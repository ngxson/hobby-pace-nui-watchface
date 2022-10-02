package com.ngxson.pacewatchface;

import java.util.Calendar;

public class Utils {
    public static boolean shouldShowSecondHand() {
        Calendar cal = Calendar.getInstance();
        return shouldShowSecondHand(cal);
    }

    public static boolean shouldShowSecondHand(Calendar cal) {
        int hour24hrs = cal.get(Calendar.HOUR_OF_DAY);
        return (hour24hrs >= NuiAlarm.SHOW_SEC_HAND_FROM_HR);
    }
}
