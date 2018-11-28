package com.xm.lib.media

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumViewType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import com.xm.lib.media.watcher.Observer


/**
 * 播放器组件
 */
class XmMediaComponent(context: Context?, attrs: AttributeSet?) : MediaViewObservable<XmMediaContract.Present>(context!!, attrs), XmMediaContract.View, Observer {
    override fun getMedia(): AbsMediaCore {
        return getPresent()?.getMediaCore()!!
    }

    override fun showView() {
        show()
    }

    override fun hideView() {
        hide()
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
        getPresent()?.setDisplay(dataSource!!)
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
        getPresent()?.prepareAsync()
    }

    override fun start() {
        getPresent()?.start()
    }

    override fun pause() {
        getPresent()?.pause()
    }

    override fun stop() {
        getPresent()?.stop()
    }

    override fun getDuration(): Long {
        return getPresent()?.getDuration()!!
    }

    override fun getCurrentPosition(): Long {
        return getPresent()?.getCurrentPosition()!!
    }

    override fun seekTo(msec: Long) {
        getPresent()?.seekTo(msec)
    }

    override fun release() {
        getPresent()?.release()
    }

    override fun update(o: MediaViewObservable<*>, event: Event) {
        getPresent()?.handleReceiveEvent(o, event)
    }

    override fun build() {
        getPresent()?.build()
    }

    @Deprecated("使用setup方法替代")
    override fun init() {
    }

    @Deprecated("暂不使用")
    override fun findViews() {
    }

    @Deprecated("暂不使用")
    override fun initListenner() {
    }

    @Deprecated("暂不使用")
    override fun initData() {
    }
}