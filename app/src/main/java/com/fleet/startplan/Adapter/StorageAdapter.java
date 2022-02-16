package com.fleet.startplan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.R;
import com.fleet.startplan.Model.ScheduleItem;
import com.fleet.startplan.Model.StorageItem;
import com.fleet.startplan.TouchHelperAdapter.StorageItemTouchHelperListener;
import java.util.ArrayList;
import java.util.Collections;


public class StorageAdapter extends RecyclerView.Adapter<ItemViewHolder> implements StorageItemTouchHelperListener {

    private ArrayList<StorageItem> stItems;

    private SPDBHelper mDBHelper;

    public interface OnStorageItemClickListener {
        void onStorageItemClick(String emoji, String contents, String category, int ListPos);
    }

    private OnStorageItemClickListener mListener = null;

    public void setOnStorageItemClickListener(OnStorageItemClickListener listener) {
        this.mListener = listener;
    }


    public StorageAdapter(ArrayList<StorageItem> stItems) {
        this.stItems = stItems;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mDBHelper = new SPDBHelper(parent.getContext());
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storage, parent, false);
        return new StorageItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onStorageBind(stItems.get(position));
    }

    @Override
    public int getItemCount() {
        return stItems.size();
    }


    class StorageItemViewHolder extends ItemViewHolder {

        TextView mItemStorageEmoji, mItemStorageContents;
        ImageView mItemStorageAdd;

        public StorageItemViewHolder(View itemView) {
            super(itemView);

            mItemStorageEmoji = itemView.findViewById(R.id.tv_item_storage_emoji);
            mItemStorageContents = itemView.findViewById(R.id.tv_item_storage_contents);
            mItemStorageAdd = itemView.findViewById(R.id.iv_item_storage_add);

            mItemStorageAdd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        StorageItem item = stItems.get(pos);
                        String emoji = item.getStEmoji();
                        String contents = item.getStContents();
                        String category = Information.CATEGORY_TODO;

                        int lastID = 0;
                        ArrayList<ScheduleItem> scItems = mDBHelper.getScheduleItems();
                        if (scItems.isEmpty()) {
                            lastID = 1;
                        } else {
                            ScheduleItem it = scItems.get(mDBHelper.getScheduleItems().size() - 1);
                            lastID = it.getScId() + 1;
                        }
                        if (mListener != null) {
                            mListener.onStorageItemClick(emoji, contents, category, lastID);
                        }
                    }

                }
            });


        }

        @Override
        void onScheduleBind(ScheduleItem scItems) {

        }


        @Override
        void onStorageBind(StorageItem stItems) {
            mItemStorageEmoji.setText(stItems.getStEmoji());
            mItemStorageContents.setText(stItems.getStContents());
            mItemStorageAdd.setImageResource(R.drawable.ic_item_storage_add_20dp);
        }
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        StorageItem from = stItems.get(from_position);
        StorageItem to = stItems.get(to_position);

        mDBHelper.swapItem(from, to);

        int fromPos = from.getStListPosition();
        int toPos = to.getStListPosition();

        from.setStListPosition(toPos);
        to.setStListPosition(fromPos);
        Collections.swap(stItems, to_position, from_position);
        notifyItemMoved(from_position, to_position);

        return true;

    }


    @Override
    public void onItemSwipe(int position) {

    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        //왼쪽 버튼 누르면 삭제
        StorageItem item = stItems.get(position);
        stItems.remove(position);
        notifyItemRemoved(position);
        int stId = item.getStId();
        mDBHelper.deleteListStorage(stId);

    }


}
