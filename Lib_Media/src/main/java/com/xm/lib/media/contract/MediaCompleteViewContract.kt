package com.xm.lib.media.contract

import android.content.Context
import com.xm.lib.media.component.MediaCompleteView
import com.xm.lib.media.contract.base.BaseMediaContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable

class MediaCompleteViewContract {
    interface View : BaseMediaContract.View<MediaCompleteView>

    class Model : BaseMediaContract.Model()

    class Present(context: Context?, view: View?) : BaseMediaContract.Present(context) {
        val model: Model? = null

        override fun process() {
        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {
        }

    }
}