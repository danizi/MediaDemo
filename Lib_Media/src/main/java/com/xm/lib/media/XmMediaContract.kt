package com.xm.lib.media

import android.content.Context
import android.view.ViewGroup
import java.util.*

class XmMediaContract {

    interface View : IMediaCore {
        fun addViewToMedia(enumViewType: EnumViewType, viewGroup: ViewGroup): XmMediaComponent
        fun mediaComponent(): XmMediaComponent
        fun setDisplay(url: String): XmMediaComponent
        fun setup(): XmMediaComponent
        fun core(absMediaCore: AbsMediaCore): XmMediaComponent
    }

    class Model {
        var dataSource: String? = null
        var addViewMap: HashMap<EnumViewType, ViewGroup>? = HashMap()
    }

    class Present(val context: Context, val view: View) {


        var model: Model? = null
        var player: AbsMediaCore? = null

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
    }
}