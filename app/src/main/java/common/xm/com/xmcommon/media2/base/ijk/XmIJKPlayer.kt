package common.xm.com.xmcommon.media2.base.ijk

import android.view.SurfaceHolder
import common.xm.com.xmcommon.media2.base.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class XmIJKPlayer : IXmMediaPlayer() {
    private var ijkMediaPlayer = IjkMediaPlayer() //IJKPlayer播放器

    override fun getDuration(): Long {
        return ijkMediaPlayer.duration
    }

    override fun getCurrentPosition(): Long {
        return ijkMediaPlayer.currentPosition
    }

    override fun getVideoHeight(): Long {
        return ijkMediaPlayer.videoHeight.toLong()
    }

    override fun getVideoWidth(): Long {
        return ijkMediaPlayer.videoWidth.toLong()
    }

    override fun isPlaying(): Boolean {
        return ijkMediaPlayer.isPlaying
    }

    override fun isLooping(): Boolean {
        return ijkMediaPlayer.isLooping
    }

    override fun setLooping() {
        ijkMediaPlayer.isLooping = true
    }

    override fun start() {
        ijkMediaPlayer.start()
    }

    override fun stop() {
        ijkMediaPlayer.stop()
    }

    override fun pause() {
        ijkMediaPlayer.pause()
    }

    @Deprecated("ijkPlayer不支持")
    override fun prepare() {

    }

    override fun prepareAsync() {
        ijkMediaPlayer.prepareAsync()
    }

    override fun release() {
        ijkMediaPlayer.release()
    }

    override fun reset() {
        ijkMediaPlayer.reset()
    }

    override fun seekTo(msec: Int) {
        ijkMediaPlayer.seekTo(msec.toLong())
    }

    override fun setDataSource(path: String?) {
        ijkMediaPlayer.dataSource = path
    }

    override fun setDisplay(sh: SurfaceHolder?) {
        ijkMediaPlayer.setDisplay(sh)
    }

    override fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener) {
        ijkMediaPlayer.setOnVideoSizeChangedListener { mp, width, height, sar_num, sar_den ->
            listener.onVideoSizeChanged(this, width, height, sar_num, sar_den)
        }
    }

    override fun setOnErrorListener(listener: OnErrorListener) {
        ijkMediaPlayer.setOnErrorListener { mp, what, extra ->
            listener.onError(this, what, extra)
        }
    }

    override fun setOnInfoListener(listener: OnInfoListener) {
        ijkMediaPlayer.setOnInfoListener { mp, what, extra ->
            listener.onInfo(this, what, extra)
        }
    }

    override fun setOnPreparedListener(listener: OnPreparedListener) {
        ijkMediaPlayer.setOnPreparedListener {
            listener.onPrepared(this)
        }
    }

    override fun setOnBufferingUpdateListener(listener: OnBufferingUpdateListener) {
        ijkMediaPlayer.setOnBufferingUpdateListener { mp, percent ->
            listener.onBufferingUpdate(this, percent)
        }
    }

    override fun setOnSeekCompleteListener(listener: OnSeekCompleteListener) {
        ijkMediaPlayer.setOnSeekCompleteListener {
            listener.onSeekComplete(this)
        }
    }

    override fun setOnCompletionListener(listener: OnCompletionListener) {
        ijkMediaPlayer.setOnCompletionListener {
            listener.onCompletion(this)
        }
    }

    @Deprecated("ijkPlayer不支持")
    override fun setOnSubtitleDataListener(listener: OnSubtitleDataListener) {
    }

    @Deprecated("ijkPlayer不支持")
    override fun setOnDrmInfoListener(listener: OnDrmInfoListener) {
    }

    override fun setOnMediaCodecSelectListener(listener: OnMediaCodecSelectListener) {
        ijkMediaPlayer.setOnMediaCodecSelectListener { mp, mimeType, profile, level ->
            listener.onMediaCodecSelect(this, mimeType, profile, level)
        }
    }

    override fun setOnNativeInvokeListener(listener: OnNativeInvokeListener) {
        ijkMediaPlayer.setOnNativeInvokeListener { what, args ->
            listener.onNativeInvoke(this, what, args)
        }
    }

    override fun setOnControlMessageListener(listener: OnControlMessageListener) {
        ijkMediaPlayer.setOnControlMessageListener {
            listener.onControlResolveSegmentUrl(this, it)
        }
    }
}