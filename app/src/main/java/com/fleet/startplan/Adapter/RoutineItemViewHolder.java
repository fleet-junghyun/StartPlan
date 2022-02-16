package com.fleet.startplan.Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Routine.StorageRoutineTodoItem;
import com.fleet.startplan.Routine.RoutineItem;

abstract public class RoutineItemViewHolder extends RecyclerView.ViewHolder{
    public RoutineItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    abstract void onRoutineBind(RoutineItem rtItems);
    abstract void onStorageRoutineTodoBind(StorageRoutineTodoItem sRtItems);
}
