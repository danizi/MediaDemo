package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.xm.lib.media.contract.BaseMediaContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.component.MediaErrorView

class MediaErrorViewContract {
    interface View : BaseMediaContract.View<MediaErrorView> {

    }

    class Model : BaseMediaContract.Model() {

    }

    class Present(context: Context?, view: View?) : BaseMediaContract.Present() {
        override fun process() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}