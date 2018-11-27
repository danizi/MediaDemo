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
        var model: Model? = Model()
        var view: View? = view

        override fun process() {

        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            val eventFrom = event?.parameter!![EventConstant.KEY_FROM]
            val eventMethod = event.parameter?.get(EventConstant.KEY_METHOD)
            if (eventMethod == EventConstant.VALUE_METHOD_ONPREPARED || eventFrom==EventConstant.VALUE_METHOD_ONSEEKCOMPLETE) {
                view?.hideView()
            }
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            val eventFrom = event?.parameter!![EventConstant.KEY_FROM]
            val eventMethod = event.parameter?.get(EventConstant.KEY_METHOD)

            if (eventFrom == EventConstant.VALUE_FROM_PREVIEW) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_CLICK -> { //点击了预览图或者预览图上面播放按钮
                        view?.showView()
                    }
                }
            } else if (eventFrom == EventConstant.VALUE_FROM_CONTROLVIEW) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_SEEKTO -> { //拖动了
                        view?.showView()
                    }
                }
            }
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {

        }
    }
}