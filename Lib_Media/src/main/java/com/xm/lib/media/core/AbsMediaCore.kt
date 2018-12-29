package com.xm.lib.media.core

import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import com.xm.lib.media.contract.XmMediaContract
import com.xm.lib.media.enum_.EnumMediaState

/**
 * 播放器引擎抽象接口
 */
abstract class AbsMediaCore {

    var model: XmMediaContract.Model? = null
    var view: XmMediaContract.View? = null

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
    var playerState: EnumMediaState? = null

    var mediaLifeState: MediaLifeState? = MediaLifeState.Idle

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
        mediaLifeState = MediaLifeState.Started
    }

    /**
     * 异步准备
     */
    abstract fun prepareAsync()

    /**
     * 暂停
     */
    open fun pause() {
        mediaLifeState = MediaLifeState.Paused
    }

    /**
     * 停止
     */
    open fun stop() {
        mediaLifeState = MediaLifeState.Stop
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
        mediaLifeState = MediaLifeState.End
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
        mediaLifeState = MediaLifeState.Idle
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
        mediaLifeState = MediaLifeState.Initialized
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
        mediaLifeState = MediaLifeState.Prepared
    }

    /**
     * 播放完成监听
     */
    open fun setOnCompletionListener(listener: OnCompletionListener) {
        mediaLifeState = MediaLifeState.PlaybackCompleted
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
        mediaLifeState = MediaLifeState.Error
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
     * 播放器的状态
     */
    /**
     * 播放生命周期
     */
    enum class MediaLifeState {
        /**
         * reset 空闲状态
         */
        Idle,

        /**
         * setDataSource 播放文件设置好了
         */
        Initialized,

        /**
         * prepareAsync方法触发OnPreparedListener.onPrepared监听进入资源准备状态
         */
        Prepared,

        /**
         * start 播放状态如果调用了seekTo还是处于播放
         */
        Started,

        /**
         * pause 暂停状态,调用start方可恢复播放
         */
        Paused,

        /**
         * Started或者Paused状态下均可调用stop(),要想重新播放，需要通过prepareAsync()和prepare()回到先前的Prepared状态重新开始才可以
         */
        Stop,

        /**
         * 播放完毕，而又没有设置循环播放的话就进入该状态,播放完成触发onCompletion , 此时可以调用start重新从头播放，stop()停止MediaPlayer，也可以seekTo()来重新定位播放位置
         */
        PlaybackCompleted,

        /**
         * 播放器播放错误
         */
        Error,

        /**
         * release 资源释放状态
         */
        End

    }

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

    /*----------------------------------------------------------------------------------
     * 抽象类监听播放器所有的监听事件
     */
    @Deprecated("不再使用了")
    abstract class OnMediaCoreLisenter {

        /*-------------
         * 播放器画布相关回调
         */
        /**
         * 画布改变
         */
        abstract fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int)

        /**
         * 画布销毁
         */
        abstract fun surfaceDestroyed(holder: SurfaceHolder?)

        /**
         * 画布创建
         */
        abstract fun surfaceCreated(holder: SurfaceHolder?)

        /*-------------
         * 播放器相关回调
         */
        /**
         * 就绪
         */
        abstract fun onPrepared(mp: AbsMediaCore)

        /**
         * 播放完成
         */
        abstract fun onCompletion(mp: AbsMediaCore)

        /**
         * 进度加载 https://github.com/Bilibili/ijkplayer/issues/1137
         */
        abstract fun onBufferingUpdate(mp: AbsMediaCore, percent: Int)

        /**
         * 滑动进度完成,todo 但是不是很准确,在断网情况下也会调用,onInfo稍微准确一点
         */
        abstract fun onSeekComplete(mp: AbsMediaCore)

        /**
         * 播放组件布局改变
         */
        abstract fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int)

        /**
         * 错误播放 https://juejin.im/post/5bc7e689f265da0adc18fb7a
         */
        abstract fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean

        /**
         * 播放信息
         * int MEDIA_INFO_VIDEO_RENDERING_START = 3;//视频准备渲染
         * int MEDIA_INFO_BUFFERING_START = 701;//开始缓冲
         * int MEDIA_INFO_BUFFERING_END = 702;//缓冲结束
         * int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;//视频选择信息
         * int MEDIA_ERROR_SERVER_DIED = 100;//视频中断,一般是视频源异常或者不支持的视频类型。
         * int MEDIA_ERROR_IJK_PLAYER = -10000,//一般是视频源有问题或者数据格式不支持,比如音频不是AAC之类的
         * int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;//数据错误没有有效的回收
         */
        abstract fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean

        /**
         * 定时文本显示
         */
        abstract fun onTimedText(mp: AbsMediaCore/*, text: IjkTimedText*/)
    }
}