package com.fleet.startplan.Bring;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.fleet.startplan.Analytics.Analytics;
import com.fleet.startplan.R;
import com.fleet.startplan.Schedule.Schedule;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DialogBring extends BottomSheetDialogFragment {

    public static String BRING_YESTERDAY_DATE = "bringYesterdayDate";
    public static String BRING_TOMORROW_DATE = "bringTomorrowDate";


    public static String BRING_STANDARD_DATE = "bringStandardDate";
    public static String BRING_CATEGORY_YESTERDAY = "bringCategory";
    public static String BRING_CATEGORY_TOMORROW = "bringTomorrow";

    private String mSelectedDate;


    public void setOnBringCompleteListener(SetBringCompleteListener mListener) {
        this.mListener = mListener;
    }

    public interface SetBringCompleteListener {
        void complete();
    }

    private SetBringCompleteListener mListener;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_bring, container, false);

        Bundle extra = this.getArguments();
        if (extra != null) {
            mSelectedDate = extra.getString(Schedule.SELECTED_DATE);
        }

        View mBringYesterday = v.findViewById(R.id.v_bring_yesterday);
        View mBringTomorrow = v.findViewById(R.id.v_bring_tomorrow);
        View mBringPickDate = v.findViewById(R.id.v_bring_pick_date);
        TextView mBringHelpText = v.findViewById(R.id.tv_bring_help_date);

        if (mSelectedDate.equals(getToday())) {
            mBringHelpText.setText("( 오늘 )");
        } else {
            mBringHelpText.setText("(" + getDate(Analytics.GET_DATE_MONTH, mSelectedDate) + "월" + getDate(Analytics.GET_DATE_DAY, mSelectedDate) + "일" + ")");
        }
        mBringHelpText.setPaintFlags(mBringHelpText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mBringYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BringYesterday.class);
                intent.putExtra(DialogBring.BRING_YESTERDAY_DATE, calculatorDate(mSelectedDate, BRING_CATEGORY_YESTERDAY));
                intent.putExtra(DialogBring.BRING_STANDARD_DATE, mSelectedDate);
                startActivityResult.launch(intent);
            }
        });

        mBringTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BringTomorrow.class);
                intent.putExtra(DialogBring.BRING_TOMORROW_DATE, calculatorDate(mSelectedDate, BRING_CATEGORY_TOMORROW));
                intent.putExtra(DialogBring.BRING_STANDARD_DATE, mSelectedDate);
                startActivityResult.launch(intent);
            }
        });

        mBringPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BringCalendarPickDate.class);
                intent.putExtra(DialogBring.BRING_STANDARD_DATE, mSelectedDate);
                startActivityResult.launch(intent);
            }
        });


        return v;
    }

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (mListener != null) {
                        mListener.complete();
                    }
                    dismiss();
                }
            });


    private String calculatorDate(String standardDate, String pos) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date standard = format.parse(standardDate);
            Calendar calendar = Calendar.getInstance();
            if (standard != null) {
                calendar.setTime(standard);
            }
            if (pos.equals(BRING_CATEGORY_YESTERDAY)) {
                calendar.add(Calendar.DATE, -1);
            } else if (pos.equals(BRING_CATEGORY_TOMORROW)) {
                calendar.add(Calendar.DATE, +1);
            }
            String result = format.format(calendar.getTime());
            return (String) (result);
        } catch (ParseException e) {
            return "";
        }
    }

    private String getToday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        return format.format(c.getTime());
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

}
