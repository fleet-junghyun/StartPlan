package com.fleet.startplan.Send;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.fleet.startplan.Analytics.Analytics;
import com.fleet.startplan.Analytics.SaturdayDecorator;
import com.fleet.startplan.Analytics.SundayDecorator;
import com.fleet.startplan.Analytics.TodayDecorator;
import com.fleet.startplan.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.threeten.bp.DayOfWeek;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SendCalendarPickDate extends AppCompatActivity {


    private String mStandardDate;

    private TextView mSubTitle, mMonthTitle;

    MaterialCalendarView mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);

        mCalendar = findViewById(R.id.pick_date_calendarView);
        mSubTitle = findViewById(R.id.tv_pick_date_sub_title);
        mMonthTitle = findViewById(R.id.tv_bring_pick_date_month);
        ImageView mPickDateExit = findViewById(R.id.iv_bring_pick_date_exit);


        //탑바 삭제(BOOLEAN)
        mCalendar.setTopbarVisible(false);
        mCalendar.addDecorator(new SundayDecorator());
        mCalendar.addDecorator(new SaturdayDecorator());
        mCalendar.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        mCalendar.setWeekDayTextAppearance(R.style.CustomTextAppearance);
        mCalendar.setDateTextAppearance(R.style.CustomText);


        Intent intent = getIntent();
        if (intent != null) {
            mStandardDate = intent.getStringExtra(DialogSend.SEND_SELECTED_DATE);
        }

        int year = getDate(Analytics.GET_DATE_YEAR, mStandardDate);
        int month = getDate(Analytics.GET_DATE_MONTH, mStandardDate);
        mMonthTitle.setText(month + "월");
        int day = getDate(Analytics.GET_DATE_DAY, mStandardDate);
        mCalendar.setSelectedDate(CalendarDay.from(year,month,day));


        TodayDecorator todayDecorator = new TodayDecorator(CalendarDay.from(year, month, day), SendCalendarPickDate.this);
        mCalendar.addDecorator(todayDecorator);

        mCalendar.state().edit().setFirstDayOfWeek(DayOfWeek.SUNDAY).
                setMinimumDate(CalendarDay.from(year - 1, month, day)).
                setMaximumDate(CalendarDay.from(year + 1, month, day)).
                setCalendarDisplayMode(CalendarMode.MONTHS).
                commit();

        mCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String ymd = getStandardDate(date);
                attachList(ymd, date, mStandardDate);
                mMonthTitle.setText(date.getMonth() + "월");
            }
        });

        mCalendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                mMonthTitle.setText(date.getMonth() + "월");
            }
        });

        mPickDateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private int getDate(String pos, String checkDate) {
        String checkYear = checkDate.split("\\s")[0];
        if (pos.equals(Analytics.GET_DATE_YEAR)) {
            String year = checkYear.split("-")[0];
            return Integer.parseInt(year);
        } else if (pos.equals(Analytics.GET_DATE_MONTH)) {
            String month = checkYear.split("-")[1];
            return Integer.parseInt(month);
        } else {
            String day = checkYear.split("-")[2];
            return Integer.parseInt(day);
        }
    }


    private void attachList(String date, CalendarDay calendarDay, String mStandardDate) {
        SendPickDateList sendPickDateList = new SendPickDateList();
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (sendPickDateList.isAdded()) {
            mFragmentTransaction.remove(sendPickDateList);
            sendPickDateList = new SendPickDateList();
        }
        mFragmentTransaction.setReorderingAllowed(true);
        mFragmentTransaction.replace(R.id.fragment_pick_date_list, sendPickDateList, "pickDate_list");
        sendPickDateList.setOnFinishListener(new SendPickDateList.SetFinishListener() {
            @Override
            public void complete() {
                finish();
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(SendPickDateList.PICK_DATE, date);
        bundle.putString(SendPickDateList.SELECTED_PICK_DATE, mStandardDate);
        try {
            sendPickDateList.setArguments(bundle);
            mFragmentTransaction.commit();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "오류가 발생했어요. 죄송합니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
        }
        if (date.equals(getToday())) {
            mSubTitle.setText(calendarDay.getMonth() + "월" + " " + calendarDay.getDay() + "일" + "(오늘로 보냅니다)");
        } else {
            mSubTitle.setText(calendarDay.getMonth() + "월" + " " + calendarDay.getDay() + "일");
        }
    }

    private String getStandardDate(CalendarDay calendarDay) {
        int y = calendarDay.getYear();
        int m = calendarDay.getMonth();
        int d = calendarDay.getDay();
        String sy = Integer.toString(y);
        String sm = "";
        String sd = "";
        if (m < 10) {
            sm = "0" + Integer.toString(m);
        } else {
            sm = Integer.toString(m);
        }
        if (d < 10) {
            sd = "0" + Integer.toString(d);
        } else {
            sd = Integer.toString(d);
        }
        String ymd = sy + "-" + sm + "-" + sd;
        return ymd;
    }

    private String getToday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        return format.format(c.getTime());
    }
}
