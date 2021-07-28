package com.ngxson.pacewatchface.widget;

import android.app.Service;

import com.ingenic.iwds.slpt.view.core.SlptViewComponent;

import java.util.List;

/**
 * Widget with the slpt component
 */
public interface HasSlptViewComponent {

    List<SlptViewComponent> buildSlptViewComponent(Service service);
}
