package com.ngxson.pacewatchface;

import com.huami.watch.watchface.AbstractSlptClock;

import com.ngxson.pacewatchface.widget.BackgroundLayer;
import com.ngxson.pacewatchface.widget.NuiClock;

/**
 * Amazfit watch faces
 */

public class PaceWatchFace extends AbstractWatchFace {

    public PaceWatchFace() {
        super(
                new NuiClock(null),
                new BackgroundLayer(null)
        );
    }

    @Override
    protected Class<? extends AbstractSlptClock> slptClockClass() {
        return PaceWatchFaceSplt.class;
    }
}