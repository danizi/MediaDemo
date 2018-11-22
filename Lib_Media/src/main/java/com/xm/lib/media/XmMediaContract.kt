package com.xm.lib.media

import android.app.Activity
import android.content.Context
import android.view.SurfaceHolder
import android.view.ViewGroup
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumViewType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.imp.IMediaCore
import com.xm.lib.media.watcher.MediaViewObservable
import java.util.*


class XmMediaContract {

    interface View : IMediaCore {
        fun addViewToMedia(enumViewType: EnumViewType, viewGroup: ViewGroup): XmMediaComponent
        fun mediaComponent(): XmMediaComponent
        fun setDisplay(dataSource: String): XmMediaComponent
        fun setup(): XmMediaComponent
        fun core(absMediaCore: AbsMediaCore): XmMediaComponent
    }

    class Model {
        var dataSource: String? = null
        var addViewMap: HashMap<EnumViewType, ViewGroup>? = HashMap()
        var xmMediaFirstW: Int? = -1
        var xmMediaFirstH: Int? = -1
    }

    class Present(val context: Context, val view: View) {
        private var model: Model? = null
        private var player: AbsMediaCore? = null

        init {
            model = Model()
        }

        fun core(absMediaCore: AbsMediaCore) {
            player = absMediaCore
            player?.model = model
            player?.view = view
            player?.context = context
            player?.tagerView = view.mediaComponent()
        }

        fun addViewToMedia(enumViewType: EnumViewType, viewGroup: ViewGroup) {
            model?.addViewMap?.put(enumViewType, viewGroup)
            view.mediaComponent().addView(viewGroup)
            //观察者模式
        }

        fun release() {
            player?.release()
        }

        fun seekTo(msec: Long) {
            player?.seekTo(msec)
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
            setOncLisenter()
        }

        private fun setOncLisenter() {
            player?.setOnLisenter(object : AbsMediaCoreOnLisenter() {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, "surfaceChanged")
                                    .setParameter("holder", holder!!)
                                    .setParameter("format", format)
                                    .setParameter("width", width)
                                    .setParameter("height", height))
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, "surfaceDestroyed")
                                    .setParameter("holder", holder!!))
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, "surfaceCreated")
                                    .setParameter("holder", holder!!))
                }

                override fun onPrepared(mp: AbsMediaCore) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, "onPrepared")
                                    .setParameter("mp", mp!!))
                    player?.start()
                }

                override fun onCompletion(mp: AbsMediaCore) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, "onCompletion")
                                    .setParameter("mp", mp!!))
                }

                override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, "onBufferingUpdate")
                                    .setParameter("mp", mp)
                                    .setParameter("percent", percent))
                }

                override fun onSeekComplete(mp: AbsMediaCore) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, "onSeekComplete")
                                    .setParameter("mp", mp))
                }

                override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, "onVideoSizeChanged")
                                    .setParameter("width", width)
                                    .setParameter("height", height)
                                    .setParameter("sar_num", sar_num))
                }

                override fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, "onError")
                                    .setParameter("what", what)
                                    .setParameter("extra", extra))
                    return false
                }

                override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, "onInfo")
                                    .setParameter("mp", mp)
                                    .setParameter("what", what)
                                    .setParameter("extra", extra))
                    return false
                }

                override fun onTimedText(mp: AbsMediaCore) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.KEY_METHOD, "onTimedText")
                                    .setParameter("mp", mp))
                }
            })
        }

        fun setDisplay(dataSource: String) {
            model?.dataSource = dataSource
        }

        fun update(o: MediaViewObservable, event: Event) {
            //预览图点击了播放图标
            if (event.eventType == EnumMediaEventType.VIEW) {

                if ("click" == event.parameter?.get(EventConstant.KEY_METHOD)) {
                    //先重置所有的状态
                    player?.prepareAsync()
                }

                if (EventConstant.VALUE_SCREEN_FULL == (event.parameter?.get(EventConstant.KEY_SCREEN_MODE))) {
                    if (model?.xmMediaFirstW == -1 && model?.xmMediaFirstH == -1) {
                        //记录原有的view的宽高
                        model?.xmMediaFirstW = view.mediaComponent().measuredWidth
                        model?.xmMediaFirstH = view.mediaComponent().measuredHeight
                    }

                    PolyvScreenUtils.hideStatusBar(context as Activity)
                    PolyvScreenUtils.setLandscape(context)
                    view.mediaComponent().layoutParams.width = PolyvScreenUtils.getNormalWH(context)[0]
                    view.mediaComponent().layoutParams.height = PolyvScreenUtils.getNormalWH(context)[1]

                } else if (EventConstant.VALUE_SCREEN_SMALL == ((event.parameter?.get(EventConstant.KEY_SCREEN_MODE)))) {
                    PolyvScreenUtils.hideStatusBar(context as Activity)
                    PolyvScreenUtils.setPortrait(context)
                    view.mediaComponent().layoutParams.width = model?.xmMediaFirstW!!
                    view.mediaComponent().layoutParams.height = model?.xmMediaFirstH!!
                }
            }
        }
    }
}