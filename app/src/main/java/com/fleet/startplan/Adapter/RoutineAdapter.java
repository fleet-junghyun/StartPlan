package com.fleet.startplan.Adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.Routine.StorageRoutineTodoItem;
import com.fleet.startplan.Routine.RoutineItem;
import com.fleet.startplan.R;
import com.fleet.startplan.TouchHelperAdapter.RoutineItemTouchHelperListener;

import java.util.ArrayList;
import java.util.Collections;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineItemViewHolder> implements RoutineItemTouchHelperListener {

    private ArrayList<RoutineItem> rtItems;
    private SPDBHelper mDBHelper;

    public void setItems(ArrayList<RoutineItem> routineItems) {
        this.rtItems = routineItems;
    }


    public RoutineAdapter(ArrayList<RoutineItem> rtItems) {
        this.rtItems = rtItems;
    }

    @NonNull
    @Override
    public RoutineItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mDBHelper = new SPDBHelper(parent.getContext());
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine, parent, false);
        return new RoutineTodoItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineItemViewHolder holder, int position) {
        holder.onRoutineBind(rtItems.get(position));
    }

    @Override
    public int getItemCount() {
        return rtItems.size();
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {

        RoutineItem from = rtItems.get(from_position);
        RoutineItem to = rtItems.get(to_position);

        mDBHelper.swapRoutineTodoItem(from, to);

        int fromPos = from.getrTodoPos();
        int toPos = to.getrTodoPos();

        from.setrTodoPos(toPos);
        to.setrTodoPos(fromPos);
        Collections.swap(rtItems, to_position, from_position);
        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {

    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        RoutineItem rtItem = rtItems.get(position);
        rtItems.remove(position);
        notifyItemRemoved(position);
        int rtID = rtItem.getrTodoId();
        mDBHelper.deleteRoutineTodoItem(rtID);
        if (rtItems.size() == 0) {
            mDBHelper.changeCategoryInRoutine(rtItem.getrTodoItemId(), Information.CATEGORY_TODO);
        } else {
            mDBHelper.changeCategoryInRoutine(rtItem.getrTodoItemId(), Information.CATEGORY_ROUTINE);
        }
    }

    class RoutineTodoItemViewHolder extends RoutineItemViewHolder {

        TextView mRoutineTodoContents;
        ImageView mRoutineTodoComplete;


        public RoutineTodoItemViewHolder(View itemView) {
            super(itemView);

            mRoutineTodoContents = itemView.findViewById(R.id.tv_item_routine_todo_contents);
            mRoutineTodoComplete = itemView.findViewById(R.id.iv_item_routine_check);


            mRoutineTodoContents.setSingleLine(true);
            mRoutineTodoContents.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mRoutineTodoContents.setSelected(true);
            mRoutineTodoContents.setMarqueeRepeatLimit(-1);
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        RoutineItem routineItem = rtItems.get(pos);
                        int id = routineItem.getrTodoId();
                        if (routineItem.getrTodoComplete() == 0) {
                            mDBHelper.updateRoutineTodoComplete(id, 1);
                            routineItem.setrTodoComplete(1);
                            mRoutineTodoComplete.setImageResource(R.drawable.ic_routine_item_check_on_28dp);
                            mRoutineTodoContents.setPaintFlags(mRoutineTodoContents.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            Toast.makeText(itemView.getContext(), "✔️", Toast.LENGTH_SHORT).show();
                        } else {
                            mDBHelper.updateRoutineTodoComplete(id, 0);
                            routineItem.setrTodoComplete(0);
                            mRoutineTodoComplete.setImageResource(R.drawable.ic_routine_item_check_off_28dp);
                            mRoutineTodoContents.setPaintFlags(0);
                        }
                        notifyItemChanged(pos);
                    }
                }
            });

        }

        @Override
        void onRoutineBind(RoutineItem rtItems) {
            mRoutineTodoContents.setText(rtItems.getrTodoContents());
            if (rtItems.getrTodoComplete() == 0) {
                mRoutineTodoComplete.setImageResource(R.drawable.ic_routine_item_check_off_28dp);
                mRoutineTodoContents.setPaintFlags(0);
            } else {
                mRoutineTodoComplete.setImageResource(R.drawable.ic_routine_item_check_on_28dp);
                mRoutineTodoContents.setPaintFlags(mRoutineTodoContents.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

        @Override
        void onStorageRoutineTodoBind(StorageRoutineTodoItem sRtItems) {

        }

    }

}
