package com.xm.lib.media.lisenter

import android.view.SurfaceHolder
import com.xm.lib.media.AbsMediaCore

abstract class AbsMediaCoreOnLisenter {

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
     */
    abstract fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean

    /**
     * 定时文本显示
     */
    abstract fun onTimedText(mp: AbsMediaCore/*, text: IjkTimedText*/)
}
