package com.xm.lib.media.core.attach.factory

import android.content.Context
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.attach.AttachCompleteLayout
import com.xm.lib.media.core.attach.AttachErrorLayout
import com.xm.lib.media.core.attach.AttachGestureLayout
import com.xm.lib.media.core.attach.AttachLoadingLayout

/**
 * 工厂
 */
class MediaAttachLayoutFactory {
    companion object {
        fun create(mediaView: EnumMediaView, context: Context?, layout: Int): IAttachLayout? {
            when {
                mediaView === EnumMediaView.PRE -> return AttachLoadingLayout(context!!, layout)
                mediaView === EnumMediaView.LOADING -> return AttachLoadingLayout(context!!, layout)
                mediaView === EnumMediaView.GESTURE -> return AttachGestureLayout(context!!, layout)
                mediaView === EnumMediaView.COMPLETE -> return AttachCompleteLayout(context!!, layout)
                mediaView === EnumMediaView.ERROR -> return AttachErrorLayout(context!!, layout)
            }
            return null
        }
    }

    /**
     * 附着在播放上的View的类型
     */
    enum class EnumMediaView {
        PRE,
        LOADING,
        GESTURE,
        COMPLETE,
        ERROR
    }

    /**
     * 附着在播放上的View接口
     */
    interface IAttachLayout {
        fun setPlayer(player: AbsMediaCore?)
    }
}

