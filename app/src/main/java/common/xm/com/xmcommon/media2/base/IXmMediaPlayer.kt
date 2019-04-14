package common.xm.com.xmcommon.media2.base

import android.drm.DrmInfo
import android.media.SubtitleData
import android.os.Bundle
import android.view.SurfaceHolder

interface IXmMediaPlayer {

    companion object {
        const val TAG = "IXmMediaPlayer"
    }

    fun getDuration(): Long
    fun getCurrentPosition(): Long
    fun getVideoHeight(): Long
    fun getVideoWidth(): Long
    fun isPlaying(): Boolean
    fun isLooping(): Boolean
    fun setLooping()

    fun start()
    fun stop()
    fun pause()
    fun prepare()
    fun prepareAsync()
    fun release()
    fun reset()
    fun seekTo(msec: Int)
    fun setDataSource(path: String?)
    fun setDisplay(sh: SurfaceHolder?)

    /*-----------------------------
     * 监听
     */
    fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener)
    fun setOnErrorListener(listener: OnErrorListener)
    fun setOnInfoListener(listener: OnInfoListener)
    fun setOnPreparedListener(listener: OnPreparedListener)
    fun setOnBufferingUpdateListener(listener: OnBufferingUpdateListener)
    fun setOnSeekCompleteListener(listener: OnSeekCompleteListener)
    fun setOnCompletionListener(listener: OnCompletionListener)

    /*-----------------------------
     * 原生播放器特有的监听
     */
    fun setOnSubtitleDataListener(listener: OnSubtitleDataListener)

    fun setOnDrmInfoListener(listener: OnDrmInfoListener)

    /*-----------------------------
     * IJKPlayer播放器特有监听
     */
    fun setOnMediaCodecSelectListener(listener: OnMediaCodecSelectListener)

    fun setOnNativeInvokeListener(listener: OnNativeInvokeListener)
    fun setOnControlMessageListener(listener: OnControlMessageListener)

}
interface OnVideoSizeChangedListener {
    fun onVideoSizeChanged(mp: IXmMediaPlayer, width: Int, height: Int)
}

interface OnErrorListener {
    fun onError(mp: IXmMediaPlayer, what: Int, extra: Int): Boolean
}

interface OnInfoListener {
    fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int): Boolean
}

interface OnPreparedListener {
    fun onPrepared(mp: IXmMediaPlayer)
}

interface OnBufferingUpdateListener {
    fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int)
}

interface OnSeekCompleteListener {
    fun onSeekComplete(mp: IXmMediaPlayer)
}

interface OnCompletionListener {
    fun onCompletion(mp: IXmMediaPlayer)
}

interface OnSubtitleDataListener {
    fun onSubtitleData(mp: IXmMediaPlayer, data: SubtitleData)
}

interface OnDrmInfoListener {
    fun onDrmInfo(mp: IXmMediaPlayer, drmInfo: DrmInfo)
}

interface OnMediaCodecSelectListener {
    fun onMediaCodecSelect(mp: IXmMediaPlayer, mimeType: String, profile: Int, level: Int): String
}

interface OnNativeInvokeListener {
    fun onNativeInvoke(mp: IXmMediaPlayer, what: Int, args: Bundle): Boolean
}

interface OnControlMessageListener {
    fun onControlResolveSegmentUrl(mp: IXmMediaPlayer, segment: Int): String
}




