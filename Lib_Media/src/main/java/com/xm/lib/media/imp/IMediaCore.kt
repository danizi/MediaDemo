package com.xm.lib.media.imp

interface IMediaCore {
    fun init()
    fun start()
    fun prepareAsync()
    fun pause()
    fun stop()
    fun getDuration(): Long
    fun getCurrentPosition(): Long
    fun seekTo(msec: Long)
    fun release()
}