package com.fleet.startplan.Analytics;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;

import androidx.core.content.ContextCompat;

import com.fleet.startplan.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class CircleDecorator implements DayViewDecorator {

    private final Drawable drawable;

    private final CalendarDay dates;
    private String decoratorType = "";

    public CircleDecorator(CalendarDay dates, Activity context, String type) {
        decoratorType = type;
        if (decoratorType.equals(Analytics.SUM_COMPLETE_100)) {
            drawable = ContextCompat.getDrawable(context, R.drawable.shape_analytics_circle_inset);
        } else if (decoratorType.equals(Analytics.SUM_COMPLETE_50)) {
            drawable = ContextCompat.getDrawable(context, R.drawable.shape_analytics_stroke_inset);
        } else {
            drawable = ContextCompat.getDrawable(context, R.drawable.shape_analytics_dot_inset);
        }
        this.dates = dates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        if (decoratorType.equals(Analytics.SUM_COMPLETE_100)) {
            view.setBackgroundDrawable(drawable);
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
        } else if (decoratorType.equals(Analytics.SUM_COMPLETE_50)) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.addSpan(new DotSpan(10, Color.parseColor("#ffe39d")));
        }
    }
}
