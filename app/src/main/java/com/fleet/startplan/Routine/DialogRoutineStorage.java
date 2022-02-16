package com.fleet.startplan.Routine;

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

import com.fleet.startplan.Adapter.StorageRoutineAdapter;
import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Dialog.DialogAdd;
import com.fleet.startplan.Dialog.DialogPremium;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.Model.Products;
import com.fleet.startplan.Schedule.ScheduleList;
import com.fleet.startplan.R;
import com.fleet.startplan.SharedPreference.PreferenceManager;
import com.fleet.startplan.TouchHelperAdapter.StorageRoutineItemTouchHelperCallback;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;

public class DialogRoutineStorage extends BottomSheetDialogFragment {

    private SPDBHelper mDBHelper;

    private int mRoutineID;

    private OnStorageRoutineTodoAddListener mListener;

    public interface OnStorageRoutineTodoAddListener {
        void clickAdd();
    }

    public void setOnStorageRoutineTodoAddListener(OnStorageRoutineTodoAddListener listener) {
        this.mListener = listener;
    }

    private static ArrayList<StorageRoutineTodoItem> stRtItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        roundTheEdges();
        View view = inflater.inflate(R.layout.dialog_storage_routine_todo, container, false);

        RecyclerView mRecyclerview = view.findViewById(R.id.rv_storage_routine);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        stRtItems = mDBHelper.getStorageRoutineTodoItems();
        Collections.sort(stRtItems, StorageRoutineTodoItem.setStorageRoutineTodoItem);
        StorageRoutineAdapter mStorageRoutineTodoAdapter = new StorageRoutineAdapter(stRtItems);

        mRecyclerview.setAdapter(mStorageRoutineTodoAdapter);
        mRecyclerview.setHasFixedSize(true);
        ItemTouchHelper mHelper = new ItemTouchHelper(new StorageRoutineItemTouchHelperCallback(mStorageRoutineTodoAdapter));
        mHelper.attachToRecyclerView(mRecyclerview);

        Bundle extra = this.getArguments();
        if (extra != null) {
            mRoutineID = extra.getInt(ScheduleList.ROUTINE_TODO_ITEM_ID);
        }

        mStorageRoutineTodoAdapter.setOnStorageRoutineTodoItemClickListener(new StorageRoutineAdapter.OnStorageRoutineTodoItemClickListener() {
            @Override
            public void onToss(int id) {

                ArrayList<StorageRoutineTodoItem> srtItems = mDBHelper.searchStorageRoutineTodo(id);
                StorageRoutineTodoItem i = srtItems.get(0);
                String contents = i.getStRtContents();
                String registerDate = i.getStRtParentRegisterDate();

                int lastID = 0;
                ArrayList<RoutineItem> rtItems = mDBHelper.getAllRoutineTodo();
                if (rtItems.isEmpty()) {
                    lastID = 1;
                } else {
                    RoutineItem item = rtItems.get(mDBHelper.getAllRoutineTodo().size() - 1);
                    lastID = item.getrTodoId() + 1;
                }

                ArrayList<RoutineItem> items = mDBHelper.getRoutineTodoItems(mRoutineID);
                boolean checkPremiumUser = PreferenceManager.getBoolean(getContext(), Products.PREMIUM_USER);
                if (!checkPremiumUser) {
                    if (items.size() > 4) {
                        DialogPremium dialogPremium = new DialogPremium();
                        dialogPremium.setStyle(DialogAdd.STYLE_NORMAL,R.style.CustomBottomSheetDialogTheme);
                        dialogPremium.show(getChildFragmentManager(), "dialogPremium");
                    } else {
                        save(lastID, contents, registerDate);
                    }
                } else {
                    save(lastID, contents, registerDate);
                }
            }
        });

        return view;
    }


    private void save(int lastID, String contents, String registerDate) {
        mDBHelper.insertRoutine(mRoutineID, Information.ROUTINE_CATEGORY_TODO, lastID, 0, contents, registerDate);
        Toast.makeText(getContext(), "루틴이 추가 됐어요 \uD83D\uDDC4", Toast.LENGTH_SHORT).show();
        if (mListener != null) {
            mListener.clickAdd();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHelper = new SPDBHelper(getContext());
    }


    private void roundTheEdges() {
        // dialog에서 모서리 둥글게 하는 코드
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

}
