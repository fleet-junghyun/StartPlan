package com.fleet.startplan.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fleet.startplan.R;

public class OpenSourceActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source);
        ImageView mOpenScBackBtn = findViewById(R.id.iv_open_source_back_btn);
        mOpenScBackBtn.setOnClickListener(v-> {
                finish();
        });
    }
}
