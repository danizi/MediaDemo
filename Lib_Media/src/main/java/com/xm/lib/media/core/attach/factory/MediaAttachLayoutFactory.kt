package com.xm.lib.media.core.attach.factory

import android.content.Context
import com.xm.lib.media.core.AbsAttachLayout
import com.xm.lib.media.core.attach.layout.AttachCompleteLayout
import com.xm.lib.media.core.attach.layout.AttachErrorLayout
import com.xm.lib.media.core.attach.layout.AttachGestureLayout
import com.xm.lib.media.core.attach.layout.AttachLoadingLayout
import com.xm.lib.media.core.constant.Constant

/**
 * 工厂
 */
class MediaAttachLayoutFactory {
    companion object {
        fun create(mediaView: Constant.EnumMediaView, context: Context?, layout: Int): AbsAttachLayout? {
            when {
                mediaView === Constant.EnumMediaView.PRE -> return AttachLoadingLayout(context!!, layout)
                mediaView === Constant.EnumMediaView.LOADING -> return AttachLoadingLayout(context!!, layout)
                mediaView === Constant.EnumMediaView.GESTURE -> return AttachGestureLayout(context!!, layout)
                mediaView === Constant.EnumMediaView.COMPLETE -> return AttachCompleteLayout(context!!, layout)
                mediaView === Constant.EnumMediaView.ERROR -> return AttachErrorLayout(context!!, layout)
            }
            return null
        }
    }
}

