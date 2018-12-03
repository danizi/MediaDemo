package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.xm.lib.media.PolyvScreenUtils
import com.xm.lib.media.R
import com.xm.lib.media.component.control.MediaControlView
import com.xm.lib.media.component.control.MediaHControlView
import com.xm.lib.media.component.control.MediaVControlView
import com.xm.lib.media.contract.base.BaseMediaContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable

class MediaControlViewContract {
    interface View : BaseMediaContract.View<MediaControlView>

    class Model : BaseMediaContract.Model() {
        var visibilityFlag: Int = android.view.View.GONE
        var hLayout: MediaHControlView? = null
        var vLayout: MediaVControlView? = null
    }

    class Present(context: Context?, val view: View?) : BaseMediaContract.Present(context) {
        var model: Model? = Model()

        override fun process() {
            view?.getView()?.show()

            model?.vLayout = MediaVControlView(context, R.layout.media_controller_v)
            view?.getView()?.addView(model?.vLayout)

            model?.hLayout = MediaHControlView(context, R.layout.media_controller_h)
            view?.getView()?.addView(model?.hLayout)

            // 添加观察者
            model?.vLayout?.addObserver(model?.hLayout)
            model?.hLayout?.addObserver(model?.vLayout)
            view?.getView()?.addObserver(model?.hLayout)
            view?.getView()?.addObserver(model?.vLayout)
        }

        override fun handleReceiveEvent(o: MediaViewObservable<*>?, event: Event?) {
            super.handleReceiveEvent(o, event)
            view?.getView()?.notifyObservers(event!!)
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            when (eventMethod) {
                EventConstant.VALUE_METHOD_CLICK -> {
                    model?.visibilityFlag = if (model?.visibilityFlag == android.view.View.GONE) {
                        view?.getView()?.show()
                        android.view.View.VISIBLE
                    } else {
                        view?.getView()?.hide()
                        android.view.View.GONE
                    }

                    if (PolyvScreenUtils.isLandscape(context)) {
                        model?.hLayout?.visibility = android.view.View.VISIBLE
                        model?.vLayout?.visibility = android.view.View.GONE
                    } else {
                        model?.vLayout?.visibility = android.view.View.VISIBLE
                        model?.hLayout?.visibility = android.view.View.GONE
                    }
                }
            }
        }
    }
}