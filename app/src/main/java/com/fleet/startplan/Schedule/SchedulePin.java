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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.R;


public class SchedulePin extends Fragment {

    private EditText mPinEditText;
    private ImageView mImageViewPinComplete;

    private SetSchedulePinListener mListener;

    public void setOnSchedulePinListener(SetSchedulePinListener mListener) {
        this.mListener = mListener;
    }

    public interface SetSchedulePinListener {
        void savePinData(String contents, String mEndDate);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_add_pin, container, false);

        mPinEditText = view.findViewById(R.id.et_add_pin);
        mImageViewPinComplete = view.findViewById(R.id.iv_add_pin_complete);

        openKeyBoard();
        changeCompleteButton();

        mImageViewPinComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPinEditText.getText().toString().equals("") || mPinEditText.getText() == null) {
                    Toast.makeText(getActivity(), "목표를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    tossPinData();
                    Toast.makeText(getActivity(), "목표가 추가됐어요.", Toast.LENGTH_SHORT).show();
                }
                mPinEditText.setText(null);
            }

        });

        return view;
    }


    private void tossPinData() {
        String mDataContents = "";
        if (mPinEditText != null) {
            mDataContents = mPinEditText.getText().toString();
        }
        String mDataEndDate = Information.CATEGORY_PIN;
        if (mListener != null) {
            mListener.savePinData(mDataContents, mDataEndDate);
        }
    }

    private void changeCompleteButton() {

        mPinEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mImageViewPinComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mImageViewPinComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mImageViewPinComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mImageViewPinComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
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
        mPinEditText.requestFocus();
    }

    @Override
    public void onDestroyView() {
        if (mPinEditText != null && mPinEditText.isFocused()) {
            mPinEditText.clearFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mPinEditText.getWindowToken(), 0);
        }
        super.onDestroyView();
    }

}
