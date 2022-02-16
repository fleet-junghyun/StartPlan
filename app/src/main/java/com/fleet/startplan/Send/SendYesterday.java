package com.fleet.startplan.Send;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class SendYesterday extends AppCompatActivity {

    private String mYesterdayDate, mSelectedDate;

    private SPDBHelper mDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        mDBHelper = new SPDBHelper(getApplicationContext());
        RecyclerView mRecyclerview = findViewById(R.id.rv_send);
        ImageView mSendExit = findViewById(R.id.iv_send_exit);
        View mSendComplete = findViewById(R.id.btn_send_complete);
        TextView mCompleteBtnText = findViewById(R.id.tv_send_complete);
        TextView mSendHelpNull = findViewById(R.id.tv_send_help_null);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Intent intent = getIntent();
        if (intent != null) {
            mSelectedDate = intent.getStringExtra(DialogSend.SEND_SELECTED_DATE);
            mYesterdayDate = intent.getStringExtra(DialogSend.SEND_YESTERDAY_DATE);
        }

        if (mYesterdayDate.equals(getToday())) {
            mCompleteBtnText.setText("오늘로 보내기");
        } else {
            mCompleteBtnText.setText("<" + mYesterdayDate + ">" + "로 보내기");
        }

        ArrayList<CopyItem> copyItems = mDBHelper.getBringData(mSelectedDate);
        Collections.sort(copyItems, CopyItem.setCopyItemList);

        if (copyItems.size() == 0) {
            mSendHelpNull.setText("아무것도 추가할 수 없어요.\uD83D\uDE13");
            mSendComplete.setBackgroundResource(R.color.gray_400);
            mCompleteBtnText.setTextColor(Color.WHITE);
        }

        CopyAdapter mCopyAdapter = new CopyAdapter(copyItems);
        mRecyclerview.setAdapter(mCopyAdapter);

        mCopyAdapter.setOnRemoveListener(new CopyAdapter.OnRemoveListener() {
            @Override
            public void onRemove(int size) {
                if (size == 0) {
                    mSendHelpNull.setText("아무것도 추가할 수 없어요.\uD83D\uDE13");
                    mSendComplete.setBackgroundResource(R.color.gray_400);
                }
            }
        });

        mSendComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (copyItems.size() != 0) {
                    mDBHelper.insertCopySchedule(copyItems, mYesterdayDate);
                    Toast.makeText(getApplicationContext(), "보내기 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "보낼 수 있는 계획이 없어요", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mSendExit.setOnClickListener(new View.OnClickListener() {
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
