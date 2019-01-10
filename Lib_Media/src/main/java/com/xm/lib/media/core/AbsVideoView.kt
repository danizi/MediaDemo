package com.xm.lib.media.core

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.xm.lib.media.core.attach.AbsAttachLayout
import com.xm.lib.media.core.listener.MediaListener

abstract class AbsVideoView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {
        init(context, attrs)
    }

    open fun init(context: Context?, attrs: AttributeSet?) {}

    open fun isPlaying(): Boolean? {
        //是否正在播放
        return false
    }

    open fun canPause(): Boolean? {
        //是否暂停
        return false
    }

    open fun canSeekBackward(): Boolean? {
        //进度能够后退
        return false
    }

    open fun canSeekForward(): Boolean? {
        //进度能否前进
        return false
    }

    open fun getCurrentPosition(): Long? {
        //总时长
        return 0
    }

    open fun pause() {
        //暂停
    }

    open fun resume() {
        //恢复播放
    }

    open fun seekTo(msec: Int) {
        //设置进度
    }

    open fun start() {
        //播放
    }

    open fun realse() {
        //释放资源
    }

    open fun setVideoPath(path: String?) {
        //设置播放资源
    }

    open fun setAttachLayout(attachLayout: AbsAttachLayout?) {
        //添加控制器
    }

    open fun setOnCompletionListener(l: MediaListener.OnCompletionListener) {
        //播放完成监听
    }

    open fun setOnErrorListener(l: MediaListener.OnErrorListener) {
        //播放错误监听
    }

    open fun setOnInfoListener(l: MediaListener.OnInfoListener) {
        //播放信息监听
    }

    open fun setOnPreparedListener(l: MediaListener.OnPreparedListener) {
        //播放资源完成监听
    }
}