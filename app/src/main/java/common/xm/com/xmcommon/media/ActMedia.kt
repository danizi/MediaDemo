package common.xm.com.xmcommon.media

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.xm.lib.ijkplayer.IJKPlayer
import com.xm.lib.media.XmMediaComponent
import com.xm.lib.media.enum_.EnumViewType
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.MediaControlView
import common.xm.com.xmcommon.media.mediaview.MediaLoading
import common.xm.com.xmcommon.media.mediaview.MediaPreView

class ActMedia : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_media)

        //播放器
        val xmMediaComponent = findViewById<XmMediaComponent>(R.id.media)

        val mediaPreView = MediaPreView(this, R.layout.media_preview)
        val mediaLoading = MediaLoading(this, R.layout.media_loading)
        val mediaController = MediaControlView(this, R.layout.media_controller)
        val mediaError = LayoutInflater.from(this).inflate(R.layout.media_error, xmMediaComponent, false) as ViewGroup
        val mediaComplete = LayoutInflater.from(this).inflate(R.layout.media_complete, xmMediaComponent, false) as ViewGroup
        val mediaGestureLight = LayoutInflater.from(this).inflate(R.layout.media_gesture_light, xmMediaComponent, false) as ViewGroup
        val mediaGestureProgress = LayoutInflater.from(this).inflate(R.layout.media_gesture_progress, xmMediaComponent, false) as ViewGroup
        val mediaGestureVolume = LayoutInflater.from(this).inflate(R.layout.media_gesture_volume, xmMediaComponent, false) as ViewGroup

        xmMediaComponent
                .core(IJKPlayer()) //使用的播放器引擎
                .setup()           //播放器初始化
                .addViewToMedia(EnumViewType.PREVIEW, mediaPreView)                                       //绑定预览视图
                .addViewToMedia(EnumViewType.LOADING, mediaLoading)                                       //绑定加载视图
                .addViewToMedia(EnumViewType.CONTROLLER, mediaController)                                 //绑定控制视图
                .addViewToMedia(EnumViewType.ERROR, mediaError)                                           //绑定错误视图
                .addViewToMedia(EnumViewType.COMPLE, mediaComplete)                                       //绑定完成视图
                .addViewToMedia(EnumViewType.GESTURE_LIGHT, mediaGestureLight)                            //绑定手势滑动亮度视图
                .addViewToMedia(EnumViewType.GESTURE_PROGRESS, mediaGestureProgress)                      //绑定手势滑动进度视图
                .addViewToMedia(EnumViewType.GESTURE_VOLUME, mediaGestureVolume)                          //绑定手势滑动声音视图
                .setDisplay("http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8")//设置播放资源

        // 观察者
        mediaPreView.addObserver(xmMediaComponent)
        mediaPreView.addObserver(mediaLoading)
        mediaPreView.addObserver(mediaController)

        mediaLoading.addObserver(xmMediaComponent)
        mediaLoading.addObserver(mediaPreView)
        mediaLoading.addObserver(mediaController)

        mediaController.addObserver(xmMediaComponent)
        mediaController.addObserver(mediaPreView)
        mediaController.addObserver(mediaLoading)

        xmMediaComponent.addObserver(mediaPreView)
        xmMediaComponent.addObserver(mediaLoading)
        xmMediaComponent.addObserver(mediaController)

    }
}
