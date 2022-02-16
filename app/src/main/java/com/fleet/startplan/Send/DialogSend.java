package com.fleet.startplan.Send;

import android.content.Intent;
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

public class DialogSend extends BottomSheetDialogFragment {


    public static String SEND_YESTERDAY_DATE = "sendYesterdayDate";
    public static String SEND_TOMORROW_DATE = "sendTomorrowDate";
    public static String SEND_SELECTED_DATE = "sendSelectedDate";
    public static String SEND_CATEGORY_YESTERDAY = "sendCategory";
    public static String SEND_CATEGORY_TOMORROW = "sendTomorrow";

    private String mSelectedDate;

    public interface SetSendCompleteListener {
        void complete();
    }

    private SetSendCompleteListener mListener;

    public void setOnSendCompleteListener(SetSendCompleteListener mListener) {
        this.mListener = mListener;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_send, container, false);

        Bundle extra = this.getArguments();
        if (extra != null) {
            mSelectedDate = extra.getString(Schedule.SELECTED_DATE);
        }

        View mSendYesterday = v.findViewById(R.id.v_send_yesterday);
        View mSendTomorrow = v.findViewById(R.id.v_send_tomorrow);
        View mSendPickDate = v.findViewById(R.id.v_send_pick_date);
        TextView mSendHelpText = v.findViewById(R.id.tv_send_help_date);

        if (mSelectedDate.equals(getToday())) {
            mSendHelpText.setText("( 오늘 )");
        } else {
            mSendHelpText.setText("(" + getDate(Analytics.GET_DATE_MONTH, mSelectedDate) + "월" + getDate(Analytics.GET_DATE_DAY, mSelectedDate) + "일" + ")");
        }

        mSendYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SendYesterday.class);
                intent.putExtra(DialogSend.SEND_SELECTED_DATE, mSelectedDate);
                intent.putExtra(DialogSend.SEND_YESTERDAY_DATE, calculatorDate(mSelectedDate, SEND_CATEGORY_YESTERDAY));
                startActivityResult.launch(intent);
            }
        });

        mSendTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SendTomorrow.class);
                intent.putExtra(DialogSend.SEND_SELECTED_DATE, mSelectedDate);
                intent.putExtra(DialogSend.SEND_TOMORROW_DATE, calculatorDate(mSelectedDate, SEND_CATEGORY_TOMORROW));
                startActivityResult.launch(intent);
            }
        });

        mSendPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SendCalendarPickDate.class);
                intent.putExtra(DialogSend.SEND_SELECTED_DATE,mSelectedDate);
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
            if (pos.equals(SEND_CATEGORY_YESTERDAY)) {
                calendar.add(Calendar.DATE, -1);
            } else if (pos.equals(SEND_CATEGORY_TOMORROW)) {
                calendar.add(Calendar.DATE, +1);
            }
            String result = format.format(calendar.getTime());
            return (String) (result);
        } catch (ParseException e) {
            return "";
        }
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


    private String getToday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        return format.format(c.getTime());
    }
}
