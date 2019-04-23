package common.xm.com.xmcommon;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import common.xm.com.xmcommon.media2.base.XmVideoView;
import common.xm.com.xmcommon.media2.log.BKLog;
import common.xm.com.xmcommon.media2.service.XmMediaPlayerService;
import common.xm.com.xmcommon.media2.view.XmPopWindow;

public class MainActivity extends AppCompatActivity {
    private @SuppressLint("InflateParams")
    ViewGroup view;
    XmVideoView xmVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置窗体为没有标题的模式
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_main, null, false);
        setContentView(view);

//        startActivity(new Intent(this, ActMedia.class));
//
//        findViewById(R.id.btn_media).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, ActMedia.class));
//            }
//        });
//
//        findViewById(R.id.btn_tabbar).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, ActTabbar.class));
//            }
//        });
        xmVideoView = findViewById(R.id.video);
        findViewById(R.id.btn_media2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xmVideoView.test();
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //xmVideoView.next();
                binder.next();
            }
        });

        findViewById(R.id.btn_full).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xmVideoView.layout(0, 200, xmVideoView.getRight(), xmVideoView.getBottom() + 200);  //ps:如果再调用requestLayout 则不会生效
            }
        });

        ImageView ivPre = findViewById(R.id.iv);
        ivPre.setVisibility(View.GONE);
        String preUrl = "https://img-blog.csdn.net/20160413112832792?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center";
        Glide.with(this).load(preUrl).error(R.drawable.ic_launcher_background).into(ivPre);//加载图片


        //youtube  底部播放列表 底部上滑 未达到一定距离 会回弹
        //bilibili 右侧播放列表 右部左滑 到达一定距离 展示列表
        RecyclerView recyclerView = null;
        ObjectAnimator.ofFloat(recyclerView, "translationX", 0, 500f).setDuration(6000).start();
        ObjectAnimator.ofFloat(recyclerView, "translationY", 0, 500f).setDuration(6000).start();
    }

    XmMediaPlayerService.XmMediaPlayerBinder binder;
    XmMediaPlayerService xmMediaPlayerService;

    @Override
    protected void onPause() {
        super.onPause();
        xmVideoView.onPause();
        //startService(new Intent(this, XmMediaPlayerService.class));
        ServiceConnection conn = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("", "onServiceConnected");
                binder = (XmMediaPlayerService.XmMediaPlayerBinder) service;
                binder.setXmVideoView(xmVideoView);
                xmMediaPlayerService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("", "onServiceDisconnected");
            }

            @Override
            public void onBindingDied(ComponentName name) {
                Log.d("", "onBindingDied");
            }

            @Override
            public void onNullBinding(ComponentName name) {
                Log.d("", "onNullBinding");
            }
        };
        bindService(new Intent(this, XmMediaPlayerService.class), conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        xmVideoView.getMediaPlayer().stop();
    }
}
