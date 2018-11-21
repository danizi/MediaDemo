package com.xm.lib.ijkplayer

import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.FrameLayout
import com.xm.lib.media.core.AbsMediaCore
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class IJKPlayer : AbsMediaCore() {

    var player: IjkMediaPlayer? = null

    override fun init() {
        //初始化相关配置
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        player = IjkMediaPlayer()
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 1)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
        player!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)
        //添加画布并设置画布监听
        mSurfaceView = createSurfaceView()
        mSurfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                mediaCoreOnLisenter?.surfaceChanged(holder, format, width, height)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                mediaCoreOnLisenter?.surfaceDestroyed(holder)
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                mediaCoreOnLisenter?.surfaceCreated(holder)
            }
        })
        //设置监听
        player?.setOnPreparedListener { mediaCoreOnLisenter?.onPrepared(this) }
        player?.setOnCompletionListener { mediaCoreOnLisenter?.onCompletion(this) }
        player?.setOnBufferingUpdateListener { p0, p1 -> mediaCoreOnLisenter?.onBufferingUpdate(this, p1) }
        player?.setOnSeekCompleteListener { mp -> mediaCoreOnLisenter?.onSeekComplete(this) }
        player?.setOnVideoSizeChangedListener { mp, width, height, sar_num, sar_den -> mediaCoreOnLisenter?.onVideoSizeChanged(this, width, height, sar_num, sar_den) }
        player?.setOnErrorListener { mp, what, extra -> mediaCoreOnLisenter?.onError(this, what, extra)!! }
        player?.setOnInfoListener { mp, what, extra -> mediaCoreOnLisenter?.onInfo(this, what, extra)!! }
        player?.setOnTimedTextListener { mp, text -> mediaCoreOnLisenter?.onTimedText(this) }
    }

    private fun createSurfaceView(): SurfaceView {
        var surfaceView = SurfaceView(context)
        var layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        surfaceView?.layoutParams = layoutParams
        return surfaceView
    }

    override fun start() {
        mSurfaceView = SurfaceView(context)
        var layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        mSurfaceView?.layoutParams = layoutParams
        mSurfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {

            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                surfaceCreated()
            }
        })
        tagerView?.addView(mSurfaceView, 0)
    }

    private fun surfaceCreated() {
        player!!.setLogEnabled(true)
        player!!.dataSource = model?.dataSource
        player!!.setScreenOnWhilePlaying(true)
        player!!.setDisplay(mSurfaceView!!.holder)
        player!!.prepareAsync()
        player!!.setOnPreparedListener {
            super.mediaCoreOnLisenter?.onPrepared(this)
        }

        player!!.setOnBufferingUpdateListener { p0, p1 ->

        }

        player?.setOnErrorListener { mp, what, extra ->
            true
        }
    }

    override fun pause() {
        player?.pause()
    }

    override fun stop() {
        player?.stop()
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
    }
}