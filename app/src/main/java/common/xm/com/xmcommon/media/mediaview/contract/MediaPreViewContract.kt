package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.bumptech.glide.Glide
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.contract.BaseMediaContract
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.component.MediaPreView

class MediaPreViewContract {

    interface View : BaseMediaContract.View<MediaPreView>

    class Model : BaseMediaContract.Model() {
        var preUrl: String? = null
        var media: AbsMediaCore? = null
    }

    class Present(context: Context, view: View) : BaseMediaContract.Present() {
        var context: Context? = context
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

        override fun handleMediaEvent(o: MediaViewObservable<*>, event: Event) {
            //播放器创建完成通知
            if (EventConstant.VALUE_METHOD_CORE == event.parameter?.get(EventConstant.KEY_METHOD)) {
                model?.media = event.parameter?.get("mp") as AbsMediaCore?
            }

            if (EventConstant.VALUE_METHOD_ONPREPARED == event.parameter?.get(EventConstant.KEY_METHOD)) {
                view?.hideView()
            }
        }

        override fun handleViewEvent(o: MediaViewObservable<*>, event: Event) {

        }

        override fun handleOtherEvent(o: MediaViewObservable<*>, event: Event) {

        }
    }
}