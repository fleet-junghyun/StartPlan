package com.fleet.startplan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fleet.startplan.Analytics.Analytics;
import com.fleet.startplan.R;
import com.fleet.startplan.Schedule.Schedule;
import com.fleet.startplan.SharedPreference.PreferenceManager;
import com.fleet.startplan.Model.Products;
import com.fleet.startplan.SuperFocus.ScreenService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {

    private long mBackBtnTime = 0;
    private AdView mAdmobView;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFCM();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdmobView = findViewById(R.id.adView);
        checkPremiumUser();
        viewPager = findViewById(R.id.sc_viewpager);
        pagerAdapter = new ScreenSlidePagerAdapter(MainActivity.this);
        viewPager.setAdapter(pagerAdapter);
        //viewpager 스와이프 비활성화
        viewPager.setUserInputEnabled(false);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    pagerAdapter.notifyItemChanged(position);
                }
            }
        });
        boolean superFocus = PreferenceManager.getBoolean(getApplicationContext(), PreferenceManager.SUPER_FOCUS);
        if(superFocus){
            Intent service = new Intent(getApplicationContext(), ScreenService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getApplicationContext().startForegroundService(service);
            } else {
                getApplicationContext().startService(service);
            }
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                Schedule schedule = new Schedule();
                schedule.setOnAnalyticsListener(new Schedule.SetAnalyticsListener() {
                    @Override
                    public void click() {
                        viewPager.setCurrentItem(1, true);
                    }
                });
                return schedule;
            } else {
                Analytics analytics = new Analytics();
                analytics.setOnScheduleListener(new Analytics.SetScheduleListener() {
                    @Override
                    public void click() {
                        viewPager.setCurrentItem(0, true);
                    }
                });
                return analytics;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public boolean containsItem(long itemId) {
            return super.containsItem(itemId);
        }
    }


    private void checkPremiumUser() {
        //유료결제 유저 구분
        boolean checkPremiumUser = PreferenceManager.getBoolean(getApplicationContext(), Products.PREMIUM_USER);
        if (!checkPremiumUser) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdmobView.loadAd(adRequest);
        } else {
            mAdmobView.destroy();
            mAdmobView.setVisibility(View.GONE);
        }

    }

    public void initFCM() {
        //firebase fcm
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Log", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.d("FCM Log", "FCM 토큰:" + token);
                    }
                });
    }


    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            if (System.currentTimeMillis() - mBackBtnTime >= 2000) {
                mBackBtnTime = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), "두번 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            } else if (System.currentTimeMillis() - mBackBtnTime < 2000) {
                finish();
            }
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        }
    }

    @Override
    protected void onResume() {
        checkPremiumUser();
        super.onResume();
    }

}