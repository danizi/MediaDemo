package com.xm.lib.ijkplayer

import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.FrameLayout
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.enum_.EnumMediaState
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class IJKPlayer : AbsMediaCore() {

    var player: IjkMediaPlayer? = null

    override fun init() {
        playerConfig()
        //addSurfaceViewAndListener()
        //initPlayerListener()
    }

    private fun playerConfig() {
        val SO_LIB_NAME = "libijkplayer.so"
        val FRAMEDROP = "framedrop"
        val START_ON_PREPARED = "start-on-prepared"
        val HTTP_DETECT_RANGE_SUPPORT = "http-detect-range-support"
        val SKIP_LOOP_FILTER = "skip_loop_filter"
        val OPENSLES = "opensles"
        val MEDIACODEC = "mediacodec"
        val MEDIACODEC_AUTO_ROTATE = "mediacodec-auto-rotate"
        val MEDIACODEC_HANDLE_RESOLUTION_CHANGE = "mediacodec-handle-resolution-change"
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin(SO_LIB_NAME)
        player = IjkMediaPlayer()
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, FRAMEDROP, 1)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, START_ON_PREPARED, 0)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, HTTP_DETECT_RANGE_SUPPORT, 1)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, SKIP_LOOP_FILTER, 48)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, OPENSLES, 1)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, MEDIACODEC, 1)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, MEDIACODEC_AUTO_ROTATE, 1)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, MEDIACODEC_HANDLE_RESOLUTION_CHANGE, 1)
    }

    private fun initPlayerListener() {
        player?.setOnPreparedListener {
            playerState = EnumMediaState.PREPARED
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
        }
        player?.setOnVideoSizeChangedListener { _, width, height, sar_num, sar_den ->
            absMediaCoreOnLisenter?.onVideoSizeChanged(this, width, height, sar_num, sar_den)
        }
        player?.setOnErrorListener { _, what, extra ->
            playerState = EnumMediaState.ERROR
            absMediaCoreOnLisenter?.onError(this, what, extra)!!
        }
        player?.setOnInfoListener { _, what, extra ->
            if (701 == what) {//视频缓存中

            } else if (702 == what) { //视频缓存完成
                absMediaCoreOnLisenter?.onSeekComplete(this)
                playerState = EnumMediaState.PLAYING
                //player?.setSpeed(20 / 10f)
            }
            absMediaCoreOnLisenter?.onInfo(this, what, extra)!!
        }
        player?.setOnTimedTextListener { _, _ ->
            absMediaCoreOnLisenter?.onTimedText(this)
        }
    }

    private fun addSurfaceViewAndListener() {
        mSurfaceView = createSurfaceView()
        mSurfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                absMediaCoreOnLisenter?.surfaceChanged(holder, format, width, height)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                absMediaCoreOnLisenter?.surfaceDestroyed(holder)
                //获取播放的内容
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                absMediaCoreOnLisenter?.surfaceCreated(holder)
            }
        })
        tagerView?.addView(mSurfaceView, 0)
    }

    private fun createSurfaceView(): SurfaceView {
        val surfaceView = SurfaceView(context)
        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        surfaceView.layoutParams = layoutParams
        return surfaceView
    }

    override fun rePlay() {
        if (mSurfaceView != null) {
            try {
                tagerView?.removeView(mSurfaceView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mSurfaceView = createSurfaceView()
        mSurfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                absMediaCoreOnLisenter?.surfaceChanged(holder, format, width, height)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                absMediaCoreOnLisenter?.surfaceDestroyed(holder)
                //获取播放的内容
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                absMediaCoreOnLisenter?.surfaceCreated(holder)
                reset()
                if (model?.curPos != (-1).toLong()) {
                    model?.player?.seekTo(model?.curPos!!)
                    model?.player?.start()
                }
                prepareAsync()
            }
        })
        tagerView?.addView(mSurfaceView, 0)
    }

    override fun prepareAsync() {
//        player?.setLogEnabled(true)
//        player?.setDisplay(mSurfaceView!!.holder)
//        player?.setScreenOnWhilePlaying(true)
//        player?.dataSource = model?.dataSource
        player?.prepareAsync()
    }

    override fun start() {
        player?.start()
    }

    override fun pause() {
        if (playerState == EnumMediaState.PLAYING) {
            player?.pause()
            playerState = EnumMediaState.PAUSE
        }
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

    override fun seekTo(msec: Long?) {
        if (msec != null) {
            if (msec > 0 && msec < getDuration()) {
                player?.seekTo(msec)
            }
        }
    }

    override fun release() {
        player?.reset()
        player?.release()
        player = null
        tagerView?.removeView(mSurfaceView)
        playerState = EnumMediaState.RELEASE
    }

    override fun reset() {
        player?.reset()
    }

    override fun getVideoHeight(): Int? {
        return player?.videoHeight
    }

    override fun getVideoWidth(): Int? {
        return player?.videoWidth
    }

    override fun setAudioStreamType() {
        player?.setAudioStreamType(1)
    }

    override fun setVolume() {
        player?.setVolume(0f, 0f)
    }

    override fun setSpeed(speed: Float) {
        player?.setSpeed(speed)
    }

    override fun setDisplay(sh: SurfaceHolder?) {
        player?.setDisplay(sh)
    }

    override fun setDataSource(path: String) {
        player?.dataSource = path
    }

    override fun getTcpSpeed(): Long? {
        return player?.tcpSpeed
    }

    override fun setOnCompletionListener(listener: OnCompletionListener) {
        player?.setOnCompletionListener {
            listener.onCompletion(this)
        }
    }

    override fun setOnBufferingUpdateListener(listener: OnBufferingUpdateListener) {
        player?.setOnBufferingUpdateListener { _, percent ->
            listener.onBufferingUpdate(this, percent)
        }
    }

    override fun setOnSeekCompleteListener(listener: OnSeekCompleteListener) {
        player?.setOnSeekCompleteListener { listener.onSeekComplete(this) }
    }

    override fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener) {
        player?.setOnVideoSizeChangedListener { _, width, height, sar_num, sar_den ->
            listener.onVideoSizeChanged(this, width, height, sar_num, sar_den)
        }
    }

    override fun setOnErrorListener(listener: OnErrorListener) {
        player?.setOnErrorListener { _, what, extra ->
            listener.onError(this, what, extra)
        }
    }

    override fun setOnInfoListener(listener: OnInfoListener) {
        player?.setOnInfoListener { _, what, extra ->
            listener.onInfo(this, what, extra)
        }
    }

    override fun setOnTimedTextListener(listener: OnTimedTextListener) {
        player?.setOnTimedTextListener { _, text ->
            listener.onTimedText(this, text.text)
        }
    }

    override fun setOnPreparedListener(listener: OnPreparedListener) {
        player?.setOnPreparedListener {
            listener.onPrepared(this)
        }
    }
}