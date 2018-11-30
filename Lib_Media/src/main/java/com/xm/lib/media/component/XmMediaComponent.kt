package com.xm.lib.media.component

import android.content.Context
import android.util.AttributeSet
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.contract.XmMediaContract
import com.xm.lib.media.enum_.EnumMediaState
import com.xm.lib.media.enum_.EnumViewType
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable
import com.xm.lib.media.watcher.Observer


/**
 * 播放器组件
 */
class XmMediaComponent(context: Context?, attrs: AttributeSet?) : MediaViewObservable<XmMediaContract.Present>(context!!, attrs), XmMediaContract.View, Observer {
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

    override fun setDisplay(dataSource: String?): XmMediaComponent {
        getPresent()?.action(Action.setDisplay, dataSource)
        return this
    }

    override fun core(absMediaCore: AbsMediaCore?): XmMediaComponent {
        getPresent()?.core(absMediaCore!!)
        return this
    }

    override fun setup(): XmMediaComponent {
        getPresent()?.setup()
        return this
    }

    override fun prepareAsync() {
        getPresent()?.action(Action.prepareAsync)
    }

    override fun start() {
        getPresent()?.action(Action.start)
    }

    override fun pause() {
        getPresent()?.action(Action.pause)
    }

    override fun stop() {
        getPresent()?.action(Action.stop)
    }

    override fun getDuration(): Long {
        return getPresent()?.action(Action.getDuration) as Long
    }

    override fun getCurrentPosition(): Long {
        return getPresent()?.action(Action.getCurrentPosition) as Long
    }

    override fun seekTo(msec: Long) {
        getPresent()?.action(Action.seekTo, msec)
    }

    override fun release() {
        getPresent()?.action(Action.release)
    }

    override fun build() {
        getPresent()?.build()
    }

    @Deprecated("使用setup方法替代")
    override fun init() {
    }

    fun getPlayState():EnumMediaState {
        return getPresent()?.getPlayState()!!
    }
}