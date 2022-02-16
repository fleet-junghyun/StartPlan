package com.fleet.startplan.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.fleet.startplan.R;
import com.fleet.startplan.SharedPreference.PreferenceManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DialogReview extends BottomSheetDialogFragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_review, container, false);

        View mAnswerNo = v.findViewById(R.id.v_dialog_review_answer_no);
        View mAnswerOk = v.findViewById(R.id.v_dialog_review_answer_ok);

        mAnswerNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mAnswerOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setBoolean(getContext(), PreferenceManager.COMPLETED_REVIEW, true);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/34oDKHX"));
                startActivity(intent);
                dismiss();
            }
        });
        return v;
    }

    //dialog 밖 부분에서 안꺼지게 하는 부분
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog d = super.onCreateDialog(savedInstanceState);
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                d.getWindow().findViewById(R.id.touch_outside).setOnClickListener(null);
                View content = d.getWindow().findViewById(R.id.design_bottom_sheet);
                ((CoordinatorLayout.LayoutParams) content.getLayoutParams()).setBehavior(null);
            }
        });
        return d;

    }
}
