package com.ngxson.pacewatchface;

import com.huami.watch.watchface.AbstractSlptClock;

import com.ngxson.pacewatchface.widget.BackgroundLayer;
import com.ngxson.pacewatchface.widget.IndicatorLayer;
import com.ngxson.pacewatchface.widget.NuiClock;

/**
 * Amazfit watch faces
 */

public class PaceWatchFace extends AbstractWatchFace {

    public PaceWatchFace() {
        super(new NuiClock(null));
        // background
        BackgroundLayer bg = new BackgroundLayer(null, false);
        bg.setContext(this.getBaseContext());
        this.widgets.add(bg);
        // indicator
        IndicatorLayer indicatorLayer = new IndicatorLayer(null, false);
        this.widgets.add(indicatorLayer);
    }

    @Override
    protected Class<? extends AbstractSlptClock> slptClockClass() {
        return PaceWatchFaceSplt.class;
    }
}