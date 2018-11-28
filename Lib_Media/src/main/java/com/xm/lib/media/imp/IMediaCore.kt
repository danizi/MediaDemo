package com.xm.lib.media.imp

interface IMediaCore {
    /**
     * 播放器初始化
     */
    fun init()

    /**
     * 播放&恢复播放
     */
    fun start()

    /**
     * 播放资源准备
     */
    fun prepareAsync()

    /**
     * 暂停
     */
    fun pause()

    /**
     * 停止
     */
    fun stop()

    /**
     * 获取总时长 单位毫秒
     */
    fun getDuration(): Long

    /**
     * 获取播放位置 单位毫秒
     */
    fun getCurrentPosition(): Long

    /**
     * 设置播放位置
     */
    fun seekTo(msec: Long)

    /**
     * 释放资源
     */
    fun release()
}