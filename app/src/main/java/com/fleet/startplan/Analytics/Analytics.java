package com.fleet.startplan.Analytics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Model.AnalyticsItem;
import com.fleet.startplan.Model.CompleteItem;
import com.fleet.startplan.Model.SumItem;
import com.fleet.startplan.R;
import com.fleet.startplan.Schedule.Schedule;
import com.fleet.startplan.Schedule.ScheduleList;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.threeten.bp.DayOfWeek;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Analytics extends Fragment {

    public static String GET_DATE_YEAR = "year";
    public static String GET_DATE_MONTH = "month";
    public static String GET_DATE_DAY = "day";
    public static String SUM_COMPLETE_100 = "sumComplete100";
    public static String SUM_COMPLETE_50 = "sumComplete50";
    public static String SUM_COMPLETE_0 = "sumComplete0";

    public SetScheduleListener mListener;

    public void setOnScheduleListener(SetScheduleListener mListener) {
        this.mListener = mListener;
    }

    public interface SetScheduleListener {
        void click();
    }


    MaterialCalendarView materialCalendarView;

    private TextView mMonthTitle, mSubTitle, mGoToday;
    private SPDBHelper mDBHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.analytics, container, false);


        ImageView mGoSchedule = v.findViewById(R.id.iv_analytics_go_schedule);
        materialCalendarView = v.findViewById(R.id.calendarView);
        mMonthTitle = v.findViewById(R.id.iv_analytics_top_title);
        mSubTitle = v.findViewById(R.id.tv_analytics_sub_title);
        mGoToday = v.findViewById(R.id.tv_analytics_go_today);

        //탑바 삭제(BOOLEAN)
        materialCalendarView.setTopbarVisible(false);
        //주말 text => blue , red;
        materialCalendarView.addDecorator(new SundayDecorator());
        materialCalendarView.addDecorator(new SaturdayDecorator());

        //이번달 날짜외의 날짜를 보여주는 코드 (보여주지 않음 설정)
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        //요일 텍스트
        materialCalendarView.setWeekDayTextAppearance(R.style.CustomTextAppearance);

        //day text
        materialCalendarView.setDateTextAppearance(R.style.CustomText);

        TodayDecorator todayDecorator = new TodayDecorator(CalendarDay.today(), getActivity());
        materialCalendarView.addDecorator(todayDecorator);

        initAnalytics();


        mMonthTitle.setText(CalendarDay.today().getMonth() + "월");

        String today = getToday();
        int year = getDate(GET_DATE_YEAR, today);
        int month = getDate(GET_DATE_MONTH, today);
        int day = getDate(GET_DATE_DAY, today);

        materialCalendarView.state().edit().setFirstDayOfWeek(DayOfWeek.SUNDAY).
                setMinimumDate(CalendarDay.from(year - 1, month, day)).
                setMaximumDate(CalendarDay.from(year + 1, month, day)).
                setCalendarDisplayMode(CalendarMode.MONTHS).
                commit();

        materialCalendarView.setSelectedDate(CalendarDay.today());


        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //날짜 클릭하면 호출되는 함수
                String ymd = getStandardDate(date);
                attachList(ymd, date);
            }
        });

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                //페이저로 이동하면 호출되는 함수
                mMonthTitle.setText(date.getMonth() + "월");
            }
        });


        mGoSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.click();
                }
            }
        });

        mGoToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialCalendarView.setCurrentDate(CalendarDay.today(), true);
                materialCalendarView.setSelectedDate(CalendarDay.today());
                String standardDate = getStandardDate(CalendarDay.today());
                attachList(standardDate, CalendarDay.today());
            }
        });

        return v;
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
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = simpleDateFormat.format(currentTime);
        return date;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mDBHelper = new SPDBHelper(getActivity().getApplicationContext());
        super.onCreate(savedInstanceState);
    }

    private void initAnalytics() {
        ArrayList<AnalyticsItem> analyticsItems = mDBHelper.getScheduleFromAnalytics();
        ArrayList<SumItem> sumItems = new ArrayList<>();

        for (int i = 0; i < analyticsItems.size(); i++) {
            ArrayList<Integer> completeList = new ArrayList<>();
            AnalyticsItem analyticsItem = analyticsItems.get(i);
            String registerDate = analyticsItem.get_registerDate();
            ArrayList<CompleteItem> completeItems = mDBHelper.searchCompleteInAnalytics(registerDate);

            for (int ii = 0; ii < completeItems.size(); ii++) {
                CompleteItem completeItem = completeItems.get(ii);
                int sumComplete = completeItem.get_complete();
                completeList.add(sumComplete);
            }

            int sum = 0;
            for (int num : completeList) {
                sum += num;
            }

            sumItems.add(new SumItem(registerDate, sum, completeItems.size()));
        }

        for (int i = 0; i < sumItems.size(); i++) {
            SumItem sumItem = sumItems.get(i);
            String registerDate = sumItem.get_registerDate();
            int todoSize = sumItem.get_todoSize();
            if (todoSize != 0) {
                double percents;
                percents = (double) sumItem.get_complete() / (double) sumItem.get_todoSize();

                int year = getDate(GET_DATE_YEAR, registerDate);
                int month = getDate(GET_DATE_MONTH, registerDate);
                int day = getDate(GET_DATE_DAY, registerDate);
                CalendarDay calendarDay = CalendarDay.from(year, month, day);

                if (percents == 1) {
                    CircleDecorator circleDecorator = new CircleDecorator(calendarDay, getActivity(), Analytics.SUM_COMPLETE_100);
                    materialCalendarView.addDecorator(circleDecorator);
                } else if (percents > 0) {
                    CircleDecorator circleDecorator = new CircleDecorator(calendarDay, getActivity(), Analytics.SUM_COMPLETE_50);
                    materialCalendarView.addDecorator(circleDecorator);
                } else {
                    CircleDecorator circleDecorator = new CircleDecorator(calendarDay, getActivity(), Analytics.SUM_COMPLETE_0);
                    materialCalendarView.addDecorator(circleDecorator);
                }
            }
        }
    }

    private void attachList(String date, CalendarDay calendarDay) {
        AnalyticsList analyticsList = new AnalyticsList();
        FragmentTransaction mFragmentTransaction = getParentFragmentManager().beginTransaction();
        if (analyticsList.isAdded()) {
            mFragmentTransaction.remove(analyticsList);
            analyticsList = new AnalyticsList();
        }
        mFragmentTransaction.setReorderingAllowed(true);
        mFragmentTransaction.replace(R.id.fragment_analytics_list, analyticsList, "analytics_list");
        Bundle bundle = new Bundle();
        bundle.putString(Schedule.SELECTED_DATE, date);
        try {
            analyticsList.setArguments(bundle);
            mFragmentTransaction.commit();
        } catch (IllegalStateException e) {
            Toast.makeText(getContext(), "오류가 발생했어요. 죄송합니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
        }

        mSubTitle.setText(calendarDay.getMonth() + "월" + " " + calendarDay.getDay() + "일");

    }


    private int getDate(String pos, String checkDate) {
        String checkYear = checkDate.split("\\s")[0];
        if (pos.equals(GET_DATE_YEAR)) {
            String year = checkYear.split("-")[0];
            return Integer.parseInt(year);
        } else if (pos.equals(GET_DATE_MONTH)) {
            String month = checkYear.split("-")[1];
            return Integer.parseInt(month);
        } else {
            String day = checkYear.split("-")[2];
            return Integer.parseInt(day);
        }
    }

    @Override
    public void onResume() {
        String standardDate = getStandardDate(materialCalendarView.getSelectedDate());
        attachList(standardDate, materialCalendarView.getSelectedDate());
        super.onResume();
    }
}
