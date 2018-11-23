package com.xm.lib.media.lisenter

import android.view.SurfaceHolder
import com.xm.lib.media.AbsMediaCore

abstract class AbsMediaCoreOnLisenter {

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
