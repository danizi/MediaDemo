package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import android.os.Build
import android.util.Log
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.contract.BaseMediaContract
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.help.LightHelper
import com.xm.lib.media.help.VolumeHelper
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.component.MediaGestureLightView

class MediaGestureLightViewContract {
    interface View : BaseMediaContract.View<MediaGestureLightView> {}

    class Model : BaseMediaContract.Model() {
        var media: AbsMediaCore? = null
        var lightResID: Int = R.mipmap.media_light
    }

    class Present(context: Context?, view: View?) : BaseMediaContract.Present() {
        var context: Context? = context
        var view: View? = view
        var model: Model? = Model()

        override fun process() {
            view?.getView()?.iv?.setImageResource(model?.lightResID!!)
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
                    EventConstant.VALUE_METHOD_ONLIGHT -> {

                        //当前媒体音量
                        if (curProgress == -1) {
                            curProgress = view?.getView()?.progress?.progress
                        }
                        val present: Float = LightHelper.getScreenBrightness(context) / 255 - event.parameter?.get("percent") as Float
                        Log.d("xxxm", "present1:" + LightHelper.getScreenBrightness(context) / 255)
                        Log.d("xxxm", "present2:" + event.parameter?.get("percent") as Float)
                        Log.d("xxxm", "present:" + present)
                        //获取滑动的距离再加
                        view?.getView()?.progress?.progress = curProgress!! + (present * 100).toInt()
                        //设置亮度
                        LightHelper.setCurWindowBrightness(context, (present * 255).toInt())
                        view?.showView()
                    }

                    EventConstant.VALUE_METHOD_UP -> {
                        view?.hideView()
                        curProgress = -1
                    }

                }
            }


        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {
        }
    }
}