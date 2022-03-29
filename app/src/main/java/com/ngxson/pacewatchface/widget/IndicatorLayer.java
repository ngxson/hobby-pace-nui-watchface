package com.ngxson.pacewatchface.widget;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ngxson.pacewatchface.resource.IndicatorResource;
import com.ngxson.pacewatchface.resource.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class IndicatorLayer extends AbstractWidget {
    private Service service;
    private Paint mGPaint;
    private boolean isSplt;
    private boolean ready = false;

    public IndicatorLayer(Context ctx, boolean isSplt) {
        super();
        this.isSplt = isSplt;
        if (ctx != null) {
            ResourceManager.preloadMinuteIndicator(ctx);
            IndicatorResource.preload(ctx, isSplt);
            this.ready = true;
        }
    }

    @Override
    public void init(Service service) {
        this.service = service;
        this.mGPaint = new Paint();
        IndicatorResource.preload(service, isSplt);
        this.ready = true;
    }

    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        List<SlptViewComponent> slpt_objects = new ArrayList<>();
        int minute = 5;
        int [] coord = IndicatorResource.getXY(minute);
        int x = coord[0];
        int y = coord[1];

        SlptPictureView indicatorImg = new SlptPictureView();
        indicatorImg.setImagePicture(IndicatorResource.getImage8c(minute));
        indicatorImg.setStart(x, y);
        slpt_objects.add(indicatorImg);

        return slpt_objects;
    }

    @Override
    public void draw(Canvas canvas, float width, float height, float centerX, float centerY) {
        if (!ready) return;

        int minute = 5;
        int [] coord = IndicatorResource.getXY(minute);
        int x = coord[0];
        int y = coord[1];

        canvas.drawBitmap(IndicatorResource.getImage(minute), x, y, mGPaint);
    }
}
