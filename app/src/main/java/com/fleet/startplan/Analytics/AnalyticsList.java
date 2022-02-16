package com.fleet.startplan.Analytics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Adapter.AnalyticsAdapter;
import com.fleet.startplan.Adapter.ScheduleAdapter;
import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Model.AnalyticsListItem;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.Model.ScheduleItem;
import com.fleet.startplan.R;
import com.fleet.startplan.Schedule.Schedule;

import java.util.ArrayList;
import java.util.Collections;

public class AnalyticsList extends Fragment {

    private RecyclerView mRecyclerView;
    private SPDBHelper mDBHelper;
    private String mSelectedDate;
    private AnalyticsAdapter mAnalyticsAdapter;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.analytics_list, container, false);

        mRecyclerView = v.findViewById(R.id.rv_analytics);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        Bundle extra = this.getArguments();
        if(extra !=null){
            mSelectedDate = extra.getString(Schedule.SELECTED_DATE);
            initList(mSelectedDate);
        }
        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mDBHelper = new SPDBHelper(getActivity().getApplicationContext());
        super.onCreate(savedInstanceState);
    }

    private void initList(String selectedDate){

        ArrayList<AnalyticsListItem> atItems = mDBHelper.getAnalyticsList(selectedDate, Information.CATEGORY_D_DAY, Information.CATEGORY_START_DAY);
        for (int i = 0; i < atItems.size(); i++) {
            AnalyticsListItem atItem = atItems.get(i);
            atItem.setScStandardDate(selectedDate);
        }
        Collections.sort(atItems, AnalyticsListItem.setAnalyticsItemList);
        mAnalyticsAdapter = new AnalyticsAdapter(atItems);
        mRecyclerView.setAdapter(mAnalyticsAdapter);

    }

}
