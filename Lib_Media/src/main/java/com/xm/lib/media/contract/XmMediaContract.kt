package com.xm.lib.media.contract

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.SurfaceHolder
import android.view.ViewGroup
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.EnumViewType
import com.xm.lib.media.component.XmMediaComponent
import com.xm.lib.media.core.MediaCoreOnLisenter
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
        }

        private fun setOncLisenter() {
            player?.setOnLisenter(object : MediaCoreOnLisenter() {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    view.mediaComponent().notifyObserver("surfaceChanged", holder!!, format!!, width!!, height!!)
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    view.mediaComponent().notifyObserver("surfaceDestroyed", holder!!)
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    view.mediaComponent().notifyObserver("surfaceCreated", holder!!)
                }

                override fun onPrepared(mp: AbsMediaCore) {
                    player?.start()
                    view.mediaComponent().notifyObserver("onPrepared")
                }

                override fun onCompletion(mp: AbsMediaCore) {
                    view.mediaComponent().notifyObserver("onCompletion", mp)
                }

                override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                    view.mediaComponent().notifyObserver("onBufferingUpdate", mp, percent)
                }

                override fun onSeekComplete(mp: AbsMediaCore) {
                    view.mediaComponent().notifyObserver("onSeekComplete", mp)
                }

                override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                    view.mediaComponent().notifyObserver("onVideoSizeChanged", mp, width, height, sar_num, sar_den)
                }

                override fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                    view.mediaComponent().notifyObserver("onError", mp, what, extra)
                    return false
                }

                override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                    view.mediaComponent().notifyObserver("onInfo", mp, what, extra)
                    return false
                }

                override fun onTimedText(mp: AbsMediaCore) {
                    view.mediaComponent().notifyObserver("onInfo", mp)
                }
            })
        }

        fun setDisplay(dataSource: String) {
            model?.dataSource = dataSource
        }

        fun update(o: MediaViewObservable, vararg arg: Any) {
            model?.addViewMap?.get(EnumViewType.PREVIEW)?.visibility = android.view.View.GONE
            Log.e("XmMediaComponent", "XmMediaComponent 观察者接受：" + arg[0])
        }
    }
}