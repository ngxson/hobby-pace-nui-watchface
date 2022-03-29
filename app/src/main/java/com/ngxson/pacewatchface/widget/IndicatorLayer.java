package com.ngxson.pacewatchface.widget;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ngxson.pacewatchface.resource.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class IndicatorLayer extends AbstractWidget {
    private Service service;
    private Paint mGPaint;

    public IndicatorLayer(Context ctx) {
        super();
        if (ctx != null) {
            ResourceManager.preloadMinuteIndicator(ctx);
        }
    }

    @Override
    public void init(Service service) {
        this.service = service;
        this.mGPaint = new Paint();
    }

    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        List<SlptViewComponent> slpt_objects = new ArrayList<>();

        SlptPictureView background = new SlptPictureView();
        background.setImagePicture(ResourceManager.minuteIndicator8c.get(5));
        slpt_objects.add(background);

        return slpt_objects;
    }

    @Override
    public void draw(Canvas canvas, float width, float height, float centerX, float centerY) {
        Bitmap bm = ResourceManager.baseBitmapMinInd;
        int min = 5;

        canvas.save();
        canvas.rotate(min * 6, centerX, centerY);
        canvas.drawBitmap(bm, centerX - bm.getWidth() / 2f, centerY - bm.getHeight() / 2f, null);
        canvas.restore();
    }
}
