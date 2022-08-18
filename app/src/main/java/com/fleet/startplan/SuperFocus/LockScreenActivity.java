package com.fleet.startplan.SuperFocus;

import android.content.Intent;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Activity.IntroActivity;
import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Dialog.DialogAdd;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.Model.Products;
import com.fleet.startplan.R;
import com.fleet.startplan.SharedPreference.PreferenceManager;
import com.fleet.startplan.TouchHelperAdapter.OnSingleClickListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class LockScreenActivity extends AppCompatActivity implements LifecycleObserver {

    public static String LOCK_SCREEN_TITLE = "lockScreenTitle";
    public static String LOCK_ROUTINE_ITEM_ID = "lockRoutineItemId";

    private SPDBHelper mDBHelper;
    private ImageView mGoYesterday;
    private ImageView mGoTomorrow;
    private TextView mDateTitle;
    private AdView mAdmobView;
    private LockAdapter mLockAdapter;

    private String standardDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        mDBHelper = new SPDBHelper(getApplicationContext());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        RecyclerView mRecyclerview = findViewById(R.id.rv_lock_screen);
        mGoYesterday = findViewById(R.id.iv_lock_go_yesterday);
        mGoTomorrow = findViewById(R.id.iv_lock_go_tomorrow);
        mDateTitle = findViewById(R.id.tv_lock_date);
        ImageView mGoApp = findViewById(R.id.iv_lock_start_app);
        mAdmobView = findViewById(R.id.lockAdView);
        getLifecycle().addObserver(this);
        checkPremiumUser();

        mDateTitle.setText(getConvertToday());
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ArrayList<LockItem> _Items = mDBHelper.getLockData(getToday(), Information.CATEGORY_D_DAY, Information.CATEGORY_START_DAY);
        for (int i = 0; i < _Items.size(); i++) {
            LockItem item = _Items.get(i);
            item.setScStandardDate(getToday());
        }
        Collections.sort(_Items, LockItem.setLockItemList);
        mLockAdapter = new LockAdapter(_Items);
        mLockAdapter.setOnItemClickListener(new LockAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int itemId, String title) {
                DialogLockRoutine dialogLockRoutine = new DialogLockRoutine();
                dialogLockRoutine.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                dialogLockRoutine.show(getSupportFragmentManager(), "dialogLockRoutine");
                Bundle bundle = new Bundle();
                bundle.putString(LOCK_SCREEN_TITLE, title);
                bundle.putInt(LOCK_ROUTINE_ITEM_ID, itemId);
                dialogLockRoutine.setArguments(bundle);
                dialogLockRoutine.setOnCompleteListener(new DialogLockRoutine.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        mLockAdapter.notifyItemChanged(position);
                    }
                });
            }
        });


        mRecyclerview.setAdapter(mLockAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new LockItemTouchHelperCallback(mLockAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerview);

        standardDate = getToday();


        boolean overTheLockScreen = PreferenceManager.getBoolean(getApplicationContext(), PreferenceManager.OVER_THE_LOCK_SCREEN);
        if (overTheLockScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }

        mGoApp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intro = new Intent(LockScreenActivity.this, IntroActivity.class);
                startActivity(intro.setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });

        mGoYesterday.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (standardDate.equals(getToday())) {
                    //어제
                    mDateTitle.setText(getConvertYesterday());
                    mGoYesterday.setVisibility(View.INVISIBLE);
                    standardDate = getYesterday();
                    resetItems(getYesterday());
                } else if (standardDate.equals(getTomorrow())) {
                    //오늘
                    mDateTitle.setText(getConvertToday());
                    mGoTomorrow.setVisibility(View.VISIBLE);
                    standardDate = getToday();
                    resetItems(getToday());
                }
            }
        });

        mGoTomorrow.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (standardDate.equals(getToday())) {
                    //내일
                    mDateTitle.setText(getConvertTomorrow());
                    mGoTomorrow.setVisibility(View.INVISIBLE);
                    standardDate = getTomorrow();
                    resetItems(getTomorrow());
                } else if (standardDate.equals(getYesterday())) {
                    //오늘
                    mDateTitle.setText(getConvertToday());
                    mGoYesterday.setVisibility(View.VISIBLE);
                    standardDate = getToday();
                    resetItems(getToday());
                }
            }
        });
    }


    private void resetItems(String _date) {
        ArrayList<LockItem> _Items = mDBHelper.getLockData(_date, Information.CATEGORY_D_DAY, Information.CATEGORY_START_DAY);
        Collections.sort(_Items, LockItem.setLockItemList);
        mLockAdapter.setItems(_Items);
        mLockAdapter.notifyDataSetChanged();
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

    private String getConvertToday() {
        Date currentTime = Calendar.getInstance().getTime();
        return DateFormat.format("MMM" + " " + "dd" + "일" + " (오늘)", currentTime).toString();
    }

    private String getConvertYesterday() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return DateFormat.format("MMM" + " " + "dd" + "일", c).toString();
    }

    private String getConvertTomorrow() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +1);
        return DateFormat.format("MMM" + " " + "dd" + "일", c).toString();
    }


    private String getToday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        return format.format(c.getTime());
    }


    private String getYesterday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return format.format(c.getTime());
    }


    private String getTomorrow() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        return format.format(c.getTime());
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.DESTROYED)) {
            mAdmobView.destroy();
        }
    }

}
