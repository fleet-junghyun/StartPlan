package com.fleet.startplan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.fleet.startplan.R;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class SettingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ImageView mSettingBackBtn = findViewById(R.id.iv_setting_back_btn);
        View mUserRecommend = findViewById(R.id.v_recommend_space);
        View mUserUpdate = findViewById(R.id.v_setting_update_space);
        View mUserReview = findViewById(R.id.v_review_space);
        View mUserPremium = findViewById(R.id.v_setting_premium_space);


        mSettingBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mUserRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
                Sharing_intent.setType("text/plain");
                String Test_Message = "시작계획 다운로드\nhttps://bit.ly/34oDKHX";
                Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message);
                Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
                startActivity(Sharing);
            }
        });

        mUserUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UpdateActivity.class);
                startActivity(intent);
            }
        });

        mUserReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewManager manager = ReviewManagerFactory.create(SettingActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(SettingActivity.this, reviewInfo);
                        flow.addOnCompleteListener(taskdone -> {
                            // This is the next follow of your app
                        });
                    }
                });
            }
        });


        mUserPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PremiumActivity.class);
                startActivity(intent);
            }
        });
    }


}
