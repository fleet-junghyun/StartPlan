package com.fleet.startplan.Copy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Model.Information;
import com.fleet.startplan.R;

import java.util.ArrayList;

public class PickDateAdapter extends RecyclerView.Adapter<PickDateItemViewHolder> {

    private static int TYPE_TODO_TIME = 0;
    private static int TYPE_TODO = 1;
    private static int TYPE_ROUTINE_PERCENTAGE = 3;
    private static int TYPE_BRING_DIVIDING_LINE_AM_PM = 4;
    private static int TYPE_BRING_DIVIDING_LINE_WHEN = 5;

    private ArrayList<PickDateItem> pdItems;

    public PickDateAdapter(ArrayList<PickDateItem> pdItems) {
        this.pdItems = pdItems;
    }


    @NonNull
    @Override
    public PickDateItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TODO_TIME) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_todo_time, parent, false);
            return new TimeItemViewHolder(v);
        } else if (viewType == TYPE_TODO) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_todo, parent, false);
            return new ToDoItemViewHolder(v);
        } else if (viewType == TYPE_ROUTINE_PERCENTAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_routine_todo, parent, false);
            return new RoutinePercentageItemViewHolder(v);
        } else if (viewType == TYPE_BRING_DIVIDING_LINE_AM_PM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_dividingline_am_pm, parent, false);
            return new AmPmItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_dividingline_when, parent, false);
            return new WhenItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PickDateItemViewHolder holder, int position) {
        holder.onPickDateListBind(pdItems.get(position));
    }

    @Override
    public int getItemCount() {
        return pdItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        PickDateItem item = pdItems.get(position);
        String category = item.getScCategory();
        if (category.equals(Information.CATEGORY_TIME)) {
            return TYPE_TODO_TIME;
        } else if (category.equals(Information.CATEGORY_TODO)) {
            return TYPE_TODO;
        } else if (category.equals(Information.CATEGORY_ROUTINE)) {
            return TYPE_ROUTINE_PERCENTAGE;
        } else if (category.equals(Information.CATEGORY_DIVIDING_LINE_AM_PM)) {
            return TYPE_BRING_DIVIDING_LINE_AM_PM;
        } else {
            return TYPE_BRING_DIVIDING_LINE_WHEN;
        }
    }

    class TimeItemViewHolder extends PickDateItemViewHolder {

        TextView mToDoTimeEmoji;
        TextView mToDoTimeContents;
        TextView mToDoStartTime;
        TextView mToDoEndTime;

        public TimeItemViewHolder(View itemView) {
            super(itemView);
            mToDoTimeEmoji = itemView.findViewById(R.id.tv_item_schedule_todo_time_emoji);
            mToDoTimeContents = itemView.findViewById(R.id.tv_item_schedule_todo_time_contents);
            mToDoStartTime = itemView.findViewById(R.id.tv_item_schedule_todo_start_time);
            mToDoEndTime = itemView.findViewById(R.id.tv_item_schedule_todo_end_time);
        }

        @Override
        void onPickDateListBind(PickDateItem pdItems) {
            mToDoTimeEmoji.setText(pdItems.getScEmoji());
            mToDoTimeContents.setText(pdItems.getScContents());
            mToDoStartTime.setText(pdItems.getStartTime());
            mToDoEndTime.setText(pdItems.getEndTime());
        }


    }

    private class ToDoItemViewHolder extends PickDateItemViewHolder {

        TextView mToDoEmoji;
        TextView mToDoContents;
        View mToDoLayout;

        public ToDoItemViewHolder(View itemView) {
            super(itemView);
            mToDoEmoji = itemView.findViewById(R.id.tv_item_schedule_todo_emoji);
            mToDoContents = itemView.findViewById(R.id.tv_item_schedule_todo_contents);
            mToDoLayout = itemView.findViewById(R.id.layout_item_schedule_todo);
        }

        @Override
        void onPickDateListBind(PickDateItem pdItems) {

            mToDoEmoji.setText(pdItems.getScEmoji());
            mToDoContents.setText(pdItems.getScContents());

        }
    }

    private class RoutinePercentageItemViewHolder extends PickDateItemViewHolder {
        TextView mPercentageItem, mPercentageEmoji, mPercentageContents;
        View mPercentageLayout;

        public RoutinePercentageItemViewHolder(View itemView) {
            super(itemView);
            mPercentageItem = itemView.findViewById(R.id.tv_item_routine_todo_percentage);
            mPercentageEmoji = itemView.findViewById(R.id.tv_item_routine_emoji);
            mPercentageContents = itemView.findViewById(R.id.tv_item_routine_contents);
            mPercentageLayout = itemView.findViewById(R.id.v_item_routine_todo_container);
        }

        @Override
        void onPickDateListBind(PickDateItem pdItems) {
            mPercentageEmoji.setText(pdItems.getScEmoji());
            mPercentageContents.setText(pdItems.getScContents());
            mPercentageItem.setText("0%");

        }
    }

    class AmPmItemViewHolder extends PickDateItemViewHolder {

        TextView mAmPm;

        public AmPmItemViewHolder(View itemView) {
            super(itemView);
            mAmPm = itemView.findViewById(R.id.tv_item_dividing_line_am_pm);
        }

        @Override
        void onPickDateListBind(PickDateItem pdItems) {
            mAmPm.setText(pdItems.getScContents());
        }

    }

    class WhenItemViewHolder extends PickDateItemViewHolder {

        TextView mEmoji, mWhen;

        public WhenItemViewHolder(View itemView) {
            super(itemView);
            mEmoji = itemView.findViewById(R.id.tv_item_dividing_line_emoji);
            mWhen = itemView.findViewById(R.id.tv_item_dividing_line_when);
        }

        @Override
        void onPickDateListBind(PickDateItem pdItems) {
            mEmoji.setText(pdItems.getScEmoji());
            mWhen.setText(pdItems.getScContents());
        }
    }
}
