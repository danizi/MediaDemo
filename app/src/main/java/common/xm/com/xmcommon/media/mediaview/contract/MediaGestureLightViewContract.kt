package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import android.os.Build
import android.util.Log
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.contract.BaseGestureContract
import com.xm.lib.media.contract.BaseMediaContract
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.help.LightHelper
import com.xm.lib.media.help.VolumeHelper
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.component.MediaGestureLightView

class MediaGestureLightViewContract {
    interface View : BaseGestureContract.View<MediaGestureLightView> {}

    class Model : BaseGestureContract.Model() {
        var lightResID: Int = R.mipmap.media_light
    }

    class Present(context: Context?, view: View?) : BaseGestureContract.Present(context) {
        var view: View? = view
        var model: Model? = Model()

        override fun process() {
            view?.getView()?.iv?.setImageResource(model?.lightResID!!)
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
                    EventConstant.VALUE_METHOD_ONLIGHT -> {
                        view?.getView()?.progress?.progress = getLightPresent(view?.getView()?.progress,event)
                        view?.showView()
                    }

                    EventConstant.VALUE_METHOD_UP -> {
                        view?.hideView()
                        resetCurProgress()
                    }
                }
            }


        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {}
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