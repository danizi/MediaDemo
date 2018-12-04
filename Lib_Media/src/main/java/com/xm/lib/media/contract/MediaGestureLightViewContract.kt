package com.xm.lib.media.contract

import android.content.Context
import com.xm.lib.media.R
import com.xm.lib.media.component.MediaGestureLightView
import com.xm.lib.media.contract.base.GestureContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable


class MediaGestureLightViewContract {
    interface View : GestureContract.View<MediaGestureLightView> {}

    class Model : GestureContract.Model() {
        var lightResID: Int = R.mipmap.media_light
    }

    class Present(context: Context?, view: View?) : GestureContract.Present(context) {
        var view: View? = view
        var model: Model? = Model()

        override fun process() {
            view?.getView()?.iv?.setImageResource(model?.lightResID!!)
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (eventFrom == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_ONLIGHT -> {
                        view?.getView()?.progress?.progress = getLightPresent(event)
                        view?.getView()?.show()
                    }

                    EventConstant.VALUE_METHOD_UP -> {
                        view?.getView()?.hide()
                        resetCurProgress()
                    }
                }
            }
        }
    }
}