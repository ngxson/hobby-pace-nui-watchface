package com.ngxson.pacewatchface.widget;

import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.analog.SlptAnalogHourView;
import com.ingenic.iwds.slpt.view.analog.SlptAnalogMinuteView;
import com.ingenic.iwds.slpt.view.analog.SlptAnalogSecondView;
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout;
import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ingenic.iwds.slpt.view.digital.SlptDayHView;
import com.ingenic.iwds.slpt.view.digital.SlptDayLView;
import com.ingenic.iwds.slpt.view.utils.SimpleFile;
import com.ngxson.pacewatchface.PaceWatchFace;
import com.ngxson.pacewatchface.PaceWatchFaceSplt;
import com.ngxson.pacewatchface.R;
import com.ngxson.pacewatchface.Utils;
import com.ngxson.pacewatchface.resource.ResourceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NuiClock extends AnalogClockWidget {
    private Bitmap hrHand;
    private Bitmap mnHand;
    private Bitmap secHand;
    private Calendar calendar;
    public static PaceWatchFaceSplt mainService;

    public NuiClock(PaceWatchFaceSplt mainService) {
        super();
        if (mainService != null) NuiClock.mainService = mainService;
    }

    @Override
    public void init(Service service) {
        this.hrHand = Util.decodeImage(service.getResources(),"nui_hands/hr.png");
        this.mnHand = Util.decodeImage(service.getResources(),"nui_hands/min.png");
        this.secHand = Util.decodeImage(service.getResources(),"nui_hands/sec.png");
    }

    @Override
    public void onDrawAnalog(Canvas canvas, float width, float height, float centerX, float centerY, float secRot, float minRot, float hrRot) {
        calendar = Calendar.getInstance();
        int hr = calendar.get(Calendar.HOUR_OF_DAY) % 12;
        int min = calendar.get(Calendar.MINUTE);
        int hrHandRot = hr * 30 + min / 2;

        canvas.save();
        canvas.rotate(hrHandRot, centerX, centerY);
        canvas.drawBitmap(hrHand, centerX - hrHand.getWidth() / 2f, centerY - hrHand.getHeight() / 2f, null);
        canvas.restore();

        canvas.save();
        canvas.rotate(minRot, centerX, centerY);
        canvas.drawBitmap(mnHand, centerX - mnHand.getWidth() / 2f, centerY - mnHand.getHeight() / 2f, null);
        canvas.restore();

        if (Utils.shouldShowSecondHand(calendar)) {
            canvas.save();
            canvas.rotate(secRot, centerX, centerY);
            canvas.drawBitmap(secHand, centerX - secHand.getWidth() / 2f, centerY - secHand.getHeight() / 2f, null);
            canvas.restore();
        }
    }

    public List<SlptViewComponent> buildSlptViewComponent(Service service, boolean isHighRes) {
        List<SlptViewComponent> slpt_objects = new ArrayList<>();

        SlptAnalogHourView slptAnalogHourView = new SlptAnalogHourView();
        slptAnalogHourView.setImagePicture(SimpleFile.readFileFromAssets(service, isHighRes ? "nui_hands/hr.png" : "nui_hands_8c/hr.png"));
        slptAnalogHourView.alignX = (byte) 2;
        slptAnalogHourView.alignY = (byte) 2;
        slptAnalogHourView.setRect(320, 320);
        slpt_objects.add(slptAnalogHourView);

        SlptAnalogMinuteView slptAnalogMinuteView = new SlptAnalogMinuteView();
        slptAnalogMinuteView.setImagePicture(SimpleFile.readFileFromAssets(service, isHighRes ? "nui_hands/min.png" : "nui_hands_8c/min.png"));
        slptAnalogMinuteView.alignX = (byte) 2;
        slptAnalogMinuteView.alignY = (byte) 2;
        slptAnalogMinuteView.setRect(320, 320);
        slpt_objects.add(slptAnalogMinuteView);

        if (Utils.shouldShowSecondHand()) {
            SlptAnalogSecondView slptAnalogSecondView = new SlptAnalogSecondView();
            slptAnalogSecondView.setImagePicture(SimpleFile.readFileFromAssets(service, isHighRes ? "nui_hands/sec.png" : "nui_hands_8c/sec.png"));
            slptAnalogSecondView.alignX = (byte) 2;
            slptAnalogSecondView.alignY = (byte) 2;
            slptAnalogSecondView.setRect(320, 320);
            slpt_objects.add(slptAnalogSecondView);
        }

        return slpt_objects;
    }

    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        return buildSlptViewComponent(service, false);
    }
}
