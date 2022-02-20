package com.fleet.startplan.Menu;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.fleet.startplan.Activity.SettingActivity;
import com.fleet.startplan.Bring.DialogBring;
import com.fleet.startplan.Dialog.DialogAdd;
import com.fleet.startplan.Dialog.DialogStorage;
import com.fleet.startplan.DividingLine.DialogDividingLine;
import com.fleet.startplan.R;
import com.fleet.startplan.Schedule.Schedule;
import com.fleet.startplan.Send.DialogSend;
import com.fleet.startplan.SuperFocus.SuperFocusActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DialogMenu extends BottomSheetDialogFragment {

    private String mSelectedDate;

    private SetListRefreshListener mListener = null;

    public void setOnListRefreshListener(SetListRefreshListener mListener) {
        this.mListener = mListener;
    }

    public interface SetListRefreshListener {
        void refresh();
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_menu, container, false);
        ConstraintLayout mGoBring = v.findViewById(R.id.layout_menu_go_bring);
        ConstraintLayout mGoSend = v.findViewById(R.id.layout_menu_go_send);
        ConstraintLayout mGoStorage = v.findViewById(R.id.layout_menu_go_storage);
        ConstraintLayout mGoDividingLine = v.findViewById(R.id.layout_menu_go_dividing_line);
        ConstraintLayout mGoSetting = v.findViewById(R.id.layout_menu_go_setting);
        ConstraintLayout mGoSuperFocus = v.findViewById(R.id.layout_menu_go_super_focus);
        TextView mSuperFocusText = v.findViewById(R.id.tv_menu_super_focus);

        mSuperFocusText.setPaintFlags(mSuperFocusText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Bundle extra = this.getArguments();
        if (extra != null) {
            mSelectedDate = extra.getString(Schedule.SELECTED_DATE);
        }

        mGoBring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBring dialogBring = new DialogBring();
                dialogBring.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                dialogBring.show(getActivity().getSupportFragmentManager(), "DialogBring");
                Bundle bundle = new Bundle();
                bundle.putString(Schedule.SELECTED_DATE, mSelectedDate);
                dialogBring.setArguments(bundle);
                dialogBring.setOnBringCompleteListener(new DialogBring.SetBringCompleteListener() {
                    @Override
                    public void complete() {
                        if (mListener != null) {
                            mListener.refresh();
                        }
                    }
                });
                dismiss();
            }
        });

        mGoSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSend dialogSend = new DialogSend();
                dialogSend.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                dialogSend.show(getActivity().getSupportFragmentManager(), "DialogSend");
                Bundle bundle = new Bundle();
                bundle.putString(Schedule.SELECTED_DATE, mSelectedDate);
                dialogSend.setArguments(bundle);
                dialogSend.setOnSendCompleteListener(new DialogSend.SetSendCompleteListener() {
                    @Override
                    public void complete() {
                        if (mListener != null) {
                            mListener.refresh();
                        }
                    }
                });
                dismiss();
            }
        });

        mGoStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStorage(mSelectedDate);
            }
        });

        mGoDividingLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDividingLine dialogDividingLine = new DialogDividingLine();
                dialogDividingLine.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                dialogDividingLine.show(getActivity().getSupportFragmentManager(), "DialogDividingLine");
                Bundle bundle = new Bundle();
                bundle.putString(Schedule.SELECTED_DATE, mSelectedDate);
                dialogDividingLine.setArguments(bundle);
                dialogDividingLine.setAddCompleteListener(new DialogDividingLine.SetAddCompleteListener() {
                    @Override
                    public void add() {
                        if (mListener != null) {
                            mListener.refresh();
                        }
                    }
                });
                dismiss();
            }
        });

        mGoSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                dismiss();
            }
        });

        mGoSuperFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SuperFocusActivity.class);
                startActivity(intent);

            }
        });


        return v;

    }


    private void openStorage(String date) {
        DialogStorage dialogStorage = new DialogStorage();
        dialogStorage.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        dialogStorage.show(getChildFragmentManager(), "DialogStorage");
        dialogStorage.setOnStorageAddListener(new DialogStorage.OnStorageAddListener() {
            @Override
            public void clickAddButton() {
                if (mListener != null) {
                    mListener.refresh();
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(Schedule.SELECTED_DATE, date);
        dialogStorage.setArguments(bundle);
    }
}
