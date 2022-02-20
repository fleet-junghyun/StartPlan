package com.fleet.startplan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fleet.startplan.R;

import java.util.Random;

public class IntroActivity extends AppCompatActivity {

    String[] str = {"오늘도 파이팅! \uD83D\uDC4A", "파이팅!\uD83E\uDD19", "계획이란 미래에 관한 현재의 결정이다.\n\n- 피터드러커",
            "우리는 할 수 있다! ✌️", "시간을 지배할 줄 아는 사람은\n인생을 지배할 줄 아는 사람이다. \n\n- 에센바흐", "오늘 가장 좋게 웃는 자는 \n최후에도 웃을 것이다.\n\n- 니체",
            "오늘이라는 날은 \n두 번 다시 오지 않는다는 것을 잊지 말라.\n\n- 단테", "오늘 할 수 있는 일에만 전력을 쏟으라 \n\n- 뉴튼",
            "나는 장래의 일을 절대로 생각하지 않는다. \n그것은 틀림없이 곧 오게 될 테니까. \n\n- 아인슈타인",
            "계획 없는 목표는 한낱 꿈에 불과하다. \n\n- 생텍쥐 페리", "운은 계획에서 비롯된다 \n\n- 브랜치 리키",
            "나는 전투를 준비하면서 계획하는 것은 \n꼭 필요함을 항상 발견해왔다. \n\n- 아이젠 하워", "미래는 현재 우리가\n 무엇을 하는거에 달려 있다. \n\n- 간디",
            "순간들을 소중히 여기다 보면,\n긴 세월은 저절로 흘러간다.\n\n- 마리아 에지워스", "승자는 시간을 관리하며 살고,\n패자는 시간에 끌려 산다.\n\n- j.하비스",
            "지금 적극적으로 실행되는 괜찮은 계획이\n다음 주의 완벽한 계획보다 낫다.\n\n - 조지 S. 패튼", "오늘도 달려 봅시다!\uD83C\uDFC3",
            "기운과 끈기는 모든 것을 이겨낸다.\n\n- 벤자민 프랭클린", "승리는 가장 끈기있게 노력하는 사람에게 간다.\n\n-나폴레옹"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        TextView mIntroComments = findViewById(R.id.tv_intro_comments);

        Random ram = new Random();
        int num = ram.nextInt(str.length);
        mIntroComments.setText(str[num]);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class)
                        .setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, 1500);
        //1초 후 인트로 실행
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
