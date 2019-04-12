package common.xm.com.xmcommon.media2.base

/**
 * 问题记录
 * 1 播放视频出现了mediaPlayer what:1 extra:-2147483648 错误，换了一个系统低版本的手机就可以播放了 播放地址：http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4
 */
interface XmMediaPlayer {

    companion object {
        const val TAG = "XmMediaPlayer"
    }

    fun getDuration(): Long
    fun getCurrentPosition(): Long
    fun getVideoHeight(): Long
    fun getVideoWidth(): Long
    fun isPlaying(): Boolean
    fun start()
    fun stop()
    fun pause()
    fun prepare()
    fun prepareAsync()
    fun release()
    fun reset()
    fun seekTo()
    fun setDataSource()
    fun setDisplay()
    fun setLooping()
    fun isLooping()
    fun setOnCompletionListener()
    fun setOnErrorListener()
    fun setOnPreparedListener()
    fun setOnBufferingUpdateListener()
    fun setOnSeekCompleteListener()
}