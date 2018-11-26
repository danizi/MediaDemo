package com.xm.lib.media.contract

import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable

class BaseMediaContract {
    interface View<T> {
        fun showView()
        fun hideView()
        fun getView(): T
    }

    open class Model {

    }

    abstract class Present {
        abstract fun process()

        open fun handleReceiveEvent(o: MediaViewObservable<*>, event: Event) {
            //播放器事件
            if (event.eventType == EnumMediaEventType.MEDIA) {
                handleMediaEvent(o, event)
            }

            //控件事件
            if (event.eventType == EnumMediaEventType.VIEW) {
                handleViewEvent(o, event)
            }

            //其他事件
            if (event.eventType == EnumMediaEventType.OTHER) {
                handleOtherEvent(o, event)
            }
        }

        abstract fun handleMediaEvent(o: MediaViewObservable<*>, event: Event)
        abstract fun handleViewEvent(o: MediaViewObservable<*>, event: Event)
        abstract fun handleOtherEvent(o: MediaViewObservable<*>, event: Event)
    }
}