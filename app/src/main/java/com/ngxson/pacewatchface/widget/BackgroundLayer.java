package com.ngxson.pacewatchface.widget;

import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;

import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout;
import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ingenic.iwds.slpt.view.digital.SlptDayHView;
import com.ingenic.iwds.slpt.view.digital.SlptDayLView;
import com.ngxson.pacewatchface.PaceWatchFaceSplt;
import com.ngxson.pacewatchface.R;
import com.ngxson.pacewatchface.data.DataType;
import com.ngxson.pacewatchface.resource.ResourceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class BackgroundLayer extends AbstractWidget {
    private static final int DAY_OFFSET_X = 263 + 6;
    private static final int DAY_OFFSET_Y = 143 + 4;
    private static final int DAY_FONT_SIZE = 22;
    private static final int DAY_OFFSET_HALF_CHAR = 5;

    private TextPaint dateFont;
    private Bitmap background;
    private byte[] backgroundSlpt;
    private Paint mGPaint;
    private Calendar calendar;
    private int lastDay = -1;
    public static PaceWatchFaceSplt mainService;
    private boolean isSlpt;

    public BackgroundLayer(PaceWatchFaceSplt mainService, boolean isSlpt) {
        super();
        if (mainService != null) NuiClock.mainService = mainService;
        this.isSlpt = isSlpt;
        updateBackground(mainService, -1);
    }

    @Override
    public void init(Service service) {
        this.mGPaint = new Paint();
        updateBackground(service, -1);

        this.dateFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.dateFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.ROBOTO));
        this.dateFont.setTextSize(DAY_FONT_SIZE);
        this.dateFont.setColor(service.getResources().getColor(R.color.malvarez_time_colour));
        this.dateFont.setTextAlign(Paint.Align.LEFT);
    }

    /**
     *
     * @param s
     * @param d if d == -2: force reload; if d < 0: get the current day
     */
    public void updateBackground(Service s, int d) {
        int day = d;
        if (s == null) return;
        if (day < 0) {
            calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        Log.d("Nui", "day != lastDay: " + (day != lastDay ? "1" : "0"));
        if (day != lastDay || d == -2) {
            int imgNum = ResourceManager.getBackgroundTodayImgNum();
            if (isSlpt) {
                this.backgroundSlpt = ResourceManager.getBackground8c(s, imgNum);
            } else {
                this.background = ResourceManager.getBackground(s, imgNum);
            }
            lastDay = day;
        }
    }

    @Override
    public List<DataType> getDataTypes() {
        // TODO: maybe this part of code is conflicted with NUI_ALARM_SEC_HAND_UPDATE
        //return Collections.singletonList(DataType.DATE);
        return Collections.emptyList();
    }

    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        List<SlptViewComponent> slpt_objects = new ArrayList<>();

        SlptPictureView background = new SlptPictureView();
        background.setImagePicture(this.backgroundSlpt);
        slpt_objects.add(background);

        Typeface timeTypeFace = ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.ROBOTO);
        SlptLinearLayout dayLayout = new SlptLinearLayout();
        if (day >= 10) dayLayout.add(new SlptDayHView());
        dayLayout.add(new SlptDayLView());
        dayLayout.setTextAttrForAll(DAY_FONT_SIZE, -1, timeTypeFace);
        dayLayout.setStart(
                day < 10 ? (DAY_OFFSET_X + DAY_OFFSET_HALF_CHAR) : DAY_OFFSET_X,
                DAY_OFFSET_Y
        );
        slpt_objects.add(dayLayout);

        return slpt_objects;
    }

    @Override
    public void onDataUpdate(DataType type, Object value) {
        // updateBackground(mainService, -1);
    }

    @Override
    public void draw(Canvas canvas, float width, float height, float centerX, float centerY) {
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        updateBackground(mainService, day);
        canvas.drawBitmap(background, 0f, 0f, mGPaint);

        canvas.drawText(
                String.valueOf(day),
                day < 10 ? (DAY_OFFSET_X + DAY_OFFSET_HALF_CHAR) : DAY_OFFSET_X,
                DAY_OFFSET_Y + DAY_FONT_SIZE - 2,
                this.dateFont
        );
    }
}
