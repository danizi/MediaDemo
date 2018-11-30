package com.xm.lib.media.imp

interface IMediaCore {
    fun init()
    fun start()//播放&恢复播放
    fun prepareAsync()
    fun pause()
    fun stop()
    fun getDuration(): Long
    fun getCurrentPosition(): Long
    fun seekTo(msec: Long)
    fun release()
}