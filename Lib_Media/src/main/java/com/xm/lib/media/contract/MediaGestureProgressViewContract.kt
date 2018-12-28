package com.xm.lib.media.contract

import android.content.Context
import com.xm.lib.media.R
import com.xm.lib.media.component.MediaGestureProgressView
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.getDuration
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.seekTo
import com.xm.lib.media.contract.base.GestureContract
import com.xm.lib.media.enum_.EnumGestureState
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable

class MediaGestureProgressViewContract {
    interface View : GestureContract.View<MediaGestureProgressView>

    class Model : GestureContract.Model() {
        var fastBackwardResID: Int? = R.mipmap.media_fast_backward  //快退
        var fastForwardResID: Int? = R.mipmap.media_fast_forward    //快进
        var progressResID: Int? = fastForwardResID
    }

    class Present(context: Context?, val view: View?) : GestureContract.Present(context) {
        private var model: Model? = Model()

        override fun process() {
            view?.getView()?.iv?.setImageResource(model?.progressResID!!)
        }


        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (eventFrom == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (event?.parameter?.get(EventConstant.KEY_METHOD)) {

                    EventConstant.VALUE_METHOD_ONPROGRESS -> {
                        view?.getView()?.progress?.progress = getProgresPresent(event, mediaView)
                        view?.getView()?.show()
                    }

                    EventConstant.VALUE_METHOD_UP -> {
                        view?.getView()?.hide()
                        resetCurProgress()
                        if (event.parameter?.get(EventConstant.KEY_GESTURE_STATE) == EnumGestureState.PROGRESS) {
                            val duration: Long = mediaView?.action(getDuration) as Long
                            val progress: Float = view?.getView()?.progress?.progress!!.toFloat() / 100F
                            mediaView?.action(seekTo, (progress * duration).toLong())
                        }
                    }
                }
            }
        }
    }
}