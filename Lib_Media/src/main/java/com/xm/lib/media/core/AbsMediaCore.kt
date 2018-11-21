package com.xm.lib.media.core

import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import com.xm.lib.media.contract.XmMediaContract
import com.xm.lib.media.imp.IMediaCore

abstract class MediaCoreOnLisenter {

    /*-------------
     * 播放器画布相关回调
     */
    abstract fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int)

    abstract fun surfaceDestroyed(holder: SurfaceHolder?)

    abstract fun surfaceCreated(holder: SurfaceHolder?)

    /*-------------
     * 播放器相关回调
     */
    abstract fun onPrepared(mp: AbsMediaCore)

    abstract fun onCompletion(mp: AbsMediaCore)

    abstract fun onBufferingUpdate(mp: AbsMediaCore, percent: Int)

    abstract fun onSeekComplete(mp: AbsMediaCore)

    abstract fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int)

    abstract fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean

    abstract fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean

    abstract fun onTimedText(mp: AbsMediaCore/*, text: IjkTimedText*/)
}

abstract class AbsMediaCore : IMediaCore {
    var model: XmMediaContract.Model? = null
    var view: XmMediaContract.View? = null
    var context: Context? = null
    var mSurfaceView: SurfaceView? = null
    var tagerView: ViewGroup? = null
    var mediaCoreOnLisenter: MediaCoreOnLisenter? = null

    /**
     * 设置监听
     */
    fun setOnLisenter(mediaCoreOnLisenter: MediaCoreOnLisenter) {
        this.mediaCoreOnLisenter = mediaCoreOnLisenter
    }
}