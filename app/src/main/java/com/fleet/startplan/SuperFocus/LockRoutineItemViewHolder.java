package com.fleet.startplan.SuperFocus;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

abstract public class LockRoutineItemViewHolder extends RecyclerView.ViewHolder {
    public LockRoutineItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    abstract void bindLockRoutine(LockRoutineItem lrItems);
}
