package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.xm.lib.media.contract.BaseMediaContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.component.MediaGestureProgressView

class MediaGestureProgressViewContract {
    interface View : BaseMediaContract.View<MediaGestureProgressView> {

    }

    class Model : BaseMediaContract.Model() {
        var fastBackwardResID: Int = R.mipmap.media_fast_backward  //快退
        var fastForwardResID: Int = R.mipmap.media_fast_forward    //快进
        var progressResID: Int = fastForwardResID
    }

    class Present(val context: Context, val view: View) : BaseMediaContract.Present() {
        var model: Model = Model()

        override fun process() {
            view.getView().iv?.setImageResource(model.progressResID)
        }

        override fun handleMediaEvent(o: MediaViewObservable<*>, event: Event) {

        }

        override fun handleViewEvent(o: MediaViewObservable<*>, event: Event) {
            if (event.parameter?.get(EventConstant.KEY_FROM) == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (event.parameter?.get(EventConstant.KEY_METHOD)) {
                    EventConstant.VALUE_METHOD_ONPROGRESS -> {
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