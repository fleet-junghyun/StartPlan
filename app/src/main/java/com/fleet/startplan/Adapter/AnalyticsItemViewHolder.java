package com.fleet.startplan.Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Model.AnalyticsListItem;

abstract public class AnalyticsItemViewHolder extends RecyclerView.ViewHolder {

    public AnalyticsItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    abstract void onAnalyticsListBind(AnalyticsListItem atItems);
}
