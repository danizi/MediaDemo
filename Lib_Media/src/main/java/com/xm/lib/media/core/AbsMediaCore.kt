package com.xm.lib.media.core

import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import com.xm.lib.media.core.constant.Constant

/**
 * 播放器引擎抽象接口
 */
abstract class AbsMediaCore {

    /**
     * 上下文对象
     */
    var context: Context? = null

    /**
     * 画布
     */
    var mSurfaceView: SurfaceView? = null

    /**
     * 装载画布容器
     */
    var tagerView: ViewGroup? = null

    /**
     * 播放器相关的回调
     */
    var absMediaCoreOnLisenter: AbsMediaCoreOnLisenter? = null

    /**
     * 播放器当前状态
     */
    //var playerState: EnumMediaState? = null

    var mediaLifeState: Constant.MediaLifeState? = Constant.MediaLifeState.Idle

    fun setOnLisenter(absMediaCoreOnLisenter: AbsMediaCoreOnLisenter) {
        this.absMediaCoreOnLisenter = absMediaCoreOnLisenter
    }

    /*----------------------------------------------------------------------------------
     * 普通方法
     */
    /**
     * 初始化
     */
    abstract fun init()

    /**
     * 开始
     */
    open fun start() {
        mediaLifeState = Constant.MediaLifeState.Started
    }

    /**
     * 异步准备
     */
    abstract fun prepareAsync()

    /**
     * 暂停
     */
    open fun pause() {
        mediaLifeState = Constant.MediaLifeState.Paused
    }

    /**
     * 停止
     */
    open fun stop() {
        mediaLifeState = Constant.MediaLifeState.Stop
    }

    /**
     * 总时间
     */
    abstract fun getDuration(): Long?

    /**
     * 当前进度
     */
    abstract fun getCurrentPosition(): Long?

    /**
     * 设置进度
     */
    abstract fun seekTo(msec: Long?)

    /**
     * 释放资源
     */
    open fun release() {
        mediaLifeState = Constant.MediaLifeState.End
    }

    /**
     * 重新播放
     */
    @Deprecated("不需要")
    abstract fun rePlay()

    /**
     * 重置状态
     */
    open fun reset() {
        mediaLifeState = Constant.MediaLifeState.Idle
    }

    /**
     * 高
     */
    abstract fun getVideoHeight(): Int?

    /**
     * 宽
     */
    abstract fun getVideoWidth(): Int?

    /**
     * 流媒体类型 音频 视频
     */
    abstract fun setAudioStreamType()

    /**
     * 设置音量&声道
     */
    abstract fun setVolume()

    /**
     * 设置播放进度
     */
    abstract fun setSpeed(speed: Float)

    /**
     * 获取播放速度
     */
    abstract fun getSpeed(): Float?

    /**
     * 设置SurfaceHolder显示多媒体
     */
    abstract fun setDisplay(sh: SurfaceHolder?)

    /**
     * 设置资源
     */
    open fun setDataSource(path: String) {
        mediaLifeState = Constant.MediaLifeState.Initialized
    }

    /**
     * 获取当前网速
     */
    abstract fun getTcpSpeed(): Long?

    /*----------------------------------------------------------------------------------
     * 设置监听方法
     */
    /**
     * 资源准备完成监听
     */
    open fun setOnPreparedListener(listener: OnPreparedListener) {
        mediaLifeState = Constant.MediaLifeState.Prepared
    }

    /**
     * 播放完成监听
     */
    open fun setOnCompletionListener(listener: OnCompletionListener) {
        mediaLifeState = Constant.MediaLifeState.PlaybackCompleted
    }

    /**
     * 资源更新进度监听
     */
    abstract fun setOnBufferingUpdateListener(listener: OnBufferingUpdateListener)

    /**
     * 设置进度完成监听
     */
    abstract fun setOnSeekCompleteListener(listener: OnSeekCompleteListener)

    /**
     * 播放器窗口变化监听
     */
    abstract fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener)

    /**
     * 播放错误监听
     */
    open fun setOnErrorListener(listener: OnErrorListener) {
        mediaLifeState = Constant.MediaLifeState.Error
    }

    /**
     * 播放信息
     */
    abstract fun setOnInfoListener(listener: OnInfoListener)

    /**
     * 定时显示文件监听
     */
    abstract fun setOnTimedTextListener(listener: OnTimedTextListener)

    /*----------------------------------------------------------------------------------
     * 监听接口类
     */
    /**
     * 播放资源播准备完成监听
     */
    interface OnPreparedListener {
        fun onPrepared(mp: AbsMediaCore)
    }

    /**
     * 视频播放完成监听
     */
    interface OnCompletionListener {
        fun onCompletion(mp: AbsMediaCore)
    }

    /**
     * 视频缓存进度监听
     */
    interface OnBufferingUpdateListener {
        fun onBufferingUpdate(mp: AbsMediaCore, percent: Int)
    }

    /**
     * 滑动进度完成监听todo 但是不是很准确,在断网情况下也会调用,onInfo稍微准确一点
     */
    interface OnSeekCompleteListener {
        fun onSeekComplete(mp: AbsMediaCore)
    }

    /**
     * 视频控件大小改变监听
     */
    interface OnVideoSizeChangedListener {
        fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int,
                               sar_num: Int, sar_den: Int)
    }

    /**
     * 错误播放监听 https://juejin.im/post/5bc7e689f265da0adc18fb7a
     */
    interface OnErrorListener {
        fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean
    }

    /**
     * 播放信息监听
     * int MEDIA_INFO_VIDEO_RENDERING_START = 3;//视频准备渲染
     * int MEDIA_INFO_BUFFERING_START = 701;//开始缓冲
     * int MEDIA_INFO_BUFFERING_END = 702;//缓冲结束
     * int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;//视频选择信息
     * int MEDIA_ERROR_SERVER_DIED = 100;//视频中断,一般是视频源异常或者不支持的视频类型。
     * int MEDIA_ERROR_IJK_PLAYER = -10000,//一般是视频源有问题或者数据格式不支持,比如音频不是AAC之类的
     * int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;//数据错误没有有效的回收
     */
    interface OnInfoListener {
        fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean
    }

    /**
     * 定时文本显示监听
     */
    interface OnTimedTextListener {
        fun onTimedText(mp: AbsMediaCore, text: String)
    }

}