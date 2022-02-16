package com.fleet.startplan.SuperFocus;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.TouchHelperAdapter.RoutineItemTouchHelperListener;


public class LockRoutineItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private LockRoutineItemTouchHelperListener listener;

    public LockRoutineItemTouchHelperCallback(LockRoutineItemTouchHelperListener listener) {
        this.listener = listener;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int drag_flags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipe_flags = 0;
        return makeMovementFlags(drag_flags, swipe_flags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    }
}
