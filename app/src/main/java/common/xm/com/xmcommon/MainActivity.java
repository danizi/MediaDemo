package common.xm.com.xmcommon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import common.xm.com.xmcommon.media2.base.XmVideoView;
import common.xm.com.xmcommon.media2.utils.ScreenUtil;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        xmVideoView.getMediaPlayer().stop();
    }
}
