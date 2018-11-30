package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.xm.lib.media.R
import com.xm.lib.media.component.MediaGestureVolumeView
import com.xm.lib.media.contract.base.GestureContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable

class MediaGestureVolumeViewContract {
    interface View : GestureContract.View<MediaGestureVolumeView>

    class Model : GestureContract.Model() {
        val volumeResID: Int? = R.mipmap.media_volume
    }

    class Present(context: Context?, view: View?) : GestureContract.Present(context) {
        var view: View? = view
        var model: Model? = Model()

        override fun process() {
            view?.getView()?.iv?.setImageResource(model?.volumeResID!!)
        }


        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (eventFrom == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_ONVOLUME -> {
                        view?.getView()?.progress?.progress = getVolumePresent(view?.getView()?.progress, event)
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