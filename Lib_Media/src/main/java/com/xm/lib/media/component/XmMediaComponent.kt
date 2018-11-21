package com.xm.lib.media.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.EnumViewType
import com.xm.lib.media.contract.XmMediaContract
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

    override fun addViewToMedia(enumViewType: EnumViewType, viewGroup: ViewGroup): XmMediaComponent {
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

    override fun update(o: MediaViewObservable, vararg arg: Any) {
        present?.update(o, arg)
    }

}