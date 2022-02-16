package com.fleet.startplan.Edit;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fleet.startplan.Dialog.DialogAdd;
import com.fleet.startplan.Dialog.DialogTime;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.R;

public class EditScheduleTime extends Fragment {

    private EditText mTimeContents;
    private ImageView mTimeComplete;
    private int mEditStartHour, mEditStartMinute, mEditEndHour, mEditEndMinute = 0;

    private SetEditScheduleTimeListener mListener;

    public void setOnEditScheduleTimeListener(SetEditScheduleTimeListener mListener) {
        this.mListener = mListener;
    }

    public interface SetEditScheduleTimeListener {
        void UpdateTimeData(int startHour, int startMinute, int endHour, int endMinute, String contents);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_add_time, container, false);

        View mTimeDialog = v.findViewById(R.id.v_btn_dialog_time);
        mTimeContents = v.findViewById(R.id.et_add_time);
        mTimeComplete = v.findViewById(R.id.iv_add_time_complete);
        mTimeContents.requestFocus();
        mTimeDialog.setBackgroundResource(R.drawable.shape_add_detail_on);

        openKeyBoard();
        changeCompleteButton();


        Bundle extra = this.getArguments();
        if (extra != null) {
            mEditStartHour = extra.getInt(Information.PUT_START_HOUR);
            mEditStartMinute = extra.getInt(Information.PUT_START_MINUTE);
            mEditEndHour = extra.getInt(Information.PUT_END_HOUR);
            mEditEndMinute = extra.getInt(Information.PUT_END_MINUTE);
            String mEditContents = extra.getString(Information.PUT_CONTENTS);
            mTimeContents.setText(mEditContents);
            mTimeComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
        }

        mTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();

                DialogTime dialogTime = new DialogTime();
                dialogTime.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                dialogTime.show(getChildFragmentManager(), "DialogTime");
                dialogTime.setOnTimeSetListener(new DialogTime.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(int sHour, int sMinute, int eHour, int eMinute) {
                        mEditStartHour = sHour;
                        mEditStartMinute = sMinute;
                        mEditEndHour = eHour;
                        mEditEndMinute = eMinute;
                        openKeyBoard();
                    }
                });

                Bundle bundle = new Bundle();
                bundle.putInt(Information.PUT_START_HOUR, mEditStartHour);
                bundle.putInt(Information.PUT_START_MINUTE, mEditStartMinute);
                bundle.putInt(Information.PUT_END_HOUR, mEditEndHour);
                bundle.putInt(Information.PUT_END_MINUTE, mEditEndMinute);
                dialogTime.setArguments(bundle);

            }
        });

        mTimeComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimeContents.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "계획을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (mTimeContents.getText() == null) {
                        Toast.makeText(getActivity(), "계획을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        updateTimeData();
                        Toast.makeText(getActivity(), "계획이 수정됐어요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        return v;


    }

    private void updateTimeData() {
        String mContents = "";
        if (mTimeContents != null) {
            mContents = mTimeContents.getText().toString();
        }
        if (mListener != null) {
            mListener.UpdateTimeData(mEditStartHour, mEditStartMinute, mEditEndHour, mEditEndMinute, mContents);
        }
        mTimeContents.setText(null);
    }

    private void changeCompleteButton() {

        mTimeContents.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mTimeComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mTimeComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mTimeComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mTimeComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
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
        mTimeContents.requestFocus();
    }

    private void closeKeyBoard() {
        mTimeContents.clearFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTimeContents.getWindowToken(), 0);
    }

}
