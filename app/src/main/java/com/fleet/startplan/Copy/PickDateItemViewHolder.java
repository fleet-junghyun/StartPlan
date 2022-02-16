package com.fleet.startplan.Copy;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

abstract public class PickDateItemViewHolder extends RecyclerView.ViewHolder {
    public PickDateItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    abstract void onPickDateListBind(PickDateItem pdItems);
}
