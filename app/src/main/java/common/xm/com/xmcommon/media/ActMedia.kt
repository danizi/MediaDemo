package common.xm.com.xmcommon.media

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xm.lib.ijkplayer.IJKPlayer
import com.xm.lib.media.component.XmMediaComponent
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumViewType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import common.xm.com.xmcommon.R
import com.xm.lib.media.component.*
import com.xm.lib.media.component.control.MediaControlView

class ActMedia : AppCompatActivity() {
    //播放器
    var preUrl = "http://img.videocc.net/uimage/2/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_1.jpg"
    var xmMediaComponent: XmMediaComponent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_media)
        xmMediaComponent = findViewById(R.id.media)
        if (xmMediaComponent != null) {
            xmMediaComponent!!
                    .core(IJKPlayer())                                                                                                        //使用的播放器引擎
                    .setup()                                                                                                                  //播放器初始化
                    .addViewToMedia(EnumViewType.PREVIEW, MediaPreView(this, R.layout.media_preview, preUrl))                        //绑定预览视图
                    .addViewToMedia(EnumViewType.LOADING, MediaLoadingView(this, R.layout.media_loading))                            //绑定加载视图
                    .addViewToMedia(EnumViewType.CONTROLLER, MediaControlView(this, R.layout.media_controller))                      //绑定控制视图
                    .addViewToMedia(EnumViewType.ERROR, MediaErrorView(this, R.layout.media_error))                                  //绑定错误视图
                    .addViewToMedia(EnumViewType.COMPLE, MediaCompleteView(this, R.layout.media_complete))                           //绑定完成视图
                    .addViewToMedia(EnumViewType.GESTURE_LIGHT, MediaGestureLightView(this, R.layout.media_gesture_light))           //绑定手势滑动亮度视图
                    .addViewToMedia(EnumViewType.GESTURE_PROGRESS, MediaGestureProgressView(this, R.layout.media_gesture_progress))  //绑定手势滑动进度视图
                    .addViewToMedia(EnumViewType.GESTURE_VOLUME, MediaGestureVolumeView(this, R.layout.media_gesture_volume))        //绑定手势滑动声音视图
                    .setDisplay("http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8")                                 //设置播放资源
                    .build()                                                                                                                   //构建
        }
    }

    override fun onResume() {
        super.onResume()
        notifyObservers("Activity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        notifyObservers("Activity", "onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        notifyObservers("Activity", "onDestroy")
    }

    private fun notifyObservers(from: String?, method: String?) {
        xmMediaComponent?.notifyObservers(
                Event().setEventType(EnumMediaEventType.OTHER)
                        .setParameter(EventConstant.KEY_FROM, from!!)
                        .setParameter(EventConstant.KEY_METHOD, method!!))
    }
}
