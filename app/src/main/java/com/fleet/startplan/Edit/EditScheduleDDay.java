package com.fleet.startplan.Edit;

import android.app.DatePickerDialog;
import android.content.Context;
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
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditScheduleDDay extends Fragment {

    private EditText mDDayEditText;
    private ImageView mDDayComplete;
    private TextView mCountDay;

    private static String D_DAY_ON_DATE = "onDate";
    private static String D_DAY_OFF_DATE = "offDate";


    private String editSelectedDate, editContents, editEndDate, mHaveDate = "";

    private SetEditScheduleDDayListener mListener;

    public void setOnEditScheduleDDayListener(SetEditScheduleDDayListener mListener) {
        this.mListener = mListener;
    }


    public interface SetEditScheduleDDayListener {
        void UpdateDDayData(String contents, String endDate);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_add_d_day, container, false);


        mDDayEditText = v.findViewById(R.id.et_add_d_day);
        mDDayComplete = v.findViewById(R.id.iv_add_d_day_complete);
        View mButtonDate = v.findViewById(R.id.v_btn_dialog_date);
        mCountDay = v.findViewById(R.id.tv_d_day_count);
        ImageView mIconDate = v.findViewById(R.id.iv_d_day_add_date);

        Bundle extra = this.getArguments();
        if (extra != null) {
            editContents = extra.getString(Information.PUT_CONTENTS);
            editSelectedDate = extra.getString(Information.PUT_REGISTER_DATE);
            int editCountDay = extra.getInt(Information.PUT_GET_COUNT_D_DAY);
            editEndDate = extra.getString(Information.PUT_END_DATE);
            mDDayEditText.setText(editContents);
            mDDayComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
            mCountDay.setText(Integer.toString(editCountDay));
            mIconDate.setVisibility(View.GONE);
            mButtonDate.setBackgroundResource(R.drawable.shape_add_detail_on);
            mHaveDate = D_DAY_ON_DATE;
        }

        openKeyBoard();
        changeCompleteButton();

        mDDayComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDDayEditText.getText().toString().equals("") || mDDayEditText.getText() == null) {
                    Toast.makeText(getActivity(), "목표를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (editEndDate.equals("") || mHaveDate.equals(D_DAY_OFF_DATE)) {
                    Toast.makeText(getActivity(), "목표를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    updateData();
                    Toast.makeText(getActivity(), "목표가 수정됐어요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });


        return v;
    }

    private void updateData() {
        if (mDDayEditText != null) {
            editContents = mDDayEditText.getText().toString();
        }
        mListener.UpdateDDayData(editContents, editEndDate);
    }


    public void openDatePicker() {
        Date to = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            to = format.parse(editSelectedDate);
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
    }

    public DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date to = null;
                try {
                    to = format.parse(editSelectedDate);

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
                editEndDate = endDate;

                Date FirstDate = format.parse(startDate);
                Date SecondDate = format.parse(endDate);
                long calDate = SecondDate.getTime() - FirstDate.getTime();
                long calDateDays = calDate / (24 * 60 * 60 * 1000);

                if (calDateDays > -1) {
                    String resultDay = Long.toString(calDateDays);
                    mCountDay.setText(resultDay);
                    mHaveDate = D_DAY_ON_DATE;
                } else {
                    Toast.makeText(getActivity(), "시작 날짜 이후를 선택해주세요!" + "\n" + "시작 날짜는" + editSelectedDate + "입니다.", Toast.LENGTH_SHORT).show();
                    mHaveDate = D_DAY_OFF_DATE;
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "오류가 발생했어요.", Toast.LENGTH_SHORT).show();
                mHaveDate = D_DAY_OFF_DATE;
            }
        }
    };


    private void openKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mDDayEditText.requestFocus();
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
