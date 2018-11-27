package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.contract.BaseMediaContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.help.VolumeHelper
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.component.MediaGestureVolumeView

class MediaGestureVolumeViewContract {
    interface View : BaseMediaContract.View<MediaGestureVolumeView> {}

    class Model : BaseMediaContract.Model() {
        val volumeResID: Int? = R.mipmap.media_volume
        var media: AbsMediaCore? = null
    }

    class Present(context: Context?, view: View?) : BaseMediaContract.Present() {
        var context: Context? = context
        var view: View? = view
        var model: Model? = Model()

        override fun process() {
            view?.getView()?.iv?.setImageResource(model?.volumeResID!!)
        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (event?.parameter?.get(EventConstant.KEY_FROM) == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (event.parameter?.get(EventConstant.KEY_METHOD)) {
                    EventConstant.VALUE_METHOD_CORE -> {
                        model?.media = event.parameter?.get("mp") as AbsMediaCore?
                    }
                }
            }
        }

        var curProgress: Int? = -1
        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (event?.parameter?.get(EventConstant.KEY_FROM) == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (event.parameter?.get(EventConstant.KEY_METHOD)) {
                    EventConstant.VALUE_METHOD_ONVOLUME -> {
                        if (curProgress == -1) {
                            curProgress = view?.getView()?.progress?.progress!!
                        }
                        //当前媒体音量
                        val present: Float = VolumeHelper.getVolume(context) - event.parameter?.get("percent") as Float
                        //获取滑动的距离再加
                        view?.getView()?.progress?.progress = curProgress!! + (present * 100).toInt()
                        //设置音量
                        VolumeHelper.setVolume(context, present)
                        view?.showView()
                    }

                    EventConstant.VALUE_METHOD_UP -> {
                        view?.hideView()
                        curProgress=-1
                    }
                }
            }
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {

        }

    }
}