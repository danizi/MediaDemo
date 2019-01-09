package com.xm.lib.ijkplayer

import android.view.SurfaceHolder
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.listener.MediaListener
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class IJKPlayer : AbsMediaCore() {

    var player: IjkMediaPlayer? = null

    override fun init() {
        super.init()

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
    }

    override fun prepareAsync() {
        player?.setScreenOnWhilePlaying(true)
        player?.prepareAsync()
    }

    override fun start() {
        super.start()
        player?.start()
    }

    override fun pause() {
        super.pause()
        player?.pause()
    }

    override fun stop() {
        super.stop()
        player?.stop()
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
        player?.release()
        player = null
        super.release()
    }

    override fun reset() {
        player?.reset()
        super.reset()
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

    override fun getSpeed(): Float? {
        return player?.getSpeed(11f)
    }

    override fun setDisplay(sh: SurfaceHolder?) {
        player?.setDisplay(sh)
    }

    override fun setDataSource(path: String) {
        super.setDataSource(path)
        player?.dataSource = path
    }

    override fun getTcpSpeed(): Long? {
        return player?.tcpSpeed
    }

    override fun setOnCompletionListener(listener: MediaListener.OnCompletionListener) {
        player?.setOnCompletionListener {
            super.setOnCompletionListener(listener)
            listener.onCompletion(this)
        }
    }

    override fun setOnBufferingUpdateListener(listener: MediaListener.OnBufferingUpdateListener) {
        player?.setOnBufferingUpdateListener { _, percent ->
            listener.onBufferingUpdate(this, percent)
        }
    }

    override fun setOnSeekCompleteListener(listener: MediaListener.OnSeekCompleteListener) {
        player?.setOnSeekCompleteListener { listener.onSeekComplete(this) }
    }

    override fun setOnVideoSizeChangedListener(listener: MediaListener.OnVideoSizeChangedListener) {
        player?.setOnVideoSizeChangedListener { _, width, height, sar_num, sar_den ->
            listener.onVideoSizeChanged(this, width, height, sar_num, sar_den)
        }
    }

    override fun setOnErrorListener(listener: MediaListener.OnErrorListener) {
        player?.setOnErrorListener { _, what, extra ->
            super.setOnErrorListener(listener)
            listener.onError(this, what, extra)
        }
    }

    override fun setOnInfoListener(listener: MediaListener.OnInfoListener) {
        player?.setOnInfoListener { _, what, extra ->
            listener.onInfo(this, what, extra)
        }
    }

    override fun setOnTimedTextListener(listener: MediaListener.OnTimedTextListener) {
        player?.setOnTimedTextListener { _, text ->
            listener.onTimedText(this, text.text)
        }
    }

    override fun setOnPreparedListener(listener: MediaListener.OnPreparedListener) {
        player?.setOnPreparedListener {
            super.setOnPreparedListener(listener)
            listener.onPrepared(this)
        }
    }
}