package common.xm.com.xmcommon;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xm.lib.component.XmTabbarComponent;
import com.xm.lib.core.OneCore;
import com.xm.lib.core.TwoCore;
import com.xm.lib.ijkplayer.IJKPlayer;
import com.xm.lib.media.enum_.EnumViewType;
import com.xm.lib.media.XmMediaComponent;

import common.xm.com.xmcommon.mediaview.MediaControlView;
import common.xm.com.xmcommon.mediaview.MediaLoading;
import common.xm.com.xmcommon.mediaview.MediaPreView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 第一种 底部菜单+Fragment
        XmTabbarComponent barLayout = findViewById(R.id.tabbar1);
        barLayout.setContainer(R.id.container)
                .addCore(new OneCore())
                .setColor(R.color.activity_color_translucence, R.color.center_bottom_text_color_red)
                .addItem(new MyFragment(), "button1", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
                .addItem(new MyFragment(), "button2", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
                .addItem(new MyFragment(), "button3", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
                .addItem(new MyFragment(), "button4", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
                .builde();

        // 第二种 底部菜单+ViewPager
        XmTabbarComponent barLayout2 = findViewById(R.id.tabbar2);
        barLayout2.bindViewPager((ViewPager) findViewById(R.id.viewpager))
                .addCore(new TwoCore())
                .setColor(R.color.activity_color_translucence, R.color.center_bottom_text_color_red)
                .addItem(new MyFragment(), "button1", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
                .addItem(new MyFragment(), "button2", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
                .addItem(new MyFragment(), "button3", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
                .addItem(new MyFragment(), "button4", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
                .builde();

        // 第三种 顶部菜单 + Viewpager + 滑块
        XmTabbarComponent barLayout3 = findViewById(R.id.tabbar3);
        barLayout3.setContainer(R.id.ViewPager3)
                .addCore(new TwoCore())
                .setColor(R.color.activity_color_translucence, R.color.center_bottom_text_color_red)
                .addItem(new MyFragment(), "button1", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
                .addItem(new MyFragment(), "button2", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
                .addItem(new MyFragment(), "button3", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
                .addItem(new MyFragment(), "button4", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
                .builde();

        //播放器
        XmMediaComponent xmMediaComponent = findViewById(R.id.media);

        MediaPreView mediaPreView = new MediaPreView(this, R.layout.media_preview);
        MediaLoading mediaLoading = new MediaLoading(this, R.layout.media_loading);
        MediaControlView mediaController = new MediaControlView(this, R.layout.media_controller);

        ViewGroup mediaError = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.media_error, xmMediaComponent, false);
        ViewGroup mediaComplete = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.media_complete, xmMediaComponent, false);
        ViewGroup mediaGestureLight = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.media_gesture_light, xmMediaComponent, false);
        ViewGroup mediaGestureProgress = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.media_gesture_progress, xmMediaComponent, false);
        ViewGroup mediaGestureVolume = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.media_gesture_volume, xmMediaComponent, false);

        xmMediaComponent
                .core(new IJKPlayer())
                .addViewToMedia(EnumViewType.PREVIEW, mediaPreView)
                .addViewToMedia(EnumViewType.LOADING, mediaLoading)
                .addViewToMedia(EnumViewType.CONTROLLER, mediaController)
                .addViewToMedia(EnumViewType.ERROR, mediaError)
                .addViewToMedia(EnumViewType.COMPLE, mediaComplete)
                .addViewToMedia(EnumViewType.GESTURE_LIGHT, mediaGestureLight)
                .addViewToMedia(EnumViewType.GESTURE_PROGRESS, mediaGestureProgress)
                .addViewToMedia(EnumViewType.GESTURE_VOLUME, mediaGestureVolume)
                .setup()
                .setDisplay("http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8");
        //.start();   //设置就会自动播放

        // 观察者
        mediaPreView.addObserver(xmMediaComponent);
        mediaPreView.addObserver(mediaLoading);
        mediaPreView.addObserver(mediaController);

        mediaLoading.addObserver(xmMediaComponent);
        mediaLoading.addObserver(mediaPreView);
        mediaLoading.addObserver(mediaController);

        mediaController.addObserver(xmMediaComponent);
        mediaController.addObserver(mediaPreView);
        mediaController.addObserver(mediaLoading);

        xmMediaComponent.addObserver(mediaPreView);
        xmMediaComponent.addObserver(mediaLoading);
        xmMediaComponent.addObserver(mediaController);
    }
}
