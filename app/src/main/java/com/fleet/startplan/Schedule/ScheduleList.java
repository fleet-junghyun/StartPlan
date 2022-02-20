package com.fleet.startplan.Schedule;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Activity.MainActivity;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.Routine.RoutineActivity;
import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.SharedPreference.PreferenceManager;
import com.fleet.startplan.Dialog.DialogAdd;
import com.fleet.startplan.R;
import com.fleet.startplan.Adapter.ScheduleAdapter;
import com.fleet.startplan.Model.ScheduleItem;
import com.fleet.startplan.TouchHelperAdapter.OnSingleClickListener;
import com.fleet.startplan.TouchHelperAdapter.ScheduleItemTouchHelperCallback;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ScheduleList extends Fragment {


    public static String ROUTINE_TODO_ITEM_ID = "routineItemID";

    private RecyclerView mRecyclerview;
    private ScheduleAdapter mScheduleAdapter;
    private SPDBHelper mDBHelper;
    private String mSelectedDate;

    private int itemPos = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.schedule_list, container, false);
        View mPlanAdd = rootView.findViewById(R.id.v_plan_add);
        mRecyclerview = rootView.findViewById(R.id.rv_schedule);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle extra = this.getArguments();
        if (extra != null) {
            mSelectedDate = extra.getString(Schedule.SELECTED_DATE);
            listRefresh(mSelectedDate);
        }
        //파동 치는 UI
        checkFirstUser(rootView, mSelectedDate);
        mPlanAdd.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                clickAddDialog(mSelectedDate);
            }
        });
        return rootView;
    }

    private void clickAddDialog(String selectedDate) {
        // dialog  add 부분
        DialogAdd dialogAdd = new DialogAdd();
        dialogAdd.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        dialogAdd.show(getChildFragmentManager(), "DialogAdd");
        dialogAdd.setOnAddRefreshListener(new DialogAdd.SetAddRefreshListener() {
            @Override
            public void completeFragmentBtn(String category) {
                if (category.equals(Information.CATEGORY_TODO) || category.equals(Information.CATEGORY_TIME)) {
                    ArrayList<ScheduleItem> scheduleItems = mDBHelper.getScheduleData(mSelectedDate, Information.CATEGORY_D_DAY, Information.CATEGORY_START_DAY);
                    Collections.sort(scheduleItems, ScheduleItem.setScheduleItemList);
                    mScheduleAdapter.setItems(scheduleItems);
                    int size = scheduleItems.size();
                    mScheduleAdapter.notifyItemInserted(size + 1);
                    mRecyclerview.scrollToPosition(size - 1);
                } else {
                    recentlySetData();
                    mScheduleAdapter.notifyDataSetChanged();
                    mRecyclerview.scrollToPosition(0);
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(Schedule.SELECTED_DATE, selectedDate);
        dialogAdd.setArguments(bundle);
    }


    public void listRefresh(String selectedDate) {
        ArrayList<ScheduleItem> scItems = mDBHelper.getScheduleData(selectedDate, Information.CATEGORY_D_DAY, Information.CATEGORY_START_DAY);
        for (int i = 0; i < scItems.size(); i++) {
            ScheduleItem scItem = scItems.get(i);
            scItem.setScStandardDate(selectedDate);
        }
        Collections.sort(scItems, ScheduleItem.setScheduleItemList);
        mScheduleAdapter = new ScheduleAdapter(scItems);
        mScheduleAdapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int itemId) {
                Intent intent = new Intent(getContext(), RoutineActivity.class);
                intent.putExtra(ScheduleList.ROUTINE_TODO_ITEM_ID, itemId);
                mStartForResult.launch(intent);
                itemPos = position;
            }
        });
        mRecyclerview.setAdapter(mScheduleAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ScheduleItemTouchHelperCallback(mScheduleAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerview);
    }


    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        recentlySetData();
                    } catch (IllegalStateException E) {
                        Toast.makeText(getContext(), "오류가 발생했어요. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    mScheduleAdapter.notifyItemChanged(itemPos);
                }
            });


    private void recentlySetData() {
        ArrayList<ScheduleItem> scheduleItems = mDBHelper.getScheduleData(mSelectedDate, Information.CATEGORY_D_DAY, Information.CATEGORY_START_DAY);
        Collections.sort(scheduleItems, ScheduleItem.setScheduleItemList);
        mScheduleAdapter.setItems(scheduleItems);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mDBHelper = new SPDBHelper(getContext());
        super.onCreate(savedInstanceState);
    }

    private String getToday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        return format.format(c.getTime());
    }

    private void checkFirstUser(ViewGroup v, String checkSelectedDate) {
        boolean checkFirst = PreferenceManager.getBoolean(getContext(), "checkFirst");
        if (!checkFirst) {
            PreferenceManager.setBoolean(getContext(), "checkFirst", true);
            PreferenceManager.setString(getContext(), "InstallationDate", getToday());
            TapTargetView.showFor(getActivity(),
                    TapTarget.forView(v.findViewById(R.id.iv_main_add), "", "")
                            .outerCircleAlpha(0.0f)
                            .cancelable(false)
                            .tintTarget(false)
                            .targetRadius(25),
                    new TapTargetView.Listener() {
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional
                            clickAddDialog(checkSelectedDate);
                        }
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        recentlySetData();
        mScheduleAdapter.notifyDataSetChanged();
    }

    private void start(){}
}
