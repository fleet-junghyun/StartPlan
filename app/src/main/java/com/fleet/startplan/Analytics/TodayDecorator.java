package com.fleet.startplan.Analytics;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.fleet.startplan.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class TodayDecorator implements DayViewDecorator {

    private final Drawable drawable;

    private final CalendarDay dates;

    public TodayDecorator(CalendarDay dates, Activity context) {
        drawable = ContextCompat.getDrawable(context, R.drawable.shape_analytics_today_circle_inset);
        this.dates = dates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }
}
