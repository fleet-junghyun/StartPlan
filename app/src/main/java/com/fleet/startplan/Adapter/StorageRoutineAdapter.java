package com.fleet.startplan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Routine.StorageRoutineTodoItem;
import com.fleet.startplan.R;
import com.fleet.startplan.Routine.RoutineItem;
import com.fleet.startplan.TouchHelperAdapter.StorageRoutineItemTouchHelperListener;

import java.util.ArrayList;
import java.util.Collections;

public class StorageRoutineAdapter extends RecyclerView.Adapter<RoutineItemViewHolder> implements StorageRoutineItemTouchHelperListener {

    private ArrayList<StorageRoutineTodoItem> stRtItems;

    private SPDBHelper mDBHelper;

    public interface OnStorageRoutineTodoItemClickListener{
        void onToss(int id);
    }

    private OnStorageRoutineTodoItemClickListener mListener = null;

    public void setOnStorageRoutineTodoItemClickListener(OnStorageRoutineTodoItemClickListener listener){
        this.mListener = listener;
    }


    public StorageRoutineAdapter(ArrayList<StorageRoutineTodoItem> sRtItems) {
        this.stRtItems = sRtItems;
    }


    @Override
    public boolean onItemMove(int from_position, int to_position) {
        StorageRoutineTodoItem from = stRtItems.get(from_position);
        StorageRoutineTodoItem to = stRtItems.get(to_position);

        mDBHelper.swapStorageRoutineTodoItem(from,to);

        int fromPos = from.getStRtListPos();
        int toPos = to.getStRtListPos();

        from.setStRtListPos(toPos);
        to.setStRtListPos(fromPos);
        Collections.swap(stRtItems,to_position,from_position);
        notifyItemMoved(from_position,to_position);

        return true;
    }

    @Override
    public void onItemSwipe(int position) {

    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        StorageRoutineTodoItem item = stRtItems.get(position);
        stRtItems.remove(position);
        notifyItemRemoved(position);
        int stRtID = item.getStRtId();
        mDBHelper.deleteStorageRoutineTodo(stRtID);
    }

    @NonNull
    @Override
    public RoutineItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mDBHelper = new SPDBHelper(parent.getContext());

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storage_routine_todo, parent, false);
        return new StorageRoutineTodoItemViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RoutineItemViewHolder holder, int position) {
        holder.onStorageRoutineTodoBind(stRtItems.get(position));
    }

    @Override
    public int getItemCount() {
        return stRtItems.size();
    }


    class StorageRoutineTodoItemViewHolder extends RoutineItemViewHolder {

        TextView mStorageRoutineTodoContents;
        ImageView mStorageRoutineTodoAdd;


        public StorageRoutineTodoItemViewHolder(View itemView) {
            super(itemView);

            mStorageRoutineTodoContents = itemView.findViewById(R.id.tv_routine_todo_contents);
            mStorageRoutineTodoAdd = itemView.findViewById(R.id.iv_routine_todo_add);

            mStorageRoutineTodoAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        StorageRoutineTodoItem item = stRtItems.get(pos);
                        int itemID = item.getStRtId();
                        if(mListener !=null){
                            mListener.onToss(itemID);
                        }
                    }
                }
            });
        }

        @Override
        void onRoutineBind(RoutineItem rtItems) {

        }

        @Override
        void onStorageRoutineTodoBind(StorageRoutineTodoItem sRtItems) {
            mStorageRoutineTodoContents.setText(sRtItems.getStRtContents());
        }
    }

}
