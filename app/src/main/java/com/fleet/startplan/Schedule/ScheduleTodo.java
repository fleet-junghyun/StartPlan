package com.fleet.startplan.Schedule;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fleet.startplan.R;

public class ScheduleTodo extends Fragment {


    private EditText mTodoEditText;
    private ImageView mImageViewTodoComplete;

    private SetScheduleTodoListener mSetScheduleTodoListener;

    public void setOnScheduleTodoListener(SetScheduleTodoListener mSetScheduleTodoListener) {
        this.mSetScheduleTodoListener = mSetScheduleTodoListener;
    }

    public interface SetScheduleTodoListener {
        void saveTodoData(String contents);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_add_todo, container, false);

        mTodoEditText = view.findViewById(R.id.et_add_todo);
        mImageViewTodoComplete = view.findViewById(R.id.iv_add_todo_complete);

        openKeyBoard();
        changeCompleteButton();

        mImageViewTodoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTodoEditText.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "계획을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (mTodoEditText.getText() == null) {
                    Toast.makeText(getActivity(), "계획을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    tossTodoData();
                    Toast.makeText(getActivity(), "계획이 추가됐어요.", Toast.LENGTH_SHORT).show();
                }

                mTodoEditText.setText(null);
            }

        });


        return view;
    }

    private void tossTodoData() {

        String mDataContents = "";
        if (mTodoEditText != null) {
            mDataContents = mTodoEditText.getText().toString();
        }
        if (mSetScheduleTodoListener != null) {
            mSetScheduleTodoListener.saveTodoData(mDataContents);
        }
    }


    private void changeCompleteButton() {
        mTodoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mImageViewTodoComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mImageViewTodoComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mImageViewTodoComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mImageViewTodoComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
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
        mTodoEditText.requestFocus();
    }

}
