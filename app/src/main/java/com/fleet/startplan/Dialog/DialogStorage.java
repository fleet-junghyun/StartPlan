package com.fleet.startplan.Dialog;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Activity.MainActivity;
import com.fleet.startplan.R;
import com.fleet.startplan.Adapter.StorageAdapter;
import com.fleet.startplan.Model.StorageItem;
import com.fleet.startplan.Schedule.Schedule;
import com.fleet.startplan.TouchHelperAdapter.StorageItemTouchHelperCallback;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;

public class DialogStorage extends BottomSheetDialogFragment {


    private SPDBHelper mDBHelper;
    private String mSelectedDate;

    private OnStorageAddListener mListener;


    public void setOnStorageAddListener(OnStorageAddListener listener) {
        this.mListener = listener;
    }

    public interface OnStorageAddListener {
        void clickAddButton();
    }

    private static ArrayList<StorageItem> stItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_storage, container, false);

        RecyclerView mRecyclerview = view.findViewById(R.id.rv_storage);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        stItems = mDBHelper.getStorageItems();
        Collections.sort(stItems, StorageItem.setStorageItemList);
        StorageAdapter mStorageAdapter = new StorageAdapter(stItems);
        mRecyclerview.setAdapter(mStorageAdapter);
        mRecyclerview.setHasFixedSize(true);
        // recyclerview 관련 코드
        ItemTouchHelper mHelper = new ItemTouchHelper(new StorageItemTouchHelperCallback(mStorageAdapter));
        mHelper.attachToRecyclerView(mRecyclerview);

        Bundle extra = this.getArguments();
        if (extra != null) {
            mSelectedDate = extra.getString(Schedule.SELECTED_DATE);
        }

        mStorageAdapter.setOnStorageItemClickListener(new StorageAdapter.OnStorageItemClickListener() {
            @Override
            public void onStorageItemClick(String emoji, String contents, String category, int ListPos) {
                int complete = 0;
                String completedTime = "";
                mDBHelper.insertSchedule(emoji, contents, category, complete, completedTime, ListPos,
                        0, 0, 0, 0, "", "", 0, mSelectedDate, "");
                if (mListener != null) {
                    mListener.clickAddButton();
                }
                Toast.makeText(getContext(), "\"" + contents + "\"" + "가" + " 추가됐어요." + "\uD83D\uDCD1", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getActivity(), "\uD83D\uDC49 오른쪽 스와이프로 삭제할 수 있어요.", Toast.LENGTH_SHORT).show();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHelper = new SPDBHelper(getContext());
    }

}
