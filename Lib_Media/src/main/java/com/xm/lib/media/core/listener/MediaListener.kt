package com.xm.lib.media.core.listener

import com.xm.lib.media.core.AbsMediaCore

class MediaListener {
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
     * 滑动进度完成监听 todo 但是不是很准确,在断网情况下也会调用,onInfo稍微准确一点
     */
    interface OnSeekCompleteListener {
        fun onSeekComplete(mp: AbsMediaCore)
    }

    /**
     * 视频控件大小改变监听
     */
    interface OnVideoSizeChangedListener {
        fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int)
    }

    /**
     * 错误播放监听 https://juejin.im/post/5bc7e689f265da0adc18fb7a
     */
    interface OnErrorListener {
        fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean
    }

    /**
     * 播放信息监听
     * int MEDIA_INFO_VIDEO_RENDERING_START = 3;                //视频准备渲染
     * int MEDIA_INFO_BUFFERING_START = 701;                    //开始缓冲
     * int MEDIA_INFO_BUFFERING_END = 702;                      //缓冲结束
     * int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;           //视频选择信息
     * int MEDIA_ERROR_SERVER_DIED = 100;                       //视频中断,一般是视频源异常或者不支持的视频类型。
     * int MEDIA_ERROR_IJK_PLAYER = -10000,                     //一般是视频源有问题或者数据格式不支持,比如音频不是AAC之类的
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