package com.xm.lib.media

import android.content.Context
import android.view.ViewGroup
import java.util.*

class XmMediaContract {

    interface View : IMediaCore {
        fun addViewToMedia(enumViewType: EnumViewType, viewGroup: ViewGroup): XmMediaComponent
        fun mediaComponent(): XmMediaComponent
        fun setDisplay(dataSource: String): XmMediaComponent
        fun setup(): XmMediaComponent
        fun core(absMediaCore: AbsMediaCore): XmMediaComponent
    }

    class Model {
        var dataSource: String? = null
        var addViewMap: HashMap<EnumViewType, ViewGroup>? = HashMap()
    }

    class Present(val context: Context, val view: View) {


        private var model: Model? = null
        private var player: AbsMediaCore? = null

        init {
            model = Model()
        }

        fun core(absMediaCore: AbsMediaCore) {
            player = absMediaCore
            player?.model = model
            player?.view = view
            player?.context = context
            player?.tagerView = view.mediaComponent()
        }

        fun addViewToMedia(enumViewType: EnumViewType, viewGroup: ViewGroup) {
            model?.addViewMap?.put(enumViewType, viewGroup)
            view.mediaComponent().addView(viewGroup)
        }

        fun release() {
            player?.release()
        }

        fun seekTo(msec: Long) {
            player?.seekTo(msec)
        }

        fun getCurrentPosition(): Long {
            return player?.getCurrentPosition()!!
        }

        fun getDuration(): Long {
            return player?.getDuration()!!
        }

        fun stop() {
            player?.stop()
        }

        fun pause() {
            player?.pause()
        }

        fun start() {
            player?.start()
        }

        fun setup() {
            player?.init()
        }

        fun setDisplay(dataSource: String) {
            model?.dataSource = dataSource
        }
    }
}