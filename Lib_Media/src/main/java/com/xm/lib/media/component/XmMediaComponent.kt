package com.xm.lib.media.component

import android.content.Context
import android.util.AttributeSet
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.getCurrentPosition
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.getDuration
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.getPlayState
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.pause
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.prepareAsync
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.release
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.replay
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.seekTo
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.start
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.stop
import com.xm.lib.media.contract.XmMediaContract
import com.xm.lib.media.enum_.EnumViewType
import com.xm.lib.media.watcher.MediaViewObservable


/**
 * 播放器组件
 */
class XmMediaComponent(context: Context?, attrs: AttributeSet?) : MediaViewObservable<XmMediaContract.Present>(context!!, attrs), XmMediaContract.View {
    /**
     * 播放的功能
     */
    class Action {
        companion object {
            val setDisplay = "setDisplay"
            val prepareAsync = "prepareAsync"
            val start = "start"
            val pause = "pause"
            val stop = "stop"
            val getDuration = "getDuration"
            val getCurrentPosition = "getCurrentPosition"
            val seekTo = "seekTo"
            val release = "release"
            val replay = "replay"
            val getPlayState = "getPlayState"
        }
    }

    override fun getView(): XmMediaComponent {
        return this
    }

    override fun createPresent(): XmMediaContract.Present {
        return XmMediaContract.Present(context, this@XmMediaComponent)
    }

    override fun addViewToMedia(enumViewType: EnumViewType?, viewGroup: MediaViewObservable<*>?): XmMediaComponent {
        getPresent()?.addViewToMedia(enumViewType!!, viewGroup!!)
        return this
    }

    override fun core(absMediaCore: AbsMediaCore?): XmMediaComponent {
        getPresent()?.core(absMediaCore!!)
        return this
    }

    @Synchronized
    override fun setDisplay(dataSource: String?): XmMediaComponent {
        getPresent()?.action(Action.setDisplay, dataSource)
        return this
    }

    @Synchronized
    override fun action(action: String?, vararg params: Any?): Any? {
        var result: Any? = Any()
        when (action) {
            release -> {
                getPresent()?.action(action)
            }
            seekTo -> {
                getPresent()?.action(action, params[0] as Long)
            }
            getDuration -> {
                result = getPresent()?.action(action)
            }
            getCurrentPosition -> {
                result = getPresent()?.action(action)
            }
            stop -> {
                getPresent()?.action(action)
            }
            pause -> {
                getPresent()?.action(action)
            }
            start -> {
                getPresent()?.action(action)
            }
            prepareAsync -> {
                getPresent()?.action(action)
            }
            replay -> {
                getPresent()?.action(action)
            }
            getPlayState -> {
                result = getPresent()?.action(action)
            }
        }
        return result
    }

    override fun build() {
        getPresent()?.build()
    }
}