package com.fleet.startplan.Schedule;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.fleet.startplan.Dialog.DialogAdd;
import com.fleet.startplan.Dialog.DialogTime;
import com.fleet.startplan.R;

public class ScheduleTime extends Fragment {

    public static String TIME_ON = "onTime";
    public static String TIME_OFF = "offTime";

    private View mTimeDialog;
    private EditText mTimeContents;
    private ImageView mTimeComplete;
    private String mHaveTime;
    private int mStartHour, mStartMinute, mEndHour, mEndMinute = 0;
    private SetScheduleTimeListener mListener;

    public void setOnScheduleTimeListener(SetScheduleTimeListener mListener) {
        this.mListener = mListener;
    }

    public interface SetScheduleTimeListener {
        void saveTimeData(String contents, int startHour,int startMinute, int endHour, int endMinute);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_add_time, container, false);

        mTimeDialog = v.findViewById(R.id.v_btn_dialog_time);
        mTimeContents = v.findViewById(R.id.et_add_time);
        mTimeComplete = v.findViewById(R.id.iv_add_time_complete);

        mHaveTime = TIME_OFF;
        mTimeContents.requestFocus();
        changeDialogBtn(mHaveTime);

        openKeyBoard();
        changeCompleteButton();

        mTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    closeKeyBoard();
                    if (mHaveTime.equals(TIME_OFF)) {
                        DialogTime dialogTime = new DialogTime();
                        dialogTime.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                        dialogTime.show(getChildFragmentManager(), "DialogTime");
                        dialogTime.setOnTimeSetListener(new DialogTime.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(int sHour, int sMinute, int eHour, int eMinute) {
                                mHaveTime = TIME_ON;
                                mStartHour = sHour;
                                mStartMinute = sMinute;
                                mEndHour = eHour;
                                mEndMinute = eMinute;
                                changeDialogBtn(TIME_ON);
                                openKeyBoard();
                            }
                        });
                    } else {
                        mHaveTime = TIME_OFF;
                        mStartHour = 0;
                        mStartMinute = 0;
                        mEndHour = 0;
                        mEndMinute = 0;
                        changeDialogBtn(TIME_OFF);
                        openKeyBoard();
                    }
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
                        } else if (mHaveTime.equals(TIME_OFF)) {
                            Toast.makeText(getActivity(), "시간을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            saveTimeData();
                            Toast.makeText(getActivity(), "계획이 추가됐어요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        });


        return v;
    }

    private void saveTimeData() {
        String mContents = "";
        if (mTimeContents != null) {
            mContents = mTimeContents.getText().toString();
        }
        if (mListener != null) {
            mListener.saveTimeData(mContents, mStartHour, mStartMinute, mEndHour, mEndMinute);
        }
        mTimeContents.setText(null);
        mHaveTime = TIME_OFF;
        changeDialogBtn(mHaveTime);
    }



    private void changeDialogBtn(String state) {

        if (state.equals(TIME_OFF)) {
            mTimeDialog.setBackgroundResource(R.drawable.shape_add_detail_off);
        } else {
            mTimeDialog.setBackgroundResource(R.drawable.shape_add_detail_on);
        }
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

    @Override
    public void onDestroyView() {
        if (mTimeContents != null && mTimeContents.isFocused()) {
            mTimeContents.clearFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mTimeContents.getWindowToken(), 0);
        }
        super.onDestroyView();
    }

    private void openKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mTimeContents.requestFocus();
    }

    private void closeKeyBoard(){
        mTimeContents.clearFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTimeContents.getWindowToken(), 0);
    }

}
