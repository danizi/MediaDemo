package com.xm.lib.media.core

import android.view.SurfaceHolder

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
