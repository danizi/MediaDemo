package com.xm.lib.media.contract.control

import android.app.Activity
import android.content.Context
import com.xm.lib.media.component.control.MediaHControlView
import com.xm.lib.media.contract.base.ControlViewContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable

class MediaHControlViewContract {
    interface View : ControlViewContract.View<MediaHControlView>

    class Model : ControlViewContract.Model() {}

    class Present(context: Context?, val view: View?) : ControlViewContract.Present<MediaHControlViewContract.Model>(context,Model()) {

        override fun process() {

        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            handleMediaEvent(o, event, view?.getView()?.seekBar!!, view.getView().tvCurrentPosition, view.getView().tvDuration)
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            handleViewEvent(o, event, view?.getView()!!)
        }

        fun startOrPause() {
            startOrPause( view?.getView()?.imgPlayPause!!)
        }

        fun fullOrSmall() {
            fullOrSmall(context as Activity,view?.getView(),view?.getView())
        }


    }
}