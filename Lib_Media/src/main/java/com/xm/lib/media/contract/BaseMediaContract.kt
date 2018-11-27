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

    open class Model

    abstract class Present {
        /**
         * 数据初始化处理
         */
        abstract fun process()

        /**
         * 分派接受的事件
         */
        open fun handleReceiveEvent(o: MediaViewObservable<*>?, event: Event?) {
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

        /**
         * 播放器事件
         */
        abstract fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?)

        /**
         * 控件事件
         */
        abstract fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?)

        /**
         * 其他事件
         */
        abstract fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?)
    }
}