package com.ngxson.pacewatchface;

import android.content.Context;
import android.content.Intent;

import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.core.SlptAbsoluteLayout;
import com.ingenic.iwds.slpt.view.core.SlptLayout;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ngxson.pacewatchface.widget.CirclesWidget;
import com.ngxson.pacewatchface.widget.HeartRateWidget;
import com.ngxson.pacewatchface.widget.MalvarezClock;
import com.ngxson.pacewatchface.widget.NuiClock;
import com.ngxson.pacewatchface.widget.Widget;

import java.util.List;

/**
 * Splt version of the watch.
 */

public class PaceWatchFaceSplt extends AbstractWatchFaceSlpt {
    Context context;
    public PaceWatchFaceSplt() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this.getApplicationContext();
        this.clock = new NuiClock();
        //this.widgets.add(new NuiClock());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected SlptLayout createClockLayout26WC() {
        SlptAbsoluteLayout result = new SlptAbsoluteLayout();

        List<SlptViewComponent> list = ((NuiClock) clock).buildSlptViewComponent(this, true);
        for (SlptViewComponent component : list) {
            result.add(component);
        }

        for (Widget widget : widgets) {
            for (SlptViewComponent component : widget.buildSlptViewComponent(this)) {
                result.add(component);
            }
        }

        return result;
    }

    @Override
    protected SlptLayout createClockLayout8C() {
        SlptAbsoluteLayout result = new SlptAbsoluteLayout();

        for (SlptViewComponent component : clock.buildSlptViewComponent(this)) {
            result.add(component);
        }

        for (Widget widget : widgets) {
            for (SlptViewComponent component : widget.buildSlptViewComponent(this)) {
                result.add(component);
            }
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
