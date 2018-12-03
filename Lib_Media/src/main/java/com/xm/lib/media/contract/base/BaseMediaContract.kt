package com.xm.lib.media.contract.base

import android.content.Context
import android.util.Log
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.component.XmMediaComponent
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable

class BaseMediaContract {
    interface View<T> {
        fun getView(): T
    }

    open class Model

    abstract class Present(context: Context?) {
        val context: Context? = context
        var eventFrom: String? = "eventFrom"
        var eventMethod: String? = "eventMethod"
        var mediaView: XmMediaComponent? = null

        abstract fun process()

        /* ------------------
         * 观察者接受事件处理
         */
        open fun handleReceiveEvent(o: MediaViewObservable<*>?, event: Event?) {
            eventFrom = event?.parameter?.get(EventConstant.KEY_FROM) as String?
            eventMethod = event?.parameter?.get(EventConstant.KEY_METHOD) as String?
            getMediaView(event)
            if (event?.eventType == EnumMediaEventType.MEDIA) {
                handleMediaEvent(o!!, event)
            }
            if (event?.eventType == EnumMediaEventType.VIEW) {
                handleViewEvent(o!!, event)
            }
            if (event?.eventType == EnumMediaEventType.OTHER) {
                handleOtherEvent(o!!, event)
            }
        }

        open fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {

        }

        open fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {}

        open fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {}

        private fun getMediaView(event: Event?) {
            if (eventFrom == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_CORE -> {
                        if (null != event?.parameter?.get("mediaComponent") as XmMediaComponent) {//获取播放器View对象
                            mediaView = event?.parameter?.get("mediaComponent") as XmMediaComponent
                        }
                    }
                }
            }
        }
    }
}