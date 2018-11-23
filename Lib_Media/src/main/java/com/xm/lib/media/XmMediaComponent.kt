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
class XmMediaComponent(context: Context, attrs: AttributeSet?) : MediaViewObservable(context, attrs), XmMediaContract.View, Observer {

    private var present: XmMediaContract.Present? = null

    init {
        setBackgroundColor(Color.BLACK)
        present = XmMediaContract.Present(context, this@XmMediaComponent)
    }

    override fun mediaComponent(): XmMediaComponent {
        return this
    }

    override fun addViewToMedia(enumViewType: EnumViewType, viewGroup: MediaViewObservable): XmMediaComponent {
        present?.addViewToMedia(enumViewType, viewGroup)
        return this
    }

    override fun setDisplay(dataSource: String): XmMediaComponent {
        present?.setDisplay(dataSource)
        return this
    }

    override fun core(absMediaCore: AbsMediaCore): XmMediaComponent {
        present?.core(absMediaCore)
        return this
    }

    override fun setup(): XmMediaComponent {
        present?.setup()
        return this
    }

    @Deprecated("使用setup方法替代")
    override fun init() {

    }

    override fun prepareAsync() {
        present?.prepareAsync()
    }

    override fun start() {
        present?.start()
    }

    override fun pause() {
        present?.pause()
    }

    override fun stop() {
        present?.stop()
    }

    override fun getDuration(): Long {
        return present?.getDuration()!!
    }

    override fun getCurrentPosition(): Long {
        return present?.getCurrentPosition()!!
    }

    override fun seekTo(msec: Long) {
        present?.seekTo(msec)
    }

    override fun release() {
        present?.release()
    }

    override fun update(o: MediaViewObservable, event: Event) {
        present?.update(o, event)
    }

    override fun build() {
        present?.build()
    }
}