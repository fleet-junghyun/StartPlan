package com.fleet.startplan.SuperFocus;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.fleet.startplan.Activity.MainActivity;
import com.fleet.startplan.R;
import com.fleet.startplan.SharedPreference.PreferenceManager;
import com.fleet.startplan.TouchHelperAdapter.OnSingleClickListener;
import com.kyleduo.switchbutton.SwitchButton;

public class SuperFocusActivity extends AppCompatActivity {

    private View _start;
    private TextView _superFocusText;
    private SwitchButton switchButton;
    private TextView overTheText;

    private boolean changeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_focus);

        _start = findViewById(R.id.v_start_super_focus);
        _superFocusText = findViewById(R.id.tv_start_super_focus);
        switchButton = findViewById(R.id.sw_over_the_lock_screen);
        overTheText = findViewById(R.id.tv_over_the_lockscreen);
        ImageView _superFocusClose = findViewById(R.id.iv_super_focus_close);
        TextView _introduceSuperFocus = findViewById(R.id.tv_super_focus_text);

        _introduceSuperFocus.setText(Html.fromHtml("<b>초집중 모드</b>" + "의 원활한 이용을 위해<br>" + "<b>다른 앱 위에 그리기 권한 허용</b>" + "이 필요합니다."));

        boolean overLayPermission = PreferenceManager.getBoolean(getApplicationContext(), PreferenceManager.OVERLAY_PERMISSION);
        if (overLayPermission) {
            _introduceSuperFocus.setVisibility(View.GONE);
        } else {
            _introduceSuperFocus.setVisibility(View.VISIBLE);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            overTheText.setText(null);
            switchButton.setThumbSize(-1,-1);
            PreferenceManager.setBoolean(getApplicationContext(), PreferenceManager.OVER_THE_LOCK_SCREEN, false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean overTheLockScreen = PreferenceManager.getBoolean(getApplicationContext(), PreferenceManager.OVER_THE_LOCK_SCREEN);
            switchButton.setChecked(overTheLockScreen);
            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(getApplicationContext(), "잠금 화면 보다 위에", Toast.LENGTH_SHORT).show();
                        PreferenceManager.setBoolean(getApplicationContext(), PreferenceManager.OVER_THE_LOCK_SCREEN, true);
                    } else {
                        Toast.makeText(getApplicationContext(), "잠금 화면 보다 아래에", Toast.LENGTH_SHORT).show();
                        PreferenceManager.setBoolean(getApplicationContext(), PreferenceManager.OVER_THE_LOCK_SCREEN, false);
                    }
                }
            });
        }

        boolean superFocus = PreferenceManager.getBoolean(getApplicationContext(), PreferenceManager.SUPER_FOCUS);

        if (!superFocus) {
            turnOnBtn();
        } else {
            turnOffBtn();
        }
        superFocus = changeBtn;

        _start.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!changeBtn) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(getApplicationContext())) {
                            Uri uri = Uri.parse("package:" + getPackageName());
                            Intent overLay = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);
                            mConfirmPermission.launch(overLay);
                        } else {
                            startLockService();
                            Toast.makeText(getApplicationContext(), "초집중 모드가 제공됩니다.", Toast.LENGTH_SHORT).show();
                            turnOffBtn();
                            PreferenceManager.setBoolean(getApplicationContext(), PreferenceManager.SUPER_FOCUS, true);
                            changeBtn = true;
                        }
                    } else {
                        startLockService();
                        Toast.makeText(getApplicationContext(), "초집중 모드가 제공됩니다.", Toast.LENGTH_SHORT).show();
                        turnOffBtn();
                        PreferenceManager.setBoolean(getApplicationContext(), PreferenceManager.SUPER_FOCUS, true);
                        changeBtn = true;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "초집중 모드가 꺼졌습니다.", Toast.LENGTH_SHORT).show();
                    stopLockService();
                    turnOnBtn();
                    PreferenceManager.setBoolean(getApplicationContext(), PreferenceManager.SUPER_FOCUS, false);
                    PreferenceManager.setBoolean(getApplicationContext(), PreferenceManager.OVER_THE_LOCK_SCREEN, false);
                    changeBtn = false;
                }
            }
        });

        _superFocusClose.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });
    }

    private ActivityResultLauncher<Intent> mConfirmPermission = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), "권한이 없습니다. 다시 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                            PreferenceManager.setBoolean(getApplicationContext(), PreferenceManager.OVERLAY_PERMISSION, false);
                            changeBtn = false;
                        } else {
                            startLockService();
                            Toast.makeText(getApplicationContext(), "초집중 모드가 제공됩니다.", Toast.LENGTH_SHORT).show();
                            turnOffBtn();
                            PreferenceManager.setBoolean(getApplicationContext(), PreferenceManager.OVERLAY_PERMISSION, true);
                            PreferenceManager.setBoolean(getApplicationContext(), PreferenceManager.SUPER_FOCUS, true);
                            changeBtn = true;
                        }
                    }
                }
            });

    private void startLockService() {
        Intent service = new Intent(SuperFocusActivity.this, ScreenService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(service);
        } else {
            startService(service);
        }
        Intent lock = new Intent(SuperFocusActivity.this, LockScreenActivity.class);
        startActivity(lock);
    }

    private void stopLockService() {
        Intent intent = new Intent(SuperFocusActivity.this, ScreenService.class);
        stopService(intent);
    }

    private void turnOnBtn() {
        _start.setBackgroundResource(R.drawable.shape_start_super_focus_btn);
        _superFocusText.setText("초집중 시작!");
        switchButton.setVisibility(View.GONE);
        overTheText.setVisibility(View.GONE);
    }

    private void turnOffBtn() {
        _start.setBackgroundResource(R.drawable.shape_stop_super_focus_btn);
        _superFocusText.setText("초집중 모드 제공 중");
        switchButton.setVisibility(View.VISIBLE);
        overTheText.setVisibility(View.VISIBLE);
    }


}
