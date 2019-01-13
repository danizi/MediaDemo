package com.xm.lib.ijkplayer

import android.text.TextUtils
import android.view.SurfaceHolder
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.constant.Constant
import com.xm.lib.media.core.listener.MediaListener
import com.xm.lib.media.core.utils.LogUtil
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class IJKPlayer : AbsMediaCore() {

    private var tag: String? = "IJKPlayer"
    private var player: IjkMediaPlayer? = null

    init {
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")

        player = IjkMediaPlayer()
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "libijkplayer.so", 1)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 0)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "start-on-prepared", 1)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
        player?.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)

        LogUtil.d(tag, "player setup")
    }


    override fun prepareAsync() {
        super.prepareAsync()
        player?.setScreenOnWhilePlaying(true)
        player?.prepareAsync()
        LogUtil.d(tag, "player prepareAsync")
    }

    override fun start() {
        super.start()
        if (canStart()) {
            player?.start()
            mediaLifeState = Constant.MediaLifeState.Started
            LogUtil.d(tag, "player start")
        } else {
            LogUtil.e(tag, "Cannot play because player state is $mediaLifeState")
        }
    }

    override fun pause() {
        super.pause()
        if (canPause()) {
            player?.pause()
            mediaLifeState = Constant.MediaLifeState.Paused
            LogUtil.d(tag, "player pause")
        } else {
            LogUtil.e(tag, "Cannot pause because player state is $mediaLifeState")
        }
    }

    override fun stop() {
        super.stop()
        if (canStop()) {
            player?.stop()
            mediaLifeState = Constant.MediaLifeState.Stop
            LogUtil.d(tag, "player stop")
        } else {
            LogUtil.e(tag, "Cannot pause because player state is $mediaLifeState")
        }
    }

    override fun getDuration(): Long? {
        val duration = player?.duration
        LogUtil.i(tag, "duration:$duration")
        return duration
    }

    override fun getCurrentPosition(): Long? {
        val currentPosition = player?.currentPosition
        LogUtil.i(tag, "currentPosition:$currentPosition")
        return currentPosition
    }

    override fun seekTo(msec: Long?) {
        if (canSeekTo(msec, getDuration()!!)) {
            player?.seekTo(msec!!)
            LogUtil.d(tag, "player seekTo$msec")
        } else {
            LogUtil.e(tag, "Cannot seekTo because Cross a line")
        }
    }

    override fun release() {
        super.release()
        player?.release()
        player = null
        mediaLifeState = Constant.MediaLifeState.End
        LogUtil.d(tag, "player release")
    }

    override fun reset() {
        super.reset()
        player?.reset()
        mediaLifeState = Constant.MediaLifeState.Idle
        LogUtil.d(tag, "player reset")
    }

    override fun getVideoHeight(): Int? {
        return player?.videoHeight
    }

    override fun getVideoWidth(): Int? {
        return player?.videoWidth
    }

    override fun setAudioStreamType() {
        player?.setAudioStreamType(1)
        LogUtil.d(tag, "player setAudioStreamType 1")
    }

    override fun setVolume() {
        player?.setVolume(0f, 0f)
        LogUtil.d(tag, "player setVolume")
    }

    override fun setSpeed(speed: Float) {
        player?.setSpeed(speed)
        LogUtil.d(tag, "player setSpeed $speed")
    }

    override fun getSpeed(): Float? {
        return player?.getSpeed(11f)
    }

    override fun setDisplay(sh: SurfaceHolder?) {
        player?.setDisplay(sh)
        LogUtil.d(tag, "player setDisplay")
    }

    override fun setDataSource(path: String) {
        super.setDataSource(path)
        if (!TextUtils.isEmpty(path)) {
            mediaLifeState = Constant.MediaLifeState.Initialized
            player?.dataSource = path
            LogUtil.d(tag, "player setDataSource $path")
        } else {
            LogUtil.e(tag, "player setDataSource path is null")
        }
    }

    override fun getTcpSpeed(): Long? {
        return player?.tcpSpeed
    }

    override fun setOnCompletionListener(listener: MediaListener.OnCompletionListener) {
        super.setOnCompletionListener(listener)
        player?.setOnCompletionListener {
            LogUtil.d(tag, "player onCompletion")
            listener.onCompletion(this)
        }
    }

    override fun setOnBufferingUpdateListener(listener: MediaListener.OnBufferingUpdateListener) {
        super.setOnBufferingUpdateListener(listener)
        player?.setOnBufferingUpdateListener { _, percent ->
            LogUtil.i(tag, "player onBufferingUpdate percent:$percent")
            listener.onBufferingUpdate(this, percent)
        }
    }

    override fun setOnSeekCompleteListener(listener: MediaListener.OnSeekCompleteListener) {
        super.setOnSeekCompleteListener(listener)
        player?.setOnSeekCompleteListener {
            mediaLifeState = Constant.MediaLifeState.PlaybackCompleted
            LogUtil.d(tag, "player onSeekComplete")
            listener.onSeekComplete(this)
        }
    }

    override fun setOnVideoSizeChangedListener(listener: MediaListener.OnVideoSizeChangedListener) {
        super.setOnVideoSizeChangedListener(listener)
        player?.setOnVideoSizeChangedListener { _, width, height, sar_num, sar_den ->
            LogUtil.d(tag, "player onVideoSizeChanged width:$width height:$height")
            listener.onVideoSizeChanged(this, width, height, sar_num, sar_den)
        }
    }

    override fun setOnErrorListener(listener: MediaListener.OnErrorListener) {
        super.setOnErrorListener(listener)
        player?.setOnErrorListener { _, what, extra ->
            mediaLifeState = Constant.MediaLifeState.Error
            LogUtil.e(tag, "player onError what:$what extra:$extra")
            listener.onError(this, what, extra)
        }
    }

    override fun setOnInfoListener(listener: MediaListener.OnInfoListener) {
        super.setOnInfoListener(listener)
        player?.setOnInfoListener { _, what, extra ->
            LogUtil.d(tag, "player onInfo what:$what extra:$extra")
            listener.onInfo(this, what, extra)
        }
    }

    override fun setOnTimedTextListener(listener: MediaListener.OnTimedTextListener) {
        super.setOnTimedTextListener(listener)
        player?.setOnTimedTextListener { _, text ->
            LogUtil.d(tag, "player onTimedText")
            listener.onTimedText(this, text.text)
        }
    }

    override fun setOnPreparedListener(listener: MediaListener.OnPreparedListener) {
        super.setOnPreparedListener(listener)
        player?.setOnPreparedListener {
            mediaLifeState = Constant.MediaLifeState.Prepared
            LogUtil.d(tag, "player onPrepared")
            listener.onPrepared(this)
        }
    }
}