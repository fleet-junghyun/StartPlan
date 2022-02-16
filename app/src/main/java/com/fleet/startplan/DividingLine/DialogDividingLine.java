package com.fleet.startplan.DividingLine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.Model.ScheduleItem;
import com.fleet.startplan.R;
import com.fleet.startplan.Schedule.Schedule;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class DialogDividingLine extends BottomSheetDialogFragment {

    private static String CATEGORY_AM_PM = "categoryAmPm";
    private static String CATEGORY_WHEN = "categoryWhen";
    public static String DIVIDING_LINE_AM = "dividingLineAm";
    public static String DIVIDING_LINE_PM = "dividingLinePm";
    public static String DIVIDING_LINE_MORNING = "dividingLineMorning";
    public static String DIVIDING_LINE_LUNCH = "dividingLineLunch";
    public static String DIVIDING_LINE_DINNER = "dividingLineDinner";
    public static String DIVIDING_LINE_DAWN = "dividingLineDawn";

    private SPDBHelper mDBHelper;
    private String mSelectedDate;

    public interface SetAddCompleteListener {
        void add();
    }

    private SetAddCompleteListener mListener = null;

    public void setAddCompleteListener(SetAddCompleteListener mListener) {
        this.mListener = mListener;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_dividing_line, container, false);

        Bundle extra = this.getArguments();
        if (extra != null) {
            mSelectedDate = extra.getString(Schedule.SELECTED_DATE);
        }

        mDBHelper = new SPDBHelper(getContext());

        View mDividingLineAm = v.findViewById(R.id.v_dividing_line_am);
        View mDividingLinePm = v.findViewById(R.id.v_dividing_line_pm);
        View mDividingLineMorning = v.findViewById(R.id.v_dividing_line_morning);
        View mDividingLineLunch = v.findViewById(R.id.v_dividing_line_lunch);
        View mDividingLineDinner = v.findViewById(R.id.v_dividing_line_dinner);
        View mDividingLineDawn = v.findViewById(R.id.v_dividing_line_dawn);


        mDividingLineAm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDividingLine(CATEGORY_AM_PM, DIVIDING_LINE_AM);
            }
        });
        mDividingLinePm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDividingLine(CATEGORY_AM_PM, DIVIDING_LINE_PM);
            }
        });
        mDividingLineMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDividingLine(CATEGORY_WHEN, DIVIDING_LINE_MORNING);
            }
        });
        mDividingLineLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDividingLine(CATEGORY_WHEN, DIVIDING_LINE_LUNCH);
            }
        });
        mDividingLineDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDividingLine(CATEGORY_WHEN, DIVIDING_LINE_DINNER);
            }
        });
        mDividingLineDawn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDividingLine(CATEGORY_WHEN, DIVIDING_LINE_DAWN);
            }
        });
        return v;
    }


    private void saveDividingLine(String pos, String when) {
        int scheduleListPos;
        int lastID = 0;
        ArrayList<ScheduleItem> scItems = mDBHelper.getScheduleItems();
        if (scItems.isEmpty()) {
            lastID = 1;
        } else {
            ScheduleItem item = scItems.get(mDBHelper.getScheduleItems().size() - 1);
            //현재 추가되는 ID
            lastID = item.getScId() + 1;
        }

        scheduleListPos = lastID;
        if (pos.equals(CATEGORY_AM_PM)) {
            if (when.equals(DIVIDING_LINE_AM)) {
                mDBHelper.insertSchedule("", "오전", Information.CATEGORY_DIVIDING_LINE_AM_PM, 0, "", scheduleListPos, 0,
                        0, 0, 0, "", "", 0, mSelectedDate, "");
                Toast.makeText(getContext(), "구분선 \"오전\"이 추가됐어요.", Toast.LENGTH_SHORT).show();
            } else {
                mDBHelper.insertSchedule("", "오후", Information.CATEGORY_DIVIDING_LINE_AM_PM, 0, "", scheduleListPos, 0,
                        0, 0, 0, "", "", 0, mSelectedDate, "");
                Toast.makeText(getContext(), "구분선 \"오전\"가 추가됐어요.", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (when.equals(DIVIDING_LINE_MORNING)) {
                mDBHelper.insertSchedule("☀️", "아침", Information.CATEGORY_DIVIDING_LINE_WHEN, 0, "", scheduleListPos, 0,
                        0, 0, 0, "", "", 0, mSelectedDate, "");
                Toast.makeText(getContext(), "구분선 ☀ \"아침\"이 추가됐어요.", Toast.LENGTH_SHORT).show();
            } else if (when.equals(DIVIDING_LINE_LUNCH)) {
                mDBHelper.insertSchedule("⛅", "점심", Information.CATEGORY_DIVIDING_LINE_WHEN, 0, "", scheduleListPos, 0,
                        0, 0, 0, "", "", 0, mSelectedDate, "");
                Toast.makeText(getContext(), "구분선 ⛅ \"점심\"이 추가됐어요.", Toast.LENGTH_SHORT).show();
            } else if (when.equals(DIVIDING_LINE_DINNER)) {
                mDBHelper.insertSchedule("\uD83C\uDF15", "저녁", Information.CATEGORY_DIVIDING_LINE_WHEN, 0, "", scheduleListPos, 0,
                        0, 0, 0, "", "", 0, mSelectedDate, "");
                Toast.makeText(getContext(), "구분선 \uD83C\uDF15 \"저녁\"이 추가됐어요.", Toast.LENGTH_SHORT).show();
            } else {
                mDBHelper.insertSchedule("\uD83C\uDF05", "새벽", Information.CATEGORY_DIVIDING_LINE_WHEN, 0, "", scheduleListPos, 0,
                        0, 0, 0, "", "", 0, mSelectedDate, "");
                Toast.makeText(getContext(), "구분선 \uD83C\uDF05 \"새벽\"이 추가됐어요.", Toast.LENGTH_SHORT).show();
            }
        }
        if (mListener != null) {
            mListener.add();
        }


    }

}
