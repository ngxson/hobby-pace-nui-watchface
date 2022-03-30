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
    public static PaceWatchFaceSplt instance;

    public void updateSlptClock() {
        super.onStartCommand(mIntent, mFlags, mStartId);
        Log.d("PaceWatchFaceSplt", "updateSlptClock");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!ready) {
            instance = this;
            context = this.getApplicationContext();

            this.clock = new NuiClock(this);
            this.widgets.add(new BackgroundLayer(this, true));
            this.widgets.add(new IndicatorLayer(this, true));

            mIntent = intent;
            mFlags = flags;
            mStartId = startId;

            CalendarResource.setupDataListender(this);
            ready = true;
        }

        return super.onStartCommand(intent, flags, startId);
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

        return result;
    }

    protected void initWatchFaceConfig() {
        //Log.w("DinoDevs-GreatFit", "Initiating watchface");
    }

    @Override
    public boolean isClockPeriodSecond() {
        return true;
    }
}
