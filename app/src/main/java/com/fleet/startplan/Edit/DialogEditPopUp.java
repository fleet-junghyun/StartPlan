package com.fleet.startplan.Edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.fleet.startplan.Model.Information;
import com.fleet.startplan.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DialogEditPopUp extends BottomSheetDialogFragment {

    private SetEditListener mSetEditListener;

    public void setOnEditItemListener(SetEditListener mSetEditListener) {
        this.mSetEditListener = mSetEditListener;
    }

    public interface SetEditListener {
        void deleteItem();
        void editItem();
        void sendTomorrow();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_edit, container, false);
        View mDeleteItem = v.findViewById(R.id.v_item_delete);
        View mEditItem = v.findViewById(R.id.v_item_edit);
        View mSendTomorrow = v.findViewById(R.id.v_item_send_tomorrow);
        ConstraintLayout mLayoutSendTomorrow = v.findViewById(R.id.layout_send_tomorrow);
        ConstraintLayout mLayoutEdit = v.findViewById(R.id.layout_edit_edit);

        //투두,투두타임 인지 아닌지 여부로 Gone 을 해주거나 visibility 해준다.
        Bundle extra = this.getArguments();
        if (extra != null) {
            String mCheckCategory = extra.getString(Information.CHECK_CATEGORY);
            if (mCheckCategory.equals(Information.CATEGORY_TODO) || mCheckCategory.equals(Information.CATEGORY_ROUTINE)) {
                mLayoutSendTomorrow.setVisibility(View.VISIBLE);
            } else if (mCheckCategory.equals(Information.CATEGORY_DIVIDING_LINE_AM_PM) || mCheckCategory.equals(Information.CATEGORY_DIVIDING_LINE_WHEN)) {
                mLayoutSendTomorrow.setVisibility(View.GONE);
                mLayoutEdit.setVisibility(View.GONE);
            } else {
                mLayoutSendTomorrow.setVisibility(View.GONE);
            }
        }

        mDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSetEditListener != null) {
                    mSetEditListener.deleteItem();
                }
                dismiss();
            }
        });

        mEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSetEditListener != null) {
                    mSetEditListener.editItem();
                }
                dismiss();
            }
        });

        mSendTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSetEditListener != null) {
                    mSetEditListener.sendTomorrow();
                }
                Toast.makeText(getContext(), "내일로 보냈어요.", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return v;
    }
}
