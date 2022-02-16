package com.fleet.startplan.Copy;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

abstract class BringItemViewHolder extends RecyclerView.ViewHolder {
    public BringItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    abstract void onBringBind(CopyItem brItems);

}
