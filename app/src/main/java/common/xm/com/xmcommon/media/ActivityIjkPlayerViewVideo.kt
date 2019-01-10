package common.xm.com.xmcommon.media

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xm.lib.ijkplayer.VideoView
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.attach.factory.MediaAttachLayoutFactory
import com.xm.lib.media.core.constant.Constant
import com.xm.lib.media.core.listener.MediaListener
import common.xm.com.xmcommon.R

class ActivityIjkPlayerViewVideo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ijk_video)

        val videoView: VideoView? = findViewById(R.id.videoView)
        videoView?.setAttachLayout(MediaAttachLayoutFactory.create(Constant.EnumMediaView.CONTROLLER, this, R.layout.media_controller_v))   //添加控制（横屏&竖屏）布局
        videoView?.setAttachLayout(MediaAttachLayoutFactory.create(Constant.EnumMediaView.GESTURE, this, R.layout.media_gesture_common))    //添加手势（进度&音量&亮度）布局
        videoView?.setAttachLayout(MediaAttachLayoutFactory.create(Constant.EnumMediaView.TIP, this, R.layout.media_gesture_common))        //添加提示 (播放错误&来电&拔下或者插上耳机&断网)布局
        videoView?.setVideoPath("http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8")
        videoView?.setOnPreparedListener(object : MediaListener.OnPreparedListener {
            override fun onPrepared(mp: AbsMediaCore) {
                videoView.start()
            }
        })
    }
}
