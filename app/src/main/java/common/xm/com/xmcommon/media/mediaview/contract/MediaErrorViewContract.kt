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