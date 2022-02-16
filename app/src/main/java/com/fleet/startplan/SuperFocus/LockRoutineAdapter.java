package com.fleet.startplan.SuperFocus;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Adapter.ScheduleAdapter;
import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.R;
import com.fleet.startplan.Routine.RoutineItem;

import java.util.ArrayList;
import java.util.Collections;

public class LockRoutineAdapter extends RecyclerView.Adapter<LockRoutineItemViewHolder> implements LockRoutineItemTouchHelperListener {

    private ArrayList<LockRoutineItem> lrItems;
    private SPDBHelper mDBHelper;

    public interface OnItemClickListener {
        void onItemClick();
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    public LockRoutineAdapter(ArrayList<LockRoutineItem> lrItems) {
        this.lrItems = lrItems;
    }

    @NonNull
    @Override
    public LockRoutineItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mDBHelper = new SPDBHelper(parent.getContext());
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lock_routine, parent, false);
        return new LockRoutineTodoItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LockRoutineItemViewHolder holder, int position) {
        holder.bindLockRoutine(lrItems.get(position));
    }

    @Override
    public int getItemCount() {
        return lrItems.size();
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        LockRoutineItem from = lrItems.get(from_position);
        LockRoutineItem to = lrItems.get(to_position);

        mDBHelper.swapLockRoutineItem(from, to);

        int fromPos = from.getrTodoPos();
        int toPos = to.getrTodoPos();

        from.setrTodoPos(toPos);
        to.setrTodoPos(fromPos);
        Collections.swap(lrItems, to_position, from_position);
        notifyItemMoved(from_position, to_position);
        return true;
    }

    private class LockRoutineTodoItemViewHolder extends LockRoutineItemViewHolder {

        TextView _RoutineTodoContents;
        ImageView _RoutineTodoComplete;


        public LockRoutineTodoItemViewHolder(View itemView) {
            super(itemView);
            _RoutineTodoContents = itemView.findViewById(R.id.tv_item_lock_routine);
            _RoutineTodoComplete = itemView.findViewById(R.id.iv_item_lock_routine);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        LockRoutineItem lockRoutineItem = lrItems.get(pos);
                        int id = lockRoutineItem.getrTodoId();
                        if (lockRoutineItem.getrTodoComplete() == 0) {
                            mDBHelper.updateRoutineTodoComplete(id, 1);
                            lockRoutineItem.setrTodoComplete(1);
                            _RoutineTodoComplete.setImageResource(R.drawable.ic_routine_item_check_on_28dp);
                            _RoutineTodoContents.setPaintFlags(_RoutineTodoContents.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            Toast.makeText(itemView.getContext(), "✔️", Toast.LENGTH_SHORT).show();
                        } else {
                            mDBHelper.updateRoutineTodoComplete(id, 0);
                            lockRoutineItem.setrTodoComplete(0);
                            _RoutineTodoComplete.setImageResource(R.drawable.ic_routine_item_check_off_28dp);
                            _RoutineTodoContents.setPaintFlags(0);
                        }
                        notifyItemChanged(pos);
                        if (mListener != null) {
                            mListener.onItemClick();
                        }
                    }
                }
            });

        }

        @Override
        void bindLockRoutine(LockRoutineItem lrItems) {
            _RoutineTodoContents.setText(lrItems.getrTodoContents());
            if (lrItems.getrTodoComplete() == 0) {
                _RoutineTodoComplete.setImageResource(R.drawable.ic_routine_item_check_off_28dp);
                _RoutineTodoContents.setPaintFlags(0);
            } else {
                _RoutineTodoComplete.setImageResource(R.drawable.ic_routine_item_check_on_28dp);
                _RoutineTodoContents.setPaintFlags(_RoutineTodoContents.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }


}
