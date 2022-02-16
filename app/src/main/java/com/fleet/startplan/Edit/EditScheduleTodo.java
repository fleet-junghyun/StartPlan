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

public class EditScheduleTodo extends Fragment {

    private EditText mTodoEditText;
    private ImageView mTodoComplete;

    private SetEditScheduleTodoListener mListener;

    public void setOnEditScheduleTodoListener(SetEditScheduleTodoListener mListener) {
        this.mListener = mListener;
    }

    public interface SetEditScheduleTodoListener {
        void UpdateTodoData(String contents);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_add_todo, container, false);
        mTodoEditText = v.findViewById(R.id.et_add_todo);
        mTodoComplete = v.findViewById(R.id.iv_add_todo_complete);

        Bundle extra = this.getArguments();
        if(extra != null){
            String mEditContents = extra.getString(Information.PUT_CONTENTS);
            mTodoEditText.setText(mEditContents);
            mTodoComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
        }


        mTodoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTodoEditText.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "할일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (mTodoEditText.getText() == null) {
                    Toast.makeText(getActivity(), "할일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    tossUpdateData();
                    Toast.makeText(getActivity(), "할일을 수정했어요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        openKeyBoard();
        changeCompleteButton();

        return v;

    }

    private void tossUpdateData(){
        String mUpdateContents = "";
        if(mTodoEditText !=null){
           mUpdateContents =  mTodoEditText.getText().toString();
        }
        if(mListener !=null){
            mListener.UpdateTodoData(mUpdateContents);
        }
    }


    private void openKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mTodoEditText.requestFocus();
    }

    private void changeCompleteButton() {
        mTodoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mTodoComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mTodoComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mTodoComplete.setImageResource(R.drawable.ic_add_complete_on_34dp);
                } else {
                    mTodoComplete.setImageResource(R.drawable.ic_add_complete_off_34dp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


}
