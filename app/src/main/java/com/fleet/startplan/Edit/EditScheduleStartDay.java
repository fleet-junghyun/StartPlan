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

import com.fleet.startplan.Model.Information;
import com.fleet.startplan.R;


public class EditScheduleStartDay extends Fragment {

    private EditText mStartDayEditText;
    private ImageView mStartDayComplete;
    private SetEditScheduleStartDayListener mListener;

    public void setOnEditScheduleStartDayListener(SetEditScheduleStartDayListener mListener) {
        this.mListener = mListener;
    }

    public interface SetEditScheduleStartDayListener {
        void UpdateStartDayData(String contents);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_add_start_day, container, false);

        mStartDayEditText = v.findViewById(R.id.et_add_start_day);
        mStartDayComplete = v.findViewById(R.id.iv_add_start_day_complete);

        Bundle extra = this.getArguments();
        if(extra != null){
            String mEditContents = extra.getString(Information.PUT_CONTENTS);
            mStartDayEditText.setText(mEditContents);
            mStartDayComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
        }


        mStartDayComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartDayEditText.getText().toString().equals("") || mStartDayEditText.getText() == null) {
                    Toast.makeText(getActivity(), "결심을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    updateData();
                    Toast.makeText(getActivity(), "결심을 수정했어요.", Toast.LENGTH_SHORT).show();
                }
                mStartDayEditText.setText(null);
            }
        });


        openKeyBoard();
        changeCompleteButton();

        return v;

    }

    private void updateData() {
        String mDataContents = "";
        if (mStartDayEditText != null) {
            mDataContents = mStartDayEditText.getText().toString();
        }
        if (mListener != null) {
            mListener.UpdateStartDayData(mDataContents);
        }
    }

    private void changeCompleteButton() {

        mStartDayEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mStartDayComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mStartDayComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mStartDayComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mStartDayComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
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
        mStartDayEditText.requestFocus();
    }

    @Override
    public void onDestroyView() {
        if (mStartDayEditText != null && mStartDayEditText.isFocused()) {
            mStartDayEditText.clearFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mStartDayEditText.getWindowToken(), 0);
        }
        super.onDestroyView();
    }
}
