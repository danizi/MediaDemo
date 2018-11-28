package com.xm.lib.media.contract

import android.content.Context
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.XmMediaComponent
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable

class BaseMediaContract {
    interface View<T> {
        fun showView()
        fun hideView()
        fun getView(): T
    }

    open class Model {
        var mediaView: XmMediaComponent? = null
        @Deprecated("请使用mediaView")
        var media: AbsMediaCore? = null
    }

    abstract class Present(context: Context?) {
        val context: Context? = context
        var eventFrom: String? = "eventFrom"
        var eventMethod: String? = "eventMethod"
        var media: AbsMediaCore? = null
        var mediaView: XmMediaComponent? = null

        /**
         * 数据初始化处理
         */
        abstract fun process()

        /**
         * 分派接受的事件
         */
        open fun handleReceiveEvent(o: MediaViewObservable<*>?, event: Event?) {
            eventFrom = event?.parameter?.get(EventConstant.KEY_FROM) as String?
            eventMethod = event?.parameter?.get(EventConstant.KEY_METHOD) as String?
            getMedia(event)
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

        /**
         * 获取播放抽象对象
         */
        private fun getMedia(event: Event?) {
            if (eventFrom == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_CORE -> {
                        media = (event?.parameter?.get("mp") as AbsMediaCore?)
                    }
                }
            }
        }

        /**
         * 获取播放组件
         */
        private fun getMediaView(event: Event?) {
            if (eventFrom == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_CORE -> {
                        if(event?.parameter?.get("mediaComponent")!=null){
                            mediaView = event?.parameter?.get("mediaComponent") as XmMediaComponent
                        }
                    }
                }
            }
        }
    }
}