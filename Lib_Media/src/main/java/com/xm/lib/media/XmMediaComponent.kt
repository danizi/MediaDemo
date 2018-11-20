package com.xm.lib.media

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout


/**
 * 播放器组件
 */
class XmMediaComponent(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), XmMediaContract.View {

    private var present: XmMediaContract.Present? = null

    init {
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
}