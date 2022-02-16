package com.fleet.startplan.Adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Model.AnalyticsListItem;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.R;
import com.fleet.startplan.Routine.RoutineItem;

import java.util.ArrayList;

public class AnalyticsAdapter extends RecyclerView.Adapter<AnalyticsItemViewHolder> {

    private static int TYPE_TODO_TIME = 0;
    private static int TYPE_TODO = 1;
    private static int TYPE_D_DAY = 2;
    private static int TYPE_START_DAY = 3;
    private static int TYPE_ROUTINE_PERCENTAGE = 4;
    private static int TYPE_DIVIDING_LINE_AM_PM = 5;
    private static int TYPE_DIVIDING_LINE_WHEN = 6;

    private ArrayList<AnalyticsListItem> atItems;
    private String category = "";

    private SPDBHelper mDBHelper;

    private static ArrayList<RoutineItem> rtItems = new ArrayList<>();

    public AnalyticsAdapter(ArrayList<AnalyticsListItem> atItems) {
        this.atItems = atItems;
    }


    @NonNull
    @Override
    public AnalyticsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mDBHelper = new SPDBHelper(parent.getContext());
        if (viewType == TYPE_TODO_TIME) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_todo_time, parent, false);
            return new AnalyticsAdapter.TimeItemViewHolder(v);
        } else if (viewType == TYPE_TODO) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_todo, parent, false);
            return new AnalyticsAdapter.ToDoItemViewHolder(v);
        } else if (viewType == TYPE_D_DAY) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_d_day, parent, false);
            return new AnalyticsAdapter.DDayItemViewHolder(v);
        } else if (viewType == TYPE_START_DAY) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_start_day, parent, false);
            return new AnalyticsAdapter.StartDayItemViewHolder(v);
        } else if (viewType == TYPE_ROUTINE_PERCENTAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_routine_todo, parent, false);
            return new AnalyticsAdapter.RoutinePercentageItemViewHolder(v);
        } else if (viewType == TYPE_DIVIDING_LINE_AM_PM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_dividingline_am_pm, parent, false);
            return new AmPmItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_dividingline_when, parent, false);
            return new WhenItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyticsItemViewHolder holder, int position) {
        holder.onAnalyticsListBind(atItems.get(position));
    }


    @Override
    public int getItemCount() {
        return atItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        AnalyticsListItem item = atItems.get(position);
        category = item.getScCategory();
        if (category.equals(Information.CATEGORY_TIME)) {
            return TYPE_TODO_TIME;
        } else if (category.equals(Information.CATEGORY_TODO)) {
            return TYPE_TODO;
        } else if (category.equals(Information.CATEGORY_D_DAY)) {
            return TYPE_D_DAY;
        } else if (category.equals(Information.CATEGORY_START_DAY)) {
            return TYPE_START_DAY;
        } else if (category.equals(Information.CATEGORY_ROUTINE)) {
            return TYPE_ROUTINE_PERCENTAGE;
        } else if (category.equals(Information.CATEGORY_DIVIDING_LINE_AM_PM)) {
            return TYPE_DIVIDING_LINE_AM_PM;
        } else {
            return TYPE_DIVIDING_LINE_WHEN;
        }

    }


    class TimeItemViewHolder extends AnalyticsItemViewHolder {

        TextView mToDoTimeEmoji;
        TextView mToDoTimeContents;
        TextView mToDoStartTime;
        TextView mToDoEndTime;
        View mToDoTimeLayout;

        public TimeItemViewHolder(View itemView) {
            super(itemView);

            mToDoTimeEmoji = itemView.findViewById(R.id.tv_item_schedule_todo_time_emoji);
            mToDoTimeContents = itemView.findViewById(R.id.tv_item_schedule_todo_time_contents);
            mToDoStartTime = itemView.findViewById(R.id.tv_item_schedule_todo_start_time);
            mToDoTimeLayout = itemView.findViewById(R.id.layout_item_schedule_todo_time);
            mToDoEndTime = itemView.findViewById(R.id.tv_item_schedule_todo_end_time);
        }


        @Override
        void onAnalyticsListBind(AnalyticsListItem atItems) {
            mToDoTimeEmoji.setText(atItems.getScEmoji());
            mToDoTimeContents.setText(atItems.getScContents());
            mToDoStartTime.setText(atItems.getStartTime());
            mToDoEndTime.setText(atItems.getEndTime());

            if (atItems.getScComplete() == 0) {
                mToDoTimeLayout.setBackgroundResource(R.drawable.shape_schedule_item_container);
                mToDoTimeContents.setPaintFlags(0);
                mToDoStartTime.setPaintFlags(0);
                mToDoEndTime.setPaintFlags(0);
            } else {
                mToDoTimeLayout.setBackgroundResource(R.drawable.shape_item_complete_on);
                mToDoTimeContents.setPaintFlags(mToDoTimeContents.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mToDoStartTime.setPaintFlags(mToDoStartTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mToDoEndTime.setPaintFlags(mToDoEndTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }


    class ToDoItemViewHolder extends AnalyticsItemViewHolder {

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
        void onAnalyticsListBind(AnalyticsListItem atItems) {
            mToDoEmoji.setText(atItems.getScEmoji());
            mToDoContents.setText(atItems.getScContents());
            if (atItems.getScComplete() == 0) {
                mToDoLayout.setBackgroundResource(R.drawable.shape_schedule_item_container);
                mToDoContents.setPaintFlags(0);
            } else {
                mToDoLayout.setBackgroundResource(R.drawable.shape_item_complete_on);
                mToDoContents.setPaintFlags(mToDoContents.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

    }


    class DDayItemViewHolder extends AnalyticsItemViewHolder {

        TextView mDDayEmoji;
        TextView mDDayContents;
        TextView mDDayCount;
        View mDDayLayout;
        ImageView mDDayInfinite;

        public DDayItemViewHolder(View itemView) {
            super(itemView);
            mDDayEmoji = itemView.findViewById(R.id.tv_item_schedule_d_day_emoji);
            mDDayContents = itemView.findViewById(R.id.tv_item_schedule_d_day_contents);
            mDDayCount = itemView.findViewById(R.id.tv_item_schedule_d_day_count);
            mDDayLayout = itemView.findViewById(R.id.layout_item_schedule_d_day);
            mDDayInfinite = itemView.findViewById(R.id.iv_item_schedule_d_day_infinite);
        }

        @Override
        void onAnalyticsListBind(AnalyticsListItem atItems) {
            mDDayEmoji.setText(atItems.getScEmoji());
            mDDayContents.setText(atItems.getScContents());
            mDDayLayout.setBackgroundResource(R.drawable.shape_item_complete_on);
            if (atItems.getdDayEndDate().equals("pin")) {
                mDDayCount.setText(null);
                mDDayInfinite.setVisibility(View.VISIBLE);
            } else {
                mDDayInfinite.setVisibility(View.GONE);
                if (atItems.getScStandardDate().equals(atItems.getScRegisteredDate())) {
                    if (atItems.getDday() == 0) {
                        mDDayCount.setText("D-DAY");
                    } else {
                        mDDayCount.setText("START");
                    }
                } else if (atItems.getCountDay() == 0) {
                    mDDayCount.setText("D-DAY");
                } else {
                    mDDayCount.setText("D-" + "" + atItems.getCountDay());
                }
            }

        }
    }


    class StartDayItemViewHolder extends AnalyticsItemViewHolder {

        TextView mStartDayEmoji, mStartDayContents, mStartDayCount;
        View mStartDayLayout;

        public StartDayItemViewHolder(View itemView) {
            super(itemView);
            mStartDayEmoji = itemView.findViewById(R.id.tv_item_schedule_start_day_emoji);
            mStartDayContents = itemView.findViewById(R.id.tv_item_schedule_start_day_contents);
            mStartDayCount = itemView.findViewById(R.id.tv_item_schedule_start_day_count);
            mStartDayLayout = itemView.findViewById(R.id.layout_item_schedule_start_day);
        }

        @Override
        void onAnalyticsListBind(AnalyticsListItem atItems) {
            mStartDayEmoji.setText(atItems.getScEmoji());
            mStartDayContents.setText(atItems.getScContents());
            mStartDayLayout.setBackgroundResource(R.drawable.shape_item_complete_on);

            if (atItems.calculateStartDay() == 0) {
                mStartDayCount.setText("1일째");
            } else {
                mStartDayCount.setText(atItems.calculateStartDay() + 1 + "일째");
            }
        }

    }


    class RoutinePercentageItemViewHolder extends AnalyticsItemViewHolder {

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
        void onAnalyticsListBind(AnalyticsListItem atItems) {
            mPercentageEmoji.setText(atItems.getScEmoji());
            mPercentageContents.setText(atItems.getScContents());

            int sum = 0;
            int id = atItems.getScId();
            rtItems = mDBHelper.getRoutineTodoItems(id);
            for (int i = 0; i < rtItems.size(); ++i) {
                RoutineItem item = rtItems.get(i);
                sum += item.getrTodoComplete();
            }
            int size = rtItems.size();
            double percent = 0;
            if (size != 0) {
                percent = Math.floor((double) sum / (double) size * 100.0);
            }
            int i = (int) percent;

            if (i == 100) {
                mPercentageLayout.setBackgroundResource(R.drawable.shape_item_complete_on);
            } else {
                mPercentageLayout.setBackgroundResource(R.drawable.shape_schedule_item_container);
            }

            mPercentageItem.setText(i + "%");
        }

    }

    class AmPmItemViewHolder extends AnalyticsItemViewHolder {
        TextView mAmPm;

        public AmPmItemViewHolder(View itemView) {
            super(itemView);
            mAmPm = itemView.findViewById(R.id.tv_item_dividing_line_am_pm);
        }

        @Override
        void onAnalyticsListBind(AnalyticsListItem atItems) {
            mAmPm.setText(atItems.getScContents());
        }
    }

    class WhenItemViewHolder extends AnalyticsItemViewHolder {

        TextView mEmoji, mWhen;

        public WhenItemViewHolder(View itemView) {
            super(itemView);
            mEmoji = itemView.findViewById(R.id.tv_item_dividing_line_emoji);
            mWhen = itemView.findViewById(R.id.tv_item_dividing_line_when);
        }

        @Override
        void onAnalyticsListBind(AnalyticsListItem atItems) {
            mEmoji.setText(atItems.getScEmoji());
            mWhen.setText(atItems.getScContents());
        }
    }
}
