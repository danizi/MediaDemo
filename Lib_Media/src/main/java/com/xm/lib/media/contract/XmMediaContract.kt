package com.xm.lib.media.contract

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.xm.lib.media.component.XmMediaComponent
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.getCurrentPosition
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.getDuration
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.getPlayState
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.pause
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.prepareAsync
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.release
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.replay
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.seekTo
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.setDisplay
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.start
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.stop
import com.xm.lib.media.contract.base.BaseMediaContract
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.AbsMediaCoreOnLisenter
import com.xm.lib.media.enum_.EnumGestureState
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumViewType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.lisenter.MediaGestureListener
import com.xm.lib.media.watcher.MediaViewObservable
import java.util.*


class XmMediaContract {

    interface View : BaseMediaContract.View<XmMediaComponent> {
        fun addViewToMedia(enumViewType: EnumViewType?, viewGroup: MediaViewObservable<*>?): XmMediaComponent
        fun action(action: String?, vararg params: Any?): Any?
        fun setDisplay(dataSource: String?): XmMediaComponent
        fun core(absMediaCore: AbsMediaCore?): XmMediaComponent
        fun build()
    }

    class Model : BaseMediaContract.Model() {
        var dataSource: String? = null
        var addViewMap: HashMap<EnumViewType, MediaViewObservable<*>>? = HashMap()
        var player: AbsMediaCore? = null
        var curPos: Long? = -1
    }

    class Present(context: Context?, val view: View?) : BaseMediaContract.Present(context) {
        private var model: Model? = Model()
        private var gestureDetector: GestureDetector? = null

        fun action(action: String?, vararg params: Any?): Any? {
            var result: Any? = Any()
            when (action) {
                release -> {
                    model?.player?.release()
                }
                seekTo -> {
                    model?.player?.seekTo(msec = params[0] as Long)
                    //更新消息主要是为了通知消息给加载页面
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_SEEKTO))
                }
                getDuration -> {
                    result = model?.player?.getDuration()!!
                }
                getCurrentPosition -> {
                    result = model?.player?.getCurrentPosition()!!
                }
                stop -> {
                    model?.player?.stop()
                }
                pause -> {
                    model?.player?.pause()
                }
                start -> {
                    model?.player?.start()
                }
                setDisplay -> {
                    model?.dataSource = params[0] as String?
                }
                prepareAsync -> {
                    //重置播放状态
                    model?.player?.reset()
                    model?.player?.prepareAsync()
                }
                replay -> {
                    model?.player?.rePlay()
                }
                getPlayState -> {
                    result = model?.player?.playerState!!
                }
            }
            return result
        }

        override fun process() {

        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {
            val eventFrom = event?.parameter?.get(EventConstant.KEY_FROM)
            val eventMethod = event?.parameter?.get(EventConstant.KEY_METHOD)
            if (eventFrom == "Activity") {
                when (eventMethod) {
                    "onDestroy" -> {
                        model?.curPos = -1
                        model?.curPos = model?.player?.getCurrentPosition()
                        action("stop")
                        action("release")
                    }

                    "onPause" -> {
                        //不可见状态
                        model?.player?.pause()
                        model?.curPos = model?.player?.getCurrentPosition()
                    }

                    "onRestart" -> {
                        //恢复状态
                        model?.player?.rePlay()
                    }
                }
            }
        }

        fun core(absMediaCore: AbsMediaCore) {
            model?.player = absMediaCore
            model?.player?.model = model
            model?.player?.view = view
            model?.player?.context = context
            model?.player?.tagerView = view?.getView()
            model?.player?.init()
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
            view?.getView()?.addObserver(view.getView())
        }

        private fun createMediaNotifyObservers() {
            view?.getView()?.notifyObservers( //todo 废弃，最好使用下面的播放组件对象
                    Event().setEventType(EnumMediaEventType.MEDIA)
                            .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                            .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_CORE)
                            .setParameter("mp", model?.player!!)
                            .setParameter("mediaComponent", view.getView()))
        }

        private fun addViews() {
            for (e in model?.addViewMap?.entries!!) {
                view?.getView()?.addView(model?.addViewMap?.get(e.key))
            }
        }

        private fun setupMediaLisenter() {
            model?.player?.setOnLisenter(object : AbsMediaCoreOnLisenter() {
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

                    if (model?.curPos != (-1).toLong()) {
                        model?.player?.seekTo(model?.curPos!!)
                        model?.player?.start()
                    }
                }

                override fun onCompletion(mp: AbsMediaCore) {
                    view?.getView()?.notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONCOMPLETION)
                                    .setParameter("mp", mp))
                    model?.curPos = -1
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
                    val event: Event? = Event()
                    event?.setEventType(EnumMediaEventType.VIEW)
                            ?.setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                            ?.setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_UP)
                    if (null != enumGestureState) {
                        event?.setParameter(EventConstant.KEY_GESTURE_STATE, enumGestureState!!)
                    }
                    view.getView().notifyObservers(event!!)
                    enumGestureState = EnumGestureState.NONE
                }
                gestureDetector?.onTouchEvent(event)!!
            }
        }
    }
}