package com.xm.lib.media.core

import android.content.Context
import android.view.SurfaceView
import android.view.ViewGroup
import com.xm.lib.media.contract.XmMediaContract
import com.xm.lib.media.imp.IMediaCore


abstract class AbsMediaCore : IMediaCore {
    var context: Context? = null
    var model: XmMediaContract.Model? = null
    var view: XmMediaContract.View? = null
    var mSurfaceView: SurfaceView? = null
    var tagerView: ViewGroup? = null
}