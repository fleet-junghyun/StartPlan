package com.fleet.startplan.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Activity.MainActivity;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.Model.ScheduleItem;
import com.fleet.startplan.R;
import com.fleet.startplan.Schedule.Schedule;
import com.fleet.startplan.Schedule.ScheduleDDay;
import com.fleet.startplan.Schedule.SchedulePin;
import com.fleet.startplan.Schedule.ScheduleStartDay;
import com.fleet.startplan.Schedule.ScheduleTime;
import com.fleet.startplan.Schedule.ScheduleTodo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class DialogAdd extends BottomSheetDialogFragment {

    private View mCategoryTodo, mCategoryTime, mCategoryPin, mCategoryDday, mCategoryStartDay, mEmojiSpace;
    private String mSelectedDate = "";

    private EmojiconEditText mEmojiEditText;
    private EmojIconActions mEmojiActions;
    private TextView mEmojiPreview;


    private View rootView;
    boolean isKeyboardShowing = false;
    //현재 키보드가 보이는상태인지 아닌지를 담는 불린 / 트루 -> 키보드가 보이는상태
    int keyboardHeight;
    //현재 디바이스의 키보드 높이

    private SPDBHelper mDBHelper;
    private SetAddRefreshListener mSetAddRefreshListener;

    public void setOnAddRefreshListener(SetAddRefreshListener mSetAddRefreshListener) {
        this.mSetAddRefreshListener = mSetAddRefreshListener;
    }

    public interface SetAddRefreshListener {
        void completeFragmentBtn(String category);
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add, container, false);

        mCategoryTodo = view.findViewById(R.id.v_add_category_todo);
        mCategoryPin = view.findViewById(R.id.v_add_category_pin);
        mCategoryDday = view.findViewById(R.id.v_add_category_d_day);
        mCategoryStartDay = view.findViewById(R.id.v_add_category_start_day);
        mCategoryTime = view.findViewById(R.id.v_add_category_time);

        mEmojiEditText = view.findViewById(R.id.et_emoji);
        ImageView mEmojiButton = view.findViewById(R.id.iv_emoji_container);
        View mEmojiContainer = view.findViewById(R.id.l_emoji_container);
        mEmojiPreview = view.findViewById(R.id.tv_emoji_preview);

        mEmojiSpace = view.findViewById(R.id.v_emoji_space);

        Bundle extra = this.getArguments();

        if (extra != null) {
            mSelectedDate = extra.getString(Schedule.SELECTED_DATE);
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

        mEmojiActions = new EmojIconActions(getActivity().getApplicationContext(), mEmojiContainer, mEmojiEditText, mEmojiButton, "#11204d", "#d4d5d8", "#ffffff");
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
                Log.e("emoji", emoji);
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

        createScheduleTodo();


        mCategoryTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCategoryColor(0);
                createScheduleTodo();
            }
        });

        mCategoryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoryColor(1);
                createScheduleTime();
            }
        });

        mCategoryPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCategoryColor(2);
                createSchedulePin();
            }
        });

        mCategoryDday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCategoryColor(3);
                createScheduleDday();
            }
        });

        mCategoryStartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoryColor(4);
                createScheduleStartDay();
            }
        });


        return view;
    }


    private void createScheduleTodo() {
        ScheduleTodo scheduleTodo = new ScheduleTodo();
        setChildFragment(scheduleTodo);
        mEmojiPreview.setText("✍️");
        scheduleTodo.setOnScheduleTodoListener(new ScheduleTodo.SetScheduleTodoListener() {
            @Override
            public void saveTodoData(String contents) {
                saveData(contents, Information.CATEGORY_TODO, 0, 0, 0, 0, "", "", mSelectedDate);
                if (mSetAddRefreshListener != null) {
                    mSetAddRefreshListener.completeFragmentBtn(Information.CATEGORY_TODO);
                }
            }
        });
        bundleData(scheduleTodo);
    }


    private void createScheduleTime() {
        ScheduleTime scheduleTime = new ScheduleTime();
        setChildFragment(scheduleTime);
        mEmojiPreview.setText("⏲️");
        scheduleTime.setOnScheduleTimeListener(new ScheduleTime.SetScheduleTimeListener() {
            @Override
            public void saveTimeData(String contents, int startHour, int startMinute, int endHour, int endMinute) {
                saveData(contents, Information.CATEGORY_TIME, startHour, startMinute, endHour, endMinute, "", "", mSelectedDate);
                if (mSetAddRefreshListener != null) {
                    mSetAddRefreshListener.completeFragmentBtn(Information.CATEGORY_TIME);
                }
            }
        });
        bundleData(scheduleTime);
    }


    private void createSchedulePin() {
        SchedulePin schedulePin = new SchedulePin();
        setChildFragment(schedulePin);
        mEmojiPreview.setText("\uD83D\uDCCC");
        schedulePin.setOnSchedulePinListener(new SchedulePin.SetSchedulePinListener() {
            @Override
            public void savePinData(String contents, String mEndDate) {
                saveData(contents, Information.CATEGORY_D_DAY, 0, 0, 0, 0, mSelectedDate,
                        Information.CATEGORY_PIN, mSelectedDate);
                if (mSetAddRefreshListener != null) {
                    mSetAddRefreshListener.completeFragmentBtn(Information.CATEGORY_PIN);
                }
            }
        });
        bundleData(schedulePin);
    }

    private void createScheduleDday() {
        ScheduleDDay scheduleDday = new ScheduleDDay();
        setChildFragment(scheduleDday);
        mEmojiPreview.setText("\uD83D\uDD25");
        scheduleDday.setOnScheduleDDayListener(new ScheduleDDay.SetScheduleDDayListener() {
            @Override
            public void saveDDayData(String contents, String endDate) {
                saveData(contents, Information.CATEGORY_D_DAY, 0, 0, 0, 0, mSelectedDate, endDate, mSelectedDate);
                if (mSetAddRefreshListener != null) {
                    mSetAddRefreshListener.completeFragmentBtn(Information.CATEGORY_D_DAY);
                }
            }
        });
        bundleData(scheduleDday);
    }

    private void createScheduleStartDay() {
        ScheduleStartDay scheduleStartDay = new ScheduleStartDay();
        setChildFragment(scheduleStartDay);
        mEmojiPreview.setText("\uD83D\uDCA1");
        scheduleStartDay.setOnStartDayListener(new ScheduleStartDay.SetScheduleStartDayListener() {
            @Override
            public void saveStartDayData(String contents) {
                saveData(contents, Information.CATEGORY_START_DAY, 0, 0, 0, 0, mSelectedDate, "", mSelectedDate);
                if (mSetAddRefreshListener != null) {
                    mSetAddRefreshListener.completeFragmentBtn(Information.CATEGORY_START_DAY);
                }
            }
        });
        bundleData(scheduleStartDay);
    }


    private void saveData(String contents, String category, int startHour,
                          int startMinute, int endHour, int endMinute, String startDate,
                          String endDate, String registerDate) {
        int complete = 0;
        String completedTime = "";
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
        int completedDday = 0;
        String deletedDate = "";
        String emoji = mEmojiPreview.getText().toString();
        if (category.equals(Information.CATEGORY_TODO) || category.equals(Information.CATEGORY_TIME)) {
            scheduleListPos = lastID;
        } else {
            scheduleListPos = 0;
        }
        mDBHelper.insertSchedule(emoji, contents, category, complete, completedTime, scheduleListPos, startHour,
                startMinute, endHour, endMinute, startDate, endDate, completedDday, registerDate, deletedDate);
        if (category.equals(Information.CATEGORY_TODO)) {
            mDBHelper.insertStorage(category, contents, emoji, registerDate, deletedDate, lastID, 0);
        }

    }

    private void bundleData(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(Schedule.SELECTED_DATE, mSelectedDate);
        fragment.setArguments(bundle);
    }

    private void setChildFragment(Fragment child) {
        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
        if (!child.isAdded()) {
            mFragmentTransaction.replace(R.id.l_add_container, child, null);
            mFragmentTransaction.setReorderingAllowed(true);
            mFragmentTransaction.commit();
        }
    }


    private void setCategoryColor(int category) {
        //0== todo
        if (category == 0) {
            mCategoryTodo.setBackgroundResource(R.drawable.shape_add_category_btn_on);
            mCategoryTime.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryPin.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryDday.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryStartDay.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            //1==pin`
        } else if (category == 1) {
            mCategoryTodo.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryTime.setBackgroundResource(R.drawable.shape_add_category_btn_on);
            mCategoryPin.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryDday.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryStartDay.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            //2==dday
        } else if (category == 2) {
            mCategoryTodo.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryTime.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryPin.setBackgroundResource(R.drawable.shape_add_category_btn_on);
            mCategoryDday.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryStartDay.setBackgroundResource(R.drawable.shape_add_category_btn_off);
        } else if (category == 3) {
            mCategoryTodo.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryTime.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryPin.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryDday.setBackgroundResource(R.drawable.shape_add_category_btn_on);
            mCategoryStartDay.setBackgroundResource(R.drawable.shape_add_category_btn_off);
        } else {
            mCategoryTodo.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryTime.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryPin.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryDday.setBackgroundResource(R.drawable.shape_add_category_btn_off);
            mCategoryStartDay.setBackgroundResource(R.drawable.shape_add_category_btn_on);
        }
    }

    @Override
    public void onStop() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmojiEditText.getWindowToken(), 0);
        super.onStop();
    }


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle
                                 savedInstanceState) {
        mDBHelper = new SPDBHelper(getContext());
        super.onCreate(savedInstanceState);
    }

    private void getKeyboardHeight(View targetView) {
//      키보드의 크기를 구하는 함수
//      구하는 원리는 엑티비티 혹은 프래그먼트 전체값을 구한다 screenHeight
//      현재 뷰의 입력정보가 들어간 rect객체의 하단값(바닥면)의 값을 구함 rectangle.bottom
//      rectangle.bottom 값의 화면상 위치는 디바이스의 맨 하단 혹은 키보드의 바로 위 두가지의 경우가 있다.
//      전체높이에서 현재 뷰의 높이를 빼서 남은 값으로 키보드의 값을 유추한다.

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






