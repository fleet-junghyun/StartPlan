package com.fleet.startplan.SuperFocus;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Dialog.DialogAdd;
import com.fleet.startplan.Dialog.DialogReview;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.R;
import com.fleet.startplan.Routine.RoutineItem;
import com.fleet.startplan.SharedPreference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;

public class LockAdapter extends RecyclerView.Adapter<LockItemViewHolder> implements LockItemTouchHelperListener {


    private static int TYPE_TODO_TIME = 0;
    private static int TYPE_TODO = 1;
    private static int TYPE_D_DAY = 2;
    private static int TYPE_START_DAY = 3;
    private static int TYPE_ROUTINE_PERCENTAGE = 4;
    private static int TYPE_DIVIDING_LINE_AM_PM = 5;
    private static int TYPE_DIVIDING_LINE_WHEN = 6;

    private ArrayList<LockItem> _Items;
    private static ArrayList<RoutineItem> rtItems = new ArrayList<>();
    private SPDBHelper mDBHelper;

    public interface OnItemClickListener {
        void onItemClick(View v, int position, int itemId, String title);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setItems(ArrayList<LockItem> _Items) {
        this._Items = _Items;
    }

    public LockAdapter(ArrayList<LockItem> _Items) {
        this._Items = _Items;
    }


    @NonNull
    @Override
    public LockItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mDBHelper = new SPDBHelper(parent.getContext());
        if (viewType == TYPE_TODO_TIME) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_todo_time, parent, false);
            return new TimeItemViewHolder(v);
        } else if (viewType == TYPE_TODO) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_todo, parent, false);
            return new ToDoItemViewHolder(v);
        } else if (viewType == TYPE_D_DAY) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_d_day, parent, false);
            return new DDayItemViewHolder(v);
        } else if (viewType == TYPE_START_DAY) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_start_day, parent, false);
            return new StartDayItemViewHolder(v);
        } else if (viewType == TYPE_ROUTINE_PERCENTAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_routine_todo, parent, false);
            return new RoutinePercentageItemViewHolder(v);
        } else if (viewType == TYPE_DIVIDING_LINE_AM_PM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_dividingline_am_pm, parent, false);
            return new AmPmItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_dividingline_when, parent, false);
            return new WhenItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LockItemViewHolder holder, int position) {
        holder.onLockBind(_Items.get(position));
    }

    @Override
    public int getItemCount() {
        return _Items.size();
    }

    @Override
    public int getItemViewType(int position) {
        LockItem item = _Items.get(position);
        String category = item.getScCategory();
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

    @Override
    public boolean onItemMove(int from_position, int to_position) {

        LockItem from = _Items.get(from_position);
        LockItem to = _Items.get(to_position);

        if (from.getScCategory().equals("dDay") || to.getScCategory().equals("dDay")
                || from.getScCategory().equals(Information.CATEGORY_START_DAY) || to.getScCategory().equals(Information.CATEGORY_START_DAY)
        ) {
            return false;
        } else {
            mDBHelper.lockSwapItem(from, to);
            int fromPos = from.getScListPosition();
            int toPos = to.getScListPosition();
            from.setScListPosition(toPos);
            to.setScListPosition(fromPos);
            Collections.swap(_Items, to_position, from_position);
            notifyItemMoved(from_position, to_position);
            return true;
        }
    }

    @Override
    public void onItemSwipe(int position) {

    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        LockItem item = null;
        try {
            item = _Items.get(position);
        } catch (ArrayIndexOutOfBoundsException e) {
            Toast.makeText(viewHolder.itemView.getContext(), "죄송합니다. 오류가 발생했어요.", Toast.LENGTH_SHORT).show();
        }
        int scId = item.getScId();
        int scComplete = item.getScComplete();
        if (scComplete == 0) {
            mDBHelper.upDateComplete(scId, 1);
            item.setScComplete(1);
            notifyItemChanged(position);
            Toast.makeText(viewHolder.itemView.getContext(), "✅ 일정을 완료했어요.", Toast.LENGTH_SHORT).show();

            boolean completedReview = PreferenceManager.getBoolean(viewHolder.itemView.getContext(), PreferenceManager.COMPLETED_REVIEW);

            if (!completedReview) {
                int exposureNumberReview = PreferenceManager.getInt(viewHolder.itemView.getContext(), PreferenceManager.EXPOSURE_NUMBER_REVIEW);
                PreferenceManager.setInt(viewHolder.itemView.getContext(), PreferenceManager.EXPOSURE_NUMBER_REVIEW, exposureNumberReview + 1);
                if (exposureNumberReview == 10) {
                    openReviewDialog(viewHolder);
                } else if (exposureNumberReview == 20) {
                    openReviewDialog(viewHolder);
                } else if (exposureNumberReview == 50) {
                    openReviewDialog(viewHolder);
                }
            }

        } else {
            mDBHelper.upDateComplete(scId, 0);
            item.setScComplete(0);
            notifyItemChanged(position);
            Toast.makeText(viewHolder.itemView.getContext(), "일정을 되돌렸어요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openReviewDialog(RecyclerView.ViewHolder vh) {
        DialogReview dialogReview = new DialogReview();
        dialogReview.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        dialogReview.show(((AppCompatActivity) vh.itemView.getContext()).getSupportFragmentManager(), "DialogReview");
    }

    private class TimeItemViewHolder extends LockItemViewHolder {

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
        void onLockBind(LockItem _item) {
            mToDoTimeEmoji.setText(_item.getScEmoji());
            mToDoTimeContents.setText(_item.getScContents());
            mToDoStartTime.setText(_item.getStartTime());
            mToDoEndTime.setText(_item.getEndTime());

            if (_item.getScComplete() == 0) {
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

    private class ToDoItemViewHolder extends LockItemViewHolder {

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
        void onLockBind(LockItem _item) {
            mToDoEmoji.setText(_item.getScEmoji());
            mToDoContents.setText(_item.getScContents());
            if (_item.getScComplete() == 0) {
                mToDoLayout.setBackgroundResource(R.drawable.shape_schedule_item_container);
                mToDoContents.setPaintFlags(0);
            } else {
                mToDoLayout.setBackgroundResource(R.drawable.shape_item_complete_on);
                mToDoContents.setPaintFlags(mToDoContents.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }

    private class DDayItemViewHolder extends LockItemViewHolder {

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
        void onLockBind(LockItem _item) {
            mDDayEmoji.setText(_item.getScEmoji());
            mDDayContents.setText(_item.getScContents());
            mDDayContents.setSingleLine(true);
            mDDayContents.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mDDayContents.setSelected(true);
            mDDayContents.setMarqueeRepeatLimit(-1);

            mDDayLayout.setBackgroundResource(R.drawable.shape_item_complete_on);
            if (_item.getdDayEndDate().equals("pin")) {
                mDDayCount.setText(null);
                mDDayInfinite.setVisibility(View.VISIBLE);
            } else {
                mDDayInfinite.setVisibility(View.GONE);
                if (_item.getScStandardDate().equals(_item.getScRegisteredDate())) {
                    if (_item.getDday() == 0) {
                        mDDayCount.setText("D-DAY");
                    } else {
                        mDDayCount.setText("START");
                    }
                } else if (_item.getCountDay() == 0) {
                    mDDayCount.setText("D-DAY");
                } else if (_item.getCountDay() > 0) {
                    mDDayCount.setText("D-" + "" + _item.getCountDay());
                }
            }
        }
    }

    private class StartDayItemViewHolder extends LockItemViewHolder {

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
        void onLockBind(LockItem _item) {
            mStartDayEmoji.setText(_item.getScEmoji());
            mStartDayContents.setText(_item.getScContents());
            mStartDayLayout.setBackgroundResource(R.drawable.shape_item_complete_on);

            mStartDayContents.setSingleLine(true);
            mStartDayContents.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mStartDayContents.setSelected(true);
            mStartDayContents.setMarqueeRepeatLimit(-1);


            if (_item.calculateStartDay() == 0) {
                mStartDayCount.setText("1일째");
            } else {
                mStartDayCount.setText(_item.calculateStartDay() + 1 + "일째");
            }
        }
    }

    private class RoutinePercentageItemViewHolder extends LockItemViewHolder {

        TextView mPercentageItem, mPercentageEmoji, mPercentageContents;
        View mPercentageLayout;


        public RoutinePercentageItemViewHolder(View itemView) {
            super(itemView);

            mPercentageItem = itemView.findViewById(R.id.tv_item_routine_todo_percentage);
            mPercentageEmoji = itemView.findViewById(R.id.tv_item_routine_emoji);
            mPercentageContents = itemView.findViewById(R.id.tv_item_routine_contents);
            mPercentageLayout = itemView.findViewById(R.id.v_item_routine_todo_container);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    LockItem item = _Items.get(pos);
                    int id = item.getScId();
                    String title = item.getScContents();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos, id, title);
                        }
                    }
                }
            });

        }

        @Override
        void onLockBind(LockItem _item) {
            mPercentageEmoji.setText(_item.getScEmoji());
            mPercentageContents.setText(_item.getScContents());
            int sum = 0;
            int id = _item.getScId();
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
                mDBHelper.upDateComplete(id, 1);
            } else {
                mPercentageLayout.setBackgroundResource(R.drawable.shape_schedule_item_container);
                mDBHelper.upDateComplete(id, 0);
            }

            mPercentageItem.setText(i + "%");
        }
    }

    private class AmPmItemViewHolder extends LockItemViewHolder {

        TextView mAmPm;

        public AmPmItemViewHolder(View itemView) {
            super(itemView);
            mAmPm = itemView.findViewById(R.id.tv_item_dividing_line_am_pm);
        }

        @Override
        void onLockBind(LockItem _item) {
            mAmPm.setText(_item.getScContents());
        }
    }

    private class WhenItemViewHolder extends LockItemViewHolder {
        TextView mEmoji, mWhen;

        public WhenItemViewHolder(View itemView) {
            super(itemView);
            mEmoji = itemView.findViewById(R.id.tv_item_dividing_line_emoji);
            mWhen = itemView.findViewById(R.id.tv_item_dividing_line_when);
        }

        @Override
        void onLockBind(LockItem _item) {
            mEmoji.setText(_item.getScEmoji());
            mWhen.setText(_item.getScContents());
        }
    }
}
