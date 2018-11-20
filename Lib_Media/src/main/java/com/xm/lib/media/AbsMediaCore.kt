package com.xm.lib.media

import android.content.Context
import android.view.SurfaceView
import android.view.ViewGroup

enum class EnumMediaState {
    PLAYING,
    PAUSE,
    STOP,
    ERROR
}

interface IMediaCore {
    fun init()
    fun start()
    fun pause()
    fun stop()
    fun getDuration(): Long
    fun getCurrentPosition(): Long
    fun seekTo(msec: Long)
    fun release()
}

abstract class AbsMediaCore : IMediaCore {
    var context: Context? = null
    var model: XmMediaContract.Model? = null
    var view: XmMediaContract.View? = null
    var mSurfaceView: SurfaceView? = null
    var tagerView: ViewGroup? = null
}