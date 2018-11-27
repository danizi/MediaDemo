package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.xm.lib.media.contract.BaseMediaContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.component.MediaLoadingView

class MediaLoadingViewContract {

    interface View : BaseMediaContract.View<MediaLoadingView>

    class Model : BaseMediaContract.Model()

    class Present(context: Context?, view: View?) : BaseMediaContract.Present() {


        var context: Context = context!!
        var model: Model? = null
        var view: View? = view

        init {
            model = Model()
        }

        override fun process() {

        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (EventConstant.VALUE_METHOD_ONPREPARED == (event?.parameter?.get(EventConstant.KEY_METHOD))) {
                view?.hideView()
            }
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (event?.parameter!![EventConstant.KEY_FROM] == EventConstant.VALUE_FROM_PREVIEW && EventConstant.VALUE_METHOD_CLICK == event.parameter?.get(EventConstant.KEY_METHOD)) {
                view?.showView()
            }
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {

        }
    }
}