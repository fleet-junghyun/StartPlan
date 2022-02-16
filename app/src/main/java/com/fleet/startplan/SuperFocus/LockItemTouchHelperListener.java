package com.fleet.startplan.SuperFocus;

import androidx.recyclerview.widget.RecyclerView;

public interface LockItemTouchHelperListener {
    boolean onItemMove(int from_position, int to_position);
    void onItemSwipe(int position);
    void onRightClick(int position, RecyclerView.ViewHolder viewHolder);
}
