package com.xm.lib.media.contract

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.PolyvScreenUtils
import com.xm.lib.media.component.XmMediaComponent
import com.xm.lib.media.contract.base.BaseMediaContract
import com.xm.lib.media.enum_.EnumGestureState
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumViewType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.imp.IMediaCore
import com.xm.lib.media.lisenter.AbsMediaCoreOnLisenter
import com.xm.lib.media.lisenter.MediaGestureListener
import com.xm.lib.media.watcher.MediaViewObservable
import java.util.*


class XmMediaContract {

    interface View : BaseMediaContract.View<XmMediaComponent>, IMediaCore {
        fun addViewToMedia(enumViewType: EnumViewType?, viewGroup: MediaViewObservable<*>?): XmMediaComponent
        fun setDisplay(dataSource: String?): XmMediaComponent
        fun setup(): XmMediaComponent
        fun core(absMediaCore: AbsMediaCore?): XmMediaComponent
        fun build()
        @Deprecated("")
        fun getMedia(): AbsMediaCore
    }

    class Model : BaseMediaContract.Model() {
        var dataSource: String? = null
        var addViewMap: HashMap<EnumViewType, MediaViewObservable<*>>? = HashMap()
        var xmMediaFirstW: Int? = -1
        var xmMediaFirstH: Int? = -1
        var screen_mode: String? = EventConstant.VALUE_SCREEN_SMALL
    }

    class Present(context: Context?, val view: View?) : BaseMediaContract.Present(context) {

        private var model: Model? = Model()
        private var player: AbsMediaCore? = null
        private var gestureDetector: GestureDetector? = null

