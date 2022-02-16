package com.fleet.startplan.Schedule;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fleet.startplan.Dialog.DialogAdd;
import com.fleet.startplan.Menu.DialogMenu;
import com.fleet.startplan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Schedule extends Fragment {

    public static String SELECTED_DATE = "selectedDate";

    private TextView mMainDate;

    // 가로 캘린더 라이브러리
    private HorizontalCalendar horizontalCalendar;
    private String mSelectedDate;

    private ImageView mGoToday;

    public SetAnalyticsListener mListener;

    public void setOnAnalyticsListener(SetAnalyticsListener mListener) {
        this.mListener = mListener;
    }

    public interface SetAnalyticsListener {
        void click();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule, container, false);

        mMainDate = v.findViewById(R.id.tv_main_date);
        mGoToday = v.findViewById(R.id.iv_go_today);

        ImageView mSelectDate = v.findViewById(R.id.iv_main_select_date);
        ImageView mGoAnalytics = v.findViewById(R.id.iv_go_analytics);
        ImageView mGoMenu = v.findViewById(R.id.iv_go_menu);


        /* start 2 months ago from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -12);

        /* end after 2 months from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 12);

        // Default Date set to Today.
        final Calendar defaultSelectedDate = Calendar.getInstance();

        horizontalCalendar = new HorizontalCalendar.Builder(v, R.id.calendar_view)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .configure()
                .colorTextTop(Color.parseColor("#6d7278"), Color.parseColor("#6d7278"))
                .colorTextMiddle(Color.parseColor("#11204d"), Color.parseColor("#11204d"))
                .formatTopText("EEE")
                .formatMiddleText("dd")
                .showTopText(true)
                .showBottomText(false)
                .textSize(8f, 16f, 0f)
                .selectorColor(Color.TRANSPARENT)
                .end()
                .defaultSelectedDate(defaultSelectedDate)
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {

            @Override
            public void onDateSelected(Calendar date, int position) {
                String standardDate = getToday();
                String selectedDate;
                mSelectedDate = DateFormat.format("yyyy-MM-dd", date).toString();
                if (mSelectedDate.equals(standardDate)) {
                    selectedDate = DateFormat.format("MMM" + " " + "dd" + "일" + " (오늘)", date).toString();
                } else {
                    selectedDate = DateFormat.format("MMM" + " " + "dd" + "일", date).toString();
                }
                mMainDate.setText(selectedDate);
                changeGoToDayColor(mSelectedDate);
                attachFragmentList(mSelectedDate);
            }
        });

        horizontalCalendar.goToday(true);


        mMainDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalCalendar.goToday(false);
            }
        });

        mGoToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalCalendar.goToday(false);

            }
        });

        mGoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMenu dialogMenu = new DialogMenu();
                dialogMenu.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                dialogMenu.show(getChildFragmentManager(),"DialogMenu");
                Bundle bundle = new Bundle();
                bundle.putString(SELECTED_DATE, mSelectedDate);
                dialogMenu.setArguments(bundle);
                dialogMenu.setOnListRefreshListener(new DialogMenu.SetListRefreshListener() {
                    @Override
                    public void refresh() {
                        attachFragmentList(mSelectedDate);
                    }
                });
            }
        });


        mSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        mGoAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.click();
                }
            }
        });

        return v;
    }

    private void attachFragmentList(String date) {

        ScheduleList scheduleList = new ScheduleList();
        FragmentTransaction mFragmentTransaction = getParentFragmentManager().beginTransaction();
        if (scheduleList.isAdded()) {
            mFragmentTransaction.remove(scheduleList);
            scheduleList = new ScheduleList();
        }
        mFragmentTransaction.setReorderingAllowed(true);
        mFragmentTransaction.replace(R.id.fragment_list, scheduleList, "fragment_list");
        Bundle bundle = new Bundle();
        bundle.putString(SELECTED_DATE, date);
        try {
            scheduleList.setArguments(bundle);
            mFragmentTransaction.commit();
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), "오류가 발생했어요. 죄송합니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
        }
    }


    private String getToday() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(currentTime);
    }

    public void changeGoToDayColor(String date) {
        String standardDate = getToday();
        int changeColor = ContextCompat.getColor(getContext(), R.color.blue_100);
        if (date.equals(standardDate)) {
            mGoToday.setVisibility(View.GONE);
        } else {
            mGoToday.setVisibility(View.VISIBLE);
            mGoToday.setColorFilter(changeColor);
        }
    }


    public void openDatePicker() {
        Date to = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            to = format.parse(mSelectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat y = new SimpleDateFormat("yyyy");
        int yy = Integer.parseInt(y.format(to));
        SimpleDateFormat m = new SimpleDateFormat("MM");
        int mm = Integer.parseInt(m.format(to));
        SimpleDateFormat d = new SimpleDateFormat("dd");
        int dd = Integer.parseInt(d.format(to));
        DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), R.style.DialogTheme, listener, yy, mm - 1, dd);
        mDatePicker.show();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.YEAR, +1);
        mDatePicker.getDatePicker().setMaxDate(ca.getTimeInMillis());


        mDatePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue_100));
    }


    public DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            try {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                if (day < 30) {
                    c.set(Calendar.DAY_OF_MONTH, day);
                } else {
                    c.set(Calendar.DAY_OF_MONTH, day - 1);
                }
                horizontalCalendar.selectDate(c, false);

            } catch (Exception e) {
                Toast.makeText(getContext(), "오류가 발생했어요.", Toast.LENGTH_SHORT).show();

            }
        }
    };


}
