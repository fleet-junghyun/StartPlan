package com.fleet.startplan.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fleet.startplan.R;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        ImageView mUpdateExit = findViewById(R.id.iv_update_exit_btn);
        View mInstagram = findViewById(R.id.v_update_instagram);
        View mUserReview = findViewById(R.id.v_update_review);
        TextView mTextAlarm = findViewById(R.id.tv_update_contents_4);
        TextView mOpenSource = findViewById(R.id.tv_open_source);
        TextView mEdit = findViewById(R.id.tv_update_contents_6);
        TextView mEditEmoji = findViewById(R.id.tv_update_contents_2);
        TextView mPremium = findViewById(R.id.tv_update_contents_5);
        TextView mRoutine = findViewById(R.id.tv_update_contents_10);
        TextView mAnalyticsText = findViewById(R.id.tv_update_contents_8);
        TextView mCalendarCopy = findViewById(R.id.tv_update_contents_3);

        setPaintFlags(mTextAlarm);
        setPaintFlags(mEdit);
        setPaintFlags(mEditEmoji);
        setPaintFlags(mPremium);
        setPaintFlags(mRoutine);
        setPaintFlags(mAnalyticsText);
        setPaintFlags(mCalendarCopy);

        mOpenSource.setPaintFlags(mOpenSource.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mOpenSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OpenSourceActivity.class);
                startActivity(intent);
            }
        });


        mUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/appboy__"));
                startActivity(intent);
            }
        });


        mUserReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/34oDKHX"));
                startActivity(intent);
            }
        });
    }

    private void setPaintFlags(TextView text) {
        text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

}
