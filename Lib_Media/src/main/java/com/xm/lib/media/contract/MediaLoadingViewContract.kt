package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.xm.lib.media.component.MediaLoadingView
import com.xm.lib.media.contract.base.BaseMediaContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable

class MediaLoadingViewContract {

    interface View : BaseMediaContract.View<MediaLoadingView>

    class Model : BaseMediaContract.Model()

    class Present(context: Context?, var view: View?) : BaseMediaContract.Present(context) {
        var model: Model? = Model()

        override fun process() {

        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            val eventFrom = event?.parameter!![EventConstant.KEY_FROM]
            val eventMethod = event.parameter?.get(EventConstant.KEY_METHOD)
            if (eventMethod == EventConstant.VALUE_METHOD_ONPREPARED) {
                view?.getView()?.hide()
            }
            if (eventMethod == EventConstant.VALUE_METHOD_ONSEEKCOMPLETE) {
                view?.getView()?.hide()
            }

            if (eventFrom == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_SEEKTO -> { //调用了seekto方法
                        view?.getView()?.show()
                    }
                }
            }
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            val eventFrom = event?.parameter!![EventConstant.KEY_FROM]
            val eventMethod = event.parameter?.get(EventConstant.KEY_METHOD)

            if (eventFrom == EventConstant.VALUE_FROM_PREVIEW) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_CLICK -> { //点击了预览图或者预览图上面播放按钮
                        view?.getView()?.show()
                    }
                }
            }
        }

    }
}