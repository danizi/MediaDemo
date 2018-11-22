package com.xm.lib.media

import android.content.Context
import android.view.SurfaceHolder
import android.view.ViewGroup
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumMediaState
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
            player?.playerState = EnumMediaState.RELEASE
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
            player?.playerState = EnumMediaState.STOP
        }

        fun pause() {
            player?.pause()
            player?.playerState = EnumMediaState.PAUSE
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
                                    .setParameter(EventConstant.METHOD, "surfaceChanged")
                                    .setParameter("holder", holder!!)
                                    .setParameter("format", format)
                                    .setParameter("width", width)
                                    .setParameter("height", height))
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.METHOD, "surfaceDestroyed")
                                    .setParameter("holder", holder!!))
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.METHOD, "surfaceCreated")
                                    .setParameter("holder", holder!!))
                }

                override fun onPrepared(mp: AbsMediaCore) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.METHOD, "onPrepared")
                                    .setParameter("mp", mp!!))
                    player?.start()
                    player?.playerState = EnumMediaState.PLAYING
                }

                override fun onCompletion(mp: AbsMediaCore) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.METHOD, "onCompletion")
                                    .setParameter("mp", mp!!))
                    player?.playerState = EnumMediaState.COMPLETION
                }

                override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.METHOD, "onBufferingUpdate")
                                    .setParameter("mp", mp)
                                    .setParameter("percent", percent))
                }

                override fun onSeekComplete(mp: AbsMediaCore) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.METHOD, "onSeekComplete")
                                    .setParameter("mp", mp))
                    player?.playerState = EnumMediaState.SEEKCOMPLETE
                }

                override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.METHOD, "onVideoSizeChanged")
                                    .setParameter("width", width)
                                    .setParameter("height", height)
                                    .setParameter("sar_num", sar_num))
                }

                override fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.METHOD, "onError")
                                    .setParameter("what", what)
                                    .setParameter("extra", extra))
                    player?.playerState = EnumMediaState.ERROR
                    return false
                }

                override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.METHOD, "onInfo")
                                    .setParameter("mp", mp)
                                    .setParameter("what", what)
                                    .setParameter("extra", extra))
                    return false
                }

                override fun onTimedText(mp: AbsMediaCore) {
                    view.mediaComponent().notifyObservers(
                            Event().setEventType(EnumMediaEventType.MEDIA)
                                    .setParameter(EventConstant.METHOD, "onTimedText")
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
                if (null != event.parameter?.get(EventConstant.METHOD)) {
                    //先重置所有的状态
                    player?.prepareAsync()
                }
            }
        }
    }
}