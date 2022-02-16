package com.fleet.startplan.Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Model.ScheduleItem;
import com.fleet.startplan.Model.StorageItem;

abstract public class ItemViewHolder extends RecyclerView.ViewHolder {
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    abstract void onScheduleBind(ScheduleItem scItems);
    abstract void onStorageBind(StorageItem stItems);
}

