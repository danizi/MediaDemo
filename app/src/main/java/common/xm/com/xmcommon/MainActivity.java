package common.xm.com.xmcommon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import common.xm.com.xmcommon.media2.base.XmVideoView;

public class MainActivity extends AppCompatActivity {
    private @SuppressLint("InflateParams")
    ViewGroup view;

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

        findViewById(R.id.btn_media2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XmVideoView().testMediaPlayer(MainActivity.this, view);
            }
        });
    }
}
