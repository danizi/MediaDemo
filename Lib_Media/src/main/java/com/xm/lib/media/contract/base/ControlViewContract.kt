package com.xm.lib.media.contract.base

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.bangke.lib.common.utils.ToolUtil
import com.xm.lib.media.PolyvScreenUtils
import com.xm.lib.media.R
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.getCurrentPosition
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.getDuration
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.getPlayState
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.pause
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.seekTo
import com.xm.lib.media.component.XmMediaComponent.Action.Companion.start
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumMediaState
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import java.util.*

class ControlViewContract {
    @Deprecated("")
    interface View<T> : BaseMediaContract.View<T>

    @Deprecated("")
    open class Model : BaseMediaContract.Model() {
        var startResId: Int? = R.mipmap.media_play
        var pauseResId: Int? = R.mipmap.media_pause
        var prepared: Boolean? = false
        var curScreenMode: String = EventConstant.VALUE_SCREEN_SMALL
        var originalMediaW: Int? = -1
        var originalMediaH: Int? = -1
    }

    abstract class Present<M : ControlViewContract.Model>(context: Context?, val model: M?) : BaseMediaContract.Present(context) {

        override fun process() {

        }

        fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?, seekBar: SeekBar, tvCurrentPosition: TextView?, tvDuration: TextView?) {
            when (event?.parameter?.get(EventConstant.KEY_METHOD)) {
                EventConstant.VALUE_METHOD_ONPREPARED -> {
                    model?.prepared = true
                    timerUpdateSeekBar(seekBar, tvCurrentPosition, tvDuration)
                }
                EventConstant.VALUE_METHOD_ONERROR -> {
                    model?.prepared = false
                }
                EventConstant.VALUE_METHOD_ONCOMPLETION -> {
                    model?.prepared = false
                }
            }
        }

        fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?, view: android.view.View?) {
            when (eventMethod) {
                "ControlView" -> {
                    view?.visibility = android.view.View.VISIBLE
                }
            }
        }

        fun timerUpdateSeekBar(seekBar: SeekBar?, tvCurrentPosition: TextView?, tvDuration: TextView?) {
            seekBar?.max = 100
            //tvDuration?.text = ToolUtil.formatTime(mediaView?.getDuration())
            tvDuration?.text = ToolUtil.formatTime(mediaView?.action(getDuration) as Long)
            val timer = Timer()
            val timerTask = object : TimerTask() {
                override fun run() {
                    seekBar?.post {
                        try {
                            tvCurrentPosition?.text = ToolUtil.formatTime(mediaView?.action(getCurrentPosition) as Long)
                            val percentF: Float = (mediaView?.action(getCurrentPosition) as Long).toFloat() / (mediaView?.action(getDuration) as Long)
                            seekBar.progress = (percentF * 100).toInt()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            timer.schedule(timerTask, 0, 1000)
        }

        fun setMediaSize(width: Int, height: Int) {
            PolyvScreenUtils.hideStatusBar(context as Activity)
            if (width != -1 && height != -1) {
                mediaView?.getView()?.layoutParams?.width = width
                mediaView?.getView()?.layoutParams?.height = height
            }
        }

        fun startOrPause(imgPlayPause: ImageView) {
            if (mediaView?.action(getPlayState) == EnumMediaState.PLAYING) {
                mediaView?.action(pause)
                imgPlayPause.setImageResource(model?.startResId!!)
            } else {
                mediaView?.action(start)
                imgPlayPause.setImageResource(model?.pauseResId!!)
            }
        }

        fun fullOrSmall(actvity: Activity?, view: android.view.View?, observable: MediaViewObservable<*>?) {
            if (PolyvScreenUtils.isLandscape(context)) {
                PolyvScreenUtils.setPortrait(actvity)
                setMediaSize(model?.originalMediaW!!, model.originalMediaH!!)
                model.curScreenMode = EventConstant.VALUE_SCREEN_FULL

            } else if (PolyvScreenUtils.isPortrait(context)) {
                savaOriginalMediaSize()
                PolyvScreenUtils.setLandscape(actvity)
                setMediaSize(model?.originalMediaW!!, model.originalMediaH!!)
                model.curScreenMode = EventConstant.VALUE_SCREEN_FULL
            }

            PolyvScreenUtils.hideStatusBar(actvity)
            view?.visibility = android.view.View.GONE
            observable?.notifyObservers(
                    Event().setEventType(EnumMediaEventType.VIEW)
                            .setParameter("ControlView", "show"))  // 通知全屏布局显示自身隐藏
        }

        private fun savaOriginalMediaSize() {
            //记录原有的view的宽高
            if (model?.originalMediaW == -1 && model.originalMediaH == -1) {
                model.originalMediaW = mediaView?.getView()?.measuredWidth
                model.originalMediaH = mediaView?.getView()?.measuredHeight
            }
        }

        /*-------
         * SeekBar监听
         */
        fun onProgressChanged(progress: Int?, fromUser: Boolean?) {}

        fun onStartTrackingTouch(seekBar: SeekBar?) {}

        fun onStopTrackingTouch(seekBar: SeekBar?) {
            val duration: Long = mediaView?.action(getDuration) as Long
            val mecs: Long = ((seekBar?.progress!!.toFloat() / 100F) * duration).toLong()
            mediaView?.action(seekTo, mecs)
        }
    }
}