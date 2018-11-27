package com.xm.lib.media

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.xm.lib.media.contract.BaseMediaContract
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
        //fun mediaComponent(): XmMediaComponent
        fun setDisplay(dataSource: String?): XmMediaComponent

        fun setup(): XmMediaComponent
        fun core(absMediaCore: AbsMediaCore?): XmMediaComponent
        fun build()
    }

    class Model : BaseMediaContract.Model() {
        var dataSource: String? = null
        var addViewMap: HashMap<EnumViewType, MediaViewObservable<*>>? = HashMap()
        var xmMediaFirstW: Int? = -1
        var xmMediaFirstH: Int? = -1
        var screen_mode: String? = EventConstant.VALUE_SCREEN_SMALL
    }

    class Present(val context: Context, val view: View) : BaseMediaContract.Present() {

        private var model: Model? = Model()
        private var player: AbsMediaCore? = null
        private var gestureDetector: GestureDetector? = null

        fun release() {
            player?.release()
        }

        fun seekTo(msec: Long) {
            player?.seekTo(msec)

            //更新消息主要是为了通知消息给加载页面
            view.getView().notifyObservers(
                    Event().setEventType(EnumMediaEventType.MEDIA)
                            .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                            .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_SEEKTO))
        }

        fun getCurrentPosition(): Long {
            return player?.getCurrentPosition()!!
        }

        fun getDuration(): Long {
            return player?.getDuration()!!
        }

        fun stop() {
            player?.stop()
        }

        fun pause() {
            player?.pause()
        }

        fun start() {
            player?.start()
        }

        fun setup() {
            player?.init()
        }

        fun setDisplay(dataSource: String) {
            model?.dataSource = dataSource
        }

        fun prepareAsync() {
            player?.prepareAsync()
        }

        fun core(absMediaCore: AbsMediaCore) {
            player = absMediaCore
            player?.model = model
            player?.view = view
            player?.context = context
            player?.tagerView = view.getView()
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
                view.getView().addObserver(v1.value)
                v1.value.addObserver(view.getView())
            }
            for (v1 in model?.addViewMap?.entries!!) {
                for (v2 in model?.addViewMap?.entries!!) {
                    if (v1.value != v2.value) {
                        v1.value.addObserver(v2.value)
                    }
                }
            }
            view?.getView().addObserver(view?.getView())
        }

        /**
         * 播放器对象创建完成，通知给各个视图
         */
        private fun createMediaNotifyObservers() {
            view.getView().notifyObservers(
                    Event().setEventType(EnumMediaEventType.MEDIA)
                            .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                            .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_CORE)
                            .setParameter("mp", player!!))
        }

        /**
         * 添加view
         */
        private fun addViews() {
            for (e in model?.addViewMap?.entries!!) {
                view.getView().addView(model?.addViewMap?.get(e.key))
            }
        }

        /**
         * 初始化播放器监听
         */
        private fun setupMediaLisenter() {
            /**
             * ijkplayer的错误回调信息解释
             * https://juejin.im/post/5bc7e689f265da0adc18fb7a
             */
            player?.setOnLisenter(object : AbsMediaCoreOnLisenter() {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_SURFACECHANGED)
                                    .setParameter("holder", holder!!)
                                    .setParameter("format", format)
                                    .setParameter("width", width)
                                    .setParameter("height", height))
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_SURFACEDESTROYED)
                                    .setParameter("holder", holder!!))
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_SURFACECREATED)
                                    .setParameter("holder", holder!!))
                }

                override fun onPrepared(mp: AbsMediaCore) {
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONPREPARED)
                                    .setParameter("mp", mp))
                    player?.start()
                }

                override fun onCompletion(mp: AbsMediaCore) {
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONCOMPLETION)
                                    .setParameter("mp", mp))
                }

                /**
                 * 怎么加缓存进度:https://github.com/Bilibili/ijkplayer/issues/1137
                 */
                override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONBUFFERINGUPDATE)
                                    .setParameter("mp", mp)
                                    .setParameter("percent", percent))
                    Log.d("xxxm", "percent:$percent")
                }

                override fun onSeekComplete(mp: AbsMediaCore) {
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONSEEKCOMPLETE)
                                    .setParameter("mp", mp))
                    Log.d("xxxm", "onSeekComplete")
                }

                override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONVIDEOSIZECHANGED)
                                    .setParameter("width", width)
                                    .setParameter("height", height)
                                    .setParameter("sar_num", sar_num))
                }

                override fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                    view.getView().notifyObservers(
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
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONINFO)
                                    .setParameter("mp", mp)
                                    .setParameter("what", what)
                                    .setParameter("extra", extra))
                    Log.d("xxxm", "what:$what" + "extra$extra")
                    return false
                }

                override fun onTimedText(mp: AbsMediaCore) {
                    view.getView().notifyObservers(
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
            gestureDetector = GestureDetector(context, MediaGestureListener(context, object : MediaGestureListener.GestureListener {

                override fun onClickListener() {
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.VIEW)
                                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_CLICK))
                }

                override fun onVolume(offset: Float) {
                    val percent = offset / view.getView().height
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.VIEW)
                                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONVOLUME)
                                    .setParameter("percent", percent))
                }

                override fun onLight(offset: Float) {
                    val percent = offset / view.getView().height
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.VIEW)
                                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONLIGHT)
                                    .setParameter("percent", percent))
                }

                override fun onProgress(offset: Float) {
                    val percent = offset / view.getView().width
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.VIEW)
                                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONPROGRESS)
                                    .setParameter("percent", percent))
                }
            }))
            view.getView().setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) { //通知所有手势视图隐藏
                    view.getView().notifyObservers(
                            Event().setEventType(EnumMediaEventType.VIEW)
                                    .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_MEDIACOMPONENT)
                                    .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_UP))
                }
                gestureDetector?.onTouchEvent(event)!!
            }
        }

        override fun process() {

        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            var eventFrom = event?.parameter?.get(EventConstant.KEY_FROM)
            var eventMethod = event?.parameter?.get(EventConstant.KEY_METHOD)

            if ("click" == event?.parameter?.get(EventConstant.KEY_METHOD)) {
                //先重置所有的状态
                player?.prepareAsync()
            }

            if (EventConstant.VALUE_SCREEN_FULL == (event?.parameter?.get(EventConstant.KEY_SCREEN_MODE))) {
                if (model?.xmMediaFirstW == -1 && model?.xmMediaFirstH == -1) {
                    //记录原有的view的宽高
                    model?.xmMediaFirstW = view.getView().measuredWidth
                    model?.xmMediaFirstH = view.getView().measuredHeight
                }

                PolyvScreenUtils.hideStatusBar(context as Activity)
                PolyvScreenUtils.setLandscape(context)
                view.getView().layoutParams.width = PolyvScreenUtils.getNormalWH(context)[0]
                view.getView().layoutParams.height = PolyvScreenUtils.getNormalWH(context)[1]

            } else if (EventConstant.VALUE_SCREEN_SMALL == ((event?.parameter?.get(EventConstant.KEY_SCREEN_MODE)))) {
                PolyvScreenUtils.hideStatusBar(context as Activity)
                PolyvScreenUtils.setPortrait(context)
                view.getView().layoutParams.width = model?.xmMediaFirstW!!
                view.getView().layoutParams.height = model?.xmMediaFirstH!!
            }

            if (eventFrom == EventConstant.VALUE_FROM_CONTROLVIEW) {
                when (eventMethod) {
                    EventConstant.VALUE_METHOD_ONSTOPTRACKINGTOUCH -> {
                        val msec = event?.parameter?.get("progress") as Int * getDuration()
                        seekTo(msec)
                    }
                }
            }
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {
            var eventFrom = event?.parameter?.get(EventConstant.KEY_FROM)
            var eventMethod = event?.parameter?.get(EventConstant.KEY_METHOD)
            if (eventFrom == "Activity") {
                when (eventMethod) {
                    "onDestroy" -> {
                        stop()
                        release()
                    }
                }
            }
        }
    }
}