package com.ngxson.pacewatchface;

import com.huami.watch.watchface.AbstractSlptClock;

import com.ngxson.pacewatchface.widget.CirclesWidget;
import com.ngxson.pacewatchface.widget.HeartRateWidget;
import com.ngxson.pacewatchface.widget.MalvarezClock;
import com.ngxson.pacewatchface.widget.NuiClock;

/**
 * Amazfit watch faces
 */

public class PaceWatchFace extends AbstractWatchFace {

    public PaceWatchFace() {
        super(
                new NuiClock()
        );
    }

    @Override
    protected Class<? extends AbstractSlptClock> slptClockClass() {
        return PaceWatchFaceSplt.class;
    }
}