package com.bangke.lib.common.ad;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangke.lib.R;
import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 广告页面
 * Created by xlm on 03/10/2018.
 */

public class AdActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llAdTimer;
    private ImageView imgAd;
    private TextView tvAdNum;
    private int time = 3;
    private String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1538978632160&di=283cd10f6d517c4906260d0306c64589&imgtype=0&src=http%3A%2F%2Fpic33.nipic.com%2F20131008%2F161930_141926337000_2.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        llAdTimer = findViewById(R.id.ll_timer);
        imgAd = findViewById(R.id.img_ad);
        tvAdNum = findViewById(R.id.tv_ad_num);
    }

    private void initEvent() {
        imgAd.setOnClickListener(this);
        llAdTimer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_timer) {
            jumpAd();

        } else if (i == R.id.img_ad) {
            joinAd();
        }
    }

    private void initData() {
        //加载广告图片
        Glide.with(this)
                .load(url)
                .crossFade()
                .into(imgAd);

        //广告倒计时
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (time > 0) {
                    tvAdNum.post(new Runnable() {
                        @Override
                        public void run() {
                            time--;
                            tvAdNum.setText("" + time);
                        }
                    });
                } else {
                    jumpAd();
                    cancel();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    private void jumpAd() {
        joinMainActivity();
    }

    private void joinMainActivity() {
        Intent intent = getIntent();
        String pkg = intent.getStringExtra("pkg");
        String cls = intent.getStringExtra("cls");
        Intent mainIntent = new Intent();
        mainIntent.setComponent(new ComponentName(pkg, cls));
        startActivity(mainIntent);
        finish();
    }

    private void joinAd() {
        Toast.makeText(this, "进入广告页", Toast.LENGTH_SHORT).show();
    }

}
