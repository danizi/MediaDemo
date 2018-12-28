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
    abstract fun start()

    /**
     * 异步准备
     */
    abstract fun prepareAsync()

    /**
     * 暂停
     */
    abstract fun pause()

    /**
     * 停止
     */
    abstract fun stop()

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
    abstract fun release()

    /**
     * 重新播放
     */
    abstract fun rePlay()

    /**
     * 重置状态
     */
    abstract fun reset()

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
     * 设置SurfaceHolder显示多媒体
     */
    abstract fun setDisplay(sh: SurfaceHolder?)

    /**
     * 设置资源
     */
    abstract fun setDataSource(path: String)

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
    abstract fun setOnPreparedListener(listener: OnPreparedListener)

    /**
     * 播放完成监听
     */
    abstract fun setOnCompletionListener(listener: OnCompletionListener)

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
    abstract fun setOnErrorListener(listener: OnErrorListener)

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
    interface OnPreparedListener {
        fun onPrepared(mp: AbsMediaCore)
    }

    interface OnCompletionListener {
        fun onCompletion(mp: AbsMediaCore)
    }

    interface OnBufferingUpdateListener {
        fun onBufferingUpdate(mp: AbsMediaCore, percent: Int)
    }

    interface OnSeekCompleteListener {
        fun onSeekComplete(mp: AbsMediaCore)
    }

    interface OnVideoSizeChangedListener {
        fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int,
                               sar_num: Int, sar_den: Int)
    }

    interface OnErrorListener {
        fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean
    }

    interface OnInfoListener {
        fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean
    }

    interface OnTimedTextListener {
        fun onTimedText(mp: AbsMediaCore, text: String)
    }

    /*----------------------------------------------------------------------------------
     * 抽象类监听播放器所有的监听事件
     */
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
         * 滑动进度完成,todo 但是不是很准确,onInfo稍微准确一点
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