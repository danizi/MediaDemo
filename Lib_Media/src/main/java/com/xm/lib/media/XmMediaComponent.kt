package com.xm.lib.media

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout


/**
 * 播放器组件
 */
class XmMediaComponent(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), XmMediaContract.View {


    var present: XmMediaContract.Present? = null

    init {
        present = XmMediaContract.Present(context, this@XmMediaComponent)
    }

    override fun mediaComponent(): XmMediaComponent {
        return this
    }

    override fun addViewToMedia(enumViewType: EnumViewType, viewGroup: ViewGroup): XmMediaComponent {
        present?.model?.addViewMap?.put(enumViewType, viewGroup)
        return this
    }

    override fun setDisplay(dataSource: String): XmMediaComponent {
        present?.model?.dataSource = dataSource
        return this
    }

    override fun core(absMediaCore: AbsMediaCore): XmMediaComponent {
        present?.core(absMediaCore)
        return this
    }

    override fun setup(): XmMediaComponent {
        present?.player?.init()
        return this
    }

    @Deprecated("使用setup方法替代")
    override fun init() {

    }

    override fun start() {
        present?.player?.start()
    }

    override fun pause() {
        present?.player?.pause()
    }

    override fun stop() {
        present?.player?.stop()
    }

    override fun getDuration(): Long {
        return present?.player?.getDuration()!!
    }

    override fun getCurrentPosition(): Long {
        return present?.player?.getCurrentPosition()!!
    }

    override fun seekTo(msec: Long) {
        present?.player?.seekTo(msec)
    }

    override fun release() {
        present?.player?.release()
    }
}