package com.xm.lib.media.imp

interface IMediaCore {
    /**
     * 播放器初始化
     */
    fun init()

    /**
     * 开始播放
     */
    fun start()

    /**
     * 异步资源准备
     */
    fun prepareAsync()

    /**
     * 暂停
     */
    fun pause()

    /**
     * 暂停
     */
    fun stop()

    /**
     * 获取视频总时长
     */
    fun getDuration(): Long

    /**
     * 获得当前播放时间
     */
    fun getCurrentPosition(): Long

    /**
     * 设置播放进度
     */
    fun seekTo(msec: Long)

    /**
     * 释放资源
     */
    fun release()

    /**
     * 重新播放
     */
    fun rePlay()
}