        /**
         * 播放器相关动作
         */
        fun action(action: String?, vararg params: Any?): Any? {
            var result: Any? = Any()
            when (action) {
                XmMediaComponent.Action.release -> {
                    player?.release()
                }
                XmMediaComponent.Action.seekTo -> {
                    player?.seekTo(msec = params[0] as Long)
                    //更新消息主要是为了通知消息给加载页面
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_SEEKTO))
                }
                XmMediaComponent.Action.getDuration -> {
                    result = player?.getCurrentPosition()!!
                }
                XmMediaComponent.Action.getCurrentPosition -> {
                    result = player?.getCurrentPosition()!!
                }
                XmMediaComponent.Action.stop -> {
                    player?.stop()
                }
                XmMediaComponent.Action.pause -> {
                    player?.pause()
                }
                XmMediaComponent.Action.start -> {
                    player?.start()
                }
                XmMediaComponent.Action.setDisplay -> {
                    model?.dataSource = params[0] as String?
                }
                XmMediaComponent.Action.prepareAsync -> {
                    player?.prepareAsync()
                }
            }
            return result
        }

        fun setup() {
            player?.init()
        }

        fun core(absMediaCore: AbsMediaCore) {
            player = absMediaCore
            player?.model = model
            player?.view = view
            player?.context = context
            player?.tagerView = view?.getView()
        }

        fun addViewToMedia(enumViewType: EnumViewType, viewGroup: MediaViewObservable<*>) {
            model?.addViewMap?.put(enumViewType, viewGroup)
        }

        @SuppressLint("ClickableViewAccessibility")
        fun build() {
            addObserver()
            createMediaNotifyObservers()
            addViews()
            setupMediaLisenter()
            processGestureDetector()
        }

        /**
         * 注册观察者
         */
        private fun addObserver() {
            for (v1 in model?.addViewMap?.entries!!) {
                view?.getView()?.addObserver(v1.value)
                v1.value.addObserver(view?.getView())
            }
            for (v1 in model?.addViewMap?.entries!!) {
                for (v2 in model?.addViewMap?.entries!!) {
                    if (v1.value != v2.value) {
                        v1.value.addObserver(v2.value)
                    }
                }
            }
            view?.getView()?.addObserver(view?.getView())
        }

        /**
         * 播放器对象创建完成，通知给各个视图
         */
        private fun createMediaNotifyObservers() {
            view?.getView()?.notifyObservers( //todo 废弃，最好使用下面的播放组件对象
                    Event().setEventType(EnumMediaEventType.MEDIA)
                            .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                            .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_CORE)
                            .setParameter("mp", player!!)
                            .setParameter("mediaComponent",  view.getView()))
        }

        /**
         * 添加view
         */
        private fun addViews() {
            for (e in model?.addViewMap?.entries!!) {
                view?.getView()?.addView(model?.addViewMap?.get(e.key))
            }
        }

        /**
         * 初始化播放器监听
         */
        private fun setupMediaLisenter() {
            player?.setOnLisenter(object : AbsMediaCoreOnLisenter() {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_SURFACECHANGED)
                                    .setParameter("holder", holder!!)
                                    .setParameter("format", format)
                                    .setParameter("width", width)
                                    .setParameter("height", height))
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_SURFACEDESTROYED)
                                    .setParameter("holder", holder!!))
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_SURFACECREATED)
                                    .setParameter("holder", holder!!))
                }

                override fun onPrepared(mp: AbsMediaCore) {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONPREPARED)
                                    .setParameter("mp", mp))
                    player?.start()
                }

                override fun onCompletion(mp: AbsMediaCore) {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONCOMPLETION)
                                    .setParameter("mp", mp))
                }

                override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONBUFFERINGUPDATE)
                                    .setParameter("mp", mp)
                                    .setParameter("percent", percent))
                    Log.d("xxxm", "percent:$percent")
                }

                override fun onSeekComplete(mp: AbsMediaCore) {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONSEEKCOMPLETE)
                                    .setParameter("mp", mp))
                    Log.d("xxxm", "onSeekComplete")
                }

                override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONVIDEOSIZECHANGED)
                                    .setParameter("width", width)
                                    .setParameter("height", height)
                                    .setParameter("sar_num", sar_num))
                }

                override fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONERROR)
                                    .setParameter("what", what)
                                    .setParameter("extra", extra))
                    return false
                }

                /**
                 * 播放的状态
                 */
                override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONINFO)
                                    .setParameter("mp", mp)
                                    .setParameter("what", what)
                                    .setParameter("extra", extra))
                    Log.d("xxxm", "what:$what" + "extra$extra")
                    return false
                }

                override fun onTimedText(mp: AbsMediaCore) {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONTIMEDTEXT)
                                    .setParameter("mp", mp))
                }
            })
        }

        /**
         * 手势相关的处理
         */
        @SuppressLint("ClickableViewAccessibility")
        private fun processGestureDetector() {
            var enumGestureState: EnumGestureState? = null
            gestureDetector = GestureDetector(context, MediaGestureListener(context!!, object : MediaGestureListener.GestureListener {
                var percent: Float? = null
                override fun onClickListener() {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.VIEW)
                                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_CLICK))
                }

                override fun onVolume(offset: Float) {
                    enumGestureState = EnumGestureState.VOLUME
                    val percent = offset / view?.getView()?.height!!
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.VIEW)
                                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONVOLUME)
                                    .setParameter("percent", percent))
                }

                override fun onLight(offset: Float) {
                    enumGestureState = EnumGestureState.LIGHT
                    val percent = offset / view?.getView()?.height!!
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.VIEW)
                                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONLIGHT)
                                    .setParameter("percent", percent))
                }

                override fun onProgress(offset: Float) {
                    enumGestureState = EnumGestureState.PROGRESS
                    percent = offset / view?.getView()?.width!!
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.VIEW)
                                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONPROGRESS)
                                    .setParameter("percent", percent!!))
                }
            }))
            view?.getView()?.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) { //通知所有手势视图隐藏
                    var event: Event = Event()
                    event.setEventType(EnumMediaEventType.VIEW)
                            .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                            .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_UP)
                    if (null != enumGestureState) {
                        event.setParameter(EventConstant.KEY_GESTURE_STATE, enumGestureState!!)
                    }
                    view.getView().notifyObservers(event)
                    enumGestureState = EnumGestureState.NONE
                }
                gestureDetector?.onTouchEvent(event)!!
            }
        }

        override fun process() {

        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {

            if (EventConstant.VALUE_SCREEN_FULL == (event?.parameter?.get(EventConstant.KEY_SCREEN_MODE))) {
                if (model?.xmMediaFirstW == -1 && model?.xmMediaFirstH == -1) {
                    //记录原有的view的宽高
                    model?.xmMediaFirstW = view?.getView()?.measuredWidth
                    model?.xmMediaFirstH = view?.getView()?.measuredHeight
                }

                PolyvScreenUtils.hideStatusBar(context as Activity)
                PolyvScreenUtils.setLandscape(context)
                view?.getView()?.layoutParams?.width = PolyvScreenUtils.getNormalWH(context)[0]
                view?.getView()?.layoutParams?.height = PolyvScreenUtils.getNormalWH(context)[1]

            } else if (EventConstant.VALUE_SCREEN_SMALL == ((event?.parameter?.get(EventConstant.KEY_SCREEN_MODE)))) {
                PolyvScreenUtils.hideStatusBar(context as Activity)
                PolyvScreenUtils.setPortrait(context)
                view?.getView()?.layoutParams?.width = model?.xmMediaFirstW!!
                view?.getView()?.layoutParams?.height = model?.xmMediaFirstH!!
            }
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {
            val eventFrom = event?.parameter?.get(EventConstant.KEY_FROM)
            val eventMethod = event?.parameter?.get(EventConstant.KEY_METHOD)
            if (eventFrom == "Activity") {
                when (eventMethod) {
                    "onDestroy" -> {
                        action("stop")
                        action("release")
                    }
                }
            }
        }

        fun getMediaCore(): AbsMediaCore {
            return model?.media!!
        }
    }
}