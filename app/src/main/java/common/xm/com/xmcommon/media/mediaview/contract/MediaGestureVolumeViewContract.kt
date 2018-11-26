package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.xm.lib.media.contract.BaseMediaContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.component.MediaGestureVolumeView

class MediaGestureVolumeViewContract {
    interface View : BaseMediaContract.View<MediaGestureVolumeView> {

    }

    class Model : BaseMediaContract.Model() {
        val volumeResID: Int? = R.mipmap.media_volume

    }

    class Present(context: Context, view: View) : BaseMediaContract.Present() {
        var context: Context? = context
        var view: View? = view
        var model: Model? = Model()

        override fun process() {
            view?.getView()?.iv?.setImageResource(model?.volumeResID!!)
        }

        override fun handleMediaEvent(o: MediaViewObservable<*>, event: Event) {

        }

        override fun handleViewEvent(o: MediaViewObservable<*>, event: Event) {
            if (event.parameter?.get(EventConstant.KEY_FROM) == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (event.parameter?.get(EventConstant.KEY_METHOD)) {
                    EventConstant.VALUE_METHOD_ONVOLUME -> {
                        view?.showView()
                    }
                    EventConstant.VALUE_METHOD_UP -> {
                        view?.hideView()
                    }
                }
            }
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>, event: Event) {

        }

    }
}