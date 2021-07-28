package com.ngxson.pacewatchface.widget;

import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout;
import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ingenic.iwds.slpt.view.digital.SlptDayHView;
import com.ingenic.iwds.slpt.view.digital.SlptDayLView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.ngxson.pacewatchface.R;
import com.ngxson.pacewatchface.resource.ResourceManager;

/**
 * Clock
 */
public class MalvarezClock extends DigitalClockWidget {
    private TextPaint dateFont;
    private Paint mGPaint;
    private Bitmap background;

    @Override
    public void init(Service service) {
        this.background = Util.decodeImage(service.getResources(),"nui_bg.png");
        this.mGPaint = new Paint();

        this.dateFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.dateFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.ROBOTO));
        this.dateFont.setTextSize(16);
        this.dateFont.setColor(service.getResources().getColor(R.color.malvarez_time_colour));
        this.dateFont.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void onDrawDigital(Canvas canvas, float width, float height, float centerX, float centerY, int seconds, int minutes, int hours, int year, int month, int day, int week, int ampm) {
        canvas.drawBitmap(background, 0f, 0f, mGPaint);
        Calendar calendar = Calendar.getInstance();
        canvas.drawText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), 263, 143, this.dateFont);
    }

    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        SlptPictureView background = new SlptPictureView();
        background.setImagePicture(Util.assetToBytes(service, "nui_bg_8c.png"));

        Typeface timeTypeFace = ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.ROBOTO);

        SlptLinearLayout dayLayout = new SlptLinearLayout();
        dayLayout.add(new SlptDayHView());
        dayLayout.add(new SlptDayLView());

        dayLayout.setTextAttrForAll(16, -1, timeTypeFace);
        dayLayout.setStart(263, 143);

        return Arrays.asList(background, dayLayout);
    }
}
