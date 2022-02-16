package com.fleet.startplan.Schedule;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.fleet.startplan.Activity.MainActivity;
import com.fleet.startplan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleDDay extends Fragment {

    public static String D_DAY_ON_DATE = "onDate";
    public static String D_DAY_OFF_DATE = "offDate";

    private EditText mDDayEditText;
    private ImageView mDDayComplete;
    private View mButtonDate;

    private String mSelectedDate, mEndDate, mHaveDate, mDdayContents = "";
    private TextView mCountDay;
    private ImageView mIconDate;

    private SetScheduleDDayListener mListener;

    public void setOnScheduleDDayListener(SetScheduleDDayListener mListener) {
        this.mListener = mListener;
    }

    public interface SetScheduleDDayListener {
        void saveDDayData(String contents, String endDate);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_add_d_day, container, false);

        mHaveDate = D_DAY_OFF_DATE;

        mDDayEditText = view.findViewById(R.id.et_add_d_day);
        mDDayComplete = view.findViewById(R.id.iv_add_d_day_complete);
        mButtonDate = view.findViewById(R.id.v_btn_dialog_date);
        mCountDay = view.findViewById(R.id.tv_d_day_count);
        mIconDate = view.findViewById(R.id.iv_d_day_add_date);

        Bundle extra = this.getArguments();
        if (extra != null) {
            mSelectedDate = extra.getString(Schedule.SELECTED_DATE);
        }

        openKeyBoard();
        changeCompleteButton();


        mDDayComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDDayEditText.getText().toString().equals("") || mDDayEditText.getText() == null) {
                    Toast.makeText(getActivity(), "목표를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (mHaveDate.equals(D_DAY_OFF_DATE) || mEndDate.equals("")) {
                    Toast.makeText(getActivity(), "목표 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    saveData();
                    initDateBtn();
                    Toast.makeText(getActivity(), "목표가 추가됐어요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHaveDate.equals(D_DAY_OFF_DATE)) {
                    openDatePicker();
                } else {
                    initDateBtn();
                }
            }
        });

        return view;
    }

    private void saveData() {
        if (mDDayEditText != null) {
            mDdayContents = mDDayEditText.getText().toString();
        }
        mListener.saveDDayData(mDdayContents, mEndDate);
        mDDayEditText.setText(null);
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
        mDatePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue_100));

        mDatePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "취소", (dialog, which) -> {
            if (which == DialogInterface.BUTTON_NEGATIVE) {
                initDateBtn();
            }
        });


    }


    public DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date to = null;
                try {
                    to = format.parse(mSelectedDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String startDate = format.format(to);
                String eYear = Integer.toString(year);
                String eMonth = String.format("%02d", month + 1);
                String eDay = String.format("%02d", day);

                String pickDate = eYear + eMonth + eDay;

                SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");

                Date formatDate = dtFormat.parse(pickDate);
                String endDate = format.format(formatDate);

                mEndDate = endDate;

                Date FirstDate = format.parse(startDate);
                Date SecondDate = format.parse(endDate);
                long calDate = SecondDate.getTime() - FirstDate.getTime();
                long calDateDays = calDate / (24 * 60 * 60 * 1000);


                if (calDateDays > -1) {
                    String resultDay = Long.toString(calDateDays);
                    mCountDay.setText(resultDay);
                    mIconDate.setVisibility(View.GONE);
                    mButtonDate.setBackgroundResource(R.drawable.shape_add_detail_on);
                    mCountDay.setVisibility(View.VISIBLE);
                    mHaveDate = D_DAY_ON_DATE;
                } else {
                    Toast.makeText(getActivity(), "시작 날짜 이후를 선택해주세요!", Toast.LENGTH_SHORT).show();
                    mHaveDate = D_DAY_OFF_DATE;
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "오류가 발생했어요.", Toast.LENGTH_SHORT).show();
                mHaveDate = D_DAY_OFF_DATE;
            }
        }
    };

    private void initDateBtn() {
        mEndDate = "";
        mIconDate.setVisibility(View.VISIBLE);
        mButtonDate.setBackgroundResource(R.drawable.shape_add_detail_off);
        mCountDay.setVisibility(View.GONE);
        mHaveDate = D_DAY_OFF_DATE;
    }

    private void changeCompleteButton() {

        mDDayEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mDDayComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mDDayComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mDDayComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mDDayComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }


    private void openKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mDDayEditText.requestFocus();
    }

    @Override
    public void onDestroyView() {
        if (mDDayEditText != null && mDDayEditText.isFocused()) {
            mDDayEditText.clearFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mDDayEditText.getWindowToken(), 0);
        }
        super.onDestroyView();
    }


}