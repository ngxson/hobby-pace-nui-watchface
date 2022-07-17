package com.ngxson.pacewatchface;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ingenic.iwds.slpt.view.core.SlptAbsoluteLayout;
import com.ingenic.iwds.slpt.view.core.SlptLayout;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ngxson.pacewatchface.resource.CalendarResource;
import com.ngxson.pacewatchface.widget.BackgroundLayer;
import com.ngxson.pacewatchface.widget.IndicatorLayer;
import com.ngxson.pacewatchface.widget.NuiClock;
import com.ngxson.pacewatchface.widget.Widget;

import java.util.Calendar;

/**
 * Splt version of the watch.
 */

public class PaceWatchFaceSplt extends AbstractWatchFaceSlpt {
    Context context;
    public PaceWatchFaceSplt() {
        super();
    }
    Intent mIntent;
    int mFlags;
    int mStartId;
    boolean ready = false;
    boolean showSecond = true;
    public static PaceWatchFaceSplt instance;

    public void updateSlptClock() {
        updateShowSecond();
        NuiAlarm.registerSecondHandUpdate(this);
        super.onStartCommand(mIntent, mFlags, mStartId);
        Log.d("PaceWatchFaceSplt", "updateSlptClock");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!ready) {
            Log.d("PaceWatchFaceSplt", "onStartCommand init");
            instance = this;
            context = this.getApplicationContext();

            this.clock = new NuiClock(this);
            this.widgets.add(new BackgroundLayer(this, true));
            this.widgets.add(new IndicatorLayer(this, true));

            mIntent = intent;
            mFlags = flags;
            mStartId = startId;

            CalendarResource.setupDataListender(this);
            NuiAlarm.registerSecondHandUpdate(this);
            updateShowSecond();
            ready = true;
        }

        return super.onStartCommand(mIntent, mFlags, mStartId);
    }

    @Override
    protected SlptLayout createClockLayout26WC() {
        return null;
    }

    @Override
    protected SlptLayout createClockLayout8C() {
        SlptAbsoluteLayout result = new SlptAbsoluteLayout();

        for (Widget widget : widgets) {
            for (SlptViewComponent component : widget.buildSlptViewComponent(this)) {
                result.add(component);
            }
        }

        for (SlptViewComponent component : clock.buildSlptViewComponent(this)) {
            result.add(component);
        }

        Log.d("PaceWatchFaceSplt", "createClockLayout8C result length = " + result.size());

        return result;
    }

    protected void initWatchFaceConfig() {
        //Log.w("DinoDevs-GreatFit", "Initiating watchface");
    }

    public void updateShowSecond() {
        Calendar cal = Calendar.getInstance();
        int hour24hrs = cal.get(Calendar.HOUR_OF_DAY);
        showSecond = (hour24hrs >= NuiAlarm.SHOW_SEC_HAND_FROM_HR);
    }

    @Override
    public boolean isClockPeriodSecond() {
        return showSecond;
    }
}
