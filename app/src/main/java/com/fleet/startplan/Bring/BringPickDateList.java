package com.fleet.startplan.Bring;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Bring.BringPickDate;
import com.fleet.startplan.Copy.PickDateAdapter;
import com.fleet.startplan.Copy.PickDateItem;
import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.R;
import com.fleet.startplan.TouchHelperAdapter.OnSingleClickListener;

import java.util.ArrayList;
import java.util.Collections;

public class BringPickDateList extends Fragment {

    private static String TURN_ON = "turnOn";
    private static String TURN_OFF = "turnOff";

    public static String PICK_DATE = "pickDate";
    public static String SELECTED_PICK_DATE = "selectedPickDate";


    private RecyclerView mRecyclerView;
    private SPDBHelper mDBHelper;
    private String mPickDate, mSelectedPickDate;
    private ConstraintLayout mNext;

    public interface SetFinishListener {
        void complete();
    }

    public void setOnFinishListener(SetFinishListener mListener) {
        this.mListener = mListener;
    }

    private SetFinishListener mListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pick_date_list, container, false);

        mRecyclerView = v.findViewById(R.id.rv_pick_date);
        mNext = v.findViewById(R.id.layout_pick_date_next);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle extra = this.getArguments();
        if (extra != null) {
            extra = getArguments();
            mPickDate = extra.getString(PICK_DATE);
            mSelectedPickDate = extra.getString(SELECTED_PICK_DATE);
            initList(mPickDate);
        }

        mNext.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getContext(), BringPickDate.class);
                intent.putExtra(PICK_DATE, mPickDate);
                intent.putExtra(SELECTED_PICK_DATE, mSelectedPickDate);
                startActivityResult.launch(intent);
            }
        });

        return v;
    }

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(mListener!=null){
                        mListener.complete();
                    }
                }
            });


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mDBHelper = new SPDBHelper(getContext());
        super.onCreate(savedInstanceState);
    }

    private void initList(String selectedDate) {
        ArrayList<PickDateItem> pickDateItems = mDBHelper.getPickDateList(selectedDate);
        if (pickDateItems.size() == 0) {
            setComplete(TURN_OFF);
        } else {
            setComplete(TURN_ON);
        }
        Collections.sort(pickDateItems, PickDateItem.setPickDateListItems);
        PickDateAdapter mPickDateAdapter = new PickDateAdapter(pickDateItems);
        mRecyclerView.setAdapter(mPickDateAdapter);
    }

    private void setComplete(String visible) {
        if (visible.equals(TURN_ON)) {
            mNext.setVisibility(View.VISIBLE);
        } else {
            mNext.setVisibility(View.GONE);
        }
    }

}
