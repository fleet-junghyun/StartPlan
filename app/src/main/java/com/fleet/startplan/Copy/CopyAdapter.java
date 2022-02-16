package com.fleet.startplan.Copy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Model.Information;
import com.fleet.startplan.R;
import com.fleet.startplan.TouchHelperAdapter.OnSingleClickListener;

import java.util.ArrayList;

public class CopyAdapter extends RecyclerView.Adapter<BringItemViewHolder> {


    private static int BRING_TYPE_TIME = 0;
    private static int BRING_TYPE_TODO = 1;
    private static int BRING_TYPE_ROUTINE_PERCENTAGE = 2;
    private static int BRING_TYPE_DIVIDING_LINE_AM_PM = 3;
    private static int BRING_TYPE_DIVIDING_LINE_WHEN = 4;


    private ArrayList<CopyItem> brItems;

    public CopyAdapter(ArrayList<CopyItem> brItems) {
        this.brItems = brItems;
    }

    public interface OnRemoveListener {
        void onRemove(int size);
    }

    private OnRemoveListener mListener = null;

    public void setOnRemoveListener(OnRemoveListener mListener) {
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public BringItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == BRING_TYPE_TIME) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_copy_time, parent, false);
            return new TimeItemViewHolder(v);
        } else if (viewType == BRING_TYPE_TODO) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_copy_todo, parent, false);
            return new TodoItemViewHolder(v);
        } else if (viewType == BRING_TYPE_ROUTINE_PERCENTAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_copy_routine, parent, false);
            return new RoutineTodoItemViewHolder(v);
        } else if (viewType == BRING_TYPE_DIVIDING_LINE_AM_PM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_copy_dividing_line_am_pm, parent, false);
            return new AmPmItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_copy_dividing_line_when, parent, false);
            return new WhenItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BringItemViewHolder holder, int position) {
        holder.onBringBind(brItems.get(position));
    }

    @Override
    public int getItemCount() {
        return brItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        CopyItem item = brItems.get(position);
        String category = item.getBrCategory();
        if (category.equals(Information.CATEGORY_TIME)) {
            return BRING_TYPE_TIME;
        } else if (category.equals(Information.CATEGORY_TODO)) {
            return BRING_TYPE_TODO;
        } else if (category.equals(Information.CATEGORY_ROUTINE)) {
            return BRING_TYPE_ROUTINE_PERCENTAGE;
        } else if (category.equals(Information.CATEGORY_DIVIDING_LINE_AM_PM)) {
            return BRING_TYPE_DIVIDING_LINE_AM_PM;
        } else {
            return BRING_TYPE_DIVIDING_LINE_WHEN;
        }
    }

    private class TimeItemViewHolder extends BringItemViewHolder {

        TextView mBrTimeEmoji, mBrTimeContents, mBrTimeAmPm;
        ImageView mTimeItemRemove;

        public TimeItemViewHolder(View itemView) {
            super(itemView);
            mBrTimeEmoji = itemView.findViewById(R.id.tv_copy_item_time_emoji);
            mBrTimeContents = itemView.findViewById(R.id.tv_copy_item_time);
            mBrTimeAmPm = itemView.findViewById(R.id.tv_copy_item_time_am_pm);
            mTimeItemRemove = itemView.findViewById(R.id.iv_copy_item_time_remove);

            mTimeItemRemove.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int pos = getAdapterPosition();
                    brItems.remove(pos);
                    notifyItemRemoved(pos);
                    if (mListener != null) {
                        mListener.onRemove(brItems.size());
                    }
                }
            });
        }

        @Override
        void onBringBind(CopyItem brItems) {
            mBrTimeEmoji.setText(brItems.getBrEmoji());
            mBrTimeContents.setText(brItems.getBrContents());
            mBrTimeAmPm.setText(brItems.getStartTime() + "-" + brItems.getEndTime());
        }
    }

    private class TodoItemViewHolder extends BringItemViewHolder {

        TextView mBrTimeEmoji, mBrTimeContents;
        ImageView mTodoItemRemove;


        public TodoItemViewHolder(View itemView) {
            super(itemView);
            mBrTimeEmoji = itemView.findViewById(R.id.tv_copy_item_todo_emoji);
            mBrTimeContents = itemView.findViewById(R.id.tv_copy_item_todo);
            mTodoItemRemove = itemView.findViewById(R.id.iv_copy_item_todo_remove);

            mTodoItemRemove.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int pos = getAdapterPosition();
                    brItems.remove(pos);
                    notifyItemRemoved(pos);
                    if (mListener != null) {
                        mListener.onRemove(brItems.size());
                    }
                }
            });

        }

        @Override
        void onBringBind(CopyItem brItems) {

            mBrTimeEmoji.setText(brItems.getBrEmoji());
            mBrTimeContents.setText(brItems.getBrContents());
        }

    }

    private class RoutineTodoItemViewHolder extends BringItemViewHolder {
        TextView mBrPercentage, mBrTimeEmoji, mBrTimeContents;
        ImageView mRoutineItemRemove;

        public RoutineTodoItemViewHolder(View itemView) {
            super(itemView);
            mBrPercentage = itemView.findViewById(R.id.tv_copy_item_routine_percentage);
            mBrTimeEmoji = itemView.findViewById(R.id.tv_copy_item_routine_emoji);
            mBrTimeContents = itemView.findViewById(R.id.tv_copy_item_routine_contents);
            mRoutineItemRemove = itemView.findViewById(R.id.iv_copy_item_routine_remove);

            mRoutineItemRemove.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int pos = getAdapterPosition();
                    brItems.remove(pos);
                    notifyItemRemoved(pos);
                    if (mListener != null) {
                        mListener.onRemove(brItems.size());
                    }
                }
            });
        }

        @Override
        void onBringBind(CopyItem brItems) {
            mBrPercentage.setText("0%");
            mBrTimeEmoji.setText(brItems.getBrEmoji());
            mBrTimeContents.setText(brItems.getBrContents());
        }
    }

    class AmPmItemViewHolder extends BringItemViewHolder {
        TextView mAmPm;
        ImageView mAmPmItemRemove;

        public AmPmItemViewHolder(View itemView) {
            super(itemView);
            mAmPm = itemView.findViewById(R.id.tv_item_copy_line_am_pm);
            mAmPmItemRemove = itemView.findViewById(R.id.iv_item_copy_dividing_line_amPm_remove);
            mAmPmItemRemove.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int pos = getAdapterPosition();
                    brItems.remove(pos);
                    notifyItemRemoved(pos);
                    if (mListener != null) {
                        mListener.onRemove(brItems.size());
                    }
                }
            });
        }

        @Override
        void onBringBind(CopyItem brItems) {
            mAmPm.setText(brItems.getBrContents());
        }
    }

    private class WhenItemViewHolder extends BringItemViewHolder {

        TextView mEmoji, mWhen;
        ImageView mWhenItemRemove;

        public WhenItemViewHolder(View itemView) {
            super(itemView);
            mEmoji = itemView.findViewById(R.id.tv_item_copy_dividing_line_emoji);
            mWhen = itemView.findViewById(R.id.tv_item_copy_dividing_line_when);
            mWhenItemRemove = itemView.findViewById(R.id.iv_item_copy_dividing_line_when_remove);
            mWhenItemRemove.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int pos = getAdapterPosition();
                    brItems.remove(pos);
                    notifyItemRemoved(pos);
                    if (mListener != null) {
                        mListener.onRemove(brItems.size());
                    }
                }
            });
        }

        @Override
        void onBringBind(CopyItem brItems) {
            mEmoji.setText(brItems.getBrEmoji());
            mWhen.setText(brItems.getBrContents());
        }
    }
}
