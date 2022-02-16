package com.fleet.startplan.Bring;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Copy.CopyAdapter;
import com.fleet.startplan.Copy.CopyItem;
import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class BringPickDate extends AppCompatActivity {

    private String mPickDate, mSelectedPickDate;

    private SPDBHelper mDBHelper;
    private View mBringComplete;
    private TextView mBringHelpNull;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bring);

        mDBHelper = new SPDBHelper(getApplicationContext());
        RecyclerView mRecyclerview = findViewById(R.id.rv_bring);
        ImageView mBringExit = findViewById(R.id.iv_bring_exit);
        mBringComplete = findViewById(R.id.btn_bring_complete);
        TextView mCompleteBtnText = findViewById(R.id.tv_bring_complete);
        mBringHelpNull = findViewById(R.id.tv_bring_help_null);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Intent intent = getIntent();
        if (intent != null) {
            mPickDate = intent.getStringExtra(BringPickDateList.PICK_DATE);
            mSelectedPickDate = intent.getStringExtra(BringPickDateList.SELECTED_PICK_DATE);
        }

        if (mSelectedPickDate.equals(getToday())) {
            mCompleteBtnText.setText("오늘로 가져오기");
        } else {
            mCompleteBtnText.setText("<" + mSelectedPickDate + ">" + "로 가져오기");
        }

        ArrayList<CopyItem> copyItems = mDBHelper.getBringData(mPickDate);
        Collections.sort(copyItems, CopyItem.setCopyItemList);
        if (copyItems.size() == 0) {
            mBringHelpNull.setText("아무것도 추가할 수 없어요.\uD83D\uDE13");
            mBringComplete.setBackgroundResource(R.color.gray_400);
            mCompleteBtnText.setTextColor(Color.WHITE);
        }
        CopyAdapter mCopyAdapter = new CopyAdapter(copyItems);

        mCopyAdapter.setOnRemoveListener(new CopyAdapter.OnRemoveListener() {
            @Override
            public void onRemove(int size) {
                if (size == 0) {
                    mBringHelpNull.setText("아무것도 추가할 수 없어요.\uD83D\uDE13");
                    mBringComplete.setBackgroundResource(R.color.gray_400);
                    mCompleteBtnText.setTextColor(Color.WHITE);
                }
            }
        });

        mRecyclerview.setAdapter(mCopyAdapter);

        mBringComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (copyItems.size() != 0) {
                    mDBHelper.insertCopySchedule(copyItems, mSelectedPickDate);
                    Toast.makeText(getApplicationContext(), "불러오기 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "불러올 수 있는 계획이 없어요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBringExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private String getToday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        return format.format(c.getTime());
    }
}
