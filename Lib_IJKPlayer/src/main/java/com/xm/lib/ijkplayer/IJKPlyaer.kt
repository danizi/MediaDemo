package com.xm.lib.ijkplayer

import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.FrameLayout
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.enum_.EnumMediaState
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class IJKPlayer : AbsMediaCore() {


    private val SO_LIB_NAME = "libijkplayer.so"
    private val FRAMEDROP = "framedrop"
    private val START_ON_PREPARED = "start-on-prepared"
    private val HTTP_DETECT_RANGE_SUPPORT = "http-detect-range-support"
    private val SKIP_LOOP_FILTER = "skip_loop_filter"
    private val OPENSLES = "opensles"
    private val MEDIACODEC = "mediacodec"
    private val MEDIACODEC_AUTO_ROTATE = "mediacodec-auto-rotate"
    private val MEDIACODEC_HANDLE_RESOLUTION_CHANGE = "mediacodec-handle-resolution-change"

    var player: IjkMediaPlayer? = null

    override fun init() {
        //初始化相关配置
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin(SO_LIB_NAME)
        player = IjkMediaPlayer()
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, FRAMEDROP, 1)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, START_ON_PREPARED, 0)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, HTTP_DETECT_RANGE_SUPPORT, 1)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, SKIP_LOOP_FILTER, 48)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, OPENSLES, 1)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, MEDIACODEC, 1)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, MEDIACODEC_AUTO_ROTATE, 1)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, MEDIACODEC_HANDLE_RESOLUTION_CHANGE, 1)

        //添加画布并设置画布监听
        mSurfaceView = createSurfaceView()
        mSurfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                absMediaCoreOnLisenter?.surfaceChanged(holder, format, width, height)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                absMediaCoreOnLisenter?.surfaceDestroyed(holder)
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                absMediaCoreOnLisenter?.surfaceCreated(holder)
            }
        })
        tagerView?.addView(mSurfaceView, 0)

        //设置监听
        player?.setOnPreparedListener {
            playerState = EnumMediaState.PLAYING
            absMediaCoreOnLisenter?.onPrepared(this)
        }
        player?.setOnCompletionListener {
            playerState = EnumMediaState.COMPLETION
            absMediaCoreOnLisenter?.onCompletion(this)
        }
        player?.setOnBufferingUpdateListener { _, p1 ->
            absMediaCoreOnLisenter?.onBufferingUpdate(this, p1)
        }
        player?.setOnSeekCompleteListener { _ ->
            playerState = EnumMediaState.SEEKCOMPLETE
            absMediaCoreOnLisenter?.onSeekComplete(this)
        }
        player?.setOnVideoSizeChangedListener { _, width, height, sar_num, sar_den ->
            absMediaCoreOnLisenter?.onVideoSizeChanged(this, width, height, sar_num, sar_den)
        }
        player?.setOnErrorListener { _, what, extra ->
            playerState = EnumMediaState.ERROR
            absMediaCoreOnLisenter?.onError(this, what, extra)!!
        }
        player?.setOnInfoListener { _, what, extra ->
            absMediaCoreOnLisenter?.onInfo(this, what, extra)!!
        }
        player?.setOnTimedTextListener { _, _ ->
            absMediaCoreOnLisenter?.onTimedText(this)
        }
    }

    private fun createSurfaceView(): SurfaceView {
        val surfaceView = SurfaceView(context)
        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        surfaceView.layoutParams = layoutParams
        return surfaceView
    }

    override fun prepareAsync() {
        player?.setLogEnabled(true)
        player?.setDisplay(mSurfaceView!!.holder)
        player?.setScreenOnWhilePlaying(true)
        player?.dataSource = model?.dataSource
        player?.prepareAsync()
    }

    override fun start() {
        player?.start()
        if (playerState == EnumMediaState.PAUSE) {
            playerState = EnumMediaState.PLAYING
        }
    }

    override fun pause() {
        player?.pause()
        playerState = EnumMediaState.PAUSE
    }

    override fun stop() {
        player?.stop()
        playerState = EnumMediaState.STOP
    }

    override fun getDuration(): Long {
        return player?.duration!!
    }

    override fun getCurrentPosition(): Long {
        return player?.currentPosition!!
    }

    override fun seekTo(msec: Long) {
        player?.seekTo(msec)
    }

    override fun release() {
        player?.reset()
        player?.release()
        player = null
        playerState = EnumMediaState.RELEASE
    }
}