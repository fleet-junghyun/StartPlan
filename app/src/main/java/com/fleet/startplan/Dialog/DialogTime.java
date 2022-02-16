package com.fleet.startplan.Dialog;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import com.fleet.startplan.Model.Information;
import com.fleet.startplan.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class DialogTime extends BottomSheetDialogFragment {

    private static String POS_START = "start";
    private static String POS_END = "end";

    private TextView mStartTime, mEndTime;

    private int mStartHour, mStartMinute, mEndHour, mEndMinute = 0;

    private OnTimeSetListener mListener = null;

    public void setOnTimeSetListener(OnTimeSetListener listener) {
        mListener = listener;
    }

    public interface OnTimeSetListener {
        void onTimeSet(int sHour, int sMinute, int eHour, int eMinute);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time, container, false);

        View mTimeStartBtn = view.findViewById(R.id.v_time_start_dialog);
        View mTimeEndBtn = view.findViewById(R.id.v_time_end_dialog);
        mStartTime = view.findViewById(R.id.tv_start_time);
        mEndTime = view.findViewById(R.id.tv_end_time);
        Button mTimeCompleted = view.findViewById(R.id.btn_time_dialog_completed);

        Bundle extra = this.getArguments();
        if (extra != null) {
            mStartHour = extra.getInt(Information.PUT_START_HOUR);
            mStartMinute = extra.getInt(Information.PUT_START_MINUTE);
            mEndHour = extra.getInt(Information.PUT_END_HOUR);
            mEndMinute = extra.getInt(Information.PUT_END_MINUTE);
        } else {
            Calendar mCurrentTime = Calendar.getInstance();
            int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mCurrentTime.get(Calendar.MINUTE);
            if (minute == 0) {
                mStartHour = hour;
                mStartMinute = 0;
                mEndHour = hour + 1;
            } else {
                mStartHour = hour + 1;
                mStartMinute = 0;
                mEndHour = hour + 2;
            }
            mEndMinute = 0;
        }


        setTime(POS_START, mStartHour, mStartMinute);
        setTime(POS_END, mEndHour, mEndMinute);

        mTimeStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(POS_START);
            }
        });

        mTimeEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(POS_END);
            }
        });

        mTimeCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onTimeSet(mStartHour, mStartMinute, mEndHour, mEndMinute);
                }
                dismiss();
            }
        });

        return view;
    }


    private int getHour(String pos) {
        if (pos.equals(POS_START))
            return mStartHour;
        else
            return mEndHour;
    }

    private int getMinute(String pos) {
        if (pos.equals(POS_START))
            return mStartMinute;
        else
            return mEndMinute;
    }

    private String makeTimeText(int hour, int minute) {
        String am_pm = "";
        int tHour = hour;
        if (hour > 12) {
            am_pm = "PM";
            tHour = hour - 12;
        } else if (hour == 12) {
            am_pm = "PM";
        } else {
            am_pm = "AM";
        }
        return String.format("%02d:%02d %s", tHour, minute, am_pm);
    }

    ///이쪽에서 문제가 발생함
    private void setTime(String pos, int hour, int minute) {
        String timeText = makeTimeText(hour, minute);
        String endText = makeTimeText(hour +1, minute);
        if (pos.equals(POS_START)) {
            mStartHour = hour;
            mStartMinute = minute;
            mStartTime.setText(timeText);
            mEndHour = hour +1;
            mEndMinute = minute;
            mEndTime.setText(endText);

        } else {
            mEndHour = hour;
            mEndMinute = minute;
            mEndTime.setText(timeText);
        }
    }

    private void openTimePicker(String pos) {
        TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), R.style.TimePicker, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                setTime(pos, hour, minute);
            }
        }, getHour(pos), getMinute(pos), false);//Yes 24 hour time
        mTimePicker.show();
        mTimePicker.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue_100));
    }

}
