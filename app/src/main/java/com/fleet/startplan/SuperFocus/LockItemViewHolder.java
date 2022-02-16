package com.fleet.startplan.SuperFocus;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

abstract public class LockItemViewHolder extends RecyclerView.ViewHolder {
    public LockItemViewHolder(View itemView){super(itemView);}
    abstract void onLockBind(LockItem _item);

}
