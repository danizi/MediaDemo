package common.xm.com.xmcommon.media

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xm.lib.ijkplayer.VideoView
import com.xm.lib.media.core.attach.factory.MediaAttachLayoutFactory
import common.xm.com.xmcommon.R

class ActivityIjkPlayerViewVideo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ijk_video)
        val videoView: VideoView? = findViewById(R.id.videoView)
        videoView?.setAttachLayout(MediaAttachLayoutFactory.create(MediaAttachLayoutFactory.EnumMediaView.CONTROLLER, this, R.layout.media_controller))
        videoView?.setAttachLayout(MediaAttachLayoutFactory.create(MediaAttachLayoutFactory.EnumMediaView.GESTURE, this, R.layout.media_gesture_common))
        videoView?.setAttachLayout(MediaAttachLayoutFactory.create(MediaAttachLayoutFactory.EnumMediaView.TIP, this, R.layout.media_gesture_common))
    }
}
