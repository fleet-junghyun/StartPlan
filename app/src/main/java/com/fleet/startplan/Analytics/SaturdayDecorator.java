package com.fleet.startplan.Analytics;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.DayOfWeek;

public class SaturdayDecorator implements DayViewDecorator {

    public SaturdayDecorator() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        int sunday = day.getDate().with(DayOfWeek.SATURDAY).getDayOfMonth();
        return sunday == day.getDay();
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLUE));
    }
}
