package com.xm.lib.media

import android.content.Context
import android.view.SurfaceView
import android.view.ViewGroup
import com.xm.lib.media.imp.IMediaCore

/**
 * 播放器引擎抽象
 */
abstract class AbsMediaCore : IMediaCore{
    var model: XmMediaContract.Model? = null
    var view: XmMediaContract.View? = null
    var context: Context? = null
    var mSurfaceView: SurfaceView? = null
    var tagerView: ViewGroup? = null
    var absMediaCoreOnLisenter: AbsMediaCoreOnLisenter? = null

    /**
     * 设置监听
     */
    fun setOnLisenter(absMediaCoreOnLisenter: AbsMediaCoreOnLisenter) {
        this.absMediaCoreOnLisenter = absMediaCoreOnLisenter
    }
}