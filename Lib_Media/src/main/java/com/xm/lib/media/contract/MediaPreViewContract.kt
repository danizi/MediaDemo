package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.bumptech.glide.Glide
import com.xm.lib.media.component.MediaPreView
import com.xm.lib.media.contract.base.BaseMediaContract
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable

class MediaPreViewContract {

    interface View : BaseMediaContract.View<MediaPreView>

    class Model : BaseMediaContract.Model() {
        var preUrl: String? = null
    }

    class Present(context: Context?, view: View?) : BaseMediaContract.Present(context) {
        var view: View? = view
        var model: Model? = Model()

        override fun process() {
            Glide.with(context).load(model?.preUrl).into(view?.getView()?.ivPre)
            view?.getView()?.show()
        }

        fun prepareAsync() {
            mediaView?.prepareAsync()
        }

        fun notifyObservers() {
            val event = Event()
                    .setEventType(EnumMediaEventType.VIEW)
                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_PREVIEW)
                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_CLICK)
            view?.getView()?.notifyObservers(event)
        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (EventConstant.VALUE_METHOD_ONPREPARED == event?.parameter?.get(EventConstant.KEY_METHOD)) {
                view?.getView()?.hide()
            }
        }
    }
}