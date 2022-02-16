package com.fleet.startplan.Adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Model.Information;
import com.fleet.startplan.Routine.RoutineActivity;
import com.fleet.startplan.Routine.RoutineItem;
import com.fleet.startplan.Schedule.ScheduleList;
import com.fleet.startplan.SharedPreference.PreferenceManager;
import com.fleet.startplan.Dialog.DialogAdd;
import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Edit.DialogEdit;
import com.fleet.startplan.R;
import com.fleet.startplan.Edit.DialogEditPopUp;
import com.fleet.startplan.Dialog.DialogReview;
import com.fleet.startplan.Model.ScheduleItem;
import com.fleet.startplan.Model.StorageItem;
import com.fleet.startplan.TouchHelperAdapter.ScheduleItemTouchHelperListener;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ScheduleAdapter extends RecyclerView.Adapter<ItemViewHolder> implements ScheduleItemTouchHelperListener {

    private static int TYPE_TODO_TIME = 0;
    private static int TYPE_TODO = 1;
    private static int TYPE_D_DAY = 2;
    private static int TYPE_START_DAY = 3;
    private static int TYPE_ROUTINE_PERCENTAGE = 4;
    private static int TYPE_DIVIDING_LINE_AM_PM = 5;
    private static int TYPE_DIVIDING_LINE_WHEN = 6;

    private ArrayList<ScheduleItem> scItems;

    private SPDBHelper mDBHelper;
    private static ArrayList<RoutineItem> rtItems = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View v, int position, int itemId);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setItems(ArrayList<ScheduleItem> scItems) {
        this.scItems = scItems;
    }

    public ScheduleAdapter(ArrayList<ScheduleItem> scItems) {
        this.scItems = scItems;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.onScheduleBind(scItems.get(position));
    }


    @Override
    public int getItemCount() {
        return scItems.size();
    }


    @Override
    public int getItemViewType(int position) {
        ScheduleItem item = scItems.get(position);
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

        ScheduleItem from = scItems.get(from_position);
        ScheduleItem to = scItems.get(to_position);

        if (from.getScCategory().equals("dDay") || to.getScCategory().equals("dDay")
                || from.getScCategory().equals(Information.CATEGORY_START_DAY) || to.getScCategory().equals(Information.CATEGORY_START_DAY)
        ) {
            return false;
        } else {
            mDBHelper.scheduleSwapItem(from, to);
            int fromPos = from.getScListPosition();
            int toPos = to.getScListPosition();
            from.setScListPosition(toPos);
            to.setScListPosition(fromPos);
            Collections.swap(scItems, to_position, from_position);
            notifyItemMoved(from_position, to_position);
            return true;
        }
    }

    @Override
    public void onItemSwipe(int position) {
    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {

        if (scItems.size() != 0) {
            ScheduleItem item = null;
            try {
                item = scItems.get(position);
            } catch (ArrayIndexOutOfBoundsException e) {
                Toast.makeText(viewHolder.itemView.getContext(), "오류가 발생했어요. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
            }
            int itemId = item.getScId();
            String registerDate = item.getScRegisteredDate();
            String emoji = item.getScEmoji();
            String contents = item.getScContents();
            String category = item.getScCategory();
            FragmentManager manager = ((AppCompatActivity) viewHolder.itemView.getContext()).getSupportFragmentManager();
            DialogEditPopUp dialogEditPopUp = new DialogEditPopUp();
            dialogEditPopUp.show(manager, "DialogItemEdit");
            dialogEditPopUp.setOnEditItemListener(new DialogEditPopUp.SetEditListener() {
                @Override
                public void deleteItem() {
                    scItems.remove(position);
                    notifyItemRemoved(position);
                    mDBHelper.deleteSchedule(itemId);
                    ArrayList<RoutineItem> r = mDBHelper.getRoutineTodoItems(itemId);
                    if (r.size() != 0) {
                        mDBHelper.deleteRoutineInSchedule(itemId, r.size());
                    }
                    Toast.makeText(viewHolder.itemView.getContext(), "\uD83D\uDDD1 계획을 삭제했어요.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void editItem() {
                    DialogEdit dialogEdit = new DialogEdit();
                    dialogEdit.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                    dialogEdit.show(((AppCompatActivity) viewHolder.itemView.getContext()).getSupportFragmentManager(), "DialogEdit");
                    Bundle bundle = new Bundle();
                    bundle.putInt(Information.PUT_EDIT_ITEM_ID, itemId);
                    dialogEdit.setArguments(bundle);
                    dialogEdit.setOnUpdateRefreshListener(new DialogEdit.SetUpdateRefreshListener() {
                        @Override
                        public void updateFragmentBtn(String emoji, String contents, int startHour, int startMinute, int endHour, int endMinute, String endDate) {
                            ScheduleItem scheduleItem;
                            scheduleItem = scItems.get(position);
                            scheduleItem.setScEmoji(emoji);
                            scheduleItem.setScContents(contents);
                            scheduleItem.setTodoStartHour(startHour);
                            scheduleItem.setTodoStartMinute(startMinute);
                            scheduleItem.setTodoEndHour(endHour);
                            scheduleItem.setTodoEndMinute(endMinute);
                            scheduleItem.setdDayEndDate(endDate);
                            notifyItemChanged(position);
                        }
                    });
                }

                @Override
                public void sendTomorrow() {
                    String tomorrow = plusOneDay(registerDate);
                    String sendCategory = "";
                    int lastID = 0;
                    ArrayList<ScheduleItem> sci = mDBHelper.getScheduleItems();
                    if (sci.isEmpty()) {
                        lastID = 1;
                    } else {
                        ScheduleItem item = sci.get(mDBHelper.getScheduleItems().size() - 1);
                        lastID = item.getScId() + 1;
                    }
                    if (category.equals(Information.CATEGORY_TODO)) {
                        sendCategory = Information.CATEGORY_TODO;
                    } else {
                        sendCategory = Information.CATEGORY_ROUTINE;
                    }
                    mDBHelper.insertSchedule(emoji, contents, sendCategory, 0, "", lastID,
                            0, 0, 0, 0, "", "",
                            0, tomorrow, "");
                    if (category.equals(Information.CATEGORY_ROUTINE)) {
                        mDBHelper.sendTomorrowRoutineData(itemId, lastID);
                    }
                }
            });
            Bundle bundle = new Bundle();
            bundle.putString(Information.CHECK_CATEGORY, category);
            try {
                dialogEditPopUp.setArguments(bundle);
            } catch (IllegalStateException e) {
                Toast.makeText(viewHolder.itemView.getContext(), "오류가 발생했어요. 죄송합니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        ScheduleItem item = null;
        try {
            item = scItems.get(position);
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

    public void openReviewDialog(RecyclerView.ViewHolder vh) {
        DialogReview dialogReview = new DialogReview();
        dialogReview.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        dialogReview.show(((AppCompatActivity) vh.itemView.getContext()).getSupportFragmentManager(), "DialogReview");
    }


    public String plusOneDay(String registerDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date defaultDate = dateFormat.parse(registerDate, new ParsePosition(0));
        long getTimeDefaultDate = defaultDate.getTime();
        long plusDay = getTimeDefaultDate + 1000 * 60 * 60 * 24;
        Date changePlusDay = new Date(plusDay);
        return dateFormat.format(changePlusDay);
    }


    class TimeItemViewHolder extends ItemViewHolder {

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
        void onScheduleBind(ScheduleItem scItems) {
            mToDoTimeEmoji.setText(scItems.getScEmoji());
            mToDoTimeContents.setText(scItems.getScContents());
            mToDoStartTime.setText(scItems.getStartTime());
            mToDoEndTime.setText(scItems.getEndTime());


            if (scItems.getScComplete() == 0) {
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

        @Override
        void onStorageBind(StorageItem stItems) {
        }


    }


    class ToDoItemViewHolder extends ItemViewHolder {

        TextView mToDoEmoji;
        TextView mToDoContents;
        View mToDoLayout;

        public ToDoItemViewHolder(View itemView) {
            super(itemView);
            mToDoEmoji = itemView.findViewById(R.id.tv_item_schedule_todo_emoji);
            mToDoContents = itemView.findViewById(R.id.tv_item_schedule_todo_contents);
            mToDoLayout = itemView.findViewById(R.id.layout_item_schedule_todo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    ScheduleItem item = scItems.get(pos);
                    int id = item.getScId();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos, id);
                        }
                    }
                }
            });


        }

        @Override
        void onScheduleBind(ScheduleItem scItems) {
            mToDoEmoji.setText(scItems.getScEmoji());
            mToDoContents.setText(scItems.getScContents());

            if (scItems.getScComplete() == 0) {
                mToDoLayout.setBackgroundResource(R.drawable.shape_schedule_item_container);
                mToDoContents.setPaintFlags(0);
            } else {
                mToDoLayout.setBackgroundResource(R.drawable.shape_item_complete_on);
                mToDoContents.setPaintFlags(mToDoContents.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

        @Override
        void onStorageBind(StorageItem stItems) {
        }

    }


    class DDayItemViewHolder extends ItemViewHolder {

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
        void onScheduleBind(ScheduleItem scItems) {
            mDDayEmoji.setText(scItems.getScEmoji());
            mDDayContents.setText(scItems.getScContents());
            mDDayContents.setSingleLine(true);
            mDDayContents.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mDDayContents.setSelected(true);
            mDDayContents.setMarqueeRepeatLimit(-1);

            mDDayLayout.setBackgroundResource(R.drawable.shape_item_complete_on);
            if (scItems.getdDayEndDate().equals("pin")) {
                mDDayCount.setText(null);
                mDDayInfinite.setVisibility(View.VISIBLE);
            } else {
                mDDayInfinite.setVisibility(View.GONE);
                if (scItems.getScStandardDate().equals(scItems.getScRegisteredDate())) {
                    if (scItems.getDday() == 0) {
                        mDDayCount.setText("D-DAY");
                    } else {
                        mDDayCount.setText("START");
                    }
                } else if (scItems.getCountDay() == 0) {
                    mDDayCount.setText("D-DAY");
                } else if (scItems.getCountDay() > 0) {
                    mDDayCount.setText("D-" + "" + scItems.getCountDay());
                }
            }
        }

        @Override
        void onStorageBind(StorageItem stItems) {
        }

    }


    class StartDayItemViewHolder extends ItemViewHolder {

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
        void onScheduleBind(ScheduleItem scItems) {
            mStartDayEmoji.setText(scItems.getScEmoji());
            mStartDayContents.setText(scItems.getScContents());
            mStartDayLayout.setBackgroundResource(R.drawable.shape_item_complete_on);

            mStartDayContents.setSingleLine(true);
            mStartDayContents.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mStartDayContents.setSelected(true);
            mStartDayContents.setMarqueeRepeatLimit(-1);


            if (scItems.calculateStartDay() == 0) {
                mStartDayCount.setText("1일째");
            } else {
                mStartDayCount.setText(scItems.calculateStartDay() + 1 + "일째");
            }

        }

        @Override
        void onStorageBind(StorageItem stItems) {

        }
    }


    class RoutinePercentageItemViewHolder extends ItemViewHolder {

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
                    ScheduleItem item = scItems.get(pos);
                    int ID = item.getScId();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos, ID);
                        }
                    }
                }
            });

        }


        @Override
        void onScheduleBind(ScheduleItem scItems) {
            mPercentageEmoji.setText(scItems.getScEmoji());
            mPercentageContents.setText(scItems.getScContents());
            int sum = 0;
            int id = scItems.getScId();
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

        @Override
        void onStorageBind(StorageItem stItems) {

        }
    }


    class AmPmItemViewHolder extends ItemViewHolder {

        TextView mAmPm;

        public AmPmItemViewHolder(View itemView) {
            super(itemView);
            mAmPm = itemView.findViewById(R.id.tv_item_dividing_line_am_pm);
        }

        @Override
        void onScheduleBind(ScheduleItem scItems) {
            mAmPm.setText(scItems.getScContents());
        }

        @Override
        void onStorageBind(StorageItem stItems) {

        }
    }


    class WhenItemViewHolder extends ItemViewHolder {

        TextView mEmoji, mWhen;

        public WhenItemViewHolder(View itemView) {
            super(itemView);
            mEmoji = itemView.findViewById(R.id.tv_item_dividing_line_emoji);
            mWhen = itemView.findViewById(R.id.tv_item_dividing_line_when);
        }

        @Override
        void onScheduleBind(ScheduleItem scItems) {
            mEmoji.setText(scItems.getScEmoji());
            mWhen.setText(scItems.getScContents());
        }

        @Override
        void onStorageBind(StorageItem stItems) {

        }
    }
}
