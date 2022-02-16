package com.fleet.startplan.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fleet.startplan.Activity.PremiumActivity;
import com.fleet.startplan.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DialogPremium extends BottomSheetDialogFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_premium_popup, container, false);
        View mGoPremium = v.findViewById(R.id.v_popup_premium_pay);

        mGoPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PremiumActivity.class);
                startActivity(intent);
                dismiss();
            }
        });
        return v;
    }
}
