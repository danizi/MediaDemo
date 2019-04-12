package common.xm.com.xmcommon.media2.base

import android.annotation.TargetApi
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import common.xm.com.xmcommon.media2.event.PlayerObservable
import common.xm.com.xmcommon.media2.log.BKLog
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.IOException

class XmVideoView {

    @TargetApi(Build.VERSION_CODES.O)
    fun testMediaPlayer(context: Context, parent: ViewGroup?) {

        val m = MediaPlayer()
        //基本方法
        m.duration
        m.currentPosition
        m.videoHeight
        m.videoWidth
        m.isPlaying
        m.isLooping
        m.start()
        m.stop()
        m
        //创建画布
        val surfaceView = SurfaceView(context)
        surfaceView.layoutParams = ViewGroup.LayoutParams(400, 400)
        parent?.addView(surfaceView)
        surfaceView.holder.setKeepScreenOn(true)
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                BKLog.d(XmMediaPlayer.TAG, "surfaceChanged width:$width height:$height")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                BKLog.d(XmMediaPlayer.TAG, "surfaceDestroyed")
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                m.reset()
                m.setDisplay(holder)
                try {
                    m.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                m.prepareAsync()
                BKLog.d(XmMediaPlayer.TAG, "surfaceCreated")
            }
        })
        m.setAudioStreamType(AudioManager.STREAM_MUSIC)
        m.setScreenOnWhilePlaying(true)
        BKLog.d(XmMediaPlayer.TAG, "播放状态:${m.isPlaying}")
        //设置监听
        m.setOnSeekCompleteListener {
            BKLog.d(XmMediaPlayer.TAG, "SeekCompleteListener")
        }
        m.setOnSubtitleDataListener { mp, data ->
            BKLog.d(XmMediaPlayer.TAG, "SubtitleDataListener")
        }
        m.setOnTimedTextListener { mp, text ->
            BKLog.d(XmMediaPlayer.TAG, "TimedTextListener")
        }
        m.setOnVideoSizeChangedListener { mp, width, height ->
            BKLog.d(XmMediaPlayer.TAG, "VideoSizeChangedListener width:$width height:$height")
        }
        m.setOnErrorListener { mp, what, extra ->
            BKLog.d(XmMediaPlayer.TAG, "ErrorListener what:$what extra:$extra")
            false
        }
        m.setOnInfoListener { mp, what, extra ->
            BKLog.d(XmMediaPlayer.TAG, "InfoListener what:$what extra:$extra")
            false
        }
        m.setOnDrmInfoListener { mp, drmInfo ->
            BKLog.d(XmMediaPlayer.TAG, "DrmInfoListener drmInfo:$drmInfo")
        }
        m.setOnBufferingUpdateListener { mp, percent ->
            BKLog.d(XmMediaPlayer.TAG, "BufferingUpdateListener percent:$percent")
        }
        m.setOnCompletionListener {
            BKLog.d(XmMediaPlayer.TAG, "CompletionListener")
        }
        m.setOnPreparedListener {
            m.start()
            BKLog.d(XmMediaPlayer.TAG, "PreparedListener")
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun testijk(context: Context, parent: ViewGroup?) {

        val m = IjkMediaPlayer()
        //创建画布
        val surfaceView = SurfaceView(context)
        surfaceView.layoutParams = ViewGroup.LayoutParams(400, 400)
        parent?.addView(surfaceView)
        surfaceView.holder.setKeepScreenOn(true)
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                BKLog.d(XmMediaPlayer.TAG, "surfaceChanged width:$width height:$height")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                BKLog.d(XmMediaPlayer.TAG, "surfaceDestroyed")
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                m.reset()
                m.setDisplay(holder)
                try {
                    m.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                m.prepareAsync()
                BKLog.d(XmMediaPlayer.TAG, "surfaceCreated")
            }
        })
        m.setAudioStreamType(AudioManager.STREAM_MUSIC)  //与原生不同
        //m.setScreenOnWhilePlaying(true)

        /**
         * IjkMediaPlayer 特有监听
         */
        m.setOnMediaCodecSelectListener { mp, mimeType, profile, level -> "" }
        m.setOnNativeInvokeListener { what, args -> false }
        m.setOnControlMessageListener { "" }

        //设置监听
        m.setOnSeekCompleteListener {
            BKLog.d(XmMediaPlayer.TAG, "SeekCompleteListener")

        }
//        m.setOnSubtitleDataListener { mp, data ->
//            BKLog.d(TAG, "SubtitleDataListener")
//        }
        m.setOnTimedTextListener { mp, text ->
            BKLog.d(XmMediaPlayer.TAG, "TimedTextListener")
        }

        m.setOnVideoSizeChangedListener { mp, width, height, sar_num, sar_den ->
            BKLog.d(XmMediaPlayer.TAG, "VideoSizeChangedListener width:$width height:$height sar_num:$sar_num sar_den:$sar_den")
        }

//        m.setOnVideoSizeChangedListener { mp, width, height ->
//            BKLog.d(TAG, "VideoSizeChangedListener width:$width height:$height")
//        }

        m.setOnErrorListener { mp, what, extra ->
            BKLog.d(XmMediaPlayer.TAG, "ErrorListener what:$what extra:$extra")
            false
        }
        m.setOnInfoListener { mp, what, extra ->
            BKLog.d(XmMediaPlayer.TAG, "InfoListener what:$what extra:$extra")
            false
        }
//        m.setOnDrmInfoListener { mp, drmInfo ->
//            BKLog.d(TAG, "DrmInfoListener drmInfo:$drmInfo")
//        }
        m.setOnBufferingUpdateListener { mp, percent ->
            BKLog.d(XmMediaPlayer.TAG, "BufferingUpdateListener percent:$percent")
        }
        m.setOnCompletionListener {
            BKLog.d(XmMediaPlayer.TAG, "CompletionListener")
        }
        m.setOnPreparedListener {
            m.start()
            BKLog.d(XmMediaPlayer.TAG, "PreparedListener")
        }
    }

    fun test() {
        // 用户添加附着页面 - 预览界面
        // 用户添加附着页面 - 完成界面
        // 用户添加附着页面 - 错误界面
        // 用户添加附着页面 - 加载界面
        // 用户添加附着页面 - 控制器竖屏界面
        // 用户添加附着页面 - 控制器横屏界面


        // 用户进入播放窗口，此时展现预览界面，点击预览界面，显示加载页面  -> 播放回调 -> 删除预览界面和加载页面
        // 播放过程中用户多次点击播放屏幕, (控制界面：显示&隐藏 有两种模式横屏和竖屏) -> 竖屏删除横屏页面，反之亦然
        // 播放过程中控制器 接受回调
    }

    val p = PlayerObservable()

    fun add() {
        //p.addObserver(PlayerObserver())
    }
}