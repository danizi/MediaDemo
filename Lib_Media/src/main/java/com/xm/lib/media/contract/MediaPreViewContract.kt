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
        var model: Model? = null

        init {
            model = Model()
        }

        override fun process() {
            Glide.with(context).load(model?.preUrl).into(view?.getView()?.ivPre)
            view?.showView()
        }

        fun prepareAsync() {
            model?.media?.prepareAsync()
        }

        fun notifyObservers() {
            var event = Event()
                    .setEventType(EnumMediaEventType.VIEW)
                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_PREVIEW)
                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_CLICK)
            view?.getView()?.notifyObservers(event)
        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            obtainMedia()

            if (EventConstant.VALUE_METHOD_ONPREPARED == event?.parameter?.get(EventConstant.KEY_METHOD)) {
                view?.hideView()
            }

        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {

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