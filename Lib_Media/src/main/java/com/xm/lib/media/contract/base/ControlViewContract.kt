package com.xm.lib.media.contract.base

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.bangke.lib.common.utils.ToolUtil
import com.xm.lib.media.PolyvScreenUtils
import com.xm.lib.media.R
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumMediaState
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.contract.MediaHControlViewContract
import java.util.*

class ControlViewContract {
    @Deprecated("")
    interface View<T> : BaseMediaContract.View<T>

    @Deprecated("")
    open class Model : BaseMediaContract.Model() {
        var startResId: Int? = R.mipmap.media_play
        var pauseResId: Int? = R.mipmap.media_pause
        var visibilityFlag: Int? = android.view.View.GONE
        var prepared: Boolean? = false
        var curScreenMode: String = EventConstant.VALUE_SCREEN_SMALL
        var originalMediaW: Int? = -1
        var originalMediaH: Int? = -1
    }

    abstract class Present<M : ControlViewContract.Model>(context: Context?, val model: M) : BaseMediaContract.Present(context) {

        override fun process() {

        }

        fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?, seekBar: SeekBar, tvCurrentPosition: TextView?, tvDuration: TextView?) {
            when (event?.parameter?.get(EventConstant.KEY_METHOD)) {
                EventConstant.VALUE_METHOD_ONPREPARED -> {
                    model.prepared = true
                    timerUpdateSeekBar(seekBar, tvCurrentPosition, tvDuration)
                }
                EventConstant.VALUE_METHOD_ONERROR -> {
                    model.prepared = false
                }
                EventConstant.VALUE_METHOD_ONCOMPLETION -> {
                    model.prepared = false
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
            tvDuration?.text = ToolUtil.formatTime(mediaView?.getDuration())
            val timer = Timer()
            val timerTask = object : TimerTask() {
                override fun run() {
                    seekBar?.post {
                        try {
                            tvCurrentPosition?.text = ToolUtil.formatTime(mediaView?.getCurrentPosition())
                            val percentF = mediaView?.getCurrentPosition()?.toFloat()!! / mediaView?.getDuration()!!
                            seekBar?.progress = (percentF * 100).toInt()
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

        /**
         * 设置播放&暂停显示的图片
         */
        fun startOrPause(imgPlayPause: ImageView) {
            if (mediaView?.getPlayerState() == EnumMediaState.PLAYING) {
                mediaView?.pause()
                imgPlayPause.setImageResource(model.startResId!!)
            } else {
                mediaView?.start()
                imgPlayPause.setImageResource(model.pauseResId!!)
            }
        }

        /**
         * 全屏&小屏幕处理
         */
        fun fullOrSmall(actvity: Activity?, view: android.view.View?, observable: MediaViewObservable<*>?) {
            PolyvScreenUtils.hideStatusBar(actvity)
            if (PolyvScreenUtils.isLandscape(context)) {
                PolyvScreenUtils.setPortrait(actvity)
                setMediaSize(model.originalMediaW!!, model.originalMediaH!!)
                model.curScreenMode = EventConstant.VALUE_SCREEN_FULL
            } else if (PolyvScreenUtils.isPortrait(context)) {
                savaOriginalMediaSize()
                PolyvScreenUtils.setLandscape(actvity)
                setMediaSize(model.originalMediaW!!, model.originalMediaH!!)
                model.curScreenMode = EventConstant.VALUE_SCREEN_FULL
            }
            view?.visibility = android.view.View.GONE
            observable?.notifyObservers(
                    Event().setEventType(EnumMediaEventType.VIEW)
                            .setParameter("ControlView", "show"))  // 通知全屏布局显示自身隐藏
        }

        private fun savaOriginalMediaSize() {
            if (model?.originalMediaW == -1 && model?.originalMediaH == -1) { //记录原有的view的宽高
                model?.originalMediaW = mediaView?.getView()?.measuredWidth
                model?.originalMediaH = mediaView?.getView()?.measuredHeight
            }
        }

        /*-------
         * SeekBar监听
         */
        fun onProgressChanged(progress: Int?, fromUser: Boolean?) {}

        fun onStartTrackingTouch(seekBar: SeekBar?) {}

        fun onStopTrackingTouch(seekBar: SeekBar?) {
            mediaView?.seekTo((seekBar?.progress!!.toFloat() / 100F * mediaView?.getDuration()!!).toLong())
        }
    }
}