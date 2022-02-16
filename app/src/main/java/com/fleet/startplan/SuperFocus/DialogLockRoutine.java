package com.fleet.startplan.SuperFocus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;

public class DialogLockRoutine extends BottomSheetDialogFragment {

    private SPDBHelper mDBHelper;
    private int _routineItemID;
    private String _titleData;

    public interface OnCompleteListener {
        void onComplete();
    }

    private OnCompleteListener mListener = null;

    public void setOnCompleteListener(OnCompleteListener mListener) {
        this.mListener = mListener;
    }


    private static ArrayList<LockRoutineItem> lockRoutineItems = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_lock_routine, container, false);

        TextView _routineTitle = v.findViewById(R.id.tv_lock_routine_title);
        RecyclerView mRecyclerView = v.findViewById(R.id.rv_lock_routine);

        Bundle extra = this.getArguments();
        if (extra != null) {
            _titleData = extra.getString(LockScreenActivity.LOCK_SCREEN_TITLE);
            _routineItemID = extra.getInt(LockScreenActivity.LOCK_ROUTINE_ITEM_ID);
        }
        _routineTitle.setText(_titleData);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lockRoutineItems = mDBHelper.getLockRoutineItems(_routineItemID);
        Collections.sort(lockRoutineItems, LockRoutineItem.setLockRoutine);
        LockRoutineAdapter mAdapter = new LockRoutineAdapter(lockRoutineItems);
        mAdapter.setOnItemClickListener(new LockRoutineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                if(mListener!=null){
                    mListener.onComplete();
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new LockRoutineItemTouchHelperCallback(mAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHelper = new SPDBHelper(getContext());
    }
}
