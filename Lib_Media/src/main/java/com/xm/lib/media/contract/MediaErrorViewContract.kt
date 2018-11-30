package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.xm.lib.media.component.MediaErrorView
import com.xm.lib.media.contract.base.BaseMediaContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable

class MediaErrorViewContract {
    interface View : BaseMediaContract.View<MediaErrorView>

    class Model : BaseMediaContract.Model()

    class Present(context: Context?, view: View?) : BaseMediaContract.Present(context) {
        var model:Model?=null
        override fun process() {
        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {
        }
    }
}