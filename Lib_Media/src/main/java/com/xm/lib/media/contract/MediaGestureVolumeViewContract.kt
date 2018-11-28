package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.R
import com.xm.lib.media.component.MediaGestureVolumeView
import com.xm.lib.media.contract.BaseGestureContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable

class MediaGestureVolumeViewContract {
    interface View : BaseGestureContract.View<MediaGestureVolumeView> {}

    class Model : BaseGestureContract.Model() {
        val volumeResID: Int? = R.mipmap.media_volume
    }

    class Present(context: Context?, view: View?) : BaseGestureContract.Present(context) {
        var view: View? = view
        var model: Model? = Model()

        override fun process() {
            view?.getView()?.iv?.setImageResource(model?.volumeResID!!)
        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (eventFrom == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_CORE -> {
                        model?.media = event?.parameter?.get("mp") as AbsMediaCore?
                    }
                }
            }
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (eventFrom == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_ONVOLUME -> {
                        view?.getView()?.progress?.progress = getVolumePresent(view?.getView()?.progress, event)
                        view?.showView()
                    }

                    EventConstant.VALUE_METHOD_UP -> {
                        view?.hideView()
                        resetCurProgress()
                    }
                }
            }
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {

        }
        private fun obtainMedia() {
            if (null == model?.media && media != null) {
                model?.media = media
            }
        }
        private fun obtainMediaView() {
            if (null == model?.mediaView && mediaView != null) {
                model?.mediaView = mediaView
            }
        }
    }
}