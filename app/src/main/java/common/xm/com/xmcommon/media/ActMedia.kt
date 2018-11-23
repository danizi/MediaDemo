package common.xm.com.xmcommon.media

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xm.lib.ijkplayer.IJKPlayer
import com.xm.lib.media.XmMediaComponent
import com.xm.lib.media.enum_.EnumViewType
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.component.*

class ActMedia : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_media)

        //播放器
        val xmMediaComponent = findViewById<XmMediaComponent>(R.id.media)

        val mediaPreView = MediaPreView(this, R.layout.media_preview)
        val mediaLoading = MediaLoadingView(this, R.layout.media_loading)
        val mediaController = MediaControlView(this, R.layout.media_controller)
        val mediaError = MediaErrorView(this, R.layout.media_error)
        val mediaComplete = MediaCompleteView(this, R.layout.media_complete)
        val mediaGestureLight = MediaGestureLightView(this, R.layout.media_gesture_light)
        val mediaGestureProgress = MediaGestureProgressView(this, R.layout.media_gesture_progress)
        val mediaGestureVolume = MediaGestureVolumeView(this, R.layout.media_gesture_volume)

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
                .build()

    }
}
