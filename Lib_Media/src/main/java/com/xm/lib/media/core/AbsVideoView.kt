package com.xm.lib.media.core

import android.content.Context
import android.util.AttributeSet
import com.xm.lib.media.core.attach.factory.MediaAttachLayoutFactory

abstract class AbsVideoView(context: Context?, attrs: AttributeSet?) {
    /**
     * 是否正在播放
     */
    abstract fun isPlaying(): Boolean?

    /**
     * 是否暂停
     */
    abstract fun canPause(): Boolean?

    /**
     * 进度能够后退
     */
    abstract fun canSeekBackward(): Boolean?

    /**
     * 进度能否前进
     */
    abstract fun canSeekForward(): Boolean?

    /**
     * 总时长
     */
    abstract fun getCurrentPosition(): Long?

    /**
     * 暂停
     */
    abstract fun pause()

    /**
     * 恢复播放
     */
    abstract fun resume()

    /**
     * 设置进度
     */
    abstract fun seekTo(msec: Int)

    /**
     * 播放
     */
    abstract fun start()

    /**
     * 释放资源
     */
    abstract fun realse()

    /**
     * 设置播放资源
     */
    abstract fun setVideoPath(path: String?)

    /**
     * 添加控制器
     */
    abstract fun setAttachLayout(attachLayout: MediaAttachLayoutFactory.IAttachLayout?)

    /**
     * 播放完成监听
     */
    abstract fun setOnCompletionListener(l: AbsMediaCore.OnCompletionListener)

    /**
     * 播放错误监听
     */
    abstract fun setOnErrorListener(l: AbsMediaCore.OnErrorListener)

    /**
     * 播放信息监听
     */
    abstract fun setOnInfoListener(l: AbsMediaCore.OnInfoListener)

    /**
     * 播放资源完成监听
     */
    abstract fun setOnPreparedListener(l: AbsMediaCore.OnPreparedListener)
}