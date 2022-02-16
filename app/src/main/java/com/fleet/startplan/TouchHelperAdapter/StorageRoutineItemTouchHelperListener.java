package com.fleet.startplan.TouchHelperAdapter;

import androidx.recyclerview.widget.RecyclerView;

public interface StorageRoutineItemTouchHelperListener {

    boolean onItemMove(int from_position, int to_position);
    void onItemSwipe(int position);
    void onLeftClick(int position, RecyclerView.ViewHolder viewHolder);
}
