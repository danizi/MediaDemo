package common.xm.com.xmcommon.media

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.LinearLayout
import com.xm.lib.ijkplayer.VideoView
import com.xm.lib.media.core.attach.layout.AttachControlLayout
import com.xm.lib.media.core.attach.layout.AttachGestureLayout
import com.xm.lib.media.core.attach.layout.AttachPreViewLayout
import com.xm.lib.media.core.constant.Constant
import com.xm.lib.media.core.utils.LogUtil
import common.xm.com.xmcommon.R

class ActivityIjkPlayerViewVideo : AppCompatActivity() {

    private var videoView: VideoView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView: ViewGroup = com.xm.lib.media.core.utils.Util.getView(this, R.layout.activity_ijk_video) as ViewGroup
        setContentView(rootView)
        videoView = VideoView.Builder(this)
                .addAttachLayout(Constant.EnumMediaView.PRE, AttachPreViewLayout(this, R.layout.media_preview))
                .addAttachLayout(Constant.EnumMediaView.CONTROLLER, AttachControlLayout(this, R.layout.media_controller))
                .addAttachLayout(Constant.EnumMediaView.GESTURE, AttachGestureLayout(this, R.layout.media_gesture_common))
                .path("http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8")
                //.path("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
                .preUrl("http://img4.imgtn.bdimg.com/it/u=546534595,3069204591&fm=26&gp=0.jpg")
                .width(LinearLayout.LayoutParams.MATCH_PARENT)
                .height(400)
                .auto()
                .build()
        rootView.addView(videoView)
        videoView?.onCreate()
    }

    override fun onStart() {
        super.onStart()
        videoView?.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView?.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
        videoView?.onRestart()
    }

    override fun onStop() {
        super.onStop()
        videoView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        videoView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        videoView?.onResume()
    }
}
