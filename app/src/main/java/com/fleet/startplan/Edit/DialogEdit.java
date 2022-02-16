package com.fleet.startplan.Edit;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.Model.ScheduleItem;
import com.fleet.startplan.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class DialogEdit extends BottomSheetDialogFragment {

    private String mContents;
    private String mEndDate;
    private String mRegisterDate;
    private int mEditItemId, mStartHour, mStartMinute, mEndHour, mEndMinute, mCountDday;

    private EmojiconEditText mEmojiEditText;
    private View mEmojiSpace;
    private EmojIconActions mEmojiActions;
    private TextView mEmojiPreview;
    private View rootView;
    boolean isKeyboardShowing = false;
    //현재 키보드가 보이는상태인지 아닌지를 담는 불린 / 트루 -> 키보드가 보이는상태
    int keyboardHeight;
    //현재 디바이스의 키보드 높이

    private SPDBHelper mDBHelper;

    private SetUpdateRefreshListener mSetUpdateRefreshListener;

    public void setOnUpdateRefreshListener(SetUpdateRefreshListener mSetUpdateRefreshListener) {
        this.mSetUpdateRefreshListener = mSetUpdateRefreshListener;
    }

    public interface SetUpdateRefreshListener {
        void updateFragmentBtn(String emoji, String contents, int startHour, int startMinute, int endHour, int endMinute, String endDate);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_edit, container, false);
        mEmojiEditText = view.findViewById(R.id.et_edit_emoji);
        ImageView mEmojiButton = view.findViewById(R.id.iv_edit_emoji_container);
        View mEmojiContainer = view.findViewById(R.id.l_edit_emoji_container);
        mEmojiPreview = view.findViewById(R.id.tv_edit_emoji_preview);
        mEmojiSpace = view.findViewById(R.id.v_edit_emoji_space);

        Bundle extra = this.getArguments();
        if (extra != null) {
            mEditItemId = extra.getInt(Information.PUT_EDIT_ITEM_ID);
        }

        ArrayList<ScheduleItem> scItems = mDBHelper.getScheduleInfo(mEditItemId);
        ScheduleItem scheduleItem = scItems.get(0);
        String mEmoji = scheduleItem.getScEmoji();
        mContents = scheduleItem.getScContents();
        String mCategory = scheduleItem.getScCategory();
        mStartHour = scheduleItem.getTodoStartHour();
        mStartMinute = scheduleItem.getTodoStartMinute();
        mEndHour = scheduleItem.getTodoEndHour();
        mEndMinute = scheduleItem.getTodoEndMinute();
        mEndDate = scheduleItem.getdDayEndDate();
        mRegisterDate = scheduleItem.getScRegisteredDate();
        mCountDday = scheduleItem.getCountDay();


        mEmojiActions = new EmojIconActions(getContext(), mEmojiContainer, mEmojiEditText, mEmojiButton, "#11204d", "#d4d5d8", "#ffffff");
        mEmojiActions.setUseSystemEmoji(true);
        mEmojiActions.setIconsIds(R.drawable.shape_add_emoji_container, R.drawable.shape_add_emoji_container);
        mEmojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isKeyboardShowing) {
                    mEmojiSpace.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams params = mEmojiSpace.getLayoutParams();
                    params.height = keyboardHeight;
                    mEmojiSpace.setLayoutParams(params);
                    mEmojiActions.ShowEmojIcon();
                }
            }
        });


        mEmojiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emoji = mEmojiEditText.getText().toString();
                if (mEmojiEditText.length() > 1) {
                    mEmojiEditText.setText(null);
                    mEmojiPreview.setText(null);
                }
                mEmojiPreview.setText(emoji);
                mEmojiActions.closeEmojIcon();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (mCategory.equals(Information.CATEGORY_TODO) || mCategory.equals(Information.CATEGORY_ROUTINE)) {
            EditScheduleTodo editScheduleTodo = new EditScheduleTodo();
            setChildFragment(editScheduleTodo);
            editScheduleTodo.setOnEditScheduleTodoListener(new EditScheduleTodo.SetEditScheduleTodoListener() {
                @Override
                public void UpdateTodoData(String contents) {
                    updateTodoData(contents);
                    dismiss();
                }
            });
            setEmoji(mEmoji);
            bundleData(Information.CATEGORY_TODO, editScheduleTodo);

        } else if (mCategory.equals(Information.CATEGORY_TIME)) {
            EditScheduleTime editScheduleTime = new EditScheduleTime();
            setChildFragment(editScheduleTime);
            editScheduleTime.setOnEditScheduleTimeListener(new EditScheduleTime.SetEditScheduleTimeListener() {
                @Override
                public void UpdateTimeData(int startHour, int startMinute, int endHour, int endMinute, String contents) {
                    updateTimeData(startHour, startMinute, endHour, endMinute, contents);
                    dismiss();
                }
            });

            setEmoji(mEmoji);
            bundleData(Information.CATEGORY_TIME, editScheduleTime);

        } else if (mEndDate.equals(Information.CATEGORY_PIN)) {
            EditSchedulePin editSchedulePin = new EditSchedulePin();
            setChildFragment(editSchedulePin);
            editSchedulePin.setOnEditSchedulePinListener(new EditSchedulePin.SetEditSchedulePinListener() {
                @Override
                public void UpdatePinData(String contents) {
                    updatePinData(contents);
                    dismiss();
                }
            });
            setEmoji(mEmoji);
            bundleData(Information.CATEGORY_TIME, editSchedulePin);


        } else if (mCategory.equals(Information.CATEGORY_D_DAY)) {
            EditScheduleDDay editScheduleDDay = new EditScheduleDDay();
            setChildFragment(editScheduleDDay);
            editScheduleDDay.setOnEditScheduleDDayListener(new EditScheduleDDay.SetEditScheduleDDayListener() {
                @Override
                public void UpdateDDayData(String contents, String endDate) {
                    updateDdayData(contents, endDate);
                    dismiss();
                }
            });
            setEmoji(mEmoji);
            bundleData(Information.CATEGORY_D_DAY, editScheduleDDay);

        } else if (mCategory.equals(Information.CATEGORY_START_DAY)) {
            EditScheduleStartDay editScheduleStartDay = new EditScheduleStartDay();
            setChildFragment(editScheduleStartDay);
            editScheduleStartDay.setOnEditScheduleStartDayListener(new EditScheduleStartDay.SetEditScheduleStartDayListener() {
                @Override
                public void UpdateStartDayData(String contents) {
                    updateStartDayData(contents);
                    dismiss();
                }
            });
            setEmoji(mEmoji);
            bundleData(Information.CATEGORY_START_DAY, editScheduleStartDay);
        } else {
            Toast.makeText(getActivity(), "오류입니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
        }

        rootView = getActivity().getWindow().getDecorView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                키보드의 show & noShow(동작순간)에 뷰의 크기가 변하게된다.
//                이를 이용해서 키보드의 높이를 구한다.
                getKeyboardHeight(rootView);
            }
        });


        return view;
    }

    private void bundleData(String pos, Fragment fragment) {
        Bundle bundle = new Bundle();
        if (pos.equals(Information.CATEGORY_TIME)) {
            bundle.putInt(Information.PUT_START_HOUR, mStartHour);
            bundle.putInt(Information.PUT_START_MINUTE, mStartMinute);
            bundle.putInt(Information.PUT_END_HOUR, mEndHour);
            bundle.putInt(Information.PUT_END_MINUTE, mEndMinute);
        } else if (pos.equals(Information.CATEGORY_D_DAY)) {
            bundle.putString(Information.PUT_REGISTER_DATE, mRegisterDate);
            bundle.putInt(Information.PUT_GET_COUNT_D_DAY, mCountDday);
            bundle.putString(Information.PUT_END_DATE, mEndDate);
        }

        bundle.putString(Information.PUT_CONTENTS, mContents);

        fragment.setArguments(bundle);
    }


    private void setEmoji(String putEmoji) {
        mEmojiPreview.setText(putEmoji);
    }

    private void setChildFragment(Fragment child) {
        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();

        if (!child.isAdded()) {
            mFragmentTransaction.replace(R.id.l_edit_fragment_container, child, null);
            mFragmentTransaction.setReorderingAllowed(true);
            mFragmentTransaction.commit();
        }
    }


    private void updateTodoData(String contents) {
        String emoji = mEmojiPreview.getText().toString();
        mDBHelper.updateTodoEditItem(mEditItemId, emoji, contents);
        updateListener(emoji, contents, 0, 0, 0, 0, "");
    }

    private void updateTimeData(int startHour, int startMinute, int endHour, int endMinute, String contents) {
        String emoji = mEmojiPreview.getText().toString();
        mDBHelper.updateTimeEditItem(mEditItemId, emoji, contents, startHour, startMinute, endHour, endMinute);
        updateListener(emoji, contents, startHour, startMinute, endHour, endMinute, "");

    }

    private void updatePinData(String contents) {
        String emoji = mEmojiPreview.getText().toString();
        mDBHelper.updatePinEditItem(mEditItemId, emoji, contents);
        updateListener(emoji, contents, 0, 0, 0, 0, Information.CATEGORY_PIN);
    }

    private void updateDdayData(String contents, String endDate) {
        String emoji = mEmojiPreview.getText().toString();
        mDBHelper.updateDdayEditItem(mEditItemId, emoji, contents, endDate);
        updateListener(emoji, contents, 0, 0, 0, 0, endDate);
    }

    private void updateStartDayData(String contents) {
        String emoji = mEmojiPreview.getText().toString();
        mDBHelper.updateStartDayEditItem(mEditItemId, emoji, contents);
        updateListener(emoji, contents, 0, 0, 0, 0, "");
    }

    private void updateListener(String emoji, String contents, int startHour, int startMinute, int endHour, int endMinute, String endDate) {
        if (mSetUpdateRefreshListener != null) {
            mSetUpdateRefreshListener.updateFragmentBtn(emoji, contents, startHour, startMinute, endHour, endMinute, endDate);
        }
    }


    private void getKeyboardHeight(View targetView) {
        Rect rectangle = new Rect();
        targetView.getWindowVisibleDisplayFrame(rectangle);
        int screenHeight = targetView.getRootView().getHeight();
        int tempKeyboardSize = screenHeight - rectangle.bottom;
        if (tempKeyboardSize > screenHeight * 0.1) {
//            대부분의 키보드 높이가 전체의 10프로이상차지해서 0.1로 정함
            keyboardHeight = screenHeight - rectangle.bottom;
            if (!isKeyboardShowing) {
//                키보드가 보여지는 경우
                isKeyboardShowing = true;
            }
        } else {
            // 키보드가 안보이는 경우
            if (isKeyboardShowing) {
                isKeyboardShowing = false;
            }
        }

    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mDBHelper = new SPDBHelper(getContext());
        super.onCreate(savedInstanceState);
    }

    public void closeKeyboard() {
        try {
            mEmojiEditText.clearFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "죄송합니다. 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeKeyboard();
    }
}
